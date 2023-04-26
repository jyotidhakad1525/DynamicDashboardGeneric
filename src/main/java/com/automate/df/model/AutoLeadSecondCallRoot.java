package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AutoLeadSecondCallRoot {
		public AutoLeadDmsEntity dmsEntity;
	    public String errorMessage;
	    public boolean success;
	    public boolean error;
}
