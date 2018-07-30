package project.java.Entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "projects")

public class ProjectsEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pro")
	@SequenceGenerator(name = "seq_pro", sequenceName = "projects_seq")
	@Column(name = "id")
	private Long id;

	@Column(name = "name")
	private String projectName;

	@Column(name = "client")
	private String clientName;

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

	public String getProjectName() {
		return projectName;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getClientName() {
		return clientName;
	}

	public void setClientName(String clientName) {
		this.clientName = clientName;
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
		return "ProjectsEntity [id=" + id + ", projectName=" + projectName + ", clientName=" + clientName
				+ ", startDate=" + startDate + ", endDate=" + endDate + "]";
	}

}
