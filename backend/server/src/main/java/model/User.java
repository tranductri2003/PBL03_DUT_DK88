package model;

public class User {
	
	public static final Integer ROLE_CODE_ADMIN = 0;
	public static final Integer ROLE_CODE_STUDENT = 1;
	
	private String userName;
	private String name;
	private String phoneNumber;
	private String token;
	private Integer roleCode;
	
	public User() {
		super();
	}

	public User(String userName, String name, String phoneNumber, String token, Integer roleCode) {
		super();
		this.userName = userName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.token = token;
		this.roleCode = roleCode;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Integer getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}
	
}
