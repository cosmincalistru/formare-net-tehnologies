package project.java.services;

//import com.sun.org.apache.xpath.internal.operations.String;
import java.lang.String;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import project.java.DAO.EmployeeDAO;
import project.java.DAO.ProjectDAO;
import project.java.DAO.UserDAO;
import project.java.DAO.WorkerDAO;
import project.java.DTO.ProjectDTO;
import project.java.DTO.WorkerDTO;
import project.java.Entities.EmployeesEntity;
import project.java.Entities.ProjectsEntity;
import project.java.Entities.UsersEntity;
import project.java.Entities.WorkersEntity;
import project.java.JAXB.EroareElement;
import project.java.JAXB.EroriRoot;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProjectService {

    private static final String DATE_FORMAT_ERROR = "ERROR: Formatul datei este necorespunzator";
    private static final String DATE_START_ERROR = "ERROR: Data de start a proeictului este obligatorie";
    private static final String DATE_BEFORE_ERROR_PROJ = "ERROR: Data incetarii proiectului trebuie sa fie dupa data inceperii acestuia";
    private static final String DATE_BEFORE_ERROR_WORKER = "ERROR: Data incetarii angajatului trebuie sa fie dupa data inceperii acestuia";
    private static final String WRONG_DATE_ERROR = "ERROR: Datele nu se afla in intervalul corect ->";
    private static final String NUME_CLIENT_ERROR = "ERROR: Numele clientului este obligatoriu";
    private static final String NUME_PROIECT_ERROR = "ERROR: Numele proiectului este obligatoriu";
    private static final String MISSING_CLIENT_ERROR = "ERROR: Campul 'client' este obligatoriu";
    private static final String DOUBLE_PROJ_ERROR = "ERROR: Worker-ul poate lucra la un singur proiect intr-o zi";

    private static final String INEXISTENT_USER_ERROR = "ERROR: Utilizatorul nu exista in baza sau nu este prezent";

    private String currentError;

    private final static GlobalService globalService = new GlobalService();

    private String inputFolder;
    private String doneFolder;

    private ProjectDAO projectDAO;
    private WorkerDAO workerDAO;
    private EmployeeDAO employeeDAO;
    private UserDAO userDAO;

    private String fileNameToSearch;
    private ProjectsEntity currentProject;

    private List<EroareElement> listaErori = new ArrayList<>();
    private EroriRoot eroriRoot = new EroriRoot();

    public void importProjectsAndWorkers() throws JAXBException, IOException {
        readFromXMLFile();
//        projectDAO.getProtectsByName("important Project");
    }

    private void readFromXMLFile() throws JAXBException, IOException {

        if (getFileFromDir() != null) {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(getFileFromDir());
                doc.getDocumentElement().normalize();

                NodeList listProiecte = doc.getElementsByTagName("proiect");

                for (int proj = 0; proj < listProiecte.getLength(); proj++) {

                    Node nProj = listProiecte.item(proj);

                    if (nProj.getNodeType() == Node.ELEMENT_NODE) {

                        Element eProj = (Element) nProj;
//                        printProject(eProj);

                        List<String> projElements = getElementsOfProject(eProj);

                        ProjectDTO projectDTO = globalService.getProjectDTO(projElements.get(0), projElements.get(1), projElements.get(2), projElements.get(3));

                        if (verifyProject(projElements, projectDTO)) {
                            if (projectExists(projElements.get(0))) {
                                currentProject = projectDAO.getProtectsByName(projElements.get(0));

                            } else {
                                currentProject = saveProject(projectDTO);
                            }
                        } else {
                            addEror(projElements.get(0), null, currentError);
                            continue;
                        }

                        NodeList listAngajati = eProj.getElementsByTagName("angajat");

                        for (int temp = 0; temp < listAngajati.getLength(); temp++) {

                            Node nWorker = listAngajati.item(temp);

                            if (nWorker.getNodeType() == Node.ELEMENT_NODE) {

                                Element eElement = (Element) nWorker;
//                                printAngajat(eElement);
                                List<String> workerElements = getElementsOfWorker(eElement);

//                                System.out.println(getUserEntity(workerElements.get(0)).getUserName());
                                if (getUserEntity(workerElements.get(0)) != null) {
                                    if (getEmployeeEntity(getUserEntity(workerElements.get(0))) != null) {
                                        WorkerDTO workerDTO = globalService.getWorkerDTO(getEmployeeEntity(getUserEntity(workerElements.get(0))),
                                                currentProject, workerElements.get(1), workerElements.get(2));
                                        if (verifyWorker(workerElements, workerDTO, projectDTO)) {
                                            saveWorker(workerDTO);
                                        } else {
                                            addEror(currentProject.getProjectName(), workerElements.get(0), currentError);
                                        }
//                                        System.out.println(getEmployeeEntity(getUserEntity(workerElements.get(0))));
                                    }
                                } else {
                                    addEror(currentProject.getProjectName(), workerElements.get(0), INEXISTENT_USER_ERROR);
                                }
                            }
                        }
                    }
                }
            } catch (ParserConfigurationException | IOException | SAXException | ParseException e) {
                e.printStackTrace();
            }

            writeInXMLFile();
            moveXmlFile();
        } else {
            System.out.println("Nu exista fisier xml pt aceasta zi");
        }
    }

    private UsersEntity getUserEntity(String workerName) {
        return workerName != null ? userDAO.getUsersByName(workerName) : null;
    }

    private EmployeesEntity getEmployeeEntity(UsersEntity userId) {
        return employeeDAO.getEmployeeByUserId(userId);
    }

    private boolean verifyProject(List<String> projElements, ProjectDTO projectDTO) {
        currentError = "";

        if (projElements.get(0) == null || projElements.get(0).isEmpty()) {
            currentError += NUME_PROIECT_ERROR;
            return false;
        }

        if (projElements.get(1) == null || projElements.get(1).isEmpty()) {
            currentError += MISSING_CLIENT_ERROR;
            return false;
        }

        if (projElements.get(1) == null || projElements.get(1).isEmpty()) {
            currentError += NUME_CLIENT_ERROR;
            return false;
        }

        if(projElements.get(2).isEmpty()){
            currentError += DATE_START_ERROR;
            return false;
        }

        if (!verifyDate(projElements.get(2)) || (!projElements.get(3).equals("") && !verifyDate(projElements.get(3)))) {
            currentError += DATE_FORMAT_ERROR;
            return false;
        }

        if  (!projElements.get(3).equals("") && projectDTO.getEndDate().before(projectDTO.getStartDate())){
            currentError += DATE_BEFORE_ERROR_PROJ;
            return false;
        }

        return true;
    }

    private boolean verifyWorker(List<String> workerElements, WorkerDTO workerDTO, ProjectDTO projectDTO) {
        currentError = workerElements.get(1) + " ";



        if (workerElements.get(0).isEmpty() || workerElements.get(0) == null){
            currentError += INEXISTENT_USER_ERROR;
            return false;
        }

        if (workerElements.get(1).isEmpty()){
            currentError += DATE_START_ERROR;
            return false;
        }

        if (workerDTO.getEndDate() != null && workerDTO.getEndDate().before(workerDTO.getStartDate())){
            currentError += DATE_BEFORE_ERROR_WORKER;
            return false;
        }

        if (!verifyDate(workerElements.get(1)) || (!workerElements.get(2).equals("") && !verifyDate(workerElements.get(2)))) {
            currentError += DATE_FORMAT_ERROR;
            return false;
        }

        if (workerDTO.getStartDate().before(projectDTO.getStartDate())){
            currentError +=  WRONG_DATE_ERROR + " Worker StartDate is before Project StartDate";
            return false;
        }

        if (projectDTO.getEndDate() != null && workerDTO.getEndDate() != null && projectDTO.getEndDate().before(workerDTO.getEndDate())){
            currentError += WRONG_DATE_ERROR + " Project EndDate is before Worker EndDate";
            return false;
        }

        if (workerDAO.getWorkerBetweenDates(workerDTO, workerDTO.getEndDate() == null ? "WWD" : "ALL") != null) {
//            System.out.println("Yass" + workerDAO.getWorkerBetweenDates(workerDTO).getEmployeeId());
            currentError += DOUBLE_PROJ_ERROR;
            return false;
        }


        currentError = "";

        return true;
    }

    private ProjectsEntity saveProject(ProjectDTO projectDTO) {
        ProjectsEntity projectsEntity = new ProjectsEntity();

        projectsEntity.setClientName(projectDTO.getClientName());
        projectsEntity.setProjectName(projectDTO.getProjectName());
        projectsEntity.setStartDate(projectDTO.getStartDate());
        projectsEntity.setEndDate(projectDTO.getEndDate());

        return projectDAO.save(projectsEntity);
    }

    private List<String> getElementsOfProject( Element eProj) {
        List<String> projElements = new ArrayList<>(4);

        projElements.add(0, eProj.getAttribute("nume"));
        projElements.add(1, eProj.getElementsByTagName("client").item(0).getTextContent());
        projElements.add(2, eProj.getElementsByTagName("data_inceput").item(0).getTextContent());
        projElements.add(3, eProj.getElementsByTagName("data_sfarsit").item(0).getTextContent());

        return projElements;
    }

    private void saveWorker(WorkerDTO workerDTO) {
        WorkersEntity workersEntity = new WorkersEntity();

        workersEntity.setEmployeeId(workerDTO.getEmployeeId());
        workersEntity.setProjectsId(workerDTO.getProjectsId());
        workersEntity.setStartDate(workerDTO.getStartDate());
        workersEntity.setEndDate(workerDTO.getEndDate());

        workerDAO.save(workersEntity);
    }

    private List<String> getElementsOfWorker(Element eElement) {
        List<String> workerElements = new ArrayList<>(3);

        workerElements.add(0, eElement.getAttribute("utilizator"));
        workerElements.add(1, eElement.getElementsByTagName("data_inceput").item(0).getTextContent());
        workerElements.add(2, eElement.getElementsByTagName("data_sfarsit").item(0).getTextContent());

        return workerElements;
    }


    private void printProject(Element eProj){
        System.out.println("nume Proiect : " + eProj.getAttribute("nume"));
        System.out.println("client : " + eProj.getElementsByTagName("client").item(0).getTextContent());
        System.out.println("data_inceput : " + eProj.getElementsByTagName("data_inceput").item(0).getTextContent());
        System.out.println("data_sfarsit : " + eProj.getElementsByTagName("data_sfarsit").item(0).getTextContent());
    }

    private void printAngajat(Element eElement){
        System.out.println("worker Proiect : " + eElement.getAttribute("utilizator"));
        System.out.println("data_inceput : " + eElement.getElementsByTagName("data_inceput").item(0).getTextContent());
        System.out.println("data_sfarsit : " + eElement.getElementsByTagName("data_sfarsit").item(0).getTextContent());
    }

    private File getFileFromDir(){
        File directory = new File(this.getInputFolder());
        if(!directory.isDirectory()) {
            System.out.println("Input Folder is not a Directory - bam");
            return null;
        }

        File[] files = directory.listFiles((dir, name) -> {
            fileNameToSearch = "proiecte_" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".xml";

            return fileNameToSearch.equalsIgnoreCase(name);
        });

        assert files != null;
        return files.length == 0 ? null : files[0];
    }

    private void moveXmlFile() throws IOException {

        String fileName = this.fileNameToSearch;

        System.out.println(fileName + " acesta este pathul fisierului ");

        Path temp = Files.move(Paths.get(this.inputFolder, fileName), Paths.get(this.doneFolder, fileName));

        if (temp != null) {
            System.out.println("File move succesfully");
        } else {
            System.out.println("Failed to move the file !");
        }
    }

    private boolean verifyDate(String inputDate) {
        return inputDate.matches("(([0-9]{4})-[0-1][0-9])-([0-3][0-9])");
    }

    private boolean projectExists (String projectName){
        return projectDAO.getProtectsByName(projectName) != null;
    }

    private void addEror (String project, String user, String eroare) {

        EroareElement eroareElement = new EroareElement();

        eroareElement.setProiect(project);
        if (user != null) {
            eroareElement.setUtilizator(user);
        }
        eroareElement.setDescriere(eroare);

        eroriRoot.getErori().add(eroareElement);
    }

    private void writeInXMLFile() throws JAXBException {
//        System.out.println(currentError);

        JAXBContext jaxbContext = JAXBContext.newInstance(EroriRoot.class);
        Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

        jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

        //Marshal the error list in console
        jaxbMarshaller.marshal(eroriRoot, System.out);

        //Marshal the error list in file
        jaxbMarshaller.marshal(eroriRoot, new File(doneFolder + "proiecte_" +
                (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + "_errors.xml" ));
    }

    private String getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getDoneFolder() {
        return doneFolder;
    }

    public void setDoneFolder(String doneFolder) {
        this.doneFolder = doneFolder;
    }

    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public WorkerDAO getWorkerDAO() {
        return workerDAO;
    }

    public void setWorkerDAO(WorkerDAO workerDAO) {
        this.workerDAO = workerDAO;
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
