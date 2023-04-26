package com.automate.df.model.sales.lead;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;

@NoArgsConstructor
@Setter
@Getter
public class HelpDeskDto {

	private int empId;
	private int branchId;
	private int orgId;
	private int id;
	private String issueStage;
	private String issueCategory;
	private String description;
	private MultipartFile file;
    private String fileName;
    private String documentType;
    private String keyName;
    private String universalId;
    private String documentPath;
    private String status;
    private Date createdDate;
}
