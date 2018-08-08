package project.java.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.util.StringUtils;

import project.java.DAO.EmployeeDAO;
import project.java.DAO.UserDAO;
import project.java.Entities.EmployeesEntity;
import project.java.Entities.UsersEntity;
import project.java.dto.UserDTO;

public class ImportEmployeesService {

	private String inputFolder;
	private String outputFolder;
	private EmployeeDAO employeeDAO;
	private UserDAO userDAO;

	public void readCSVFile() throws ParseException {

		String csvSplitBy = ";";

		File file = getCurrentFile();

		File dir = new File(outputFolder);

		dir.mkdirs();
		for (File f : dir.listFiles()) {
			f.delete();
		}

		try (BufferedReader br = new BufferedReader(new FileReader(file))) {
			br.readLine();

			String line = null;

			while ((line = br.readLine()) != null) {

				String[] userData = line.split(csvSplitBy, -1);
				Date date = null;
				try {
					SimpleDateFormat dateFormat = new SimpleDateFormat("dd-mm-yyyy");
					if (!userData[3].isEmpty()) {
						date = dateFormat.parse(userData[3]);
					}
				} catch (ParseException e) {
					writeCsvFile(file.getName(), line, "Data trebuie sa aiba formatul zz-ll-aaaa");
					continue;
				}
				UserDTO user = new UserDTO();

				user.setNume(userData[0]);
				user.setPrenume(userData[1]);
				user.setEmail(userData[2]);
				user.setDataNasterii(date);
				user.setUtilizator(userData[4]);
				user.setParola(userData[5]);

				Pattern pattern = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$",
						Pattern.CASE_INSENSITIVE);
				Matcher mat = pattern.matcher(user.getEmail());

				if (!StringUtils.isEmpty(user.getNume()) && !StringUtils.isEmpty(user.getEmail())) {
					System.out.println(user);
					if ((StringUtils.isEmpty(user.getUtilizator()) && StringUtils.isEmpty(user.getParola()))
							|| (!StringUtils.isEmpty(user.getUtilizator()) && !StringUtils.isEmpty(user.getParola()))) {
						if (mat.matches()) {
							System.out.println(user);
							saveEmp(user);
						} else {
							System.out.println("Adresa de email este invalida !");
							System.out.println("Adresa email: " + user.getEmail());
							writeCsvFile(file.getName(), line, "Adresa de email este invalida");
							continue;
						}

					} else {
						System.out.println("Utilizatorul si parola sunt obligatorii impreuna !");
						System.out.println("Utilizator: " + user.getUtilizator() + " Parola: " + user.getParola());
						writeCsvFile(file.getName(), line, "Utilizatorul si parola sunt obligatorii impreuna !");
						continue;
					}
				} else {
					System.out.println("Numele si emailul sunt obligatorii !");
					System.out.println("Nume: " + user.getNume() + " Email: " + user.getEmail());
					writeCsvFile(file.getName(), line, "Numele si emailul sunt obligatorii !");
					continue;
				}

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void saveEmp(UserDTO user) {
		UsersEntity u = null;
		if (!user.getUtilizator().isEmpty()) {
			u = new UsersEntity();
			u.setUserName(user.getUtilizator());
			u.setPassword(user.getParola());
			u = userDAO.save(u);
		}

		EmployeesEntity e = new EmployeesEntity();
		e.setLastName(user.getNume());
		e.setFirstName(user.getPrenume());
		e.setEmail(user.getEmail());
		e.setDateOfBirth(user.getDataNasterii());
		e.setUserId(u);
		employeeDAO.save(e);
	}

	public void moveFile() throws IOException {

		String fileName = getCurrentFile().getName();

		System.out.println(fileName + " acesta este pathul fisierului ");

		Path temp = Files.move(Paths.get(inputFolder, fileName), Paths.get(outputFolder, fileName));

		if (temp != null) {
			System.out.println("File move succesfully");
		} else {
			System.out.println("Failed to move the file !");
		}
	}

	public void writeCsvFile(String fileName, String line, String motiv) throws ParseException {

		File dir = new File(outputFolder);

		File file = new File(dir, fileName.replace(".csv", "_ERRORS.csv"));
		String s = file.exists() ? "" : "\"sep=;\"\nNume;Prenume;Email;DataNasterii;Utilizator;Parola;Motiv\n";

		try (FileWriter fileWriter = new FileWriter(file, true)) {

			fileWriter.append(s);
			fileWriter.append(line);
			fileWriter.append(";");
			fileWriter.append(motiv);
			fileWriter.append("\n");

			System.out.println("CSV file was created succesfully");
		} catch (Exception e) {
			System.out.println("Error in the CsvFileWriter");
			e.printStackTrace();
		}
	}

	public void tratament() throws ParseException {
		File fisierulCautat = getCurrentFile();
		System.out.println(fisierulCautat);
		if (fisierulCautat != null) {
			System.out.println("vom trata fisierul : " + fisierulCautat.getName());
			readCSVFile();
		}
	}

	public File getCurrentFile() {
		File dir = new File(inputFolder);
		if (!dir.isDirectory()) {
			System.out.println("nu e bine");
			return null;
		}

		System.out.println("directorul de lucru este : " + inputFolder);

		File[] files = dir.listFiles(new FilenameFilter() {

			public boolean accept(File directory, String fileName) {
				String fileNameDesired = "EMPLOYEES_" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".csv";
				if (fileNameDesired.equalsIgnoreCase(fileName)) {
					return true;
				}
				return false;
			}

		});

		for (File f : files) {
			return f;
		}

		return null;
	}

	public String getInputFolder() {
		return inputFolder;
	}

	public void setInputFolder(String inputFolder) {
		this.inputFolder = inputFolder;
	}

	public String getOutputFolder() {
		return outputFolder;
	}

	public void setOutputFolder(String outputFolder) {
		this.outputFolder = outputFolder;
	}

	public EmployeeDAO getEmployeeDAO() {
		return employeeDAO;
	}

	public void setEmployeeDAO(EmployeeDAO employeeDAO) {
		this.employeeDAO = employeeDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
