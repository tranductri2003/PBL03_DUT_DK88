package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Pattern;

public class QueryData {

//	public static ResponseObject updateUserPassWord(String token, String oldPass, String newPass) {
//		String checkUserPermissionSQL = "SELECT * FROM NUser WHERE token = ? AND passWord = ?";
//		HashMap<Integer, Object> params = new HashMap();
//		params.put(1, token);
//		params.put(2, oldPass);
//		DatabaseHelper.getInstance().setQuery(checkUserPermissionSQL, params);
//		try {
//			if (!DatabaseHelper.getInstance().readData().next())
//				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "!", null);
//		} catch (SQLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
//	
//	public static ResponseObject updateProfile(String token, String name, String phoneNumber, String email) {
//		
//	}
	
	public static ResponseObject isValidProfile(User user, String pass) {
		if (pass == null || pass.length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Password is empty!", null);
		if (user.getName() == null || user.getName().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Name is empty!", null);
		if (user.getPhoneNumber() == null || user.getPhoneNumber().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "PhoneNumber is empty!", null);;
		if (user.getUserName() == null || user.getUserName().length() == 0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Username is empty!", null);;
		if (user instanceof Student) {
			Student tmp = (Student) user;
			if (tmp.getStudentID() == null || tmp.getStudentID().length() == 0)
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Student ID is empty!", null);;
		} else if (user instanceof Admin) {
			Admin tmp = (Admin) user;
			String regex = "^(.+)@(.+)$";
			Pattern pattern = Pattern.compile(regex); 
			if (tmp.getEmail() == null || tmp.getEmail().length() == 0 || !pattern.matcher(tmp.getEmail()).matches())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Email is not valid!", null);;
		} else {
			// ...
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}
	
	public static ResponseObject insertStudent(String userName, String hashPass, String studentID, String name,
			String phoneNumber) {
		try {
			Student student = new Student(userName, name, phoneNumber, studentID, Student.STATUS_NEW_USER);
			ResponseObject tmp = isValidProfile(student, hashPass);
			if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
				return tmp;
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
				res.setToken(rs.getString("token"));
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
				res.setToken(rs.getString("token"));
				res.setStatus(rs.getInt("status"));
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

	public static ResponseObject readUserInfo(String userName, String hashPass, String roleCode) {

		if (roleCode.equals(User.USER_TYPE_ADMIN))
			return readAdminInfo(userName, hashPass);
		else if (roleCode.equals(User.USER_TYPE_STUDENT))
			return readStudentInfo(userName, hashPass);
		else {
			// new role in future..
		}

		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

}
