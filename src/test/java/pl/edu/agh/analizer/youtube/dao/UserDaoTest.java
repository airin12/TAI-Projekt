package pl.edu.agh.analizer.youtube.dao;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.transaction.TransactionConfiguration;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.analizer.youtube.model.User;
import pl.edu.agh.analizer.youtube.model.User.UserRole;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("file:src/main/webapp/WEB-INF/main-context.xml")
@Transactional
@TransactionConfiguration(defaultRollback=true)
public class UserDaoTest {

	@Autowired
	private UserDao userDao;
	
	@Test
	public void insertUserTest() {
		
		User user = new User("UNAME", "PASSWORD", UserRole.USER);
		int res = userDao.insertUser(user);
		Assert.assertEquals(2, res);
	}
	
	@Test
	public void insertAnalystTest() {
		
		User user = new User("UNAME", "PASSWORD", UserRole.ANALYST);
		int res = userDao.insertUser(user);
		Assert.assertEquals(3, res);
	}
	
	@Test(expected = DuplicateKeyException.class)
	public void addUserTwiceTest() {
		
		User user = new User("UNAME", "PASSWORD", UserRole.USER);
		int res = userDao.insertUser(user);
		Assert.assertEquals(2, res);
		
		res = userDao.insertUser(user);
		Assert.assertEquals(0, res);
		
	}
	
	@Test
	public void insertAndSelectTest() {
		
		String userName = "UNAME";
		User user = new User(userName, "PASSWORD", UserRole.USER);
		userDao.insertUser(user);
		
		User selected = userDao.selectUser(userName);
		
		Assert.assertEquals(user, selected);
	}
	
	@Test
	public void insertDeleteAndSelectTest() {
		
		String userName = "UNAME";
		User user = new User(userName, "PASSWORD", UserRole.USER);
		userDao.insertUser(user);
		userDao.deleteUser(userName);
		
		User selected = userDao.selectUser(userName);
		Assert.assertNull(selected);
	}
}
