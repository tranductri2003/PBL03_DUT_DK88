package model;

import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper; 
import com.fasterxml.jackson.databind.ObjectWriter; 

public class QueryStudentClass {

	private Integer idQuery;
	private String targetID;
	private List<String> haveClass;
	private String wantClass;
	
	public QueryStudentClass() { }
	
	public QueryStudentClass(Integer idQuery, String targetID, List<String> haveClass, String wantClass) {
		super();
		this.idQuery = idQuery;
		this.targetID = targetID;
		this.haveClass = haveClass;
		this.wantClass = wantClass;
	}

	public Integer getIdQuery() {
		return idQuery;
	}

	public void setIdQuery(Integer idQuery) {
		this.idQuery = idQuery;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public List<String> getHaveClass() {
		return haveClass;
	}

	public void setHaveClass(List<String> haveClass) {
		this.haveClass = haveClass;
	}

	public String getWantClass() {
		return wantClass;
	}

	public void setWantClass(String wantClass) {
		this.wantClass = wantClass;
	}

	@Override
	public String toString() {
		ObjectWriter ow = (ObjectWriter) new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			return ow.writeValueAsString(this);
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}
	
}
