package project.java.DTO;

import project.java.Entities.ProjectsEntity;
import project.java.Entities.UsersEntity;

import java.util.Date;

public class EmployeeDTO {

    private String firstName;
    private String lastName;
    private String email;
    private Date birthDate;
    private ProjectsEntity project;
    private UsersEntity userID;

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public ProjectsEntity getProject() {
        return project;
    }

    public void setProject(ProjectsEntity project) {
        this.project = project;
    }

    public UsersEntity getUserID() {
        return userID;
    }

    public void setUserID(UsersEntity userID) {
        this.userID = userID;
    }
}
