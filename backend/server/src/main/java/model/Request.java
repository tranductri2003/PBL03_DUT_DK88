package model;

public class Request {
	
	private Integer requestID;
	private String targetID;
	private String requestCode;
	
	public static final Integer REQUEST_CODE_ACTIVE = 0;
	public static final Integer REQUEST_CODE_BAN = 1;
	
	public Request() {
		
	}
	
	public Request(Integer requestID, String targetID, String requestCode) {
		super();
		this.requestID = requestID;
		this.targetID = targetID;
		this.requestCode = requestCode;
	}
	
	
	
}
