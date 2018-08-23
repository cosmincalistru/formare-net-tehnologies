package project.java.DAOImpl;

import java.util.List;

import org.hibernate.*;

import org.hibernate.criterion.Restrictions;
import project.java.DAO.EmployeeDAO;
import project.java.Entities.EmployeesEntity;
import project.java.Entities.UsersEntity;

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

	@Override
	public EmployeesEntity getEmployeeByUserId(UsersEntity userId) {
		Session session = this.sessionFactory.openSession();

		Criteria criteria = session.createCriteria(EmployeesEntity.class);
		criteria.add(Restrictions.eq("user.id", userId.getId()));
		List result = criteria.list();

		session.close();
		return result.isEmpty() ? null : (EmployeesEntity)result.get(0);
	}

}
