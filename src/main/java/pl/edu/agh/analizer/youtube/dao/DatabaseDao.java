package pl.edu.agh.analizer.youtube.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collections;
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

	private static long saveAnalysisAndGetID(Report report, String username) {
		
		String sql = "INSERT INTO ANALYSIS(TITLE, USERNAME) VALUES('" + report.getTitle() + "','" + username + "')";
		System.out.println(sql);
		Connection conn = null;
		long id = -1;

		try {
			conn = dataSource.getConnection();
			Statement statement = conn.createStatement();
			int res = statement.executeUpdate(sql, Statement.RETURN_GENERATED_KEYS);
			System.out.println("res = " + res);

			ResultSet rs = statement.getGeneratedKeys();
			if (rs.next()) {
				id = rs.getLong(2);
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
	
	private static Map<Long, Long> saveRowsAndGetIDs(Report report, Long analysisID) {
		
		Map<Long, Long> ids = new HashMap<Long, Long>();
		
		
		
		return ids;
		
	}

	public static void addReport(Report report, String username) {

		long analysisID = saveAnalysisAndGetID(report, username);
		System.out.println(analysisID);
	}
	
	public static boolean removeRaport(String name){
		return true;
	}

	public static void main(String[] args) {

//		Report report = getReport("Analiza1");
//
//		for (Map.Entry<String, List<Long>> e : report.getValues().entrySet()) {
//			System.out.println(e.getKey());
//			for (Long l : e.getValue()) {
//				System.out.println(l);
//			}
//		}
		addReport(Report.empty(), "palys");
		
		for (String s : getReportsNames()) {
			System.out.println(s);
		}
	}

}
