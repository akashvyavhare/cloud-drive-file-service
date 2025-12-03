package com.clouddrive.service;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.clouddrive.model.UserFile;

public interface FileService {

	UserFile uploadUserFile(String userID, MultipartFile file);
	List<UserFile> listAllFiles(String userID);
	void downloadFile(String userId, String fileId);
}
