package service;

import java.util.Map;

import model.ActiveRequest;
import model.BanRequest;
import model.Request;
import model.ResponseObject;
import model.Student;
import model.User;
import repository.ClassRepository;
import repository.GroupRepository;
import repository.RequestRepository;
import repository.UserRepository;

public class RequestService {

	public static ResponseObject saveRequest(Request request, String token) {
		ResponseObject tmp = RequestRepository.saveRequest(request);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		if (request instanceof ActiveRequest)
			return RequestRepository.saveActiveRequest((ActiveRequest) request);
		if (request instanceof BanRequest)
			return RequestRepository.saveBanRequest((BanRequest) request);
		return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Unknown request!", null);
	}
	
	public static ResponseObject readRequestByPage(String token, Integer pageNumber) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to read request!", null);
		return RequestRepository.readRequestByPage(Math.max(pageNumber, 1));
	}
	
	public static ResponseObject readRequestDetail(Request request, String token) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to read request!", null);
		if (request.getRequestCode().equals(Request.REQUEST_CODE_ACTIVE))
			return RequestRepository.readActiveRequest(request);
		return RequestRepository.readBanRequest(request);
	}
	
	public static ResponseObject handleRequest(Request request, String token, Boolean isAccepted) {
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to handle request!", null);
		RequestRepository.delRequestIfExist(request);
		if (!isAccepted) return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
		if (request.getRequestCode().equals(Request.REQUEST_CODE_ACTIVE))
			if (UserRepository.readStudentStatus(request.getTargetID()).equals(Student.STATUS_NEW_USER))
				return UserRepository.updateAccountStatus(request.getTargetID(), Student.STATUS_ACTIVE_NOGROUP_USER);
		ClassRepository.delQueryByTargetID(request.getTargetID());
		ClassRepository.insertResetQueryClass(request.getTargetID());
		String groupID = GroupRepository.readGroupIDByStudentID(request.getTargetID());
		GroupRepository.delGroup(groupID);
		for (String id : groupID.split("-"))
			GroupService.leaveGroup(id);
		return UserRepository.updateAccountStatus(request.getTargetID(), Student.STATUS_BAN_USER);
	}
	
}
