package com.clouddrive.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.clouddrive.CloudDriveFileServiceApplication;
import com.clouddrive.model.UserFile;
import com.clouddrive.service.FileService;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.websocket.server.PathParam;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileService fileService;
	
	@Autowired
	private ObjectMapper mapper;
	
	@GetMapping("/list-files/user/{id}")
	public List<UserFile> getAllFiles(@PathVariable("id") String userId){
		return fileService.listAllFiles(userId);
	}
	
	@PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	public ResponseEntity<?>  upload( @RequestPart( value = "data" ) String data, @RequestPart("file") MultipartFile file) {
		try {
			UserFile userFile = mapper.readValue(data,UserFile.class);
			if(userFile.getUserId()==null) {
				return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("user Id Is empty");
			}
			UserFile saveUserFile = fileService.uploadUserFile(userFile.getUserId(), file);
			return ResponseEntity.status(HttpStatus.OK).body(saveUserFile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fail To upload Files");
	}
	
	@GetMapping("/download/user/{userId}/file/{userFileId}")
	public void downloadFile(@PathVariable("userId") String userId, @PathVariable("userFileId") String userFileId, HttpServletResponse response) throws IOException {
		try {
			UserFile userFile = fileService.getUserFileBy(userId, userFileId);
			if(userFile==null) {
				response.sendError(HttpStatus.NOT_FOUND.value(), "File Not Found..!");
				return;
			}
			ResponseInputStream<GetObjectResponse> downloadFileStream = fileService.downloadFileStream(userFileId);
			
			response.setStatus(HttpStatus.OK.value());
			response.setContentType("application/octet-stream");
			response.setHeader(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + userFile.getFileName() + "\"");
			
			byte[] buffer= new byte[8024];
			int byteRead;
			ServletOutputStream outputStream = response.getOutputStream();
			while((byteRead = downloadFileStream.read(buffer))!=-1) {
				outputStream.write(buffer,0,byteRead);
			}
		} catch (Exception e) {
			System.out.println(e);
			response.sendError(500, "Something went Wrong..!");
		}
		
	}


}
