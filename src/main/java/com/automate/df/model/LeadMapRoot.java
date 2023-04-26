package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadMapRoot {
	
		public DmsEntityMap dmsEntity;
	    public String errorMessage;
	    public boolean success;
	    public boolean error;

}
