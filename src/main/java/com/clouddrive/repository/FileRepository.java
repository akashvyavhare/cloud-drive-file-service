package com.clouddrive.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clouddrive.model.UserFile;
import java.util.List;


@Repository
public interface FileRepository extends MongoRepository<UserFile, String>{
	
	List<UserFile> findByUserId(String userId);
	
	UserFile findByIdAndUserId(String userFileId, String userId );

}
