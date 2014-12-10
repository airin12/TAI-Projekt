package pl.edu.agh.analizer.youtube.dao;

import pl.edu.agh.analizer.youtube.model.User;

public interface UserDao {

	int insertUser(User user);
	
	void deleteUser(String name);
}
