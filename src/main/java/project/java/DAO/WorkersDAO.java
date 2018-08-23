package project.java.DAO;

import java.util.List;

import project.java.Entities.WorkersEntity;

public interface WorkersDAO {

	public List<WorkersEntity> getAllWorkers();

	public WorkersEntity save(WorkersEntity w);

}
