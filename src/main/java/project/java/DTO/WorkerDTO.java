package project.java.DTO;

import project.java.Entities.EmployeesEntity;
import project.java.Entities.ProjectsEntity;

import java.util.Date;

public class WorkerDTO {

    private EmployeesEntity employeeId;
    private ProjectsEntity projectsId;
    private Date startDate;
    private Date endDate;

    public EmployeesEntity getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(EmployeesEntity employeeId) {
        this.employeeId = employeeId;
    }

    public ProjectsEntity getProjectsId() {
        return projectsId;
    }

    public void setProjectsId(ProjectsEntity projectsId) {
        this.projectsId = projectsId;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
}
