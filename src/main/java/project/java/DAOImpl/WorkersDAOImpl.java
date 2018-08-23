package project.java.DAOImpl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import project.java.DAO.WorkersDAO;
import project.java.Entities.WorkersEntity;

public class WorkersDAOImpl implements WorkersDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;

	}

	@Override
	public WorkersEntity save(WorkersEntity w) {

		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.merge(w);
		tx.commit();
		session.close();
		return w;

	}

	@SuppressWarnings("unchecked")
	@Override
	public List<WorkersEntity> getAllWorkers() {

		Session session = this.sessionFactory.openSession();

		List<WorkersEntity> workersList = session.createQuery("from WorkersEntity").list();
		session.close();

		return workersList;

	}

}
