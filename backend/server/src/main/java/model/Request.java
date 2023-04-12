package model;

public class Request {
	
	private Integer requestID;
	private String targetID;
	private Integer requestCode;
	
	public static final Integer REQUEST_CODE_ACTIVE = 0;
	public static final Integer REQUEST_CODE_BAN = 1;
	
	public Request() { }
	
	public Request(Integer requestID, String targetID, Integer requestCode) {
		super();
		this.requestID = requestID;
		this.targetID = targetID;
		this.requestCode = requestCode;
	}

	public Integer getRequestID() {
		return requestID;
	}

	public void setRequestID(Integer requestID) {
		this.requestID = requestID;
	}

	public String getTargetID() {
		return targetID;
	}

	public void setTargetID(String targetID) {
		this.targetID = targetID;
	}

	public Integer getRequestCode() {
		return requestCode;
	}

	public void setRequestCode(Integer requestCode) {
		this.requestCode = requestCode;
	}
	
	
}
