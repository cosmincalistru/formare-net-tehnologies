package project.java.DAOImpl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import project.java.DAO.ProjectsDAO;
import project.java.Entities.ProjectsEntity;

public class ProjectsDAOImpl implements ProjectsDAO {

	private SessionFactory sessionFactory;

	public void setSessionFactory(SessionFactory sessionFactory) {

		this.sessionFactory = sessionFactory;

	}

	public List<String> showProjectName() {

		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();

		Criteria crit = session.createCriteria(ProjectsEntity.class);
		ProjectionList projList = Projections.projectionList();
		projList.add(Projections.property("projectName"));
		crit.setProjection(projList);

		@SuppressWarnings("unchecked")
		List<String> results = crit.list();
		System.out.println(results);

		session.close();

		return results;

	}

	public ProjectsEntity findByName(String name) {

		Session session = this.sessionFactory.getCurrentSession();
		session.beginTransaction();

		Criteria criteria = session.createCriteria(ProjectsEntity.class);
		ProjectsEntity yourObject = (ProjectsEntity) criteria.add(Restrictions.eq("projectName", name)).uniqueResult();

		return yourObject;

	}

	@Override
	public ProjectsEntity save(ProjectsEntity p) {

		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.persist(p);
		tx.commit();
		session.close();
		return p;
	}

	@Override
	public ProjectsEntity merge(ProjectsEntity p) {

		Session session = this.sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.merge(p);
		tx.commit();
		session.close();
		return p;

	}

}
