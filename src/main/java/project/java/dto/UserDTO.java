package project.java.dto;

import java.util.Date;

public class UserDTO {

	private String nume;
	private String prenume;
	private String email;
	private Date dataNasterii;
	private String utilizator;
	private String parola;

	public String getNume() {
		return nume;
	}

	public void setNume(String nume) {
		this.nume = nume;
	}

	public String getPrenume() {
		return prenume;
	}

	public void setPrenume(String prenume) {
		this.prenume = prenume;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Date getDataNasterii() {
		return dataNasterii;
	}

	public void setDataNasterii(Date dataNasterii) {
		this.dataNasterii = dataNasterii;
	}

	public String getUtilizator() {
		return utilizator;
	}

	public void setUtilizator(String utilizator) {
		this.utilizator = utilizator;
	}

	public String getParola() {
		return parola;
	}

	public void setParola(String parola) {
		this.parola = parola;
	}

	@Override
	public String toString() {
		return "UserDTO [nume=" + nume + ", prenume=" + prenume + ", email=" + email + ", dataNasterii=" + dataNasterii
				+ ", utilizator=" + utilizator + ", parola=" + parola + "]";
	}

}
