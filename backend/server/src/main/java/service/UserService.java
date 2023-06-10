package service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import model.Admin;
import model.Group;
import model.ResponseObject;
import model.Student;
import model.User;
import repository.ClassRepository;
import repository.GroupRepository;
import repository.UserRepository;

public class UserService {
	
	public static ResponseObject isValidProfile(User user, String pass) {
		if (pass == null || pass.length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Password is empty!", null);
		if (user.getName() == null || user.getName().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Name is empty!", null);
		if (user.getPhoneNumber() == null || user.getPhoneNumber().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "PhoneNumber is empty!", null);;
		if (user.getUserName() == null || user.getUserName().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Username is empty!", null);;
		if (user instanceof Student) {
			Student tmp = (Student) user;
			if (tmp.getStudentID() == null || tmp.getStudentID().length() == 0)
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Student ID is empty!", null);;
		} else if (user instanceof Admin) {
			Admin tmp = (Admin) user;
			String regex = "^(.+)@(.+)$";
			Pattern pattern = Pattern.compile(regex); 
			if (tmp.getEmail() == null || tmp.getEmail().length() == 0 || !pattern.matcher(tmp.getEmail()).matches())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Email is not valid!", null);;
		} else {
			// ...
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
	public static ResponseObject createStudentAccount(Student student, String hashPass) {
		ResponseObject tmp = isValidProfile(student, hashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		if (UserRepository.isUserExist(student.getUserName()))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User already existed!", null);
		if (UserRepository.isStudentExist(student.getStudentID()))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Student already existed!", null);
		tmp = UserRepository.createAccount(student, hashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		student.setStatus(Student.STATUS_NEW_USER);
		return UserRepository.createStudentProfile(student);
	}
	
	public static ResponseObject createAdminAccount(Admin admin, String hashPass, String token) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to create account!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!token_data.get("roleCode").equals(User.ROLE_CODE_ADMIN))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to create admin account!", null);
		ResponseObject tmp = isValidProfile(admin, hashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		if (UserRepository.isUserExist(admin.getUserName()))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User already existed!", null);
		tmp = UserRepository.createAccount(admin, hashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		return UserRepository.createAdminProfile(admin);
	}
	
	public static ResponseObject login(String userName, String hashPass) {
		String doubleHashPass = HashService.hash(hashPass);
		ResponseObject tmp = UserRepository.readUser(userName, doubleHashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		User user = (User) tmp.getData();
		if (user.getRoleCode().equals(User.ROLE_CODE_STUDENT))
			return UserRepository.readStudentProfile(user);
		else if (user.getRoleCode().equals(User.ROLE_CODE_ADMIN))
			return UserRepository.readAdminProfile(user);
		else {
			//...
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Unknown role..", null); 
		}
	}
	
	public static ResponseObject changePassword(String token, String userName, String oldHashPass, String newHashPass) {
		String oldDoubleHashPass = HashService.hash(oldHashPass);
		String newDoubleHashPass = HashService.hash(newHashPass);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		ResponseObject tmp = UserRepository.readUser(userName, oldDoubleHashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		User user = (User) tmp.getData();
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")) && !user.getUserName().equals((String)token_data.get("userName")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to change password!", null);
		Integer studentStatus = UserRepository.readStudentStatus((String)token_data.get("studentID"));
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && (Student.STATUS_BAN_USER.equals(studentStatus) || Student.STATUS_NEW_USER.equals(studentStatus)))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		return UserRepository.updatePassword(userName, newDoubleHashPass);
	}
	
	public static ResponseObject changePublicInfo(String token, Map<String, Object> data) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		Integer studentStatus = UserRepository.readStudentStatus((String)token_data.get("studentID"));
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && (Student.STATUS_BAN_USER.equals(studentStatus) || Student.STATUS_NEW_USER.equals(studentStatus)))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		String userName = (String) data.get("userName");
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !userName.equals((String)token_data.get("userName")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to change public info!", null);
		return UserRepository.updatePublicInfo(data);
	}
	
	public static ResponseObject readStudentInfo(String studentID) {
		return UserRepository.readPublicInfo(studentID);
	}
	
	public static ResponseObject readListStudentInfo(List<String> listStudentID) {
		if (listStudentID.size() > Group.MAX_SIZE)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Can't not read too much student info at a time!", null);
		List<Map<String, Object>> res = new ArrayList<>();
		for (String studentID : listStudentID) {
			ResponseObject tmp = UserRepository.readPublicInfo(studentID);
			if (tmp.getRespCode() == ResponseObject.RESPONSE_OK)
				res.add((Map<String, Object>) tmp.getData());
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", res);
	}
	
	public static ResponseObject readAllStudentID(String token) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Only admin allow to read all student id!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", UserRepository.readAllStudentID());
	}
	
	public static ResponseObject changeUserStatus(String token, String studentID, Integer status) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Only admin allow to change student status!", null);
		if (status.equals(Student.STATUS_BAN_USER) || status.equals(Student.STATUS_NEW_USER)) {
			ClassRepository.delQueryByTargetID(studentID);
			ClassRepository.insertResetQueryClass(studentID);
			String groupID = GroupRepository.readGroupIDByStudentID(studentID);
			if (groupID != null) {
				GroupRepository.delGroup(groupID);
				for (String id : groupID.split("-"))
					GroupService.leaveGroup(id);
			}
		} else {
			Integer oldStatus = UserRepository.readStudentStatus(studentID);
			if (oldStatus.equals(Student.STATUS_BAN_USER) || oldStatus.equals(Student.STATUS_NEW_USER))
				status = Student.STATUS_ACTIVE_NOGROUP_USER;
			else
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Error!", null);
		}
		return UserRepository.updateAccountStatus(studentID, status);
	}
	
}
