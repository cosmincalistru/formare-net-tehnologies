package com.training.services;

import com.training.dao.EmployeeDao;
import com.training.dao.WorkerDao;
import com.training.model.Employee;
import com.training.model.Worker;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.xwpf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;

public class DocService {

    private String inputFolder;
    private String outputFolder;
    private Set<String> docKeys = new HashSet<>();
    private Map<String, Boolean> weekendDates = new HashMap<>();
    private Map<String, String> raportDetails = new HashMap<>();
    private int month;
    private int year;
    private int nrOfDaysInTheMonth;

    @Autowired
    public EmployeeDao employeeDao;

    @Autowired
    public WorkerDao workerDao;


    public void start() {
        List<Employee> empList = employeeDao.findAll();
        for (Employee emp : empList) {
            report(emp, 2, 2016);
        }


    }

    public void report(Employee employee, int luna, int an) {
        month = luna;
        year = an;
        LocalDate date = LocalDate.of(an, luna, 1);
        nrOfDaysInTheMonth = date.lengthOfMonth();
        if (nrOfDaysInTheMonth < 31) {
            for (int i = nrOfDaysInTheMonth; i <= 31; i++) {
                raportDetails.put(Integer.toString(i) + "", "");
                raportDetails.put("ziua_" + i + "_cheie", "");
                docKeys.add(Integer.toString(i));
            }
        }
        createHashSet();
        setWeekendDays();
        setVacations(employee);
        setEmployeeData(employee);
        readDoc();
    }


    private void readDoc() {

        try {
            File file = new File("D:\\formare\\files\\Macheta.docx");
            FileInputStream fis = new FileInputStream(file);
            XWPFDocument doc = new XWPFDocument(OPCPackage.open(fis));

            for (XWPFTable tbl : doc.getTables()) {
                for (XWPFTableRow row : tbl.getRows()) {
                    for (XWPFTableCell cell : row.getTableCells()) {
                        for (XWPFParagraph p : cell.getParagraphs()) {
                            for (XWPFRun r : p.getRuns()) {
                                String text = r.getText(0);
                                if (text != null && docKeys.contains(text)) {
                                    text = text.replace(text, raportDetails.get(text));
                                    r.setText(text, 0);
                                    if (weekendDates.containsKey(text)) {
                                        cell.setColor("A6A6A6");
                                   }
                                }
                            }
                        }
                    }
                }
            }

            doc.write(new FileOutputStream(new File("D:\\formare\\files\\RALComp.docx")));


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private void setEmployeeData(Employee employee){
        List<Worker> workers = workerDao.findByUsernameAndReportMonth(employee.getUser().getUsername(),month,year);
        Set<String> clientNames = new HashSet<>();
        Set<String> projectNames = new HashSet<>();
        for(Worker w: workers){
            clientNames.add(w.getProject().getClient());
            projectNames.add(w.getProject().getName());
        }
        if(clientNames.size()>1){
            raportDetails.put("client_cheie",String.join(",",clientNames));
        }
        else {
            raportDetails.put("client_cheie",clientNames.toString());
        }
        if(projectNames.size()>1){
            raportDetails.put("lista_proiecte_cheie", String.join(",",projectNames));
        }
        else{
            raportDetails.put("lista_proiecte_cheie", projectNames.toString());
        }
        raportDetails.put("Luna_text_An_numar_cheie",month + " " + year);
        raportDetails.put("Nume_Virgula_Prenume_cheie", employee.getLastName().toUpperCase() + ", " + employee.getFirstName());
        raportDetails.put("consultant_cheie",employee.getLastName() + " " + employee.getFirstName());
        SimpleDateFormat format= new SimpleDateFormat("dd.MM.yyyy");
        Date repDate = null;
        try{
            repDate=format.parse(nrOfDaysInTheMonth+"."+month+"."+year);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        raportDetails.put("data_raportului_cheie",format.format(repDate));
    }

    private void setWeekendDays() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date startDate = null;
        Date endDate = null;
        try {
            startDate = formatter.parse(year + "-" + (month < 10 ? ("0" + month) : month) + "-01");
            endDate = formatter.parse(year + "-" + (month < 10 ? ("0" + month) : month) + "-" + nrOfDaysInTheMonth);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar start = Calendar.getInstance();
        start.setTime(startDate);

        Calendar end = Calendar.getInstance();
        end.setTime(endDate);
        end.add(Calendar.DATE, 1);
        for (Calendar date = start; date.before(end); date.add(Calendar.DATE, 1)) {
            if (date.get(Calendar.DAY_OF_WEEK) == 7 || date.get(Calendar.DAY_OF_WEEK) == 1) {
                int dayOfMonth = date.get(Calendar.DAY_OF_MONTH);
                weekendDates.put(Integer.toString(dayOfMonth), true);
                weekendDates.put("ziua_" + dayOfMonth + "_cheie", true);
                docKeys.add(Integer.toString(dayOfMonth));
            }
        }
    }

    private void createHashSet() {
        docKeys.add("client_cheie");
        docKeys.add("lista_proiecte_cheie");
        docKeys.add("Luna_text_An_numar_cheie");
        docKeys.add("Nume_Virgula_Prenume_cheie");
        for(int i =1; i<=31;i++) {
            docKeys.add("ziua_"+i+"_cheie");
        }
        docKeys.add("suma_cheie");
        docKeys.add("consultant_cheie");
        docKeys.add("data_raportului_cheie");
    }

    public String getInputFolder() {
        return inputFolder;
    }

    public void setInputFolder(String inputFolder) {
        this.inputFolder = inputFolder;
    }

    public String getOutputFolder() {
        return outputFolder;
    }

    public void setOutputFolder(String outputFolder) {
        this.outputFolder = outputFolder;
    }
}
