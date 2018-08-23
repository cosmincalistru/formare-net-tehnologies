package project.java.Entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "workers")
public class WorkersEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_wrk")
	@SequenceGenerator(name = "seq_wrk", sequenceName = "workers_seq")
	@Column(name = "id")
	private Long id;

	@ManyToOne
	@JoinColumn(name = "id_projects")
	private ProjectsEntity projectsId;

	@ManyToOne
	@JoinColumn(name = "id_employees")
	private EmployeesEntity employeeId;

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

	public EmployeesEntity getEmployee() {
		return employeeId;
	}

	public void setEmployee(EmployeesEntity employeeId) {
		this.employeeId = employeeId;
	}

	@Override
	public String toString() {
		return "WorkersEntity [id=" + id + ", projectsId=" + projectsId + ", employee=" + employeeId + ", startDate="
				+ startDate + ", endDate=" + endDate + "]";
	}

}
