package project.java.DAO;

import java.util.List;

import project.java.Entities.ProjectsEntity;

public interface ProjectsDAO {

	public List<String> showProjectName();

	public ProjectsEntity save(ProjectsEntity p);

	public ProjectsEntity findByName(String name);

	public ProjectsEntity merge(ProjectsEntity p);

}
