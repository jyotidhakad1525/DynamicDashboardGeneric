package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Root {
	public String pageId;
	public Integer recordId;
	public List<Rootparam> rootparams;
}
