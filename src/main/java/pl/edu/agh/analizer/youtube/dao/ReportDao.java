package pl.edu.agh.analizer.youtube.dao;

import java.util.List;

import pl.edu.agh.analizer.youtube.reports.Report;

public interface ReportDao {
	
	/**
	 * Selects names of all reports
	 * @return list of names of reports
	 */
	List<String> selectReportsNames();
	
	/**
	 * Selects names of reports created by specified user
	 * @param username name of user
	 * @return list of selected names
	 */
	List<String> selectUsersReportsNames(String username);
	
	/**
	 * Selects report with specified title
	 * @param reportName title of selected report
	 * @return selected report or null if report with specified title does not exist
	 */
	Report selectReport(String reportName);
	
	/**
	 * Deletes report with specified title
	 * @param reportName title of report
	 */
	void deleteReport(String reportName);
	
	/**
	 * Inserts report created by specified user
	 * @param report report to insert
	 * @param username creators username
	 * @return 1 if successfully inserted, else 0
	 */
	int insertReport(Report report, String username);

}
