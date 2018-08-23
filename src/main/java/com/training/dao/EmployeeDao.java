package com.training.dao;

import com.training.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeDao extends JpaRepository<Employee,Long> {

   List<Employee> findAll();

   Employee save(Employee employee);

    void deleteById(Long aLong);

    Employee findByLastNameAndEmail(String lastName,String email);

    @Query("select e from Employee e inner join e.user u where u.username like ?1")
    Employee findByUsername(String username);
}
