package controller;

import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.DataHasher;
import model.DatabaseHelper;
import model.QueryData;
import model.ResponseObject;
import model.Student;

@RestController
@RequestMapping(path = "/api/v1/User")
public class UserController {
	
	@GetMapping("")
	String test() {
		DatabaseHelper.getInstance();
		String tmp1 = DataHasher.hash("secuoikhanh10namnua");
		String tmp2 = DataHasher.hash("192e6996a7fd84accb7e7f85f92bc6877f7c87680180b3da16bf7e77d94e464c");
		return tmp1 + " " + tmp2;
	}
	
	@PostMapping("/CreateAccount")
	ResponseEntity<ResponseObject> createAccount(@RequestBody Map<String, Object> body) {
		String userName = (String) body.get("userName");
		String hashPass = (String) body.get("hashPass");
		String studentID = (String) body.get("studentID");
		String name = (String) body.get("name");
		String phoneNumber = (String) body.get("phoneNumber");
		return ResponseEntity.status(HttpStatus.OK)
				.body(QueryData.insertStudent(userName, hashPass, studentID, name, phoneNumber));
	}
	
	@PostMapping("/Login")
	ResponseEntity<ResponseObject> login(@RequestBody Map<String, Object> body) {
		String userName = (String) body.get("userName");
		String hashPass = (String) body.get("hashPass");
		return ResponseEntity.status(HttpStatus.OK)
				.body(QueryData.readUserInfo(userName, hashPass));
	}
	
//	@PostMapping("/EditProfile")
//	ResponseEntity<ResponseObject> editProfile(@RequestBody Map<String, Object> body) {
//		String userName = (String) body.get("userName");
//		String hashPass = (String) body.get("hashPass");
//		String studentID = (String) body.get("studentID");
//		String name = (String) body.get("name");
//		String phoneNumber = (String) body.get("phoneNumber");
//		return ResponseEntity.status(HttpStatus.OK)
//				.body(QueryData.insertStudent(userName, hashPass, studentID, name, phoneNumber));
//	}
	
}
