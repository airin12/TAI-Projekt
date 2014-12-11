package pl.edu.agh.analizer.youtube.dao;

import pl.edu.agh.analizer.youtube.model.User;


public interface UserDao {

	
	/**
	 * Inserts user into database 
	 * 
	 * @param user user to insert
	 * @return number of rows inserted
	 */
	int insertUser(User user);
	
	/**
	 * Deletes user with specified name 
	 * @param name name of user to dalete
	 */
	void deleteUser(String name);
	
	/**
	 * Selects user with specified name
	 * @param name name of user
	 * @return selected user or null if user does not exist
	 */
	User selectUser(String name);
}
