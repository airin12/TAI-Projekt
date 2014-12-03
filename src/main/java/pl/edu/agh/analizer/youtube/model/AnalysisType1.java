package pl.edu.agh.analizer.youtube.model;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;

public class AnalysisType1 implements Analysis{

	private String name;
	private String type;
	private String user;
	
	
	
	public void setName(String name) {
		this.name = name;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setUser(String user) {
		this.user = user;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return name;
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return type;
	}

	@Override
	public String getUser() {
		// TODO Auto-generated method stub
		return user;
	}

}
