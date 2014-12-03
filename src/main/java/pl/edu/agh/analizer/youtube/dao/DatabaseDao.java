package pl.edu.agh.analizer.youtube.dao;

import java.util.LinkedList;
import java.util.List;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;
import pl.edu.agh.analizer.youtube.model.TestAnalysis;

public class DatabaseDao {

	//na razie prosty mock
	public static List<Analysis> getAvailableAnalysis(){
		List<Analysis> analysis = new LinkedList<Analysis>();
		analysis.add(new TestAnalysis());
		return analysis;
	}
	
	public static List<Analysis> getUsersAnalysys(String username){
		List<Analysis> analysis = new LinkedList<Analysis>();
		analysis.add(new TestAnalysis());
		return analysis;
	}
}
