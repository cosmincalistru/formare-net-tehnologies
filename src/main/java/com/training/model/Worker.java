package com.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "workers")
public class Worker {

    @Id
    @SequenceGenerator(name = "workers_seq", sequenceName = "workers_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "workers_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "date_start")
    private Date date_start;

    @Column(name = "date_end")
    private Date date_end;

    @OneToOne
    @JoinColumn(name = "id_employee")
    private Employee employee;

    @OneToOne
    @JoinColumn(name="id_project")
    private Project project;

    public Worker() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getDate_start() {
        return date_start;
    }

    public void setDate_start(Date date_start) {
        this.date_start = date_start;
    }

    public Date getDate_end() {
        return date_end;
    }

    public void setDate_end(Date date_end) {
        this.date_end = date_end;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }
}
