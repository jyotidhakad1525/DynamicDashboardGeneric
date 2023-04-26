package com.automate.df.model;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LeadBulkUploadRes {

	String errorFileDownloadLink;
	List<LeadBulkUploadOutput> data;
}
