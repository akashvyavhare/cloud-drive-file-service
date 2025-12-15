package com.clouddrive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.clouddrive.model.UserFile;
import com.clouddrive.repository.FileRepository;

@Service
public class FileServiceImpl implements FileService {
	
	@Autowired
	private FileRepository fileRepository;
	
	@Value("cloud-drive.s3.bucket-name")
	private String S3_BUCKET_NAME;
	
	@Value("cloud-drive.s3.upload-path")
	private String S3_UPLOAD_PATH; 

	@Override
	public UserFile uploadUserFile(String userID, MultipartFile file) {
		UserFile userFile = new UserFile();
		userFile.setId(userID);
		return fileRepository.save(userFile);
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
