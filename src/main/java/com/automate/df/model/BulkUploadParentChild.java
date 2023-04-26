package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BulkUploadParentChild {

	String sno;
	String parentIdentifier;
	Object parentChild;
	List<BulkUploadChildChild> childChildList;
}
