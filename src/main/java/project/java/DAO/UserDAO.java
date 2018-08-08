package project.java.DAO;

import project.java.Entities.UsersEntity;
import project.java.dto.UserDTO;

public interface UserDAO {
		public void insertIntoUsers(UserDTO user);

		public UsersEntity save(UsersEntity u);
}
