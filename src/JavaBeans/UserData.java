package JavaBeans;


import Enums.ClientType;


public class UserData {
	
	
	private String userName;
	private String password;
	private ClientType type;
	
	public UserData(String userName, String password, ClientType type) {
		this.userName = userName;
		this.password = password;
		this.type = type;
	}

	public String getUserName() {
		return userName;
	}
	
	
	public void setUserName(String userId) {
		this.userName = userId;
	}
	
	public ClientType getType() {
		return type;
	}
	public void setType(ClientType type) {
		this.type = type;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	
	
	

}
