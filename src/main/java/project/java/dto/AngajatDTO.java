package project.java.dto;

import java.util.Date;

public class AngajatDTO {

	private String angajat;
	private Date data_inceput;
	private Date data_sfarsit;

	public String getAngajat() {
		return angajat;
	}

	public void setAngajat(String angajat) {
		this.angajat = angajat;
	}

	public Date getData_inceput() {
		return data_inceput;
	}

	public void setData_inceput(Date data_inceput) {
		this.data_inceput = data_inceput;
	}

	public Date getData_sfarsit() {
		return data_sfarsit;
	}

	public void setData_sfarsit(Date data_sfarsit) {
		this.data_sfarsit = data_sfarsit;
	}

	@Override
	public String toString() {
		return "AngajatDTO [angajat=" + angajat + ", data_inceput=" + data_inceput + ", data_sfarsit=" + data_sfarsit
				+ "]";
	}

}
