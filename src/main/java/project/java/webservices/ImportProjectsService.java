package project.java.webservices;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.StringUtils;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import proiect.java.exceptions.DateException;
import proiect.java.utils.XmlError;
import project.java.DAO.ProjectsDAO;
import project.java.DAO.UserDAO;
import project.java.DAO.WorkersDAO;
import project.java.Entities.ProjectsEntity;
import project.java.Entities.UsersEntity;
import project.java.Entities.WorkersEntity;
import project.java.dto.AngajatDTO;
import project.java.dto.ProjectDTO;

public class ImportProjectsService {

	private String inputFolder;
	private String outputFolder;
	private ProjectsDAO projectsDAO;
	private UserDAO userDAO;
	private WorkersDAO workersDAO;

	public void readXMLFile() throws ParseException, ParserConfigurationException, SAXException, IOException,
			TransformerException, DOMException {

		File file = getCurrentFile();

		DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

		Document document = (Document) documentBuilder.parse(file);

		NodeList nodeList = document.getElementsByTagName("proiecte");
		Node proiecte = nodeList.item(0);

		List<ProjectDTO> listaProiecte = new ArrayList<>();

		List<XmlError> listaMotive = new ArrayList<XmlError>();

		for (int i = 0; i < proiecte.getChildNodes().getLength(); i++) {
			Node node = proiecte.getChildNodes().item(i);

			if (node.getNodeType() == Node.ELEMENT_NODE) {
				Element proiect = (Element) node;
				System.out.println("nume: " + proiect.getAttribute("nume"));

				ProjectDTO project = new ProjectDTO();
				project.setNumeProiect(proiect.getAttribute("nume"));

				List<AngajatDTO> listAngajati = new ArrayList<>();
				for (int j = 0; j < proiect.getChildNodes().getLength(); j++) {
					Node copilNode = proiect.getChildNodes().item(j);

					if (copilNode.getNodeName().equals("client")) {
						System.out.println("client: " + copilNode.getTextContent());
						project.setClient(copilNode.getTextContent());
					}

					if (copilNode.getNodeName().equals("data_inceput")) {
						System.out.println("data inceput: " + copilNode.getTextContent());
						try {
							project.setData_inceput(
									dateParser("data de inceput a proiectului " + project.getNumeProiect(),
											copilNode.getTextContent()));
						} catch (DateException de) {
							XmlError error = new XmlError();
							error.setMesaj(de.getMessage());
							error.setNumeProiect(project.getNumeProiect());
							error.setUtilizator("");
							listaMotive.add(error);
							continue;
						}
					}

					if (copilNode.getNodeName().equals("data_sfarsit")) {
						try {
							project.setData_sfarsit(
									dateParser("data de sfarsit a proiectului " + project.getNumeProiect(),
											copilNode.getTextContent()));
						} catch (DateException de) {
							XmlError error = new XmlError();
							error.setMesaj(de.getMessage());
							error.setNumeProiect(project.getNumeProiect());
							error.setUtilizator("");
							listaMotive.add(error);
							continue;
						}
					}

					if (copilNode.getNodeName().equals("angajati")) {

						outerloop: for (int k = 0; k < copilNode.getChildNodes().getLength(); k++) {
							Node node1 = copilNode.getChildNodes().item(k);

							if (node1.getNodeType() == Node.ELEMENT_NODE) {
								Element angajat = (Element) node1;

								AngajatDTO angajatDto = new AngajatDTO();

								System.out.println("utilizator: " + angajat.getAttribute("utilizator"));
								angajatDto.setAngajat(angajat.getAttribute("utilizator"));

								for (int m = 0; m < angajat.getChildNodes().getLength(); m++) {

									Node angajatCopilNode = angajat.getChildNodes().item(m);

									if (angajatCopilNode.getNodeName().equals("data_inceput")) {
										try {
											angajatDto.setData_inceput(dateParser(
													"data de inceput a angajatului " + angajatDto.getAngajat(),
													angajatCopilNode.getTextContent()));
										} catch (DateException de) {
											XmlError error = new XmlError();
											error.setMesaj(de.getMessage());
											error.setNumeProiect(project.getNumeProiect());
											error.setUtilizator(angajatDto.getAngajat());
											listaMotive.add(error);
											continue outerloop;
										}
									}

									if (angajatCopilNode.getNodeName().equals("data_sfarsit")) {
										try {
											angajatDto.setData_sfarsit(dateParser(
													"data de sfarsit a angajatului " + angajatDto.getAngajat(),
													angajatCopilNode.getTextContent()));
										} catch (DateException de) {
											XmlError error = new XmlError();
											error.setMesaj(de.getMessage());
											error.setNumeProiect(project.getNumeProiect());
											error.setUtilizator(angajatDto.getAngajat());
											listaMotive.add(error);
											continue outerloop;
										}
									}

								}
								try {
									dateComparator("Testare date angajat " + angajatDto.getAngajat(),
											project.getData_inceput(), project.getData_sfarsit(),
											angajatDto.getData_inceput(), angajatDto.getData_sfarsit());
								} catch (DateException de) {
									XmlError error = new XmlError();
									error.setMesaj(de.getMessage());
									error.setNumeProiect(project.getNumeProiect());
									error.setUtilizator(angajatDto.getAngajat());
									listaMotive.add(error);
									continue outerloop;
								}
								listAngajati.add(angajatDto);
								System.out.println("lista de angajati este " + listAngajati);
							}

						}

					}
				}
				project.setListaAngajati(listAngajati);
				System.out.println(" ________ ");
				listaProiecte.add(project);

			}
		}
		for (ProjectDTO proj : listaProiecte) {
			ProjectsEntity pr = projectsDAO.findByName(proj.getNumeProiect());

			System.out.println(pr + " =project");

			if (pr == null) {
				saveProj(proj);
			} else {

			}

			for (AngajatDTO angajat : proj.getListaAngajati()) {

				System.out.println(angajat.getAngajat());
				UsersEntity u = userDAO.findByUsername(angajat.getAngajat());

				if (u == null) {
					System.out.println("Utilizator inexistent " + u);
					XmlError error = new XmlError();
					error.setMesaj("Utilizator inexistent");
					error.setNumeProiect(proj.getNumeProiect());
					error.setUtilizator(angajat.getAngajat());
					listaMotive.add(error);
				} else {
					WorkersEntity w = new WorkersEntity();
					w.setStartDate(angajat.getData_inceput());
					w.setEndDate(angajat.getData_sfarsit());
					w.setProjectsId(pr);
					w.setEmployee(u.getEmployee());
					if (w.getEndDate() == null) {
						XmlError error = new XmlError();
						error.setMesaj("Angajatul are o data de inceput nula");
						error.setNumeProiect(proj.getNumeProiect());
						error.setUtilizator(angajat.getAngajat());
						listaMotive.add(error);
					}

					List<WorkersEntity> workersList = workersDAO.getAllWorkers();

					boolean save = true;
					for (WorkersEntity workerDinBaza : workersList) {
						try {
							System.out.println(workerDinBaza.getEmployee().getId());
							System.out.println(u.getEmployee().getId());
							System.out.println(workerDinBaza.getEndDate());
							System.out.println(angajat.getData_inceput());
							if (workerDinBaza.getEmployee().getId().equals(u.getEmployee().getId())) {
								if (workerDinBaza.getEndDate() == null && angajat.getData_sfarsit() != null
										&& angajat.getData_sfarsit().before(workerDinBaza.getStartDate())) {
									continue;
								} else if (workerDinBaza.getEndDate() != null && (angajat.getData_inceput()
										.after(workerDinBaza.getEndDate())
										|| (angajat.getData_sfarsit() != null
												&& angajat.getData_sfarsit().before(workerDinBaza.getStartDate())))) {
									continue;
								}
								save = false;
								break;
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}

					if (save) {
						System.out.println("a mers salvarea " + w);
//						workersDAO.save(w);
					} else {
						System.out.println("salvarea nu s-a facut");
						XmlError error = new XmlError();
						error.setMesaj("Angajatul este deja asignat pe alt proiect");
						error.setNumeProiect(proj.getNumeProiect());
						error.setUtilizator(angajat.getAngajat());
						listaMotive.add(error);
					}

				}
			}
		}

		System.out.println("+++" + listaMotive);
		writeXMLFile(getCurrentFile().getName(), listaMotive);
	}

	public void saveProj(ProjectDTO project) throws ParserConfigurationException, TransformerException {

		ProjectsEntity p = new ProjectsEntity();

		p.setClientName(project.getClient());
		p.setStartDate(project.getData_inceput());
		p.setEndDate(project.getData_sfarsit());
		p.setProjectName(project.getNumeProiect());
		projectsDAO.save(p);

	}

	public void writeXMLFile(String fileName, List<XmlError> erori)

			throws ParserConfigurationException, TransformerException {

		File dir = new File(outputFolder);
		File file = new File(dir, fileName.replace(".xml", "_ERRORS.xml"));

		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();

		Document doc = docBuilder.newDocument();
		Element rootElement = doc.createElement("erori");
		doc.appendChild(rootElement);

		for (XmlError eroareXml : erori) {
			Element eroare = doc.createElement("eroare");
			rootElement.appendChild(eroare);
			eroare.appendChild(doc.createTextNode(eroareXml.getMesaj()));

			if (!eroareXml.getUtilizator().isEmpty()) {
				Attr attr = doc.createAttribute("utilizator");
				attr.setValue(eroareXml.getUtilizator());
				eroare.setAttributeNode(attr);
			}
			
			Attr attr1 = doc.createAttribute("proiect");
			attr1.setValue(eroareXml.getNumeProiect());
			eroare.setAttributeNode(attr1);

		}

		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		DOMSource source = new DOMSource(doc);

		StreamResult result = new StreamResult(file);

		transformer.transform(source, result);

		System.out.println("File saved !");
	}

	public void tratamentXML() throws ParseException, ParserConfigurationException, SAXException, IOException {

		File fisierulCautat = getCurrentFile();
		System.out.println(fisierulCautat);
		if (fisierulCautat != null) {
			System.out.println("vom trata fisierul : " + fisierulCautat.getName());
			try {
				readXMLFile();
			} catch (Exception e) {
				e.printStackTrace();
			}
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
				String fileNameDesired = "proiecte_" + (new SimpleDateFormat("yyyyMMdd")).format(new Date()) + ".xml";
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

	private static Date dateParser(String camp, String dataString) throws DateException {

		if (StringUtils.isEmpty(dataString)) {
			return null;
		}

		Date data = null;
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			data = dateFormat.parse(dataString);
		} catch (ParseException e) {
			throw new DateException(camp + " este incorecta " + dataString);
		}

		return data;
	}

	private static void dateComparator(String camp, Date dataInceput, Date dataSfarsit, Date dataInceputAngajat,
			Date dataSfarsitAngajat) throws DateException {
		if (dataSfarsit != null) {
			if (dataSfarsitAngajat == null) {
				throw new DateException("Data sfarsit angajat e nula chiar daca data sfarsit proiect nu e null.");
			} else if (!((dataInceputAngajat.after(dataInceput) && dataInceputAngajat.before(dataSfarsit))
					&& ((dataSfarsitAngajat.after(dataInceput) && dataSfarsitAngajat.before(dataSfarsit))))) {
				throw new DateException(camp + " nu este intre parametrii.");
			}
		} else {
			if (!dataInceputAngajat.after(dataInceput)) {
				throw new DateException(camp + " nu este intre parametrii.");
			}
		}
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

	public WorkersDAO getWorkersDAO() {
		return workersDAO;
	}

	public void setWorkersDAO(WorkersDAO workersDAO) {
		this.workersDAO = workersDAO;
	}

	public ProjectsDAO getProjectsDAO() {
		return projectsDAO;
	}

	public void setProjectsDAO(ProjectsDAO projectsDAO) {
		this.projectsDAO = projectsDAO;
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

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public void setUserDAO(UserDAO userDAO) {
		this.userDAO = userDAO;
	}

}
