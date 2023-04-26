package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class BulkUploadParent {

	String sno;
	String parentIdentifier;
	Object parent;
	List<BulkUploadParentChild> parentChildList;
}
