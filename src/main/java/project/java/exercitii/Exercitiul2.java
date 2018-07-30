package project.java.exercitii;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import project.java.DAO.EmployeeDAO;
import project.java.Entities.EmployeesEntity;

import java.util.List;

public class Exercitiul2 {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		List<EmployeesEntity> employeesList = employeeDAO.list();

		for (EmployeesEntity em : employeesList) {

			System.out.println("Employees list : " + em.getFirstName() + " " + em.getLastName());

		}
		
		context.close();

	}

}
