package com.automate.df.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class FormData {

	private Integer busFormKeyMapId;
	private String formKeyVal;
	private boolean listFlag;
	private Integer index;
}
