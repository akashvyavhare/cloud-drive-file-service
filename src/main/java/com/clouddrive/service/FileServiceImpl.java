package com.clouddrive.service;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.clouddrive.model.UserFile;
import com.clouddrive.repository.FileRepository;

import jakarta.servlet.http.HttpServletResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;
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
	@Transactional
	public UserFile uploadUserFile(String userID, MultipartFile file) {
		
		try {
			//System.out.println("S3_BUCKET_NAME "+ S3_BUCKET_NAME);
			String fileOriginName = file.getOriginalFilename();
			//File uploadedFile = new File(fileOriginName);
			
			String s3FileSeprator = "/";
			
//			FileOutputStream foStream = new FileOutputStream(uploadedFile);
//			foStream.write(file.getBytes());
//			foStream.close();
			
			String s3FileKey = S3_UPLOAD_PATH + s3FileSeprator + userID + s3FileSeprator + fileOriginName;
			
			PutObjectRequest putObject = PutObjectRequest.builder()
										.bucket(S3_BUCKET_NAME).key(s3FileKey).build();
			PutObjectResponse putObjectResponce = s3Client.putObject(putObject,RequestBody.fromBytes(file.getBytes()));
			if(!putObjectResponce.sdkHttpResponse().isSuccessful()) {
				throw new RuntimeException("S3 File upload Fail...");
			}
			
			UserFile userFile = new UserFile();
			userFile.setFileName(fileOriginName);
			userFile.setUserId(userID);
			userFile.setFilePath(s3FileKey);
			
			return fileRepository.save(userFile);
		} catch (Exception e) {
			System.out.println(e);
			throw new RuntimeException("S3 File upload Fail...");
		}
		
	}

	@Override
	public List<UserFile> listAllFiles(String userID) {
		return fileRepository.findByUserId(userID);
	}
	
	

	@Override
	public ResponseInputStream<GetObjectResponse> downloadFileStream(String userFileId) {
		try {
			UserFile userFile = fileRepository.findById(userFileId).orElse(null);
			if(userFile==null) {
				return null;
			}
			GetObjectRequest getObjectRequest = GetObjectRequest.builder().bucket(S3_BUCKET_NAME).key(userFile.getFilePath()).build();
			return s3Client.getObject(getObjectRequest);
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public UserFile getUserFileBy(String userId, String userFileId) {
		return fileRepository.findByIdAndUserId(userFileId, userId);
	}

}
