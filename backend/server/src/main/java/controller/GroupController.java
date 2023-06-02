package controller;

import java.util.ArrayList;
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
import org.springframework.web.bind.annotation.RestController;

import model.Group;
import model.ResponseObject;
import service.GroupService;
import service.TokenService;

@RestController
@RequestMapping(path = "/api/v1/Group")
public class GroupController {

	@GetMapping("/")
	ResponseEntity<ResponseObject> test() {
		List<String> in = new ArrayList<>();
		in.add("UY"); in.add("YEU"); in.add("KHANH");
		Group tmp = new Group("102210240", Group.STATUS_NEW_GROUP, in);
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(new ResponseObject(0, "OK!", tmp)); 
	}
	
	@GetMapping("/Joined/{studentID}")
	ResponseEntity<ResponseObject> getJoinedGroupID(@RequestHeader("token") String token, @PathVariable String studentID) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read group data!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(GroupService.getJoinedGroupID(token, studentID));
	}
	
	@GetMapping("/Info/{groupID}")
	ResponseEntity<ResponseObject> getGroupInfo(@RequestHeader("token") String token, @PathVariable String groupID) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read group data!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(GroupService.readGroupInfo(token, groupID));
	}
	
	@PostMapping("/VoteGroup")
	ResponseEntity<ResponseObject> voteGroup(@RequestHeader("token") String token, @RequestBody Map<String, String> data) {
		if (!TokenService.isValidToken(token))
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(ResponseObject.RESPONSE_TOKEN_EXPIRED, "Login again to read group data!", null));
		return ResponseEntity.status(HttpStatus.OK)
				.header("token", TokenService.generateToken(TokenService.getDataFromToken(token)))
				.body(GroupService.voteGroup(token, data));
	}
}
