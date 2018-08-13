package com.training.model;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name="projects")
public class Project {

    @Id
    @SequenceGenerator(name="projects_seq", sequenceName = "projects_seq", allocationSize = 1)
    @GeneratedValue(strategy= GenerationType.SEQUENCE, generator="projects_seq")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "client")
    private String client;

    @Column(name = "date_start")
    private Date date_start;

    @Column(name="date_end")
    private Date date_end;


    public Project() {
    }

    public Project(Long id, String name, String client, Date date_start, Date date_end) {
        this.id = id;
        this.name = name;
        this.client = client;
        this.date_start = date_start;
        this.date_end = date_end;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
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
}
