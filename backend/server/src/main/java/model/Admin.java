package model;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Admin extends User {
	private String email;
	
	public Admin() {
		super();
	}

	public Admin(String userName, String name, String phoneNumber, String email) {
		super(userName, name, phoneNumber, User.ROLE_CODE_ADMIN);
		this.email = email;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	
}
