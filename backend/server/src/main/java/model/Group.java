package model;

import java.util.ArrayList;
import java.util.List;

public class Group {
	
	private String groupID;
	private Integer status;
	private List<String> voteYes;
		
	public static final Integer STATUS_NOT_EXIST_YET_GROUP = -1;
	public static final Integer STATUS_NEW_GROUP = 0;
	public static final Integer STATUS_TRADE_GROUP = 1;
	public static final Integer STATUS_END_TRADE_GROUP = 2;
	
	public static final Integer MAX_SIZE = 5;
	
	public Group() {
		super();
		voteYes = new ArrayList<>();
	}

	public Group(String groupID, Integer status, List<String> voteYes) {
		super();
		this.groupID = groupID;
		this.status = status;
		this.voteYes = voteYes;
	}

	public String getGroupID() {
		return groupID;
	}

	public void setGroupID(String groupID) {
		this.groupID = groupID;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<String> getVoteYes() {
		return voteYes;
	}

	public void setVoteYes(List<String> voteYes) {
		this.voteYes = voteYes;
	}

	
	
}
