package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Student extends User {

	private String studentID;
	private Integer status;
	
	public static final Integer STATUS_NEW_USER = 0;
	public static final Integer STATUS_ACTIVE_USER = 1;
	public static final Integer STATUS_BAN_USER = 2;
	
	public Student() {
		super();
	}
	
	public Student(String userName, String name, String phoneNumber, String studentID, Integer status) {
		super(userName, name, phoneNumber, User.ROLE_CODE_STUDENT);
		this.studentID = studentID;
		this.status = status;
	}
	
	public Student(String userName, String name, String phoneNumber, String studentID) {
		super(userName, name, phoneNumber, User.ROLE_CODE_STUDENT);
		this.studentID = studentID;
		this.status = STATUS_NEW_USER;
	}

	public String getStudentID() {
		return studentID;
	}

	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
	
	
}
