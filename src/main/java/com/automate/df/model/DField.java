package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DField {

	int id;
	String fieldName;
	Object value;
	String domtype;
}
