package pl.edu.agh.analizer.youtube.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;
import pl.edu.agh.analizer.youtube.model.BasicAnalysis;
import pl.edu.agh.analizer.youtube.model.TestAnalysis;

public class DatabaseDao {
	
	private static DataSource dataSource;
	
	static {
		ApplicationContext context = new ClassPathXmlApplicationContext("spring-database.xml");
		dataSource = (DataSource) context.getBean("dataSource");
	}

	public static List<Analysis> getAvailableAnalysis(){
		String sql = "SELECT * FROM ANALYSIS";
		Connection conn = null;
		List<Analysis> analysis = new LinkedList<Analysis>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				analysis.add(new BasicAnalysis(
						rs.getString("TITLE"),
						"wykres",
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
	
	public static List<Analysis> getUsersAnalysys(String username){
		
		String sql = "SELECT * FROM ANALYSIS WHERE USERNAME = ?";
		Connection conn = null;
		List<Analysis> analysis = new LinkedList<Analysis>();
		
		try {
			conn = dataSource.getConnection();
			PreparedStatement ps = conn.prepareStatement(sql);
			ps.setString(1, username);
			ResultSet rs = ps.executeQuery();
			
			while (rs.next()) {
				analysis.add(new BasicAnalysis(
						rs.getString("TITLE"),
						"wykres",
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
}
