package controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import model.QueryStudentClass;
import model.ResponseObject;
import service.ClassService;
import service.TokenService;

@RestController
@RequestMapping(path = "/api/v1/Class")
public class ClassController {
	
	@GetMapping("/")
	String test() {
		List<String> haveClass = new ArrayList<>();
		haveClass.add("ABC"); haveClass.add("XYZ"); haveClass.add("123");
		QueryStudentClass tmp = new QueryStudentClass(1, "102210240", haveClass, "456");
		return tmp.toString();
	}
	
	@PostMapping("/ChangeClass")
	ResponseEntity<ResponseObject> changeClass(@RequestHeader("token") String token, @RequestBody QueryStudentClass query) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to change class!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.body(ClassService.changeStudentClass(token, query));
	}
	
	@GetMapping("/QueryClass/{idQuery}")
	ResponseEntity<ResponseObject> getNewQuery(@RequestHeader("token") String token, @PathVariable Integer idQuery) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read class!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.body(ClassService.readNewQueryClass(token, idQuery));
	}

	
}
