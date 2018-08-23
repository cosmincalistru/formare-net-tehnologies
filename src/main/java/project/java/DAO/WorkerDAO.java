package project.java.DAO;
import project.java.DTO.WorkerDTO;
import project.java.Entities.WorkersEntity;

import java.util.List;

public interface WorkerDAO {

    public WorkersEntity save(WorkersEntity e);

    public List<WorkersEntity> list();

    public WorkersEntity getWorkersById(long id);

    public WorkersEntity getWorkerBetweenDates(WorkerDTO workerDTO, String caz);
}
