package service;

import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.StreamUtils;
import org.springframework.web.multipart.MultipartFile;

import model.ResponseObject;
import model.Student;
import model.User;
import repository.FileRepository;

public class FileService {
	
	private static final Path PATH = Paths.get("File");
	private static FileService instance;
	
	private FileService() {
		try {
			Files.createDirectories(PATH);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static FileService getInstance() {
		if (instance == null) instance = new FileService();
		return instance;
	}
	
	private Boolean isImage(String fileExtension) {
		return Arrays.asList(new String[] {"png", "jpg", "jpeg", "bmp"}).contains(fileExtension.trim().toLowerCase());
	}
	
	public ResponseObject saveImage(MultipartFile file, String token) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to upload file!", null);
		Map<String, Object> data_token = TokenService.getDataFromToken(token);
		if (User.ROLE_CODE_STUDENT.equals((Integer)data_token.get("roleCode")) && !Student.STATUS_ACTIVE_USER.equals((Integer)data_token.get("status")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		if (file.isEmpty())
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "File not found", null);
		if (file.getSize() / (1e6) > 5.0)
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "File too large (>5Mb)", null);
		String fileExtension = FilenameUtils.getExtension(file.getOriginalFilename());
		// TODO check file image or not and save with token
		if (!isImage(fileExtension))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "File is not image", null);
		String fileNewName = UUID.randomUUID().toString().replace("-", "");
		String fileName = fileNewName + "." + fileExtension;
		Path finalFilePath = PATH.resolve(Paths.get(fileName)).normalize().toAbsolutePath();
		if (!finalFilePath.getParent().equals(PATH.toAbsolutePath()))
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with file system in server!", null);
		try {
			Files.copy(file.getInputStream(), finalFilePath, StandardCopyOption.REPLACE_EXISTING);
			ResponseObject tmp  = FileRepository.insertImage(fileName, data_token.get("userName").toString());
			if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
				return tmp;
			return new ResponseObject(ResponseObject.RESPONSE_OK, "File upload successfully!", fileName);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with file system in server!", null);
	}
	
	public ResponseObject loadImage(String fileName, String token) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to load file!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !Student.STATUS_ACTIVE_USER.equals((Integer)token_data.get("status")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
//		ResponseObject tmp = FileRepository.getImageOwner(fileName);
//		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
//			return tmp;
//		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")) && !token_data.get("userName").toString().equals(tmp.getData().toString()))
//			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to upload file!", null);
		if (!isImage(FilenameUtils.getExtension(fileName)))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "File is not image", null);
		try {
			Path file = PATH.resolve(fileName);
			Resource resource = new UrlResource(file.toUri());
			if (resource.exists() || resource.isReadable()) {
				byte[] data = StreamUtils.copyToByteArray(resource.getInputStream());
				return new ResponseObject(ResponseObject.RESPONSE_OK, "OK!", data);
			}
			return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with file system in server!", null); 
		} catch (IOException e) { }
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with file system in server!", null);
	}
	
	public ResponseObject delImageIfExist(String fileName, String token) {
		if (!TokenService.isValidToken(token))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Login again to load file!", null);
		Map<String, Object> token_data = TokenService.getDataFromToken(token);
		ResponseObject tmp = FileRepository.getImageOwner(fileName);
		if (tmp.getRespCode() != ResponseObject.RESPONSE_OK)
			return tmp;
		if (!User.ROLE_CODE_ADMIN.equals((Integer)token_data.get("roleCode")) && !token_data.get("userName").toString().equals(tmp.getData().toString()))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "You not allow to delete file!", null);
		if (User.ROLE_CODE_STUDENT.equals((Integer)token_data.get("roleCode")) && !Student.STATUS_ACTIVE_USER.equals((Integer)token_data.get("status")))
			return new ResponseObject(ResponseObject.RESPONSE_REQUEST_ERROR, "Your account is not active!", null);
		FileRepository.delImageIfExist(fileName);
		Path file = PATH.resolve(fileName);
		try {
			Files.deleteIfExists(file);
			return new ResponseObject(ResponseObject.RESPONSE_OK, "Delete file successfully!", null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new ResponseObject(ResponseObject.RESPONSE_SYSTEM_ERROR, "Something wrong with file system in server!", null);
	}
	
}
