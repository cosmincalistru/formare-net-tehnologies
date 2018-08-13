package com.training.dao;

import com.training.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectDao extends JpaRepository<Project, Long> {

    List<Project> findAll();

    Project save(Project s);

    void deleteById(Long id);
}
