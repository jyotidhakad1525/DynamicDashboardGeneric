package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FieldGroup {

	int id;
	String key;
	String className;
	String type;
	String subType;
	String dataOptionsValue;
	String positionLevel;
}
