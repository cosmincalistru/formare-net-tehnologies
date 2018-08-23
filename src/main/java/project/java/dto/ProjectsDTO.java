package project.java.dto;

import java.util.List;

public class ProjectsDTO {

	private List<ProjectDTO> listaProiecte;

	public List<ProjectDTO> getListaProiecte() {
		return listaProiecte;
	}

	public void setListaProiecte(List<ProjectDTO> listaProiecte) {
		this.listaProiecte = listaProiecte;
	}

	@Override
	public String toString() {
		return "ProjectsDTO [listaProiecte=" + listaProiecte + "]";
	}

}
