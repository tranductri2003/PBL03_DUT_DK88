package repository;

import java.util.HashMap;

import model.ActiveRequest;
import model.BanRequest;
import model.Request;
import model.ResponseObject;

public class RequestRepository {

	public static void delRequestIfExist(Request request) {
		
		String delRequestSQL = "DELETE FROM Request WHERE requestID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(delRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delActiveRequestSQL = "DELETE FROM ActiveRequest WHERE requestID = ?";
		params.clear();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(delActiveRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delBanRequestSQL = "DELETE FROM BanRequest WHERE requestID = ?";
		params.clear();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(delBanRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
	}
	
	public static ResponseObject saveRequest(Request request) {
		String saveRequestSQL = "INSERT INTO Request VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getTargetID());
		params.put(2, request.getRequestCode());
		DatabaseHelper.getInstance().setQuery(saveRequestSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
	public static ResponseObject saveActiveRequest(ActiveRequest request) {
		String saveActiveRequestSQL = "INSERT INTO ActiveRequest VALUES (?, ?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		params.put(2, request.getImageFront());
		params.put(3, request.getImageBack());
		DatabaseHelper.getInstance().setQuery(saveActiveRequestSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", request);
	}
	
	public static ResponseObject saveBanRequest(BanRequest request) {
		String saveBanRequestSQL = "INSERT INTO BanRequest VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		params.put(2, request.getMoreDetail());
		DatabaseHelper.getInstance().setQuery(saveBanRequestSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		for (String fileName : request.getImageProof()) {
			String saveImageProofSQL = "INSERT INTO ImageRequest VALUES (?, ?)";
			params.clear();
			params.put(1, request.getRequestID());
			params.put(2, fileName);
			DatabaseHelper.getInstance().setQuery(saveImageProofSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", request);
	}
	
}
