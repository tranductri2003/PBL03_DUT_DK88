package model;

public class User {
	
	public static final Integer ROLE_CODE_ADMIN = 0;
	public static final Integer ROLE_CODE_STUDENT = 1;
	
	private String userName;
	private String name;
	private String phoneNumber;
	private Integer roleCode;
	
	public User() {
		super();
	}

	public User(String userName, String name, String phoneNumber, Integer roleCode) {
		super();
		this.userName = userName;
		this.name = name;
		this.phoneNumber = phoneNumber;
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

	public Integer getRoleCode() {
		return roleCode;
	}

	public void setRoleCode(Integer roleCode) {
		this.roleCode = roleCode;
	}
	
}
