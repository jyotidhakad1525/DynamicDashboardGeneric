package com.automate.df.advice;



import java.util.Date;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.ErrorDetails;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class AutomateAdvice {

	@ExceptionHandler(DynamicFormsServiceException.class)
	public ResponseEntity<?> globalSettingsExceptionHandler(DynamicFormsServiceException ex,WebRequest request){
		ErrorDetails errorDetails = new ErrorDetails(new Date(), ex.getMessage(),request.getDescription(false),ex.getStatusCode().toString());
		log.debug("globalSettingsExceptionHandler "+errorDetails);
		return new ResponseEntity<>(errorDetails,ex.getStatusCode());
	}
	
}
