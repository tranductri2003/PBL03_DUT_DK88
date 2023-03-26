package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class QueryData {
	
	public static ResponseObject insertStudent(String userName, String hashPass, String studentID, String name, String phoneNumber) {
		try {
			String checkUserExistSQL = "SELECT * FROM NUser WHERE userName = ?" ;
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(checkUserExistSQL, params);
			if (DatabaseHelper.getInstance().readData().next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User already existed!", null);
			String createAccountSQL = "INSERT INTO NUser VALUES (?, ?, ?, ?)";
			params.clear();
			params.put(1, userName);
			params.put(2, hashPass);
			params.put(3, name);
			params.put(4, phoneNumber);
			DatabaseHelper.getInstance().setQuery(createAccountSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			String createProfileSQL = "INSERT INTO Student VALUES (?, ?, ?)";
			params.clear();
			params.put(1, userName);
			params.put(2, studentID);
			params.put(3, Student.STATUS_NEW_USER);
			DatabaseHelper.getInstance().setQuery(createProfileSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Account successfully created!", new Student(userName, name, phoneNumber, studentID, Student.STATUS_NEW_USER));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject readStudent(String userName, String hashPass) {
		try {
			Student res = new Student();
			String readStudentSQL = "SELECT * FROM NUser "
					+ "JOIN Student ON NUser.userName = ? AND NUser.userName = Student.userName";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(readStudentSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User does not exist!", null);
			while (rs.next()) {
				res.setUserName(userName);
				res.setStudentID(rs.getString("studentID"));
				res.setName(rs.getString("name"));
				res.setPhoneNumber(rs.getString("phoneNumber"));
				res.setStatus(rs.getInt("status"));
				String pass = rs.getString("passWord");
				if (!pass.equals(hashPass))
					return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Incorrect password!", null);
			}
			String readHaveAndWantClassSQL = "SELECT * FROM USER_CLASS WHERE studentID = ?";
			params.clear();
			params.put(1, res.getStudentID());
			DatabaseHelper.getInstance().setQuery(readHaveAndWantClassSQL, params);
			rs = DatabaseHelper.getInstance().readData();
			while (rs.next()) {
				String classID = rs.getString("classID");
				Boolean isHave = rs.getBoolean("have");
				if (isHave)
					res.addHaveClass(classID);
				else
					res.setNeedClass(classID);
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			
		} 
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
}
