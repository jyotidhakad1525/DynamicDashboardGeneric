package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DropdownReq {

	
	String bu;
	String dropdownType;
	int parentId;
}
