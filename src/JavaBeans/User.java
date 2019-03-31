package JavaBeans;

import Enums.ClientType;

public class User {
	
	private long id;
	private String userName;
	private String password;
	private Long companyId;
	private ClientType type;
	
	public User(long id, String userName, String password, Long companyId, ClientType type) {
		this(userName, password, companyId, type);
		this.id = id;
	}
	
	public User(String userName, String password, Long companyId, ClientType type) {
		this(userName, password, type);
		this.companyId = companyId;
	}
	
	public User(String userName, String password, ClientType type) {
		super();
		this.userName = userName;
		this.password = password;
		this.companyId = null;
		this.type = type;
	}
	
	public User() {
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Long getCompanyId() {
		return companyId;
	}

	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}

	public ClientType getType() {
		return type;
	}

	public void setType(ClientType type) {
		this.type = type;
	}

	
}
