package model;

public class User {
	
	public static final String USER_TYPE_ADMIN = "ADMIN";
	public static final String USER_TYPE_STUDENT = "STUDENT";
	
	private String userName;
	private String name;
	private String phoneNumber;
	private String token;
	
	public User() {
		super();
	}

	public User(String userName, String name, String phoneNumber) {
		super();
		this.userName = userName;
		this.name = name;
		this.phoneNumber = phoneNumber;
		this.token = DataHasher.hash(userName, name, phoneNumber);
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

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}
	
	
	
}
