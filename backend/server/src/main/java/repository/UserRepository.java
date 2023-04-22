package repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

import model.Admin;
import model.ResponseObject;
import model.Student;
import model.User;
import service.HashService;

public class UserRepository {
	
	public static Boolean isUserExist(String userName) {
		String checkUserExistSQL = "SELECT * FROM NUser WHERE userName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, userName);
		DatabaseHelper.getInstance().setQuery(checkUserExistSQL, params);
		try {
			return DatabaseHelper.getInstance().readData().next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static Boolean isStudentExist(String studentID) {
		String checkStudentExistSQL = "SELECT * FROM Student WHERE studentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, studentID);
		DatabaseHelper.getInstance().setQuery(checkStudentExistSQL, params);
		try {
			return DatabaseHelper.getInstance().readData().next();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static Boolean isStudentActive(String studentID) {
		String checkStudentActiveSQL = "SELECT status FROM Student WHERE studentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, studentID);
		DatabaseHelper.getInstance().setQuery(checkStudentActiveSQL, params);
		try {
			ResultSet tmp = DatabaseHelper.getInstance().readData();
			tmp.next();
			Integer status = tmp.getInt("status");
			return status.equals(Student.STATUS_ACTIVE_USER);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static ResponseObject updateAccountStatus(String studentID, Integer status) {
		String updateStatusSQL = "UPDATE Student SET status = ? WHERE studentID = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.put(1, status);
		params.put(2, studentID);
		DatabaseHelper.getInstance().setQuery(updateStatusSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Account status update successfully!", null); 
	}
	
	public static ResponseObject createAccount(User user, String hashPass) {
		String createAccountSQL = "INSERT INTO NUser VALUES (?, ?, ?, ?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, user.getUserName());
		params.put(2, HashService.hash(hashPass));
		params.put(3, user.getName());
		params.put(4, user.getPhoneNumber());
		params.put(5, user.getRoleCode());
		DatabaseHelper.getInstance().setQuery(createAccountSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Account successfully created!", null); 
	}
	
	public static ResponseObject createStudentProfile(Student student) {
		String createProfileSQL = "INSERT INTO Student VALUES (?, ?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, student.getUserName());
		params.put(2, student.getStudentID());
		params.put(3, student.getStatus());
		DatabaseHelper.getInstance().setQuery(createProfileSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Account successfully created!", student);
	}
	
	public static ResponseObject createAdminProfile(Admin admin) {
		String createProfileSQL = "INSERT INTO Student VALUES (?, ?)";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, admin.getUserName());
		params.put(2, admin.getEmail());
		DatabaseHelper.getInstance().setQuery(createProfileSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Account successfully created!", admin);
	}
	
	public static ResponseObject readUser(String userName, String doubleHashPass) {
		User user = new User();
		HashMap<Integer, Object> params = new HashMap();
		String loginSQL = "SELECT * FROM NUser WHERE userName = ? AND passWord = ?";
		params.put(1, userName);
		params.put(2, doubleHashPass);
		DatabaseHelper.getInstance().setQuery(loginSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Username or password is not correct!", null);
			while (rs.next()) {
				user.setUserName(rs.getString("userName"));
				user.setName(rs.getString("name"));
				user.setPhoneNumber(rs.getString("phoneNumber"));
				user.setRoleCode(rs.getInt("roleCode"));
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Login successfully!", user);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject readStudentProfile(User user) {
		Student student = new Student();
		student.setUserName(user.getUserName());
		student.setName(user.getName());
		student.setPhoneNumber(user.getPhoneNumber());
		student.setRoleCode(user.getRoleCode());
		String readStudentSQL = "SELECT * FROM Student WHERE userName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, user.getUserName());
		DatabaseHelper.getInstance().setQuery(readStudentSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			while (rs.next()) {
				student.setStudentID(rs.getString("studentID"));
				student.setStatus(rs.getInt("status"));
			}
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", student);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject readAdminProfile(User user) {
		Admin admin = new Admin();
		admin.setUserName(user.getUserName());
		admin.setName(user.getName());
		admin.setPhoneNumber(user.getPhoneNumber());
		admin.setRoleCode(user.getRoleCode());
		String readAdminSQL = "SELECT * FROM Admin WHERE userName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, user.getUserName());
		DatabaseHelper.getInstance().setQuery(readAdminSQL, params);
		ResultSet rs = DatabaseHelper.getInstance().readData();
		try {
			if (!rs.isBeforeFirst())
				return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
			while (rs.next())
				admin.setEmail(rs.getString("email"));
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Login success!", admin);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with database!", null);
	}
	
	public static ResponseObject updatePassword(String userName, String newDoubleHashPass) {
		String updatePassSQL = "UPDATE NUser SET passWord = ? WHERE userName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, newDoubleHashPass);
		params.put(2, userName);
		DatabaseHelper.getInstance().setQuery(updatePassSQL, params);
		DatabaseHelper.getInstance().updateData();
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Password update successfully!", null);
	}
	
	public static ResponseObject updatePublicInfo(User user) {
		String updateUserInfoSQL = "UPDATE NUser SET name = ?, phoneNumber = ? WHERE userName = ?";
		HashMap<Integer, Object> params = new HashMap();
		params.clear();
		params.put(1, user.getName());
		params.put(2, user.getPhoneNumber());
		params.put(3, user.getUserName());
		DatabaseHelper.getInstance().setQuery(updateUserInfoSQL, params);
		DatabaseHelper.getInstance().updateData();
		if (user.getRoleCode().equals(User.ROLE_CODE_ADMIN)) {
			Admin admin = (Admin) user;
			String updateAdminInfoSQL = "UPDATE Admin SET email = ? WHERE userName = ?";
			params.clear();
			params.put(1, admin.getEmail());
			params.put(2, admin.getUserName());
			DatabaseHelper.getInstance().setQuery(updateUserInfoSQL, params);
			DatabaseHelper.getInstance().updateData();
		}
		return new ResponseObject(ResponseObject.RESPONSE_OK, "Public info update successfully!", null);
	}
	
	
}
