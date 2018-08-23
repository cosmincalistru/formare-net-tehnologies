package com.training.main;

import com.training.dao.EmployeeDao;
import com.training.model.Employee;
import com.training.services.DocService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class Exercitiul6 {



    public static void main(String[] args) {

        ApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
        DocService docService = (DocService) context.getBean("docService");
        docService.start();


    }

}
