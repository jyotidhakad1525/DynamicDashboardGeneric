package com.automate.df.model.oh;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class PostOfficeRoot {
	 @JsonProperty("Message") 
	    public String message;
	    @JsonProperty("Status") 
	    public String status;
	    @JsonProperty("PostOffice") 
	    public List<PostOffice> postOffice;
}
