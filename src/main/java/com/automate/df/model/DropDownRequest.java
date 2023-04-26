package com.automate.df.model;


import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DropDownRequest {

	
	String bu;
	String dropdownType;
	String parentId;
}
