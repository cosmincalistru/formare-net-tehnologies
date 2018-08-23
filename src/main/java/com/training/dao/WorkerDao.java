package com.training.dao;

import com.training.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository
public interface WorkerDao extends JpaRepository<Worker, Long> {
    List<Worker> findAll();

    Worker save(Worker s);

    void deleteById(Long id);


    @Query("select w from Worker w inner join w.employee e inner join e.user u where u.username =?1 and (((Day(?2-w.date_start))<=0 and (Day(?3-w.date_start))>=0) or ((Day(?2-w.date_end))<=0 and (day(?3-w.date_end))>=0) or ((day(?2-w.date_start))>=0 and (day(?3-w.date_end))<=0) or(w.date_end is null and (Day(?3-w.date_start))>=0) or (cast(?3 as date) is null and (Day(?2-w.date_end))<=0) or (w.date_end is null and cast(?3 as date) is null and (Day(?2 - w.date_start))<=0))")
    List<Worker> findByUsername(String username, Date startEmpDate, Date endEmpDate);

    @Query("select w from Worker w inner join w.project p where p.name like ?1 and (((Day(?2-w.date_start))<=0 and (Day(?3-w.date_end))<=0 and (day(?3-w.date_start))>=0)  or ((day(?2-w.date_start))<=0 and (day(?3-w.date_end))<=0) or ((Day(?2-w.date_start))>0) or (w.date_end is null and cast(?3 as date) is not null))")
    List<Worker> findByProject(String projectName, Date startProjDate, Date endProjDate);
}
