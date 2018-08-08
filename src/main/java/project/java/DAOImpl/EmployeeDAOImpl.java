package project.java.DAOImpl;

import java.util.List;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import project.java.DAO.EmployeeDAO;
import project.java.Entities.EmployeesEntity;

public class EmployeeDAOImpl implements EmployeeDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;

	}

	@Override
	public EmployeesEntity save(EmployeesEntity e) {

		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(e);
		tx.commit();
		session.close();
		return e;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<EmployeesEntity> list() {

		Session session = this.sessionFactory.openSession();

		List<EmployeesEntity> employeesList = session.createQuery("from EmployeesEntity").list();
		session.close();

		return employeesList;
	}

	@Override
	public EmployeesEntity getEmployeesById(long id) {

		Session session = this.sessionFactory.openSession();

		EmployeesEntity employee = (EmployeesEntity) session.load(EmployeesEntity.class, id);
		Hibernate.initialize(employee);

		return employee;
	}

}
