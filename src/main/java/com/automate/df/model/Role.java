package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Role {

	int roleId;
	String roleName;
	String mode;
	int fieldId;
}
