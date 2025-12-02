package com.clouddrive.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.clouddrive.model.UserFile;

@Repository
public interface FileRepository extends MongoRepository<UserFile, String>{

}
