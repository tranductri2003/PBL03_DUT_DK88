package service;

import java.util.Map;
import java.util.regex.Pattern;

import model.Admin;
import model.ResponseObject;
import model.Student;
import model.User;
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
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change password!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		ResponseObject tmp = UserRepository.readUser(userName, oldDoubleHashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		User user = (User) tmp.getData();
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")) && !user.getUserName().equals((String)token_data.get("userName")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to change password!", null);
		return UserRepository.updatePassword(userName, newDoubleHashPass);
	}
	
	public static ResponseObject changePublicInfo(String token, User user) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change public info!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!user.getUserName().equals((String)token_data.get("userName")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to change public info!", null);
		return UserRepository.updatePublicInfo(user);
	}
	
	
}
