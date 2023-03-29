package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class QueryData {

	public static ResponseObject insertStudent(String userName, String hashPass, String studentID, String name,
			String phoneNumber) {
		try {
			Student student = new Student(userName, name, phoneNumber, studentID, Student.STATUS_NEW_USER);
			String checkUserExistSQL = "SELECT * FROM NUser WHERE userName = ?";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(checkUserExistSQL, params);
			if (DatabaseHelper.getInstance().readData().next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User already existed!", null);
			String createAccountRole = "INSERT INTO USER_TYPE VALUES (?, ?)";
			params.clear();
			params.put(1, student.getToken());
			params.put(2, User.USER_TYPE_STUDENT);
			DatabaseHelper.getInstance().setQuery(createAccountRole, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			String createAccountSQL = "INSERT INTO NUser VALUES (?, ?, ?, ?, ?)";
			params.clear();
			params.put(1, student.getUserName());
			params.put(2, DataHasher.hash(hashPass));
			params.put(3, student.getName());
			params.put(4, student.getPhoneNumber());
			params.put(5, student.getToken());
			DatabaseHelper.getInstance().setQuery(createAccountSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			String createProfileSQL = "INSERT INTO Student VALUES (?, ?, ?)";
			params.clear();
			params.put(1, student.getUserName());
			params.put(2, student.getStudentID());
			params.put(3, student.getStatus());
			DatabaseHelper.getInstance().setQuery(createProfileSQL, params);
			if (!DatabaseHelper.getInstance().updateData())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Account successfully created!", student);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

	public static ResponseObject readAdminInfo(String userName, String hashPass) {
		try {
			Admin res = new Admin();
			String readAdminSQL = "SELECT * FROM NUser "
					+ "JOIN Admin ON NUser.userName = ? AND NUser.userName = Admin.userName";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(readAdminSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User does not exist!", null);
			while (rs.next()) {
				res.setUserName(userName);
				res.setName(rs.getString("name"));
				res.setPhoneNumber(rs.getString("phoneNumber"));
				res.setEmail(rs.getString("email"));
				String pass = rs.getString("passWord");
				if (!pass.equals(DataHasher.hash(hashPass)))
					return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Incorrect password!", null);
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", res);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

	public static ResponseObject readStudentInfo(String userName, String hashPass) {
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
				if (!pass.equals(DataHasher.hash(hashPass)))
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

	public static ResponseObject getUserRoleByUserName(String userName) {
		try {
			String identifyUserTypeAndExistSQL = "SELECT type_code FROM NUser "
					+ "JOIN USER_TYPE ON NUser.userName = ? AND NUser.token = USER_TYPE.token";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(identifyUserTypeAndExistSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User does not exist!", null);
			String userType = rs.getString("type_code");
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", userType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

	public static ResponseObject getUserRoleByToken(String token) {
		try {
			String identifyUserTypeAndExistSQL = "SELECT type_code FROM NUser "
					+ "JOIN USER_TYPE ON NUser.token = ? AND NUser.token = USER_TYPE.token";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, token);
			DatabaseHelper.getInstance().setQuery(identifyUserTypeAndExistSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User does not exist!", null);
			String userType = rs.getString("type_code");
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", userType);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject getImageOwner(String fileName) {
		try {
			String getImageOwnerSQL = "SELECT * FROM Image WHERE fileName = ?";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, fileName);
			DatabaseHelper.getInstance().setQuery(getImageOwnerSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Image not found!", null);
			String token = rs.getString("token");
			return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", token);
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject insertImage(String fileName, String token) {
		String insertImageSQL = "INSERT INTO Image VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, fileName);
		params.put(2, token);
		DatabaseHelper.getInstance().setQuery(insertImageSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}

	public static ResponseObject readUserInfo(String userName, String hashPass) {

		ResponseObject tmp = getUserRoleByUserName(userName);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		String userType = tmp.getData().toString();
		if (userType.equals(User.USER_TYPE_ADMIN))
			return readAdminInfo(userName, hashPass);
		else if (userType.equals(User.USER_TYPE_STUDENT))
			return readStudentInfo(userName, hashPass);
		else {
			// new role in future..
		}

		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

}
