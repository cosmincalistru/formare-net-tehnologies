package com.training.main;

import com.training.services.CsvReaderService;
import org.slf4j.event.Level;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Component;

import java.util.logging.Logger;

import static org.slf4j.event.Level.ERROR;


@Component
public class Exercitiul4 {

        public static void main(String[] args) {
            ApplicationContext context = new ClassPathXmlApplicationContext("configurare.xml");
            CsvReaderService csvReaderService = (CsvReaderService) context.getBean("csvReaderService");//se recomanda ca numele bean-urilor sa fie cu litera mica incepute
            csvReaderService.tratament();
       }

}
