package model;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

public class DataHandler {

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
			Student student = new Student(userName, name, phoneNumber, "", studentID, Student.STATUS_NEW_USER);
			ResponseObject tmp = isValidProfile(student, hashPass);
			if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
				return tmp;
			String checkUserExistSQL = "SELECT * FROM NUser WHERE userName = ?";
			HashMap<Integer, Object> params = new HashMap();
			params.put(1, userName);
			DatabaseHelper.getInstance().setQuery(checkUserExistSQL, params);
			if (DatabaseHelper.getInstance().readData().next())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "User already existed!", null);
			String createAccountSQL = "INSERT INTO NUser VALUES (?, ?, ?, ?, ?)";
			params.clear();
			params.put(1, student.getUserName());
			params.put(2, DataHasher.hash(hashPass));
			params.put(3, student.getName());
			params.put(4, student.getPhoneNumber());
			params.put(5, student.getRoleCode());
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
			// TODO Auto-generated catch block

		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject insertImage(String fileName, String userName) {
		String insertImageSQL = "INSERT INTO Image VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, fileName);
		params.put(2, userName);
		DatabaseHelper.getInstance().setQuery(insertImageSQL, params);
		if (!DatabaseHelper.getInstance().updateData())
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
		return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", null);
	}

	public static ResponseObject readUserInfo(String userName, String hashPass) {

		try {
			String doubleHashPass = DataHasher.hash(hashPass);
			User user = new User();
			HashMap<Integer, Object> params = new HashMap();
			String readUserSQL = "SELECT * FROM NUser WHERE userName = ? AND passWord = ?";
			params.put(1, userName);
			params.put(2, doubleHashPass);
			DatabaseHelper.getInstance().setQuery(readUserSQL, params);
			ResultSet rs = DatabaseHelper.getInstance().readData();
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Username or password is not correct!", null);
			while (rs.next()) {
				user.setUserName(rs.getString("userName"));
				user.setName(rs.getString("name"));
				user.setPhoneNumber(rs.getString("phoneNumber"));
				user.setRoleCode(rs.getInt("roleCode"));
			}
			Map<String, Object> token_data = new HashMap();
			token_data.put("userName", user.getUserName());
			token_data.put("userRole", user.getRoleCode());
			if (user.getRoleCode() == User.ROLE_CODE_ADMIN) {
				Admin admin = new Admin();
				admin.setUserName(user.getUserName());
				admin.setName(user.getName());
				admin.setPhoneNumber(user.getPhoneNumber());
				admin.setRoleCode(user.getRoleCode());
				String readAdminSQL = "SELECT * FROM Admin WHERE userName = ?";
				params.clear();
				params.put(1, userName);
				DatabaseHelper.getInstance().setQuery(readAdminSQL, params);
				rs = DatabaseHelper.getInstance().readData();
				if (!rs.isBeforeFirst())
					return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
				while (rs.next())
					admin.setEmail(rs.getString("email"));
				admin.setToken(TokenProvider.generateToken(token_data));
				return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", admin);
			} else if (user.getRoleCode() == User.ROLE_CODE_STUDENT) {
				Student student = new Student();
				student.setUserName(user.getUserName());
				student.setName(user.getName());
				student.setPhoneNumber(user.getPhoneNumber());
				student.setRoleCode(user.getRoleCode());
				String readStudentSQL = "SELECT * FROM Student WHERE userName = ?";
				params.clear();
				params.put(1, userName);
				DatabaseHelper.getInstance().setQuery(readStudentSQL, params);
				rs = DatabaseHelper.getInstance().readData();
				if (!rs.isBeforeFirst())
					return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
				while (rs.next()) {
					student.setStudentID(rs.getString("studentID"));
					student.setStatus(rs.getInt("status"));
				}
				token_data.put("status", student.getStatus());
				student.setToken(TokenProvider.generateToken(token_data));
				return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", student);
			} else {
				//...
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block

		}

		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}

}
