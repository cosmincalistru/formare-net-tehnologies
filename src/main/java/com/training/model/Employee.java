package com.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @SequenceGenerator(name= "emp_seq", sequenceName = "employees_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "emp_seq")
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name="id_user")
     private User user;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "email")
    private String email;

    @Column(name = "birth_date")
    private Date birthDate;

    public Employee() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

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

//    public Project getProjectId() {
//        return projectId;
//    }
//
//    public void setProjectId(Project projectId) {
//        this.projectId = projectId;
//    }


    @Override
    public String toString() {
        return "First Name: " + this.getFirstName() + "\nLast Name: " + this.getLastName() + "\nEmail: " +getEmail() + "\nBirthdate: "+ this.getBirthDate()  ;
    }
}
