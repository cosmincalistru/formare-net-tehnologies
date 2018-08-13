package project.java.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.java.DAO.EmployeeDAO;
import project.java.Entities.EmployeesEntity;
import project.java.services.EmployeeService;


public class Exercitiul4 {

	public static void main(String[] args) throws IOException, ParseException {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		List<EmployeesEntity> employeesList = employeeDAO.list();

		for (EmployeesEntity em : employeesList) {

//			System.out.println("Employees list : " + em.getFirstName() + " " + em.getLastName());

		}


		EmployeeService emp = (EmployeeService) context.getBean("employeeService");
		emp.readFromCSV();


		context.close();

	}

}
