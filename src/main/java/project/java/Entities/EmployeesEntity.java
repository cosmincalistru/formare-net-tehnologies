package project.java.Entities;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "employees")
public class EmployeesEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_emp")
	@SequenceGenerator(name = "seq_emp", sequenceName = "employees_seq")
	@Column(name = "id")
	private Long id;

	@OneToOne
	@JoinColumn(name = "id_user")
	private UsersEntity user;

	@ManyToOne
	@JoinColumn(name = "id_projects")
	private ProjectsEntity projectsId;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "birth_date")
	private Date dateOfBirth;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public UsersEntity getUser() {
		return user;
	}

	public void setUser(UsersEntity user) {
		this.user = user;
	}

	public ProjectsEntity getProjectsId() {
		return projectsId;
	}

	public void setProjectsId(ProjectsEntity projectsId) {
		this.projectsId = projectsId;
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

	public Date getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(Date dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	@Override
	public String toString() {
		return "EmployeesEntity [id=" + id + ", user=" + user + ", projectsId=" + projectsId + ", firstName="
				+ firstName + ", lastName=" + lastName + ", email=" + email + ", dateOfBirth=" + dateOfBirth + "]";
	}

}
