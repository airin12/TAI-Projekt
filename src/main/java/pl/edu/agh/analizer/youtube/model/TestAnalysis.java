package pl.edu.agh.analizer.youtube.model;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;

public class TestAnalysis implements Analysis{

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "testowa nazwa";
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return "wykres";
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return "marcin";
	}

}
