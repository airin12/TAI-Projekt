package pl.edu.agh.analizer.youtube.model;

public class User {
	
	public enum UserRole {
		ANALYST {@Override public String getDatabaseValue() {return "ROLE_ANALYST";}},
		USER {@Override public  String getDatabaseValue() {return "ROLE_USER";}};
		
		public abstract String getDatabaseValue();
		
		public static UserRole of(String role) {
			for (UserRole u : values()) {
				if(u.getDatabaseValue().equals(role)) {
					return u;
				}
			}
			return null;
		}
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		User other = (User) obj;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (role != other.role)
			return false;
		return true;
	}
	
	

}
