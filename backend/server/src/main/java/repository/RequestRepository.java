package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.ActiveRequest;
import model.BanRequest;
import model.Request;
import model.ResponseObject;

public class RequestRepository {
	
	private static final Integer PAGE_SIZE = 10;
	
	public static void delRequestIfExist(Request request) {
		
		String delActiveRequestSQL = "DELETE FROM ActiveRequest WHERE requestID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(delActiveRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delImageRequest = "DELETE FROM ImageRequest WHERE requestID = ?";
		DatabaseHelper.getInstance().setQuery(delImageRequest, params);
		DatabaseHelper.getInstance().updateData();
		
		String delBanRequestSQL = "DELETE FROM BanRequest WHERE requestID = ?";
		DatabaseHelper.getInstance().setQuery(delBanRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
		String delRequestSQL = "DELETE FROM Request WHERE requestID = ?";
		DatabaseHelper.getInstance().setQuery(delRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		
	}
	
	public static ResponseObject saveRequest(Request request) {
		String saveRequestSQL = "INSERT INTO Request VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getTargetID());
		params.put(2, request.getRequestCode());
		DatabaseHelper.getInstance().setQuery(saveRequestSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().updateData();
		try {
			if (!rs.next())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			request.setRequestID(rs.getInt(1));
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject saveActiveRequest(ActiveRequest request) {
		String saveActiveRequestSQL = "INSERT INTO ActiveRequest VALUES (?, ?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		params.put(2, request.getImageFront());
		params.put(3, request.getImageBack());
		DatabaseHelper.getInstance().setQuery(saveActiveRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", request);
	}
	
	public static ResponseObject saveBanRequest(BanRequest request) {
		if (request.getImageProof().size() > 20) 
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Too many images!", null); 
		String saveBanRequestSQL = "INSERT INTO BanRequest VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		params.put(2, request.getMoreDetail());
		DatabaseHelper.getInstance().setQuery(saveBanRequestSQL, params);
		DatabaseHelper.getInstance().updateData();
		for (String fileName : request.getImageProof()) {
			String saveImageProofSQL = "INSERT INTO ImageRequest VALUES (?, ?)";
			params.clear();
			params.put(1, request.getRequestID());
			params.put(2, fileName);
			DatabaseHelper.getInstance().setQuery(saveImageProofSQL, params);
			DatabaseHelper.getInstance().updateData();
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", request);
	}
	
	public static ResponseObject readActiveRequest(Request request) {
		ActiveRequest activeRequest = new ActiveRequest();
		activeRequest.setRequestID(request.getRequestID());
		activeRequest.setRequestCode(request.getRequestCode());
		activeRequest.setTargetID(request.getTargetID());
		String readRequestSQL = "SELECT * FROM ActiveRequest WHERE requestID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(readRequestSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			while (rs.next()) {
				activeRequest.setImageFront(rs.getString("imageFront"));
				activeRequest.setImageBack(rs.getString("imageBack"));
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", activeRequest);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject readBanRequest(Request request) {
		BanRequest banRequest = new BanRequest();
		banRequest.setRequestID(request.getRequestID());
		banRequest.setRequestCode(request.getRequestCode());
		banRequest.setTargetID(request.getTargetID());
		String readRequestSQL = "SELECT * FROM BanRequest WHERE requestID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, request.getRequestID());
		DatabaseHelper.getInstance().setQuery(readRequestSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			while (rs.next())
				banRequest.setMoreDetail(rs.getString("moreDetail"));
			String readImageRequestSQL = "SELECT * FROM ImageRequest WHERE requestID = ?";
			DatabaseHelper.getInstance().setQuery(readImageRequestSQL, params);
			rs = DatabaseHelper.getInstance().readData();
			List<String> requestImages = new ArrayList<>();
			while (rs.next())
				requestImages.add(rs.getString("fileName"));
			banRequest.setImageProof(requestImages);
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", banRequest);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject readRequestByPage(Integer pageNumber) {
		Integer offset = (pageNumber - 1) * PAGE_SIZE;
		String readRequestByPageSQL = "SELECT * FROM Request ORDER BY requestID "
				+ "OFFSET ? ROWS FETCH NEXT ? ROWS ONLY";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, offset);
		params.put(2, PAGE_SIZE);
		DatabaseHelper.getInstance().setQuery(readRequestByPageSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		List<Request> requests = new ArrayList<>();
		try {
			while (rs.next()) {
				Request tmp = new Request();
				tmp.setRequestID(rs.getInt("requestID"));
				tmp.setTargetID(rs.getString("targetID"));
				tmp.setRequestCode(rs.getInt("requestCode"));
				requests.add(tmp);
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", requests);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
}
