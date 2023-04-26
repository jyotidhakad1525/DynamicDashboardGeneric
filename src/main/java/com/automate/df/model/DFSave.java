package com.automate.df.model;

import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DFSave {

	String pageId;
	String uuid;
	List<DField> params;
}
