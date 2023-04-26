package com.automate.df.service.impl;


import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.automate.df.constants.GsAppConstants;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.exception.MyFileNotFoundException;
import com.automate.df.service.FileStorageService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

  // private final Path fileStorageLocation;
    
	
	@Value("${tmp.path}")
	String tmpPath;

	@Autowired
	Environment env;
	
	@Value("${app.upload.attachment.dir}")
	String attachmentUploadDir;
	
	@Value("${app.upload.attachment.size}")
	Long uploadSize;
	
	@Value("${app.upload.allowed.filetypes}")
	String uploadTypes;
   /* @Autowired
    public FileStorageServiceImpl(FileStorageProperties fileStorageProperties) {
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    public String storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            return fileName;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }*/

    public Resource loadFileAsResource(String fileName) {
    	log.debug("loadFileAsResource(){}");
    	log.debug("fileName :"+(tmpPath+fileName));
        try {
        	
            Path filePath = Paths.get(tmpPath+fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
            	log.debug("Given file found");
                return resource;
            } else {
                throw new MyFileNotFoundException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new MyFileNotFoundException("File not found " + fileName, ex);
        }
    }


	@Override
	public String storeFile(MultipartFile file) throws DynamicFormsServiceException {
		Path copyLocation=null;
		try {
			if(null!=file && file.getSize()>uploadSize) {
				throw new DynamicFormsServiceException(env.getProperty("FILE_EXCEEDS_GIVEN_SIZE"), HttpStatus.BAD_REQUEST);
			}
			if(!validateFileType(file.getOriginalFilename())) {
				throw new DynamicFormsServiceException(env.getProperty("INVALID_FILE_FORMAT"), HttpStatus.BAD_REQUEST);
			}
			
			
            copyLocation = Paths.get(attachmentUploadDir + File.separator + file.getOriginalFilename());
            Files.copy(file.getInputStream(), copyLocation,StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            log.error("Exception in GenericServiceImpl:upload() ",e);
            throw new DynamicFormsServiceException("Could not store file " + file.getOriginalFilename()
            + ". Please try again!",HttpStatus.INTERNAL_SERVER_ERROR);
           
        }
		return null!=copyLocation?copyLocation.toString():GsAppConstants.FILE_UPLOAD_ISSUE;
	}
	
	private boolean validateFileType(String originalFilename) {
	
		
		if(null!=uploadTypes) {
			return Arrays.asList(uploadTypes.split(GsAppConstants.COMMA_SEPERATOR)).stream().anyMatch(x->originalFilename.contains(x));
		}
		return false;
		
	}
}
