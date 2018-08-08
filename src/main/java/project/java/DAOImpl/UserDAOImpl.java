package project.java.DAOImpl;

import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.classic.Session;

import project.java.DAO.UserDAO;
import project.java.Entities.UsersEntity;
import project.java.dto.UserDTO;

public class UserDAOImpl implements UserDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;

	}

	@Override
	public UsersEntity save(UsersEntity u) {

		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(u);
		tx.commit();
		session.close();
		return u;
	}

	public void insertIntoUsers(UserDTO user) {

		Session session = this.sessionFactory.openSession();
		session.beginTransaction();

		UsersEntity userEntity = new UsersEntity();
		userEntity.setPassword(user.getParola());
		userEntity.setUserName(user.getUtilizator());

		session.save(userEntity);

		session.getTransaction().commit();
		session.close();

	}

}
