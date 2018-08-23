package project.java.mainex2;

import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import project.java.DAO.EmployeeDAO;
import project.java.Entities.EmployeesEntity;



public class SpringHibernateMain {

	public static void main(String[] args) {

		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");

		EmployeeDAO employeeDAO = context.getBean(EmployeeDAO.class);

		List<EmployeesEntity> employeesList = employeeDAO.getAllImployees();

		for (EmployeesEntity em : employeesList) {

			System.out.println("Employees list : " + em.getFirstName() + " " + em.getLastName());

		}
		
		context.close();

	}

}
