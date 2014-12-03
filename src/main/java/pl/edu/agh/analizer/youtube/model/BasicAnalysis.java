package pl.edu.agh.analizer.youtube.model;

import pl.edu.agh.analizer.youtube.interfaces.Analysis;

public class BasicAnalysis implements Analysis {
	
	private final String name;
	
	private final String type;
	
	private final String user;

	public BasicAnalysis(String name, String type, String user) {
		super();
		this.name = name;
		this.type = type;
		this.user = user;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getType() {
		return type;
	}

	@Override
	public String getUser() {
		return user;
	}

}
