package com.training.main;

import com.training.services.XMLService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Exercitiul5 {

    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
        XMLService xmlService = (XMLService) context.getBean("xmlService");
        xmlService.tratament();


    }
}
