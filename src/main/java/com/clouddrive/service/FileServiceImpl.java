package com.clouddrive.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clouddrive.model.UserFile;
import com.clouddrive.repository.FileRepository;

import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectResponse;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	private FileRepository fileRepository;
	
	@Autowired
	private S3Client s3Client;
	
	@Value("${cloud-drive.s3.bucket-name}")
	private String S3_BUCKET_NAME;
	
	@Value("${cloud-drive.s3.file-upload-path}")
	private String S3_UPLOAD_PATH; 
	
	

	@Override
	public UserFile uploadUserFile(String userID, MultipartFile file) {
		
		try {
			System.out.println("S3_BUCKET_NAME "+ S3_BUCKET_NAME);
			String fileOriginName = file.getOriginalFilename();
			File uploadedFile = new File(fileOriginName);
			
			FileOutputStream foStream = new FileOutputStream(uploadedFile);
			foStream.write(file.getBytes());
			foStream.close();
			PutObjectRequest putObject = PutObjectRequest.builder()
										.bucket(S3_BUCKET_NAME).key(fileOriginName).build();
			PutObjectResponse putObjectResponce = s3Client.putObject(putObject,RequestBody.fromFile(uploadedFile));
			if(!putObjectResponce.sdkHttpResponse().isSuccessful()) {
				throw new RuntimeException("S3 File upload Fail...");
			}
			
			UserFile userFile = new UserFile();
			userFile.setFileName(fileOriginName);
			userFile.setUserId(userID);
			
			return fileRepository.save(userFile);
		} catch (Exception e) {
			System.out.println(e);
			return null;
		}
		
	}

	@Override
	public List<UserFile> listAllFiles(String userID) {
		return fileRepository.findAll();
	}

	@Override
	public void downloadFile(String userId, String fileId) {
		// TODO Auto-generated method stub
		
	}

}
