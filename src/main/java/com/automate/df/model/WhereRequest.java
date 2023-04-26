package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class WhereRequest {

	String key;
	String type;
	List<QueryParam> values;
}
