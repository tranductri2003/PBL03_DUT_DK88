package model;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

public class Student extends User {

	private String studentID;
	private Integer status;
	private List<String> haveClass;
	private String needClass;
	
	public static final int STATUS_NEW_USER = 0;
	public static final int STATUS_ACTIVE_USER = 1;
	public static final int STATUS_BAN_USER = 2;
	
	public Student() {
		super();
		haveClass = new ArrayList<>();
	}
	
	public Student(String userName, String name, String phoneNumber, String studentID, Integer status) {
		super(userName, name, phoneNumber);
		this.studentID = studentID;
		this.status = status;
		this.haveClass = new ArrayList<>();
		this.needClass = new String();
	}

	public Student(String userName, String name, String phoneNumber, String studentID, Integer status, List<String> haveClass, String needClass) {
		super(userName, name, phoneNumber);
		this.studentID = studentID;
		this.status = status;
		this.haveClass = haveClass;
		this.needClass = needClass;
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

	public List<String> getHaveClass() {
		return haveClass;
	}
	
	public void addHaveClass(String classID) {
		haveClass.add(classID);
	}

	public void setHaveClass(List<String> haveClass) {
		this.haveClass = haveClass;
	}

	public String getNeedClass() {
		return needClass;
	}

	public void setNeedClass(String needClass) {
		this.needClass = needClass;
	}

	@Override
	public String toString() {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			
		}
		return "";
		
	}
	
}
