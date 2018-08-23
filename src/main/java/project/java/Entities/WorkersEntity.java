package project.java.Entities;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "workers")

public class WorkersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_work")
    @SequenceGenerator(name = "seq_work", sequenceName = "workers_seq")
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "id_employees")
    private EmployeesEntity employeeId;

    @ManyToOne
    @JoinColumn(name = "id_projects")
    private ProjectsEntity projectsId;

    @Column(name = "date_start")
    private Date startDate;

    @Column(name = "date_end")
    private Date endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

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

    @Override
    public String toString() {
        return "WorkersEntity [id=" + id + ", employeeId=" + employeeId + ", projectsId=" + projectsId
                + ", startDate=" + startDate + ", endDate=" + endDate + "]";
    }
}
