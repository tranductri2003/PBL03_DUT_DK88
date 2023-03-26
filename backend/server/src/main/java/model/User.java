package model;

public class User {
	private String userName;
	private String name;
	private String phoneNumber;
	
	public User() {
		super();
	}

	public User(String userName, String name, String phoneNumber) {
		super();
		this.userName = userName;
		this.name = name;
		this.phoneNumber = phoneNumber;
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
	
}
