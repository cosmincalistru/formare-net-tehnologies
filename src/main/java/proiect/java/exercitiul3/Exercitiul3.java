package proiect.java.exercitiul3;

import java.io.IOException;
import java.text.ParseException;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.java.webservices.ImportEmployeesService;

public class Exercitiul3 {

	public static void main(String[] args) throws ParseException, IOException {

		@SuppressWarnings("resource")
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

		ImportEmployeesService employeeService = context.getBean(ImportEmployeesService.class);

		employeeService.tratament();
		employeeService.moveFile();

	}

}
