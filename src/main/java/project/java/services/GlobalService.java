package project.java.services;

import project.java.DAO.ProjectDAO;
import project.java.DAO.WorkerDAO;
import project.java.DTO.EmployeeDTO;
import project.java.DTO.ProjectDTO;
import project.java.DTO.UserDTO;
import project.java.DTO.WorkerDTO;
import project.java.Entities.EmployeesEntity;
import project.java.Entities.ProjectsEntity;
import project.java.Entities.UsersEntity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

class GlobalService {

    private ProjectDAO projectDAO;
    private WorkerDAO workerDAO;

    UserDTO getUser(String username, String password){

        UserDTO user = new UserDTO();

        user.setUsername(username);
        user.setPassword(password);

        return user;
    }

    EmployeeDTO getEmployee(String[] items, UsersEntity usersEntity) throws ParseException {

        EmployeeDTO employeeDTO = new EmployeeDTO();

        employeeDTO.setFirstName(items[0]);
        employeeDTO.setLastName(items[1]);
        employeeDTO.setEmail(items[2]);

        if (items.length > 3 && !items[3].equals("")) {
            employeeDTO.setBirthDate(new SimpleDateFormat("dd-mm-yyyy").parse(items[3]));
        }
        if (items.length > 5) {
            employeeDTO.setUserID(usersEntity);
        }

        return employeeDTO;
    }

    WorkerDTO getWorkerDTO(EmployeesEntity employeesEntity, ProjectsEntity projectsEntity,
                        String startDate, String endDate) throws ParseException {

        WorkerDTO workerDTO = new WorkerDTO();

        workerDTO.setEmployeeId(employeesEntity);
        workerDTO.setProjectsId(projectsEntity);
        workerDTO.setStartDate(startDate.isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
        workerDTO.setEndDate(endDate.isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(endDate));

        return workerDTO;
    }

    ProjectDTO getProjectDTO(String projectName, String clientName,
                          String startDate, String endDate) throws ParseException {

        ProjectDTO projectDTO = new ProjectDTO();

        projectDTO.setProjectName(projectName);
        projectDTO.setClientName(clientName);
        projectDTO.setStartDate(startDate.isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(startDate));
        projectDTO.setEndDate(endDate.isEmpty() ? null : new SimpleDateFormat("yyyy-MM-dd").parse(endDate));

        return projectDTO;
    }



    public ProjectDAO getProjectDAO() {
        return projectDAO;
    }

    public void setProjectDAO(ProjectDAO projectDAO) {
        this.projectDAO = projectDAO;
    }

    public WorkerDAO getWorkerDAO() {
        return workerDAO;
    }

    public void setWorkerDAO(WorkerDAO workerDAO) {
        this.workerDAO = workerDAO;
    }
}
