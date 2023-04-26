package com.automate.df;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

import com.automate.df.util.ObjectMapperUtils;

@SpringBootApplication
public class DynamicFormsV2Application {

	public static void main(String[] args) {
		SpringApplication.run(DynamicFormsV2Application.class, args);
	}
	
	
	
	@Bean
	public ModelMapper modelMapper() {
	    return new ModelMapper();
	}
	
	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	
	public ObjectMapperUtils objectMapperUtils() {
		return new ObjectMapperUtils();
	}
}
