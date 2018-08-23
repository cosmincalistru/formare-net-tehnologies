package com.training.services;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import com.training.dao.EmployeeDao;
import com.training.dao.UserDao;
import com.training.model.Employee;
import com.training.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class CsvReaderService {

    private static StringBuilder errorHolder = new StringBuilder("Nume,Prenume,Email,DataNasterii,Utilizator,Parola,Motiv\n");
    private static String[] nextRecord;
    private static int line = 1;

    @Autowired
    private EmployeeDao employeeDao;

    @Autowired
    private UserDao userDao;

    private String inputFolder;

    private String outputFolder;

    private String inputFileName = "EMPLOYEES_" + (new SimpleDateFormat("yyyyMMdd").format(new Date())) + ".csv";
    private String errorsFileName = "EMPLOYEES_" + (new SimpleDateFormat("yyyyMMdd").format(new Date())) + "_ERRORS.csv";

    public void tratament() {

        File fisierCautat = findFile();
        if (fisierCautat != null) {
            System.out.println("Se incepe citirea fisierului: " + inputFileName);
            readCsv(fisierCautat);
        }
        else System.out.println("Nu s-a gasit nici un fisier.");
    }

    public File findFile() {
        File dir = new File(inputFolder);
        if (!dir.isDirectory()) {
            System.out.println("InputFolder nu este corect");
            return null;
        }

        File[] files = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase(inputFileName);
            }
        });

        for (File f : files) {
            return f;
        }

        return null;

    }

    private void readCsv(File f) {

        boolean fileExists = false;
        try (
                BufferedReader reader = new BufferedReader(new FileReader(f));
                CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()
        ) {

            fileExists = true;
            while ((nextRecord = csvReader.readNext()) != null) {
                line++;
                readLines(nextRecord, line);

            }
        } catch (IOException e) {
            e.printStackTrace();

        } finally {
            line = 1;
            if (fileExists) {
                moveFileToDone();
                if (!errorHolder.toString().equals("Nume,Prenume,Email,DataNasterii,Utilizator,Parola,Motiv\n")) {
                    saveErrorFile();
                }
            }
        }

        System.out.println(errorHolder);
    }

    private void readLines(String[] nextRecord, int line) {
        String lastName = nextRecord[0];
        String firstName = nextRecord[1];
        String email = nextRecord[2];
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        df.setLenient(false);
        Date birthDate = null;
        if (!nextRecord[3].equals("")) {
            try {
                birthDate = df.parse(nextRecord[3]);
            } catch (ParseException e) {
                appendError("Data nu respecta formatul zz-ll-aaaa");

            }
        }
        String username = nextRecord[4];
        String password = nextRecord[5];
        System.out.println("Se trece la verificarea username-ului: " + username + " si a parolei: " + password);
        checkAndSaveUserinDB(username, password);
        checkAndSaveEmployeeInDB(firstName, lastName, email, birthDate, username);
    }

    private void appendError(String error) {
        for (String record : nextRecord) {
            errorHolder.append(record + ",");
        }
        errorHolder.append("Eroare randul " + line + ": " + error + "\n");
    }

    private void checkAndSaveUserinDB(String username, String password) {//numele functiei nu indica faptul ca userul e si salvat

        if (!username.equals("") && !password.equals("")) {
            System.out.println("Se verifica daca username " + username + " exista deja in baza de date");
            User checkedUser = checkUsernameInDB(username);
            if (checkedUser == null) {
                System.out.println("Se creeaza userul cu username-ul: " + username);
                User user = new User();
                user.setUsername(username);
                user.setPassword(password);
                try {
                    System.out.println("Se salveaza in baza de date userul:" + username);
                    userDao.save(user);
                } catch (Exception e) {
                    appendError("Userul nu a putut fi creat din cauza unei erori la conexiunea cu baza de date.");
                    System.out.println("Userul nu a putut fi creat din cauza unei erori la conexiunea cu baza de date.");
                }

            } else {
                appendError("Userul exista deja in baza de date");//apreciez efortul dar nu ti s-a cerut sa verifici existenta user-ului in baza de date; nu schimba. dar citeste specificatiile
                System.out.println("Userul este deja in baza de date");
            }
        } else if ((!username.equals("") && password.equals("")) || (username.equals("") && !password.equals(""))) {
            appendError("Combo-ul utilizator/parola sunt obligatorii impreuna");
            System.out.println("Username-ul sau parola nu sunt prezente");
        }

    }

    private User checkUsernameInDB(String username) {
        User checkedUser = null;
        try {
            checkedUser = userDao.findByUsername(username);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return checkedUser;
    }

    private void checkAndSaveEmployeeInDB(String firstName, String lastName, String email, Date birthDate, String username) {
        User user = null;
        System.out.println("Se verifica daca username-ul" + username + " este diferit de null si daca exista deja in baza de date");
        if (!username.equals("")) {
            user = checkUsernameInDB(username);
        }
        System.out.println("Se verifica daca numele: " + lastName + " si emailul " + email + " nu sunt nule si daca emailul este valid.");
        if (checkEmployeeCredentials(lastName, email)) {
            System.out.println("Se verifica daca exista un angajat in baza de date cu numele: " + lastName + " si emailul: " + email);
            Employee checkEmp = employeeDao.findByLastNameAndEmail(lastName, email);
            if (checkEmp == null) {
                System.out.println("Se creeaza un nou angajat cu numele " + lastName + " " + firstName);
                Employee emp = new Employee();
                emp.setFirstName(firstName);
                emp.setLastName(lastName);
                emp.setBirthDate(birthDate);
                emp.setUser(user);
                emp.setEmail(email);

                System.out.println("Se salveaza nou angajat in baza de date");
                employeeDao.save(emp);
            } else {
                appendError("Angajatul este deja in baza de date");
            }
        }
    }

    private boolean checkEmployeeCredentials(String lastName, String email) {

        if (lastName.equals("")) {
            appendError("Numele este obligatoriu.");
            System.out.println("Numele lipseste din document");
            return false;
        } else if (email.equals("")) {
            appendError("Emailul este obligatoriu");
            System.out.println("Emailul lipseste din document");
            return false;
        } else if (!emailIsValid(email)) {
            appendError("Emailul nu este valid");
            System.out.println("Adresa de email " + email + " nu este valida.");
            return false;
        } else
            return true;
    }

    private boolean emailIsValid(String email) {
        String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
        Pattern p = Pattern.compile(ePattern);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    private void moveFileToDone() {
        try {
            System.out.println("Se muta documentul " + inputFileName + " in folderul done");
            Files.move(Paths.get(inputFolder, inputFileName), Paths.get(outputFolder, inputFileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Documentul " + inputFileName + " a fost mutat cu success.");
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Documentul " + inputFileName + " nu a putut fi mutat.");
        }
    }

    private void saveErrorFile() {
        System.out.println("Se incepe crearea documentului " + errorsFileName + ".");
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(Paths.get(outputFolder, errorsFileName).toUri()))))) {
            writer.write(errorHolder.toString());
            System.out.println("Documentul "+ errorsFileName + " a fost creat si salvat cu success.");
        } catch (IOException ex) {
            ex.printStackTrace();
            System.out.println("Documentul "+ errorsFileName + " nu a fost creat.");
        }
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
}
