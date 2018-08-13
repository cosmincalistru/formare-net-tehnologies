package project.java.DAO;
import java.util.List;

import project.java.Entities.EmployeesEntity;

public interface EmployeeDAO {
	
	public EmployeesEntity save(EmployeesEntity e);
	
	public List<EmployeesEntity> list();
	
	public EmployeesEntity getEmployeesById(long id);

}
