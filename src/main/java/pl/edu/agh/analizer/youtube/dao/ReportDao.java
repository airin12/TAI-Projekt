package pl.edu.agh.analizer.youtube.dao;

import java.util.List;

import pl.edu.agh.analizer.youtube.reports.Report;

public interface ReportDao {
	
	List<String> selectReportsNames();
	
	List<String> selectUsersReportsNames(String username);
	
	Report selectReport(String reportName);
	
	void deleteReport(String reportName);
	
	int insertReport(Report report, String username);

}
