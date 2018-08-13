package project.java.DAOImpl;

import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import project.java.DAO.UserDAO;
import project.java.Entities.UsersEntity;

import java.util.List;

public class UserDAOImpl implements UserDAO {
    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;

    }

    @Override
    public UsersEntity save(UsersEntity e) {

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(e);
        tx.commit();
        session.close();

        return e;

    }

    @SuppressWarnings("unchecked")
    @Override
    public List<UsersEntity> list() {

        Session session = this.sessionFactory.openSession();

        List<UsersEntity> employeesList = session.createQuery("from EmployeesEntity").list();
        session.close();

        return employeesList;
    }

    @Override
    public UsersEntity getUsersById(long id) {

        Session session = this.sessionFactory.openSession();

        UsersEntity employee = (UsersEntity) session.load(UsersEntity.class, id);
        Hibernate.initialize(employee);

        return employee;
    }
}
