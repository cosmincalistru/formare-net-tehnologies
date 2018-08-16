package com.training.dao;

import com.training.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends JpaRepository<Employee,Long> {

   List<Employee> findAll();

   Employee save(Employee employee);

    void deleteById(Long aLong);

    Employee findByLastNameAndEmail(String lastName,String email);
}
