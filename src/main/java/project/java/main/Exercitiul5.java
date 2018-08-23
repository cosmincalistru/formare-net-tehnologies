package project.java.main;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import project.java.services.ProjectService;

import javax.xml.bind.JAXBException;
import java.io.IOException;

public class Exercitiul5 {

    public static void main(String[] args) throws JAXBException, IOException {

        ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

        ProjectService projectService = (ProjectService) context.getBean("projectService");
        projectService.importProjectsAndWorkers();

        context.close();

    }
}
