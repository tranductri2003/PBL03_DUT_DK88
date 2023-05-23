package controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import model.ActiveRequest;
import model.BanRequest;
import model.Request;
import model.ResponseObject;
import service.FileService;
import service.RequestService;
import service.TokenService;

@RestController
@RequestMapping(path = "/api/v1/Request")
public class RequestController {
		
	@GetMapping("/")
	ResponseEntity<ResponseObject> test() {
		Request tmp1 = new Request(1, "Lam chong em", Request.REQUEST_CODE_ACTIVE);
		ActiveRequest tmp2 = new ActiveRequest(1, "Lam chong em", Request.REQUEST_CODE_ACTIVE, "abc.png", "xyz.jpg");
		BanRequest tmp3 = new BanRequest(2, "sdfsdf", Request.REQUEST_CODE_BAN, "anh em yeu nhieu lam..", List.of("123.sdf", "sdf.fg", "dsf.g"));
		Map<String, Object> tmp = new HashMap<>();
		tmp.put("request", tmp1);
		tmp.put("isAccepted", false);
		
		return ResponseEntity.status(HttpStatus.OK)
				.body(new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", tmp));
	}
	
	@PostMapping("/Active")
	ResponseEntity<ResponseObject> requestActive(@RequestHeader("token") String token, @RequestBody ActiveRequest request) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to send request!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(RequestService.saveRequest(request, token));
	}
	
	@PostMapping("/Ban")
	ResponseEntity<ResponseObject> requestBan(@RequestHeader("token") String token, @RequestBody BanRequest request) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to send request!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(RequestService.saveRequest(request, token));
	}
	
	@GetMapping("/Page/{pageNumber}")
	ResponseEntity<ResponseObject> readRequestByPage(@RequestHeader("token") String token, @PathVariable Integer pageNumber) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read request!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(RequestService.readRequestByPage(token, pageNumber));
	}
	
	@PostMapping("/Detail")
	ResponseEntity<ResponseObject> readDetailRequest(@RequestHeader("token") String token, @RequestBody Request request) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read request!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(RequestService.readRequestDetail(request, token));
	}
	
	@PostMapping("/Handle")
	ResponseEntity<ResponseObject> handleRequest(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to handle request!", null));
		Integer requestID = Integer.parseInt(body.get("requestID").toString());
		String targetID = body.get("targetID").toString();
		Integer requestCode = Integer.parseInt(body.get("requestCode").toString());
		Request request = new Request(requestID, targetID, requestCode);
		Boolean isAccepted = Boolean.parseBoolean(body.get("isAccepted").toString());
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(RequestService.handleRequest(request, token, isAccepted));
	}
	
	
}
