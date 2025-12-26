package com.clouddrive.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clouddrive.model.UserFile;

import jakarta.servlet.http.HttpServletResponse;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.services.s3.model.GetObjectResponse;

public interface FileService {

	UserFile uploadUserFile(String userID, MultipartFile file);
	
	List<UserFile> listAllFiles(String userID);
	
	UserFile getUserFileBy(String userId, String userFileId);
	
	ResponseInputStream<GetObjectResponse> downloadFileStream(String userFileId);
}
