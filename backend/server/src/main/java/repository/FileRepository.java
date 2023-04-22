package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.ResponseObject;

public class FileRepository {

	public static ResponseObject getImageOwner(String fileName) {
		try {
			String getImageOwnerSQL = "SELECT * FROM Image WHERE fileName = ?";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, fileName);
			DatabaseHelper.getInstance().setQuery(getImageOwnerSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Image not found!", null);
			String userName = rs.getString("owner");
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", userName);
		} catch (SQLException e) {

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject insertImage(String fileName, String userName) {
		String insertImageSQL = "INSERT INTO Image VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, fileName);
		params.put(2, userName);
		DatabaseHelper.getInstance().setQuery(insertImageSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
	public static void delImageIfExist(String fileName) {
		String delImageSQL = "DELETE FROM Image WHERE fileName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, fileName);
		DatabaseHelper.getInstance().setQuery(delImageSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
}
