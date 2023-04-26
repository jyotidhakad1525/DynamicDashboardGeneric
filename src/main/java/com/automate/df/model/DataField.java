package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DataField {

	String fieldName;
	String oldFieldValue;
	String newFieldValue;
}
