package pl.edu.agh.analizer.youtube.reports;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class AllReports {
	
	// Rozwiazanie tymczasowe. Zostanie zast¹pione przez bazê danych.
	private Map<String, List<Report>> reports = new HashMap<String, List<Report>>();
	
	public void addReport(String analyst, Report report) {
		
		if (!reports.containsKey(analyst)) {
			reports.put(analyst, new ArrayList<Report>());
		}
		
		reports.get(analyst).add(report);
	}
	
	public void removeReport(String analyst, Report report) {
		
		if (!reports.containsKey(analyst)) {
			throw new IllegalArgumentException("There are no reports for " + analyst);
		}
		
		if (!reports.get(analyst).contains(report)) {
			throw new IllegalArgumentException("Specified report does not exist");
		}
		
		reports.get(analyst).remove(report);
	}
	
	public List<Report> getAllReports(String analyst) {
		
		if (!reports.containsKey(analyst)) {
			throw new IllegalArgumentException("There are no reports for " + analyst);
		}
		
		return reports.get(analyst);
	}
	
	public List<Report> getAllReports() {

		List<Report> allReports = new ArrayList<Report>();
		
		for (List<Report> l : reports.values()) {
			allReports.addAll(l);
		}
		
		return allReports;
	}

}
