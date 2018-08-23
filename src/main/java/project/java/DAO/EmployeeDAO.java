package project.java.DAO;
import java.util.List;

import project.java.Entities.EmployeesEntity;
import project.java.Entities.UsersEntity;

public interface EmployeeDAO {
	
	public EmployeesEntity save(EmployeesEntity e);
	
	public List<EmployeesEntity> list();
	
	public EmployeesEntity getEmployeesById(long id);

	public EmployeesEntity getEmployeeByUserId(UsersEntity userId);

}
