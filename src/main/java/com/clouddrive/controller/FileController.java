package com.clouddrive.controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
public class FileController {
	
	@GetMapping("/all")
	public List<String> getAllFiles(){
		return new ArrayList<String>();
	}

}
