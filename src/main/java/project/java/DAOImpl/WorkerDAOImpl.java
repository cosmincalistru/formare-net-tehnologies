package project.java.DAOImpl;

import org.hibernate.*;
import org.hibernate.criterion.Restrictions;
import project.java.DAO.WorkerDAO;
import project.java.DTO.WorkerDTO;
import project.java.Entities.WorkersEntity;
import java.util.List;

public class WorkerDAOImpl implements WorkerDAO {

    private SessionFactory sessionFactory;

    public void setSessionFactory(SessionFactory sessionFactory) {

        this.sessionFactory = sessionFactory;
    }

    @Override
    public WorkersEntity save(WorkersEntity e) {

        Session session = this.sessionFactory.openSession();
        Transaction tx = session.beginTransaction();
        session.persist(e);
        tx.commit();
        session.close();

        return e;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<WorkersEntity> list() {

        Session session = this.sessionFactory.openSession();

        List<WorkersEntity> workersList = session.createQuery("from WorkersEntity").list();
        session.close();

        return workersList;
    }

    @Override
    public WorkersEntity getWorkersById(long id) {

        Session session = this.sessionFactory.openSession();

        WorkersEntity worker = (WorkersEntity) session.load(WorkersEntity.class, id);
        Hibernate.initialize(worker);

        session.close();
        return worker;
    }

    @Override
    public WorkersEntity getWorkerBetweenDates(WorkerDTO workerDTO, String caz) {

        Session session = this.sessionFactory.openSession();

        Criteria criteria = session.createCriteria(WorkersEntity.class);
        List result = null;

        if (caz == "WWD") {
            result = criteria.add(Restrictions.and(
                    Restrictions.eq("employeeId.id", workerDTO.getEmployeeId().getId()),
                    Restrictions.or(
                            Restrictions.and(
                                    Restrictions.isNotNull("endDate"),
                                    Restrictions.ge("endDate", workerDTO.getStartDate())
                            ),
                            Restrictions.isNull("endDate")
                    )
                    )
            ).list();
        } else {
                result = criteria.add(Restrictions.and(
                        Restrictions.eq("employeeId.id", workerDTO.getEmployeeId().getId()),
                        Restrictions.or(
                            Restrictions.and(
                                    Restrictions.isNotNull("endDate"),
                                    Restrictions.or(
                                            Restrictions.and(
                                                    Restrictions.le("startDate", workerDTO.getStartDate()),
                                                    Restrictions.ge("endDate", workerDTO.getEndDate())
                                            ),
                                            Restrictions.or(
                                                    Restrictions.between("startDate", workerDTO.getStartDate(), workerDTO.getEndDate()),
                                                    Restrictions.between("endDate", workerDTO.getStartDate(), workerDTO.getEndDate())
                                            )
                                    )

                            ),
                                Restrictions.and(
                                        Restrictions.isNull("endDate"),
                                        Restrictions.or(
                                                Restrictions.le("startDate", workerDTO.getStartDate()),
                                                Restrictions.le("startDate", workerDTO.getEndDate())
                                        )
                                )
                        ))).list();
            }


        session.close();
        return result.isEmpty() ? null : (WorkersEntity)result.get(0);
    }
}
