package service;

import java.util.Map;

import model.QueryStudentClass;
import model.ResponseObject;
import model.Student;
import model.User;
import repository.ClassRepository;
import repository.UserRepository;

public class ClassService {

	public static ResponseObject changeStudentClass(String token, QueryStudentClass query) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change class!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !Student.STATUS_ACTIVE_USER.equals((Integer)token_data.get("status")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		ClassRepository.delQueryByTargetID(query.getTargetID());
		return ClassRepository.updateStudentClass(query);
	}
	
	public static ResponseObject readNewQueryClass(String token, Integer curQueryID) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change class!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !Student.STATUS_ACTIVE_USER.equals((Integer)token_data.get("status")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		return ClassRepository.readNewQuery(curQueryID);
	}
	
}
