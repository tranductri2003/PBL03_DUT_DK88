package controller;

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

import model.ResponseObject;
import service.FileService;

@RestController
@RequestMapping(path = "/api/v1/Request")
public class RequestController {
	
	@GetMapping("/")
	String test() {
		return "OK";
	}
	
	@PostMapping("/Active")
	ResponseEntity<ResponseObject> requestActive(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
		
		
		
		return null;
	}
	
	@PostMapping("/Ban")
	ResponseEntity<ResponseObject> requestBan(@RequestHeader("token") String token, @RequestBody Map<String, Object> body) {
		
		
		
		return null;
	}
	
}
