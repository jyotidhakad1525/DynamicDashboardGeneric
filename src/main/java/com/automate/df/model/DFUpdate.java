package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DFUpdate {

	
	String pageId;
	String recordId;
	String uuid;
	List<DField> params;
}
