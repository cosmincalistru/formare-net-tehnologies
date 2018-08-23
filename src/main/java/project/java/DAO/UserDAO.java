package project.java.DAO;

import project.java.Entities.UsersEntity;

import java.util.List;

public interface UserDAO {

    public UsersEntity save(UsersEntity e);

    public List<UsersEntity> list();

    public UsersEntity getUsersById(long id);

    public UsersEntity getUsersByName(String name);
}
