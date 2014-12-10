package pl.edu.agh.analizer.youtube.dao;


import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.analizer.youtube.model.User;
import pl.edu.agh.analizer.youtube.model.User.UserRole;

public class UserDaoImpl implements UserDao {
	
	private DataSourceTransactionManager transactionManager;
	
	private JdbcTemplate jdbcTemplate;
	
	public UserDaoImpl() {

		super();
		DataSource dataSource = transactionManager.getDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public void setTransactionManager(DataSourceTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertUser(User user) {

		String sql = "INSERT INTO USERS VALUES(?,?,TRUE)";
		
		Object[] params = new Object[]{user.getName(), user.getPassword()};
		int[] types = new int[]{Types.VARCHAR, Types.VARCHAR};
		int res = jdbcTemplate.update(sql, params, types);
		
		if (res > 0) {
			sql = "INSERT INTO USER_ROLES(USERNAME, ROLE) VALUES (?,?)";
			params = new Object[]{user.getName(), UserRole.USER.getDatabaseValue()};
			res += jdbcTemplate.update(sql, params, types);
			
			if (user.getRole() == UserRole.ANALYST) {
				params[1] = user.getRole().getDatabaseValue();
				res += jdbcTemplate.update(sql, params, types);
			}
		}
		
		return res;
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public void deleteUser(String name) {

		String sql = "DELETE FROM USERS WHERE USERNAME = ?";
		Object[] params = new Object[]{name};
		jdbcTemplate.update(sql, params);
		
		sql = "DELETE FROM USER_ROLES WHERE USERNAME = ?";
		jdbcTemplate.update(sql, params);
		
	}

}
