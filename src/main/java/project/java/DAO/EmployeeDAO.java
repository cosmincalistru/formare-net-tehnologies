package project.java.DAO;

import project.java.Entities.EmployeesEntity;

import java.util.List;

public interface EmployeeDAO {
	
	public void save(EmployeesEntity e);
	
	public List<EmployeesEntity> list();
	
	public EmployeesEntity getEmployeesById(long id);

}
