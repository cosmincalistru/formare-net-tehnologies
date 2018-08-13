package com.training.dao;

import com.training.model.Worker;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface WorkerDao extends JpaRepository<Worker, Long> {
    List<Worker> findAll();

    Worker save(Worker s);

    void deleteById(Long id);
}
