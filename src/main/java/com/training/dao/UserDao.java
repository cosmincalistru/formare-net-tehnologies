package com.training.dao;

import com.training.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserDao extends JpaRepository<User, Long> {

    List<User> findAll();

    User save(User s);

    void deleteById(Long id);

    User findByUsername(String Username);
}
