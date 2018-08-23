package project.java.DAOImpl;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
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

        List<UsersEntity> usersList = session.createQuery("from UsersEntity").list();
        session.close();

        return usersList;
    }

    @Override
    public UsersEntity getUsersById(long id) {

        Session session = this.sessionFactory.openSession();

        UsersEntity employee = (UsersEntity) session.load(UsersEntity.class, id);
        Hibernate.initialize(employee);

        return employee;
    }

    @Override
    public UsersEntity getUsersByName(String name) {
        Session session = this.sessionFactory.openSession();

        Criteria criteria = session.createCriteria(UsersEntity.class);
        criteria.add(Restrictions.eq("userName", name));
        List result = criteria.list();

        session.close();

        return result.isEmpty() ? null : (UsersEntity)result.get(0);
    }
}
