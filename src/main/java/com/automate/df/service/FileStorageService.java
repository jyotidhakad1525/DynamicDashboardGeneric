package com.automate.df.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import com.automate.df.exception.DynamicFormsServiceException;

public interface FileStorageService {

	public String storeFile(MultipartFile file) throws DynamicFormsServiceException;

	public Resource loadFileAsResource(String fileName);

}
