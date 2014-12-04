package pl.edu.agh.analizer.youtube.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;
import pl.edu.agh.analizer.youtube.model.BasicAnalysis;
import pl.edu.agh.analizer.youtube.reports.Report;

import com.google.common.collect.Lists;

public class DatabaseDao {

	private static DataSource dataSource;

	static {
		ApplicationContext context = new ClassPathXmlApplicationContext(
				"spring-database.xml");
		dataSource = (DataSource) context.getBean("dataSource");
	}

	@Deprecated
	public static List<Analysis> getAvailableAnalysis() {
		String sql = "SELECT * FROM ANALYSIS";
		Connection conn = null;
		List<Analysis> analysis = new LinkedList<Analysis>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				analysis.add(new BasicAnalysis(rs.getString("TITLE"), "wykres",
						rs.getString("USERNAME")));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return analysis;
	}

	@Deprecated
	public static List<Analysis> getUsersAnalysys(String username) {

		String sql = "SELECT * FROM ANALYSIS WHERE USERNAME = ?";
		Connection conn = null;
		List<Analysis> analysis = new LinkedList<Analysis>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				analysis.add(new BasicAnalysis(rs.getString("TITLE"), "wykres",
						rs.getString("USERNAME")));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return analysis;
	}

	public static List<String> getReportsNames() {
		String sql = "SELECT * FROM ANALYSIS";
		Connection conn = null;
		List<String> names = new LinkedList<String>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				names.add(rs.getString("TITLE"));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return names;
	}

	public static List<String> getUsersReportsNames(String username) {

		String sql = "SELECT * FROM ANALYSIS WHERE USERNAME = ?";
		Connection conn = null;
		List<String> names = new LinkedList<String>();

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				names.add(rs.getString("TITLE"));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		return names;
	}

	private static List<String> getColumnNames(String reportName) {

		List<String> columnNames = new LinkedList<String>();
		String sql = "SELECT AC.* FROM ANALYSIS_COLUMN AC "
				+ "INNER JOIN ANALYSIS AN ON AC.ANALYSISID = AN.ID "
				+ "WHERE AN.TITLE = ?";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, reportName);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				columnNames.add(rs.getString("TITLE"));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}

		return columnNames;
	}

	public static Report getReport(String reportName) {

		List<String> columnNames = getColumnNames(reportName);
		Map<String, List<Long>> values = new HashMap<String, List<Long>>();

		for (String columnName : columnNames) {
			values.put(columnName, new LinkedList<Long>());
		}

		String sql = "SELECT V.VALUE AS VALUE, R.ROW_NUMBER AS ROW_NUMBER, C.TITLE AS TITLE FROM ANALYSIS AN "
				+ "INNER JOIN ANALYSIS_ROW R ON R.ANALYSISID = AN.ID "
				+ "INNER JOIN ANALYSIS_COLUMN C ON C.ANALYSISID = AN.ID "
				+ "INNER JOIN ANALYSIS_VALUE V ON V.ROWID = R.ID AND V.COLUMNID = C.ID "
				+ "WHERE AN.TITLE = ? " + "ORDER BY 2";
		Connection conn = null;

		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, reportName);
			ResultSet rs = ps.executeQuery();

			while (rs.next()) {
				String columnName = rs.getString("TITLE");
				values.get(columnName).add(rs.getLong("VALUE"));
			}

			rs.close();
			ps.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}

		return new Report(columnNames, values, reportName);
	}
	
	private static long executeInsertQueryAndGetID(String query, int idIndex) {
		Connection conn = null;
		long id = -1;
		
		try {
			conn = dataSource.getConnection();
			Statement statement = conn.createStatement();
			int res = statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(idIndex);
			}

			statement.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
		
		return id;
	}
	
	private static void executeUpdate(String query) {
		Connection conn = null;
		
		try {
			conn = dataSource.getConnection();
			Statement statement = conn.createStatement();
			statement.executeUpdate(query);
			statement.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		} finally {
			if (conn != null) {
				try {
					conn.close();
				} catch (SQLException e) {

				}
			}
		}
	}

	private static long saveAnalysisAndGetID(Report report, String username) {
		
		String sql = "INSERT INTO ANALYSIS(TITLE, USERNAME) VALUES('" + report.getTitle() + "','" + username + "')";
		long id = executeInsertQueryAndGetID(sql, 2);
		
		return id;
	}
	
	private static Map<Long, Long> saveRowsAndGetIDs(Report report, Long analysisID) {
		
		Map<Long, Long> ids = new HashMap<Long, Long>();
		
		for (int i = 0; i < report.getRowCount(); i++) {
			String sql = "INSERT INTO ANALYSIS_ROW(ANALYSISID, ROW_NUMBER) VALUES(" + analysisID + "," + i + ")";
			long id = executeInsertQueryAndGetID(sql, 1);
			ids.put((long)i, id);
		}
		
		return ids;
		
	}
	
	private static Map<String, Long> saveColumnsAndGetIDs(Report report, Long analysisID) {
		
		Map<String, Long> ids = new HashMap<String, Long>();
		
		for (String columnName : report.getColumnHeaders()) {
			String sql = "INSERT INTO ANALYSIS_COLUMN(ANALYSISID, TITLE) VALUES(" + analysisID + ",'" + columnName + "')";
			long id = executeInsertQueryAndGetID(sql, 1);
			ids.put(columnName, id);
		}
		
		return ids;
	}
	
	private static void saveValues(Map<Long, Long> rowIDs, Map<String, Long> columnIDs, Report report) {
		
		for (Map.Entry<String, List<Long>> e : report.getValues().entrySet()) {
			for (int i = 0; i < e.getValue().size(); i++) {
				String sql = "INSERT INTO ANALYSIS_VALUE(VALUE, ROWID, COLUMNID) VALUES(" + e.getValue().get(i) + "," + rowIDs.get((long)i) + "," + columnIDs.get(e.getKey()) + ")";
				executeUpdate(sql);
			}
		}
	}
	
	

	public static boolean removeRaport(String name){
		
		String deleteValuesSQL = "DELETE FROM ANALYSIS_VALUE " +
								 "WHERE COLUMNID IN (SELECT C.ID FROM ANALYSIS_COLUMN C " +
								 					"INNER JOIN ANALYSIS AN ON AN.ID = C.ANALYSISID " +
								 					"WHERE AN.TITLE = '" + name + "')";	
		String deleteRowsSql = "DELETE FROM ANALYSIS_ROW " +
							   "WHERE ANALYSISID IN (SELECT ID FROM ANALYSIS " +
							   						"WHERE TITLE = '" + name + "')";
		String deleteColumnsSql = "DELETE FROM ANALYSIS_COLUMN " +
				   				  "WHERE ANALYSISID IN (SELECT ID FROM ANALYSIS " +
				   				  					   "WHERE TITLE = '" + name + "')";
		String deleteAnalysisSql = "DELETE FROM ANALYSIS WHERE TITLE = '" + name + "'";
		
		executeUpdate(deleteValuesSQL);
		executeUpdate(deleteColumnsSql);
		executeUpdate(deleteRowsSql);
		executeUpdate(deleteAnalysisSql);
		
		return true;
	}
	
	public static void addReport(Report report, String username) {

		long analysisID = saveAnalysisAndGetID(report, username);
		Map<Long, Long> rowIDs = saveRowsAndGetIDs(report, analysisID);
		Map<String, Long> columnIDs = saveColumnsAndGetIDs(report, analysisID);
		saveValues(rowIDs, columnIDs, report);
	}
	
	public static void addUser(String username, String password, boolean isAnalyst) {
		
		String sql = "INSERT INTO USERS VALUES('" + username + "','" + password + "',True)";
		executeUpdate(sql);
		
		String sqlRolePrefix = "INSERT INTO USER_ROLES(USERNAME, ROLE) VALUES('" + username + "',";
		String sqlRoleUserSufix = "'ROLE_USER')";
		String sqlRoleAnalystSufix = "'ROLE_ANALYST')";
		
		executeUpdate(sqlRolePrefix + sqlRoleUserSufix);
		
		if (isAnalyst) {
			executeUpdate(sqlRolePrefix + sqlRoleAnalystSufix);
		}
	}
	
	public static void deleteUser(String username) {
		
		String deleteUserRolesSql = "DELETE FROM USER_ROLES WHERE USERNAME = '" + username + "'";
		String deleteUserSql = "DELETE FROM USERS WHERE USERNAME = '" + username + "'";
		
		executeUpdate(deleteUserRolesSql);
		executeUpdate(deleteUserSql);
	}

	public static void main(String[] args) {
		
		removeRaport("PLOOT");
		
		Map<String, List<Long>> map = new HashMap<String, List<Long>>();
		map.put("VALUE_HEADER", Lists.newArrayList(1l,8l,3l,4l));
		SimpleDateFormat f = new SimpleDateFormat("yyyy-mm-dd");
		
		try {
			map.put("DATE", Lists.newArrayList(f.parse("2014-08-01").getTime(),f.parse("2014-09-01").getTime(),f.parse("2014-10-01").getTime(),f.parse("2014-11-01").getTime()));
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		System.out.println(map);
		Report report = new Report(Lists.newArrayList("VALUE_HEADER", "DATE"), map, "PLOOT");
		addReport(report, "palys");

		Report ret = getReport("PLOOT");

		for (Map.Entry<String, List<Long>> e : ret.getValues().entrySet()) {
			System.out.println(e.getKey());
			for (Long l : e.getValue()) {
				System.out.println(l);
			}
		}
		
		for (String s : getReportsNames()) {
			System.out.println(s);
		}
		
		//deleteUser("bbb");
		
		//removeRaport("JAVA_ANALIZA");
	}

}
