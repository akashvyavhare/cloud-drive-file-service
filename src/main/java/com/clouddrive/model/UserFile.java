package com.clouddrive.model;


import java.util.Arrays;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "user-file")
public class UserFile {

	@Id
	private String id;
	private String userId;
	private String fileName;
	private String filePath;
	private byte[] fileBytes;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
	public byte[] getFileBytes() {
		return fileBytes;
	}
	public void setFileBytes(byte[] fileBytes) {
		this.fileBytes = fileBytes;
	}
	@Override
	public String toString() {
		return "UserFile [id=" + id + ", userId=" + userId + ", fileName=" + fileName + ", filePath=" + filePath
				+ ", fileBytes=" + Arrays.toString(fileBytes) + "]";
	}
	
	
	
}
