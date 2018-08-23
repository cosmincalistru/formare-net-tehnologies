package com.training.services;

import com.training.dao.EmployeeDao;
import com.training.dao.ProjectDao;
import com.training.dao.UserDao;
import com.training.dao.WorkerDao;
import com.training.model.Employee;
import com.training.model.Project;
import com.training.model.User;
import com.training.model.Worker;
import org.springframework.beans.factory.annotation.Autowired;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class XMLService {

    private String inputFolder;
    private String outputFolder;
    private Map<String, String> xmlAttributes = new HashMap<>();
    private Map<String, String> xmlText = new HashMap<>();
    private final String MAPSEPARATOR = "---";

    @Autowired
    private ProjectDao projectDao;

    @Autowired
    private WorkerDao workerDao;

    @Autowired
    private UserDao userDao;

    @Autowired
    private EmployeeDao employeeDao;

    private String inputFileName = "proiecte_" + (new SimpleDateFormat("yyyyMMdd").format(new Date())) + ".xml";
    private String errorsFileName = "proiecte_" + (new SimpleDateFormat("yyyyMMdd").format(new Date())) + "_ERRORS.xml";

/*
    //Pentru verificare query-ului de extragere a worker-ilor care blocheaza updatarea datelor proiectului
    public void search(){

        try {
            List<Worker> w =  workerDao.findByProject("Dematerialization", parseDate("2010-08-01"), parseDate("2013-09-01"));
            List<Worker> w1 =  workerDao.findByProject("Dematerialization", parseDate("2011-08-01"), parseDate("2016-09-01"));
            List<Worker> w2 =  workerDao.findByProject("Dematerialization", parseDate("2011-08-01"), parseDate("2020-09-01"));
            List<Worker> w3 =  workerDao.findByProject("Dematerialization", parseDate("2014-08-01"), parseDate("2020-09-01"));
            List<Worker> w4 =  workerDao.findByProject("Dematerialization", parseDate("2014-08-01"), parseDate("2016-09-01"));
            List<Worker> w5 =  workerDao.findByProject("Dematerialization", parseDate("2014-08-01"), null);
            List<Worker> w6 =  workerDao.findByProject("Dematerialization", parseDate("2011-08-01"), null);
            System.out.println(w1.size());
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

*/
    public void tratament() {

        File fisierCautat = findFile();

        if (fisierCautat != null) {
            System.out.println("Se incepe citirea fisierului: " + inputFileName);
            readXML(fisierCautat);
        } else {
            System.out.println("Fisierul cautat nu exista");
        }
    }

    private File findFile() {

        File dir = new File(inputFolder);

        if (!dir.isDirectory()) {
            System.out.println("Eroare!!!Mai baga o fisa");
            return null;
        }
        System.out.println("Se cauta fisierul " + inputFileName);
        File[] file = dir.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.equalsIgnoreCase(inputFileName);
            }
        });

        for (File f : file) {
            return f;
        }

        return null;

    }


    private void readXML(File f) {

        try {

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(f);
            doc.getDocumentElement().normalize();
            NodeList nodeProjectList = doc.getElementsByTagName("proiect");
            for (int i = 0; i < nodeProjectList.getLength(); i++) {
                Node nodeProject = nodeProjectList.item(i);
                readProjectDetails(nodeProject);
            }


        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        moveFileToDone();
        if (xmlAttributes.size() > 0) {
            writeErrors();
        }
    }

    private void readProjectDetails(Node nodeProject) {
        if (nodeProject.getNodeType() == Node.ELEMENT_NODE) {
            System.out.println("Se incepe cautarea proiectelor in fisier.");
            Element elementProject = (Element) nodeProject;

            String projectName = elementProject.getAttribute("nume");
            String client = elementProject.getElementsByTagName("client").item(0).getTextContent();
            Date startProjectDate = null;
            Date endProjectDate = null;
            try {
                String data = elementProject.getElementsByTagName("data_inceput").item(0).getTextContent();
                if (!data.equalsIgnoreCase("")) {
                    startProjectDate = parseDate(data);
                }
            } catch (ParseException e) {
                saveProjectError(projectName, "data_inceput a proiectului " + projectName + " nu respecta formatul aaaa-ll-zz");
                System.out.println("data_inceput a proiectului " + projectName + " nu respecta formatul aaaa-ll-zz");
            }
            try {
                String data = elementProject.getElementsByTagName("data_sfarsit").item(0).getTextContent();
                if (!data.equalsIgnoreCase("")) {
                    endProjectDate = parseDate(data);
                }
            } catch (ParseException e) {
                saveProjectError(projectName, "data_sfarsit a proiectului " + projectName + " nu respecta formatul aaaa-ll-zz");
                System.out.println("data_sfarsit a proiectului " + projectName + " nu respecta formatul aaaa-ll-zz");
            }
            if (checkProjectDetails(projectName, client, startProjectDate, endProjectDate) && createAndSaveProjectInDB(projectName, client, startProjectDate, endProjectDate)) {
                System.out.println("Se incepe cautarea angajatilor pentru proiectul " + projectName);
                NodeList nodeEmployeeList = ((Element) nodeProject).getElementsByTagName("angajat");
                for (int j = 0; j < nodeEmployeeList.getLength(); j++) {
                    Node nodeEmployee = nodeEmployeeList.item(j);
                    readEmployeeDetails(nodeEmployee, projectName);
                }
            }
        }
    }

    private boolean checkProjectDetails(String projectName, String client, Date startProjectDate, Date endProjectDate) {

        if (client.equalsIgnoreCase("")) {
            System.out.println("Numele clientului lipseste din document.");
            saveProjectError(projectName, "Numele clientului este obligatoriu");
            return false;
        } else if (startProjectDate == null) {
            System.out.println("Data de inceput a proiectului " + projectName + " lipseste din document.");
            saveProjectError(projectName, "Data de inceput este obligatorie");
            return false;
        } else if (endProjectDate != null && startProjectDate.compareTo(endProjectDate) > 0) {
            System.out.println("Data de inceput a proiectului " + projectName + " este dupa data de sfarsit.");
            saveProjectError(projectName, "Data de inceput nu poate fi dupa data de sfarsit");
            return false;
        } else
            return true;
    }

    private boolean checkAndUpdateProjectDatesInDB(Project project, Date newStartProjectDate, Date newEndProjectDate) {
        List<Worker> workers = workerDao.findByProject(project.getName(), newStartProjectDate, newEndProjectDate);
        if (workers.size() == 0) {
            if (project.getDate_start().compareTo(newStartProjectDate) != 0)
                project.setDate_start(newStartProjectDate);
            if (project.getDate_end() != null && newEndProjectDate != null && project.getDate_end().compareTo(newEndProjectDate) != 0)
                project.setDate_end(newEndProjectDate);
            projectDao.save(project);
            return true;
        } else {
            saveProjectError(project.getName(), "Datele proiectului nu pot fi modificate deoarece este cel putin un angajat programat sa lucreze la acest proiect");
            return false;
        }

    }

    private boolean createAndSaveProjectInDB(String projectName, String client, Date startProjectDate, Date endProjectDate) {
        Project project = projectDao.findByName(projectName);
        if (project != null) {
            System.out.println("Proiectul " + projectName + " se afla deja in baza de date");
            if (project.getDate_start().compareTo(startProjectDate) != 0 || (project.getDate_end() != null && endProjectDate != null && project.getDate_end().compareTo(endProjectDate) != 0)) {
                return checkAndUpdateProjectDatesInDB(project, startProjectDate, endProjectDate);
            }
            return true;
        } else {
            Project newProject = new Project();
            newProject.setName(projectName);
            newProject.setClient(client);
            newProject.setDate_start(startProjectDate);
            newProject.setDate_end(endProjectDate);
            try {
                System.out.println("Se incepe salvarea proiectului " + projectName + " in baza de date.");
                projectDao.save(newProject);
                System.out.println("Proiectul " + projectName + " a fost salvat cu success in baza de date.");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Proiectul " + projectName + " nu a putut fi salvat cu success in baza de date din cauza unei erori de conexiune.");
                return false;
            }
            return true;
        }
    }

    private void readEmployeeDetails(Node nodeEmployee, String projectName) {
        if (nodeEmployee.getNodeType() == Node.ELEMENT_NODE) {
            Element elementEmployee = (Element) nodeEmployee;
            Project project = projectDao.findByName(projectName);
            String username = elementEmployee.getAttribute("utilizator");
            Date startDateEmp = null;
            Date endDateEmp = null;
            try {
                String data = elementEmployee.getElementsByTagName("data_inceput").item(0).getTextContent();
                if (!data.equalsIgnoreCase("")) {
                    startDateEmp = parseDate(data);
                }
            } catch (ParseException e) {
                saveEmployeeError(projectName, username, "data_inceput a utilizatorului " + username + " pe proiectul " + projectName + " nu respecta formatul aaaa-ll-zz");
            }
            try {
                String data = elementEmployee.getElementsByTagName("data_sfarsit").item(0).getTextContent();
                if (!data.equalsIgnoreCase("")) {
                    endDateEmp = parseDate(data);
                }
            } catch (ParseException e) {
                saveEmployeeError(projectName, username, "data_sfarsit a utilizatorului " + username + " pe proiectul " + projectName + " nu respecta formatul aaaa-ll-zz");
            }
            if (checkUserDetailse(username, startDateEmp, endDateEmp, project)) {
                createAndSaveWorkerInDb(username, startDateEmp, endDateEmp, project);
            }
        }
    }

    private boolean checkUserDetailse(String username, Date startDateEmp, Date endDateEmp, Project project) {
        User user = userDao.findByUsername(username);
        List<Worker> worker = workerDao.findByUsername(username, startDateEmp, endDateEmp);
        if (user == null) {
            System.out.println("Utilizatorul " + username + " nu exista in baza de date.");
            saveEmployeeError(project.getName(), username, "Utilizatorul nu exista in baza de date");
            return false;
        } else if (startDateEmp == null) {
            System.out.println("Data de inceput a utilizatorului " + username + " pe proiectul " + project.getName() + " lipseste din document");
            saveEmployeeError(project.getName(), username, "Data de inceput este obligatorie");
            return false;
        } else if (startDateEmp.compareTo(project.getDate_start()) < 0) {
            System.out.println("Data de inceput a utilizatorului " + username + " pe proiectul " + project.getName() + " este inaintea datei de inceput a proiectului.");
            saveEmployeeError(project.getName(), username, "Data de inceput a utilizatorului este inaintea datei de start a proiectului");
            return false;
        } else if (project.getDate_end() != null && startDateEmp.compareTo(project.getDate_end()) > 0) {
            System.out.println("Data de inceput a utilizatorului " + username + " pe proiectul " + project.getName() + " este dupa data de finalizare a proiectului.");
            saveEmployeeError(project.getName(), username, "Data de inceput a utilizatorului este dupa data de finalizare a proiectului");
            return false;
        } else if (endDateEmp != null && startDateEmp.compareTo(endDateEmp) > 0) {
            System.out.println("Data de inceput a utilizatorului " + username + " pe proiectul " + project.getName() + " este dupa data de finalizare");
            saveEmployeeError(project.getName(), username, "Data de inceput a utilizatorului e dupa data de finalizare.");
            return false;
        } else if (endDateEmp != null && project.getDate_end() != null && endDateEmp.compareTo(project.getDate_end()) > 0) {
            System.out.println("Data de finalizare a utilizatorului " + username + " pe proiectul " + project.getName() + " este dupa data de finalizare a proiectului");
            saveEmployeeError(project.getName(), username, "Data de finalizare a utilizatorului este dupa data de finalizare a proiectului");
            return false;
        } else if (worker.size() > 0) {
            System.out.println("Utilizatorul " + username + " lucreaza la alt proiect in aceeasi perioada.");
            saveEmployeeError(project.getName(), username, "Utilizatorul lucreaza la alt proiect in aceasta perioada");
            return false;
        }
        return true;
    }

    private boolean createAndSaveWorkerInDb(String username, Date startDateEmp, Date endDateEmp, Project project) {
        Employee employee = employeeDao.findByUsername(username);
        Worker worker = new Worker();
        worker.setDate_start(startDateEmp);
        worker.setDate_end(endDateEmp);
        worker.setEmployee(employee);
        worker.setProject(project);
        try {
            System.out.println("Se incepe salvarea worker-ului in baza de date.");
            workerDao.save(worker);
            System.out.println("Worker-ul a fost salvat cu success in baza de date.");
        } catch (Exception e) {
            System.out.println("Worker-ul nu a putut fi salvat cu success in baza de date.");
            return false;
        }
        return true;
    }


    private Date parseDate(String stringDate) throws ParseException {
        Date date = null;
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        date = df.parse(stringDate);
        return date;
    }

    private void saveProjectError(String projectName, String error) {
        xmlAttributes.put("error" + (xmlAttributes.size() + 1), projectName);
        xmlText.put(projectName, error);
    }

    private void saveEmployeeError(String projectName, String username, String error) {
        xmlAttributes.put("error" + (xmlAttributes.size() + 1), (projectName + MAPSEPARATOR + username));
        xmlText.put((projectName + MAPSEPARATOR + username), error);
    }

    private void moveFileToDone() {
        try {
            System.out.println("Se incepe mutarea fisierului " + inputFileName + " in folderul done");
            Files.move(Paths.get(inputFolder, inputFileName), Paths.get(outputFolder, inputFileName), StandardCopyOption.REPLACE_EXISTING);
            System.out.println("Fisierul " + inputFileName + " a fost mutat cu success.");
        } catch (IOException e) {
            System.out.println("Fisierul " + inputFileName + " nu a putut fi mutat.");
            e.printStackTrace();
        }
    }


    private void writeErrors() {
        try {

            System.out.println("Se incepe crearea fisierului pentru erori.");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("erori");
            doc.appendChild(rootElement);

            for (int i = 1; i <= xmlAttributes.size(); i++) {
                Element eroare = doc.createElement("eroare");
                rootElement.appendChild(eroare);
                String attKey = xmlAttributes.get("error" + i);
                String[] attributes = attKey.split(MAPSEPARATOR);
                if (attributes.length > 1) {
                    eroare.setAttribute("proiect", attributes[0]);
                    eroare.setAttribute("utilizator", attributes[1]);
                } else {
                    eroare.setAttribute("proiect", attributes[0]);
                }
                eroare.appendChild(doc.createTextNode(xmlText.get(attKey)));
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(outputFolder, errorsFileName));

            transformer.transform(source, result);
            System.out.println("Fisierul " + errorsFileName + " a fost creat cu success.");

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            System.out.println("Fisierul " + errorsFileName + " nu a putut fi salvat.");
            e.printStackTrace();
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
