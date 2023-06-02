package service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import model.Group;
import model.ResponseObject;
import model.Student;
import model.User;
import repository.ClassRepository;
import repository.GroupRepository;
import repository.UserRepository;

public class GroupService {
	
	public static Boolean isValidGroup(String groupID) {
		List<String> students = Arrays.asList(groupID.split("-"));
		if (students.size() > Group.MAX_SIZE || students.size() < 2) return false;
		List<List<String>> classHave = new ArrayList<>();
		List<String> classWant = new ArrayList<>();
		for (String studentID : students) {
//			if (!UserRepository.readStudentStatus(studentID).equals(Student.STATUS_ACTIVE_NOGROUP_USER)) return false;
			Map<String, Object> tmp = ClassRepository.readClassByStudentID(studentID);
			classHave.add((List<String>) tmp.get("haveClass"));
			classWant.add((String) tmp.get("wantClass"));
		}
		for (int i = 0; i < students.size(); i++)
			if (!classHave.get((i + 1) % students.size()).contains(classWant.get(i))) return false;
		return true;
	}
	
	public static ResponseObject getJoinedGroupID(String token, String studentID) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		Integer studentStatus = UserRepository.readStudentStatus((String)token_data.get("studentID"));
		if (!studentID.equals((String)token_data.get("studentID")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to get joined group info!", null);
		if (!User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) || Student.STATUS_BAN_USER.equals(studentStatus) || Student.STATUS_NEW_USER.equals(studentStatus))
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", GroupRepository.readGroupIDByStudentID(studentID));
	}
	
	public static void createGroup(String groupID) {
		GroupRepository.insertGroup(groupID);
		List<String> students = Arrays.asList(groupID.split("-"));
		List<String> classWant = new ArrayList<>();
		for (String studentID : students) {
			Map<String, Object> tmp = ClassRepository.readClassByStudentID(studentID);
			classWant.add((String) tmp.get("wantClass"));
		}
		for (int i = 0; i < students.size(); i++) {
			GroupRepository.insertGroupStudentClass(groupID, students.get(i), classWant.get(i));
			GroupRepository.insertGroupStudentClass(groupID, students.get((i + 1) % students.size()), classWant.get(i));
		}
		
	}
	
	public static void joinGroup(String studentID, String groupID) {
		UserRepository.updateAccountStatus(studentID, Student.STATUS_ACTIVE_INGROUP_USER);
		GroupRepository.insertStudentInGroup(groupID, studentID);
	}
	
	public static void leaveGroup(String studentID) {
		UserRepository.updateAccountStatus(studentID, Student.STATUS_ACTIVE_NOGROUP_USER);
		GroupRepository.delStudentInGroup(studentID);
	}
	
	public static ResponseObject readGroupInfo(String token, String groupID) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		Integer studentStatus = UserRepository.readStudentStatus((String)token_data.get("studentID"));
		if (!User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) || Student.STATUS_BAN_USER.equals(studentStatus) || Student.STATUS_NEW_USER.equals(studentStatus))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		if (!isValidGroup(groupID))
			return new ResponseObject(ResponseObject.RESPONSE_OUTDATE_DATA, "Group not exist, waiting to system refresh and try again!", null);
		Group group = GroupRepository.readGroupByID(groupID);
		//group.setStatus(Math.max(group.getStatus(), Group.STATUS_NEW_GROUP));
		if (group.getStatus().equals(Group.STATUS_NOT_EXIST_YET_GROUP))
			group.setStatus(Group.STATUS_NEW_GROUP);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", group);
	}
	
	public static ResponseObject voteGroup(String token, Map<String, String> data) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		String studentID = data.get("studentID");
		String groupID = data.get("groupID");
		List<String> students = Arrays.asList(groupID.split("-"));
		if (!students.contains(studentID))
			return new ResponseObject(ResponseObject.RESPONSE_OUTDATE_DATA, "You not allow to vote this group!", null);
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !studentID.equals((String)token_data.get("studentID")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account not allow to vote!", null);
		if (!isValidGroup(groupID))
			return new ResponseObject(ResponseObject.RESPONSE_OUTDATE_DATA, "Group not exist, waiting to system refresh and try again!", null);
		Group group = GroupRepository.readGroupByID(groupID);
		if (group.getStatus().equals(Group.STATUS_NOT_EXIST_YET_GROUP)) {
			createGroup(groupID);
			group.setStatus(group.STATUS_NEW_GROUP);
		}
		if (group.getVoteYes().contains(studentID)) {
			group.getVoteYes().remove(groupID);
			leaveGroup(studentID);
		} else {
			group.getVoteYes().add(studentID);
			joinGroup(studentID, groupID);
			if (group.getVoteYes().size() == students.size()) {
				group.setStatus(group.getStatus() + 1);
				group.getVoteYes().clear();
			}
		}
		if (group.getStatus().equals(Group.STATUS_END_TRADE_GROUP)) {
			for (String id : students)
				leaveGroup(id);
			group.setStatus(Group.STATUS_NEW_GROUP);
		}
		GroupRepository.saveGroup(group);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
}
