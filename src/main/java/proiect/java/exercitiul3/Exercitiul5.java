package proiect.java.exercitiul3;

import java.io.IOException;
import java.text.ParseException;

import javax.xml.parsers.ParserConfigurationException;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.xml.sax.SAXException;

import project.java.webservices.ImportProjectsService;

public class Exercitiul5 {

	public static void main(String[] args)
			throws ParseException, ParserConfigurationException, SAXException, IOException {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

		ImportProjectsService projectsService = context.getBean(ImportProjectsService.class);

		projectsService.tratamentXML();
//		projectsService.moveFile();
	}

}
