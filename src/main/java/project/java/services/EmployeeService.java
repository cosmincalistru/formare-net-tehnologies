package project.java.services;

import project.java.DAO.EmployeeDAO;
import project.java.DAO.UserDAO;
import project.java.DTO.EmployeeDTO;
import project.java.DTO.UserDTO;
import project.java.Entities.EmployeesEntity;
import project.java.Entities.UsersEntity;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmployeeService {

    private String inputFolder;
    private String doneFolder;

    private UserDAO userDAO;
    private EmployeeDAO employeeDAO;

    private String fileNameToSearch;
    private StringBuffer errorsToWrite = new StringBuffer();



    private boolean testDate(String inputDate){
        return inputDate.matches("([0-3][0-9])-([0-1][0-9])-([0-9]{4})") || inputDate.equals("");
    }

    private boolean checkEmail(String email){
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}", Pattern.CASE_INSENSITIVE);
        Matcher mat = pattern.matcher(email);

        return mat.matches();
    }

    private boolean testNameAndEmail(String nume, String email){
        return !nume.isEmpty() && !email.isEmpty() && this.checkEmail(email);
    }

    private boolean testUserAndPass(String user, String pass) {
        return (!user.isEmpty() && !pass.isEmpty()) || (user.isEmpty() && pass.isEmpty());
    }

    private void printMyEmployee(String[] list){
        if (list.length < 4) {
            System.out.println("Employee [nume= " + list[0] + " , prenume=" + list[1]
                    + " , email=" + list[2]  + "]");
        } else if (list.length < 5){
            System.out.println("Employee [nume= " + list[0] + " , prenume=" + list[1]
                    + " , email=" + list[2] + " , dataNasterii=" + list[3] + "]");
        } else if (list.length < 6){
            System.out.println("Employee [nume= " + list[0] + " , prenume=" + list[1]
                    + " , email=" + list[2] + " , dataNasterii=" + list[3]
                    + " , utilizator=" + list[4]  + "]");
        } else {
            System.out.println("Employee [nume= " + list[0] + " , prenume=" + list[1]
                    + " , email=" + list[2] + " , dataNasterii=" + list[3]
                    + " , utilizator=" + list[4]
                    + " , parola=" + list[5] + "]");
        }
    }

    private File getFileFromDir(){
        File directory = new File(this.getInputFolder());
        if(!directory.isDirectory()) {
            System.out.println("Input Folder is not a Directory - bam");
            return null;
        }

        System.out.println("Your Directory is: " + this.inputFolder);

        File[] files = directory.listFiles((dir, name) -> {
            fileNameToSearch = "EMPLOYEES_" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".csv";

            return fileNameToSearch.equalsIgnoreCase(name);

        });

        assert files != null;
        return files.length == 0 ? null : files[0];
    }

    private UserDTO getUser(String username, String password){

        UserDTO user = new UserDTO();

        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    private UsersEntity saveUser(UserDTO userDTO){

        UsersEntity usersEntity = new UsersEntity();

        if (!userDTO.getUsername().isEmpty()) {
//            usersEntity.setId((long) 2);
            usersEntity.setUserName(userDTO.getUsername());
            usersEntity.setPassword(userDTO.getPassword());
            userDAO.save(usersEntity);

            return usersEntity;
        }

        return null;
    }

    private EmployeeDTO getEmployee(String[] items) throws ParseException {

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setFirstName(items[0]);
        employeeDTO.setLastName(items[1]);
        employeeDTO.setEmail(items[2]);

        if (items.length > 3 && !items[3].equals("")) {
            employeeDTO.setBirthDate(new SimpleDateFormat("dd-mm-yyyy").parse(items[3]));
        }
        if (items.length > 5) {
            UsersEntity usersEntity = saveUser(getUser(items[4], items[5]));
            employeeDTO.setUserID(usersEntity);
        }

        return employeeDTO;
    }

    private void saveEmployee (EmployeeDTO employeeDTO) {
        EmployeesEntity employeesEntity = new EmployeesEntity();

        employeesEntity.setFirstName(employeeDTO.getFirstName());
        employeesEntity.setLastName(employeeDTO.getLastName());
        employeesEntity.setEmail(employeeDTO.getEmail());
        employeesEntity.setDateOfBirth(employeeDTO.getBirthDate());
        employeesEntity.setUser(employeeDTO.getUserID());
        employeesEntity.setProjectsId(employeeDTO.getProject());

        employeeDAO.save(employeesEntity);
    }

    private void moveCsvFile() throws IOException {

        String fileName = this.fileNameToSearch;

        System.out.println(fileName + " acesta este pathul fisierului ");

        Path temp = Files.move(Paths.get(this.inputFolder, fileName), Paths.get(this.doneFolder, fileName));

        if (temp != null) {
            System.out.println("File move succesfully");
        } else {
            System.out.println("Failed to move the file !");
        }
    }

    private void concatError(String line, String motiv){
        this.errorsToWrite.append(line);
        this.errorsToWrite.append(";");
        this.errorsToWrite.append(motiv);
        this.errorsToWrite.append("\n");
    }

    private void writeCsvErrorFile() throws ParseException {

        File dir = new File(doneFolder);

        File file = new File(dir, this.fileNameToSearch.replace(".csv", "_ERRORS.csv"));
        String s = "\"sep=;\"\nNume;Prenume;Email;DataNasterii;Utilizator;Parola;Motiv\n";

        try (FileWriter fileWriter = new FileWriter(file)) {

            fileWriter.append(s);
            fileWriter.append(this.errorsToWrite);

            System.out.println("CSV file was created succesfully");
        } catch (Exception e) {
            System.out.println("Error - CSV file hasn't been created");
            e.printStackTrace();
        }
    }

    public void readFromCSV() throws IOException, ParseException {
        File csvFile = getFileFromDir();
        BufferedReader br = null;
        String line;
        String cvsSplitBy = ";";
        String error = "";

        File dirOut = new File(doneFolder);
        dirOut.mkdirs();

        if (csvFile != null) {

            try {
//            System.out.println("before open " + this.inputFolder + "\\" + csvFile.getName());
                br = new BufferedReader(new FileReader(csvFile));
                line = br.readLine();
                while ((line = br.readLine()) != null) {
                    error = "";
                    // use separator
                    String[] list = line.split(cvsSplitBy, -1);
                    System.out.println(list.length + " -> " + line);

                    // verificare lungime lista si format data
                    if (!testDate((list.length < 4) ? "" : list[3])) {
                        System.out.println("You have a wrong Date format");
                        error += "You have a wrong Date format ";

                    }

                    //verificare existenta nume si email si formatul email-ului
                    if (!testNameAndEmail(list[0], list[2])) {
                        System.out.println("Name or/and Email is Empty or Email doesn't have a valid format");
                        error += "Name or/and Email is Empty or Email doesn't have a valid format ";
                    }

                    if (!(list.length < 5 ?
                            testUserAndPass("", "")
                            : list.length >= 6 && testUserAndPass(list[4], list[5]))) {
                        System.out.println("Combo-ul utilizator/parola sunt obligatorii impreuna");
                        error += "Combo-ul utilizator/parola sunt obligatorii impreuna ";
                    }

                    if (!testDate(list.length < 4 ? "" : list[3]) || !testNameAndEmail(list[0], list[2]) ||
                            !(list.length < 5 ?
                                    testUserAndPass("", "")
                                    : list.length >= 6 && testUserAndPass(list[4], list[5]))) {
                        System.out.println("Not ok " + error);

                        concatError(line, error);
                        continue;
                    } else {
                        saveEmployee(getEmployee(list));
                    }

//                printMyEmployee(list);
                    System.out.println("-->");

                }

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            if(this.errorsToWrite != null){
                writeCsvErrorFile();
            }
            moveCsvFile();
        } else {
            System.out.println("Nu exista fisier pentru aceasta zi");
        }

    }

    public void setInputFolder(String inputFolder) {

        this.inputFolder = inputFolder;

    }

    private String getInputFolder() {

        return this.inputFolder;

    }

    public void setDoneFolder(String doneFolder) {

        this.doneFolder = doneFolder;

    }

    public String getDoneFolder() {

        return this.doneFolder;

    }

    public void setFileNameToSearch(String fileNameToSearch) {
        this.fileNameToSearch = fileNameToSearch;
    }

    public void setEmployeeDAO(EmployeeDAO employeeDAO) {
        this.employeeDAO = employeeDAO;
    }

    public void setUserDAO(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public StringBuffer getErrorsToWrite() {
        return errorsToWrite;
    }

    public void setErrorsToWrite(StringBuffer errorsToWrite) {
        this.errorsToWrite = errorsToWrite;
    }
}
