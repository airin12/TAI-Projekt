package pl.edu.agh.analizer.youtube.model;

public class User {
	
	public enum UserRole {
		ANALYST {@Override public String getDatabaseValue() {return "ROLE_ANALYST";}},
		USER {@Override public  String getDatabaseValue() {return "ROLE_USER";}};
		
		public abstract String getDatabaseValue();
	}
	
	private String name;
	
	private String password;
	
	private UserRole role;

	public User(String name, String password, UserRole role) {
		super();
		this.name = name;
		this.password = password;
		this.role = role;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public UserRole getRole() {
		return role;
	}

	public void setRole(UserRole role) {
		this.role = role;
	}
	
	

}
