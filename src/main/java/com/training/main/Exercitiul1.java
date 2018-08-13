package com.training.main;

import com.training.dao.EmployeeDao;
import com.training.model.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


@Component
public class Exercitiul1 {

    @Autowired
    private EmployeeDao employeeDao;

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
        Exercitiul1 p = context.getBean(Exercitiul1.class);
        p.start();

    }



    private void start(){

        addEmployee();
        addEmployee();
        addEmployee();
        addEmployee();
        addEmployee();
        addEmployee();
        addEmployee();
        addEmployee();


        List<Employee> list= employeeDao.findAll();
        for(Employee emp: list){
            System.out.println(emp.toString());
        }
    }

    private void addEmployee(){
        Employee emp = new Employee();
        emp.setFirstName("Costel");
        emp.setLastName("Petrea");
        emp.setEmail("costel.petrea@hgrup.com");
        Calendar date = new GregorianCalendar(1990,02,05);
        emp.setBirthDate(date.getTime());
        employeeDao.save(emp);
//        System.out.println("\n\n\n\n\n"+emp);
    }
}
