package project.java.DAO;
import project.java.Entities.ProjectsEntity;
import java.util.List;

public interface ProjectDAO {

    public ProjectsEntity save(ProjectsEntity e);

    public List<ProjectsEntity> list();

    public ProjectsEntity getProtectsById(long id);

    public ProjectsEntity getProtectsByName(String name);
}
