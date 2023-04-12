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
import service.TokenService;

@RestController
@RequestMapping(path = "/api/v1/File")
public class FileController {
	
	@GetMapping("/")
	String test() {
		return "OK";
	}
	
	@PostMapping("/UploadImage")
	ResponseEntity<ResponseObject> uploadFile(@RequestHeader("token") String token, @RequestParam("file") MultipartFile file) {
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(FileService.getInstance().saveImage(file, token));
	}
	
	@GetMapping("/GetImage/{fileName:.+}")
	ResponseEntity<ResponseObject> getImage(@RequestHeader("token") String token, @PathVariable String fileName) {
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(FileService.getInstance().loadImage(fileName, token));
	}
	
//	@GetMapping("/DelImage/{fileName:.+}")
//	ResponseEntity<ResponseObject> delImage(@RequestHeader("token") String token, @PathVariable String fileName) {
//		return ResponseEntity.status(HttpStatus.OK)
//				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
//				.body(FileService.getInstance().delImageIfExist(fileName, token));
//	}
	
}
