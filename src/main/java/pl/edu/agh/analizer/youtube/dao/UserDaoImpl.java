package pl.edu.agh.analizer.youtube.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import javax.sql.DataSource;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.annotation.Transactional;

import pl.edu.agh.analizer.youtube.model.User;
import pl.edu.agh.analizer.youtube.model.User.UserRole;

public class UserDaoImpl implements UserDao {

	private DataSourceTransactionManager transactionManager;

	private JdbcTemplate jdbcTemplate;

	public UserDaoImpl() {

		super();
	}

	public void setTransactionManager(
			DataSourceTransactionManager transactionManager) {
		this.transactionManager = transactionManager;
		DataSource dataSource = transactionManager.getDataSource();
		jdbcTemplate = new JdbcTemplate(dataSource);
	}

	@Override
	@Transactional(rollbackFor = Exception.class)
	public int insertUser(User user) {

		String sql = "INSERT INTO USERS VALUES(?,?,TRUE)";

		Object[] params = new Object[] { user.getName(), user.getPassword() };
		int[] types = new int[] { Types.VARCHAR, Types.VARCHAR };
		int res = jdbcTemplate.update(sql, params, types);

		if (res > 0) {
			sql = "INSERT INTO USER_ROLES(USERNAME, ROLE) VALUES (?,?)";
			params = new Object[] { user.getName(),
					UserRole.USER.getDatabaseValue() };
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
		
		Object[] params = new Object[] { name };
		String sql = "DELETE FROM USER_ROLES WHERE USERNAME = ?";
		jdbcTemplate.update(sql, params);
		

		sql = "DELETE FROM USERS WHERE USERNAME = ?";
		jdbcTemplate.update(sql, params);

	}
	
	private class UserHolder {
		
		private User user;
		
		public void setUser(User user) {
			this.user = user;
		}
		
		public User getUser() {
			return user;
		}
	}

	@Override
	public User selectUser(String name) {

		String sql = "SELECT U.USERNAME AS NAME, U.PASSWORD AS PASS, UR.ROLE AS ROLE FROM USERS U "
				+ "INNER JOIN USER_ROLES UR ON UR.USERNAME = U.USERNAME "
				+ "WHERE U.USERNAME = ?";
		
		Object[] params = new Object[] {name};
		final UserHolder userHolder = new UserHolder();
		
		jdbcTemplate.query(sql, params, new RowCallbackHandler() {

			@Override
			public void processRow(ResultSet rs) throws SQLException {
				userHolder.setUser(new User(rs.getString("NAME"), rs.getString("PASS"), UserRole.of(rs.getString("ROLE"))));
			}
			
		});
		
		return userHolder.getUser();
	}

}
