package project.java.dto;

import java.util.Date;
import java.util.List;

public class ProjectDTO {

	private String numeProiect;
	private String client;
	private Date data_inceput;
	private Date data_sfarsit;
	private List<AngajatDTO> listaAngajati;

	public String getNumeProiect() {
		return numeProiect;
	}

	public void setNumeProiect(String numeProiect) {
		this.numeProiect = numeProiect;
	}

	public String getClient() {
		return client;
	}

	public void setClient(String client) {
		this.client = client;
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

	public List<AngajatDTO> getListaAngajati() {
		return listaAngajati;
	}

	public void setListaAngajati(List<AngajatDTO> listaAngajati) {
		this.listaAngajati = listaAngajati;
	}

	@Override
	public String toString() {
		return "ProjectDTO [numeProiect=" + numeProiect + ", client=" + client + ", data_inceput=" + data_inceput
				+ ", data_sfarsit=" + data_sfarsit + ", listaAngajati=" + listaAngajati + "]";
	}

}
