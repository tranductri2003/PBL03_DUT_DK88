package controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.Admin;
import model.ResponseObject;
import model.Student;
import model.User;
import repository.DatabaseHelper;
import service.HashService;
import service.TokenService;
import service.UserService;

@RestController
@RequestMapping(path = "/api/v1/User")
public class UserController {
	
	@GetMapping("/")
	ResponseEntity<ResponseObject> test() {
		DatabaseHelper.getInstance();
		String tmp1 = HashService.hash("secuoikhanh10namnua");
		String tmp2 = HashService.hash("vanyeuemnhungaydautien");
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ResponseObject(0, "OK!", tmp1 + " " + tmp2));
	}
	
	@PostMapping("/CreateAccountStudent")
	ResponseEntity<ResponseObject> createAccountStudent(@RequestBody Map<String, Object> body) {
		String hashPass = (String) body.get("hashPass");
		Student student = new Student(
				body.get("userName").toString(), 
				body.get("name").toString(),
				body.get("phoneNumber").toString(),
				body.get("studentID").toString(),
				body.get("facebook").toString()
		);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(UserService.createStudentAccount(student, hashPass));
	}
	
//	@PostMapping("/CreateAccountAdmin")
//	ResponseEntity<ResponseObject> createAccountAdmin(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
//		String hashPass = (String) body.get("hashPass");
//		Admin admin = new Admin(
//				body.get("userName").toString(), 
//				body.get("name").toString(),
//				body.get("phoneNumber").toString(),
//				body.get("email").toString()
//		);
//		ResponseObject tmp = UserService.createAdminAccount(admin, hashPass, token);
//		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
//			return ResponseEntity.status(HttpStatus.OK).body(tmp);
//		return ResponseEntity
//				.status(HttpStatus.OK)
//				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
//				.body(UserService.createAdminAccount(admin, hashPass, token));
//	}
	
	@PostMapping("/Login")
	ResponseEntity<ResponseObject> login(@RequestBody Map<String, Object> body) {
		String userName = (String) body.get("userName");
		String hashPass = (String) body.get("hashPass");
		ResponseObject tmp = UserService.login(userName, hashPass);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return ResponseEntity.status(HttpStatus.OK).body(tmp);
		User user = (User) tmp.getData();
		Map<String, Object> token_data = new HashMap<>();
		token_data.put("userName", user.getUserName());
		token_data.put("roleCode", user.getRoleCode());
		if (user instanceof Student) {
			Student student = (Student) user;
			token_data.put("status", student.getStatus());
			token_data.put("studentID", student.getStudentID());
		}
		return ResponseEntity
				.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(token_data))
				.body(tmp);
	}
	
	@PostMapping("/ChangePassword")
	ResponseEntity<ResponseObject> changePassword(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to change password!", null));
		String userName = (String) body.get("userName");
		String oldHashPass = (String) body.get("oldHashPass");
		String newHashPass = (String) body.get("newHashPass");
		return ResponseEntity
				.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(UserService.changePassword(token, userName, oldHashPass, newHashPass));
	}
	
	@PostMapping("/ChangePublicInfo")
	ResponseEntity<ResponseObject> changePublicInfo(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to change info!", null));
		return ResponseEntity
				.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(UserService.changePublicInfo(token, body));
	}
	
}
