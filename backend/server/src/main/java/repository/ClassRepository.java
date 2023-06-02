package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.qos.logback.core.joran.sanity.Pair;
import model.QueryStudentClass;
import model.ResponseObject;
import model.Student;
import service.HashService;

public class ClassRepository {
	
	public static Map<String, Object> readClassByStudentID(String studentID) {
		String readClassByStudentIDSQL = "SELECT * FROM StudentClass WHERE StudentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, studentID);
		DatabaseHelper.getInstance().setQuery(readClassByStudentIDSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		List<String> haveClass = new ArrayList<>();
		String wantClass = null;
		try {
			while (rs.next()) {
				Boolean have = rs.getBoolean("have");
				if (!have) wantClass = rs.getString("classID");
				else haveClass.add(rs.getString("classID"));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("wantClass", wantClass);
		tmp.put("haveClass", haveClass);
		return tmp;
	}
	
	public static void insertResetQueryClass(String targetID) {
		String insertResetQueryClassSQL = "INSERT INTO QueryClass VALUES (?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, targetID);
		DatabaseHelper.getInstance().setQuery(insertResetQueryClassSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static void delQueryByTargetID(String targetID) {
		String delStudentClassSQL = "DELETE FROM StudentClass WHERE StudentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, targetID);
		DatabaseHelper.getInstance().setQuery(delStudentClassSQL, params);
		DatabaseHelper.getInstance().updateData();
		String delQueryClassSQL = "DELETE FROM QueryClass WHERE TargetID = ?";
		DatabaseHelper.getInstance().setQuery(delQueryClassSQL, params);
		DatabaseHelper.getInstance().updateData();
	}
	
	public static ResponseObject updateStudentClass(QueryStudentClass query) {
//		ResponseObject tmp = delQueryByTargetID(query.getTargetID());
//		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
//			return tmp;
		String insertQueryClassSQL = "INSERT INTO QueryClass VALUES (?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, query.getTargetID());
		DatabaseHelper.getInstance().setQuery(insertQueryClassSQL, params);
		DatabaseHelper.getInstance().updateData();
		String insertStudentClassSQL = "INSERT INTO StudentClass VALUES (?, ?, ?)";
		params.clear();
		params.put(1, query.getTargetID());
		params.put(2, query.getWantClass());
		params.put(3, false);
		DatabaseHelper.getInstance().setQuery(insertStudentClassSQL, params);
		DatabaseHelper.getInstance().updateData();
		for (String hc : query.getHaveClass()) {
			params.clear();
			params.put(1, query.getTargetID());
			params.put(2, hc);
			params.put(3, true);
			DatabaseHelper.getInstance().setQuery(insertStudentClassSQL, params);
			DatabaseHelper.getInstance().updateData();
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
				QueryStudentClass tmp = new QueryStudentClass();
				tmp.setIdQuery(rs.getInt("idQuery"));
				tmp.setTargetID(rs.getString("targetID"));
				queries.add(tmp);
			}
			for (int i = 0; i < queries.size(); i++) {
				String readClassStudentSQL = "SELECT * FROM StudentClass WHERE studentID = ?";
				params.clear();
				params.put(1, queries.get(i).getTargetID());
				DatabaseHelper.getInstance().setQuery(readClassStudentSQL, params);
				rs = DatabaseHelper.getInstance().readData();
				List<String> hc = new ArrayList<>();
				while (rs.next()) {
					Boolean have = rs.getBoolean("have");
					String classID = rs.getString("classID");
					if (have)
						hc.add(classID);
					else
						queries.get(i).setWantClass(classID);
				}
				queries.get(i).setHaveClass(hc);
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", queries);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
}
