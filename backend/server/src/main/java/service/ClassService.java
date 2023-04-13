package service;

import java.util.Map;

import model.QueryStudentClass;
import model.ResponseObject;
import repository.ClassRepository;
import repository.UserRepository;

public class ClassService {

	public static ResponseObject changeStudentClass(String token, QueryStudentClass query) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change class!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!query.getTargetID().equals(token_data.get("studentID").toString()) || !UserRepository.isStudentActive(query.getTargetID()))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to change class!", null);
		ClassRepository.delQueryByTargetID(query.getTargetID());
		return ClassRepository.updateStudentClass(query);
	}
	
	public static ResponseObject readNewQueryClass(String token, Integer curQueryID) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to change class!", null);
		return ClassRepository.readNewQuery(curQueryID);
	}
	
}
