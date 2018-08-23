package project.java.DAOImpl;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import project.java.DAO.ProjectDAO;
import project.java.Entities.ProjectsEntity;

import java.util.List;

public class ProjectDAOImpl implements ProjectDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public ProjectsEntity save(ProjectsEntity e) {

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(e);
        tx.commit();
        session.close();

        return e;
    }

    @Override
    public List<ProjectsEntity> list() {
        Session session = this.sessionFactory.openSession();

        List<ProjectsEntity> projectsList = session.createQuery("from ProjectsEntity").list();
        session.close();

        return projectsList;
    }

    @Override
    public ProjectsEntity getProtectsById(long id) {

        Session session = this.sessionFactory.openSession();

        ProjectsEntity project = (ProjectsEntity) session.load(ProjectsEntity.class, id);
        Hibernate.initialize(project);

        return project;
    }

    @Override
    public ProjectsEntity getProtectsByName(String name) {
        Session session = this.sessionFactory.openSession();

        Criteria criteria = session.createCriteria(ProjectsEntity.class);
        criteria.add(Restrictions.eq("projectName", name));
        List result = criteria.list();

        session.close();
        return result.isEmpty() ? null : (ProjectsEntity)result.get(0);
    }
}
