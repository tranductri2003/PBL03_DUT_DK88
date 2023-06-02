package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.Group;
import model.QueryStudentClass;
import model.ResponseObject;
import model.Student;

public class GroupRepository {
	
	public static void insertGroup(String groupID) {
		String insertGroupSQL = "INSERT INTO NGroup VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, groupID);
		params.put(2, Group.STATUS_NEW_GROUP);
		DatabaseHelper.getInstance().setQuery(insertGroupSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static void insertGroupStudentClass(String groupID, String studentID, String classID) {
		String insertGroupStudentClassSQL = "INSERT INTO GroupStudentClass VALUES (?, ?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, groupID);
		params.put(2, studentID);
		params.put(3, classID);
		DatabaseHelper.getInstance().setQuery(insertGroupStudentClassSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static void insertStudentInGroup(String groupID, String studentID) {
		String insertStudentInGroupSQL = "INSERT INTO StudentInGroup VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, studentID);
		params.put(2, groupID);
		DatabaseHelper.getInstance().setQuery(insertStudentInGroupSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static Integer readGroupStatus(String groupID) {
		String readStatusGroupSQL = "SELECT * FROM NGroup WHERE groupID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, groupID);
		DatabaseHelper.getInstance().setQuery(readStatusGroupSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			while (rs.next()) {
				return rs.getInt("status");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return Group.STATUS_NOT_EXIST_YET_GROUP;
	}
	
	public static String readGroupIDByStudentID(String studentID) {
		String readGroupIDByStudentIDSQL = "SELECT * FROM StudentInGroup WHERE studentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, studentID);
		DatabaseHelper.getInstance().setQuery(readGroupIDByStudentIDSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			while (rs.next()) {
				return rs.getString("groupID");
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	public static List<String> readGroupVote(String groupID) {
		String readVoteGroupSQL = "SELECT * FROM GroupStudent WHERE groupID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, groupID);
		DatabaseHelper.getInstance().setQuery(readVoteGroupSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		List<String> voteYes = new ArrayList<>();
		try {
			while (rs.next()) {
				Boolean yes = rs.getBoolean("vote");
				if (yes)
					voteYes.add(rs.getString("studentID"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return voteYes;
	}
	
	public static Group readGroupByID(String groupID) {
		Group group = new Group();
		group.setGroupID(groupID);
		group.setStatus(readGroupStatus(groupID));
		group.setVoteYes(readGroupVote(groupID));
		return group;
	}
	
	public static void delGroup(String groupID) {
		
		HashMap<Integer, Object> params = new HashMap<>();
		params.put(1, groupID);
		String delGroupStudentSQL = "DELETE FROM GroupStudent WHERE groupID = ?";
		DatabaseHelper.getInstance().setQuery(delGroupStudentSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delGroupStudentClassSQL = "DELETE FROM GroupStudentClass WHERE groupID = ?";
		DatabaseHelper.getInstance().setQuery(delGroupStudentClassSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delGroupSQL = "DELETE FROM NGroup WHERE groupID = ?";
		DatabaseHelper.getInstance().setQuery(delGroupSQL, params);
		DatabaseHelper.getInstance().updateData();
				
	}
	
	public static void delStudentInGroup(String studentID) {
		HashMap<Integer, Object> params = new HashMap<>();
		params.put(1, studentID);
		String delStudentInGroupSQL = "DELETE FROM StudentInGroup WHERE studentID = ?";
		DatabaseHelper.getInstance().setQuery(delStudentInGroupSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static List<String> groupInvole(String studentID, String classID) {
		
		String readGroupInvoleSQL = "SELECT * FROM GroupStudentClass WHERE studentID = ? AND classID = ?";
		HashMap<Integer, Object> params = new HashMap<>();
		params.put(1, studentID);
		params.put(2, classID);
		DatabaseHelper.getInstance().setQuery(readGroupInvoleSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		List<String> group = new ArrayList<>();
		try {
			while (rs.next())
				group.add(rs.getString("groupID"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return group;
	}
	
	public static void saveGroup(Group group) {
		
		String updateGroupStatusSQL = "UPDATE NGroup SET status = ? WHERE groupID = ?";
		HashMap<Integer, Object> params = new HashMap<>();
		params.put(1, group.getStatus());
		params.put(2, group.getGroupID());
		DatabaseHelper.getInstance().setQuery(updateGroupStatusSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delGroupStudentSQL = "DELETE FROM GroupStudent WHERE groupID = ?";
		params.clear();
		params.put(1, group.getGroupID());
		DatabaseHelper.getInstance().setQuery(delGroupStudentSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		List<String> students = Arrays.asList(group.getGroupID().split("^"));
		String updateVoteSQL = "INSERT INTO GroupStudent VALUES (?, ?, ?)";
		for (String studentID : students) {
			params.clear();
			params.put(1, group.getGroupID());
			params.put(2, studentID);
			params.put(3, group.getVoteYes().contains(studentID));
			DatabaseHelper.getInstance().setQuery(updateVoteSQL, params);
			DatabaseHelper.getInstance().updateData();
		}
		
	}
	
}
