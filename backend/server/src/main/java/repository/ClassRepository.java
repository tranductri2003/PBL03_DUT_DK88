package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import model.QueryStudentClass;
import model.ResponseObject;
import model.Student;
import service.HashService;

public class ClassRepository {

	public static ResponseObject delQueryByTargetID(String targetID) {
		String delStudentClassSQL = "DELETE FROM StudentClass WHERE StudentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, targetID);
		DatabaseHelper.getInstance().setQuery(delStudentClassSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		String delQueryClassSQL = "DELETE FROM QueryClass WHERE TargetID = ?";
		DatabaseHelper.getInstance().setQuery(delQueryClassSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
	public static ResponseObject updateStudentClass(QueryStudentClass query) {
//		ResponseObject tmp = delQueryByTargetID(query.getTargetID());
//		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
//			return tmp;
		String insertQueryClassSQL = "INSERT INTO QueryClass VALUES (?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, query.getTargetID());
		DatabaseHelper.getInstance().setQuery(insertQueryClassSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		String insertStudentClassSQL = "INSERT INTO StudentClass VALUES (?, ?, ?)";
		params.clear();
		params.put(1, query.getTargetID());
		params.put(2, query.getWantClass());
		params.put(3, false);
		DatabaseHelper.getInstance().setQuery(insertStudentClassSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		for (String hc : query.getHaveClass()) {
			params.clear();
			params.put(1, query.getTargetID());
			params.put(2, hc);
			params.put(3, true);
			DatabaseHelper.getInstance().setQuery(insertStudentClassSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Update class successfully!", null);
	}
	
	public static ResponseObject readNewQuery(Integer curQueryID) {
		String readNewQuerySQL = "SELECT * FROM QueryClass WHERE idQuery > ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, curQueryID);
		DatabaseHelper.getInstance().setQuery(readNewQuerySQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		List<QueryStudentClass> queries = new ArrayList<>();
		try {
			while (rs.next()) {
				QueryStudentClass tmp1 = new QueryStudentClass();
				tmp1.setIdQuery(rs.getInt("idQuery"));
				tmp1.setTargetID(rs.getString("targetID"));
				String readClassStudentSQL = "SELECT * FROM StudentClass WHERE studentID = ?";
				params.clear();
				params.put(1, tmp1.getTargetID());
				DatabaseHelper.getInstance().setQuery(readNewQuerySQL, params);
				ResultSet tmp2 = DatabaseHelper.getInstance().readData();
				List<String> hc = new ArrayList<>();
				while (tmp2.next()) {
					Boolean have = tmp2.getBoolean("have");
					String classID = tmp2.getString("classID");
					if (have)
						hc.add(classID);
					else
						tmp1.setWantClass(classID);
				}
				tmp1.setHaveClass(hc);
				queries.add(tmp1);
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", queries);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
}
