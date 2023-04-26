package com.automate.df.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.EntityManager;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import com.automate.df.constants.GsAppConstants;
import com.automate.df.dao.LeadStageRefDao;
import com.automate.df.dao.dashboard.DmsLeadDao;
import com.automate.df.entity.LeadStageRefEntity;
import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AutoLeadSecondCallRoot;
import com.automate.df.model.AutoLeadTask;
import com.automate.df.model.DMSResponse;
import com.automate.df.model.DmsAddress;
import com.automate.df.model.DmsContactDto;
import com.automate.df.model.DmsContactMapDto;
import com.automate.df.model.DmsLeadDto;
import com.automate.df.model.DmsLeadMapDto;
import com.automate.df.model.EnqSource;
import com.automate.df.model.LeadBulkUploadOutput;
import com.automate.df.model.LeadBulkUploadRes;
import com.automate.df.model.LeadCustRefReq;
import com.automate.df.model.LeadCustomerRefRoot;
import com.automate.df.model.LeadCustomerReferenceDto;
import com.automate.df.model.LeadErrorMsg;
import com.automate.df.model.LeadMapReq;
import com.automate.df.model.LeadMapRoot;
import com.automate.df.model.LeadMapSalesConsultantReq;
import com.automate.df.model.LeadMgmtRoot;
import com.automate.df.model.Task;
import com.automate.df.service.GenericService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import springfox.documentation.swagger2.mappers.ModelMapper;

/**
 * 
 * @author srujan
 *
 */
@Service
@Slf4j
public class GenericServiceImpl implements GenericService {

	@Autowired
	Environment env;

	@Value("${app.upload.attachment.dir}")
	String attachmentUploadDir;

	@Value("${app.upload.attachment.size}")
	Long uploadSize;

	@Value("${app.upload.allowed.filetypes}")
	String uploadTypes;

	@Value("${leadsource.leadcustomerref.url}")
	String leadCustRefUrl;

	@Value("${leadsource.contact.url}")
	String leadContactUrl;

	@Value("${leadsource.account.url}")
	String leadAccountUrl;

	@Value("${leadsource.map.lead.url}")
	String leadMapUrl;

	@Value("${leadsource.map.salesconsultant.url}")
	String leadSalesConsultantUrl;

	@Value("${leadsource.auto.firsturl}")
	String autoLeadFirstUrl;

	@Value("${leadsource.auto.secondurl}")
	String autoLeadSecondUrl;

	@Value("${leadsource.auto.thirdurl}")
	String autoLeadThridUrl;

	@Value("${leadsource.auto.fourthurl}")
	String autoLeadFourthUrl;

	@Autowired
	ObjectMapper om;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	RestTemplate restTemplate;

	@Value("${file.controller.url}")
	String fileControllerUrl;

	@Value("${tmp.path}")
	String tmpPath;

	@Autowired
	ModelMapper modelMapper;
	
	@Value("${lead.sourceofenq.url}")
	String sourceOfEnqUrl;

	List<EnqSource> enqList = new ArrayList<>();

	@Override
	public String uploadAttachment(MultipartFile file) throws DynamicFormsServiceException {
		Path copyLocation = null;
		try {
			if (null != file && file.getSize() > uploadSize) {
				throw new DynamicFormsServiceException("File Exceeds Given Size", HttpStatus.BAD_REQUEST);
			}
			if (!validateFileType(file.getOriginalFilename())) {
				throw new DynamicFormsServiceException("Invalid file format", HttpStatus.BAD_REQUEST);
			}

			copyLocation = Paths.get(tmpPath + File.separator + file.getOriginalFilename());
			Files.copy(file.getInputStream(), copyLocation, StandardCopyOption.REPLACE_EXISTING);
			log.debug("FileUploaded Successfully");
		} catch (DynamicFormsServiceException e) {
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			log.error("Exception in uploadAttachment() ", e);
			throw new DynamicFormsServiceException(
					"Could not store file " + file.getOriginalFilename() + ". Please try again!",
					HttpStatus.INTERNAL_SERVER_ERROR);

		}
		return null != copyLocation ? copyLocation.toString() : GsAppConstants.FILE_UPLOAD_ISSUE;
	}

	private boolean validateFileType(String originalFilename) {

		log.debug("originalFilename  " + originalFilename);
		log.debug("uploadTypes " + uploadTypes);
		if (null != uploadTypes) {
			return Arrays.asList(uploadTypes.split(GsAppConstants.COMMA_SEPERATOR)).stream()
					.anyMatch(x -> originalFilename.contains(x));
		}
		return false;

	}

	@Autowired
	DmsLeadDao dmsLeadDao;
	
	
	@Override
	public LeadBulkUploadRes processLeadMgmtBulkUpload(MultipartFile file) throws DynamicFormsServiceException {
		String filePath = uploadAttachment(file);
		String errorFilePath = null;
		LeadBulkUploadRes res = new LeadBulkUploadRes();
		log.debug("filePath::" + filePath);
		List<LeadMgmtRoot> dataList = readExcelFile(filePath);
		log.debug("dataList " + dataList);
		List<LeadBulkUploadOutput> errorObjList = new ArrayList<>();
		List<LeadBulkUploadOutput> successObjList = new ArrayList<>();
		if (dataList != null && !dataList.isEmpty()) {
			log.debug("size of dataList " + dataList.size());
			for (LeadMgmtRoot lead : dataList) {
				String firstName = lead.getDmsLeadDto().getFirstName();
				String lastName = lead.getDmsLeadDto().getLastName();
				
				List<DmsLead> dbList = dmsLeadDao.verifyFirstName(firstName,lastName);
				
				String customerType = lead.getDmsContactDto().getCustomerType();
				LeadErrorMsg leadErrorMsg = null;
				String errorMsg = null;
				String statusCode = null;
				String firstNameErrmsg=null;
			//	try {

					if (null != dbList && !dbList.isEmpty()) {
						log.debug("Lead Exists in DB with given first name and last name");
						firstNameErrmsg = "[{\"message\":\"Lead Exists with given FirstName & LastName\",\"httpStatus\":\"BAD_REQUEST\",\"statusCode\":400}]";
					}
					log.debug("firstNameErrmsg::"+firstNameErrmsg);
					
					try {
						if (null == firstNameErrmsg) {
							try {
								if (null != customerType && customerType.equalsIgnoreCase("Individual")) {
									leadErrorMsg = restInvoke(leadContactUrl, lead);
								} else {
									leadErrorMsg = restInvoke(leadAccountUrl, lead);
								}
							} catch (DynamicFormsServiceException e) {
								log.debug("Exception inside loop " + e.getMessage() + " Status " + e.getStatusCode());
								errorMsg = e.getMessage();
								if (null != errorMsg) {
									errorMsg = errorMsg.substring(errorMsg.indexOf("["), errorMsg.length());
								}
								statusCode = e.getStatusCode().getReasonPhrase();
							}
						} else {
							String errormsg = "[{\"message\":\"Lead Exists with given FirstName & LastName\",\"httpStatus\":\"BAD_REQUEST\",\"statusCode\":400}]";
							throw new DynamicFormsServiceException(errormsg, HttpStatus.BAD_REQUEST);
						}
					}catch(DynamicFormsServiceException e) {
						log.debug("Exception " + e.getMessage() + " Status " + e.getStatusCode());
						errorMsg = e.getMessage();
						if (null != errorMsg) {
							errorMsg = errorMsg.substring(errorMsg.indexOf("["), errorMsg.length());
						}
						statusCode = e.getStatusCode().getReasonPhrase();
					}
				/*} catch (DynamicFormsServiceException e) {
					log.debug("Exception " + e.getMessage() + " Status " + e.getStatusCode());
					errorMsg = e.getMessage();
					if (null != errorMsg) {
						errorMsg = errorMsg.substring(errorMsg.indexOf("["), errorMsg.length());
					}
					statusCode = e.getStatusCode().getReasonPhrase();
				}*/
				
				log.debug("firstNameErrmsg::"+firstNameErrmsg);
				log.debug("errorMsg::::"+errorMsg);
				
				LeadErrorMsg[] lm = null;
				if (null != errorMsg) {
					try {
						lm = om.readValue(errorMsg, LeadErrorMsg[].class);
					} catch (JsonProcessingException e) {
						e.printStackTrace();
						throw new DynamicFormsServiceException("Exception while processing the response ",HttpStatus.INTERNAL_SERVER_ERROR);
					}
					if (null != lm) {
						LeadErrorMsg errObj = lm[0];
						if (null != errObj) {
							log.debug("Error Msg is " + errObj.getMessage() + " status Code " + errObj.getStatusCode());
							if (errObj.getStatusCode() >= 400 && errObj.getStatusCode() <= 499) { 
								String msg=null;
								if(null!=firstNameErrmsg) {
									msg = errObj.getMessage() ;
								}else {
									msg = errObj.getMessage();
								}
								LeadBulkUploadOutput obj = buildLeadBulkUploadOutputObj(lead, msg);
								errorObjList.add(obj);
							}
						}
					
						
						if (errObj.getStatusCode() == 500 || errObj.getStatusCode() == 200
								|| errObj.getStatusCode() == 0) {
							LeadBulkUploadOutput obj = buildLeadBulkUploadOutputObj(lead, errObj.getMessage());
							successObjList.add(obj);
						}
					}
				}
				updateSubSourceToSuccedeedObj(successObjList);
			}
			log.debug("successObjList ::" + successObjList);
			log.debug("errorObjList " + errorObjList);
			errorFilePath = generateErrorSheet(errorObjList);
		}
		res.setErrorFileDownloadLink(errorFilePath);
		res.setData(errorObjList);
		return res;
	}

	private void updateSubSourceToSuccedeedObj(List<LeadBulkUploadOutput> successObjList) {
		// TODO Auto-generated method stub

	}

	private String generateErrorSheet(List<LeadBulkUploadOutput> errorObjList) {
		String fileName = "ErrorSheet" + System.currentTimeMillis() + ".xlsx";
		try {
			Workbook workbook = new XSSFWorkbook();
			Sheet sheet = workbook.createSheet();
			createHeaderRow(sheet);
			int rowCount = 0;

			for (LeadBulkUploadOutput obj : errorObjList) {
				Row row = sheet.createRow(++rowCount);
				writeErrorObj(obj, row);
			}
			String excelFilePath = tmpPath + File.separator + fileName;
			fileName = fileControllerUrl + "/downloadFile/" + fileName;
			log.debug("ErrorfileGeneated at  " + excelFilePath);
			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
				workbook.write(outputStream);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private void writeErrorObj(LeadBulkUploadOutput obj, Row row) {
		// TODO Auto-generated method stub
		row.createCell(0).setCellValue(obj.getOrgId());
		row.createCell(1).setCellValue(obj.getBranch());
		row.createCell(2).setCellValue(obj.getLocation());
		row.createCell(2).setCellValue(obj.getFirstName());
		row.createCell(3).setCellValue(obj.getLastName());
		row.createCell(4).setCellValue(obj.getMobileNo());
		row.createCell(5).setCellValue(obj.getAlternateMobileNo());
		row.createCell(6).setCellValue(obj.getEmail());
		row.createCell(7).setCellValue(obj.getModel());
		row.createCell(8).setCellValue(obj.getSegment());
		row.createCell(9).setCellValue(obj.getCustomerType());
		row.createCell(10).setCellValue(obj.getEnqSource());
		row.createCell(11).setCellValue(obj.getSubSource());
		row.createCell(12).setCellValue(obj.getPinCode());
		row.createCell(13).setCellValue(obj.getErrorMsg());

	}

	private void createHeaderRow(Sheet sheet) {
		Row row = sheet.createRow(0);
		row.createCell(0).setCellValue("orgId");
		row.createCell(1).setCellValue("Branch");
		row.createCell(2).setCellValue("Location");
		row.createCell(2).setCellValue("First Name");
		row.createCell(3).setCellValue("Last Name");
		row.createCell(4).setCellValue("Mobile No");
		row.createCell(5).setCellValue("Alternate Mobile No.");
		row.createCell(6).setCellValue("E-Mail");
		row.createCell(7).setCellValue("Model");
		row.createCell(8).setCellValue("Segment");
		row.createCell(9).setCellValue("Customer Type");
		row.createCell(10).setCellValue("Enquiry Source");
		row.createCell(11).setCellValue("Sub Source");
		row.createCell(12).setCellValue("PinCode");
		row.createCell(13).setCellValue("Error Message");
	}

	private LeadBulkUploadOutput buildLeadBulkUploadOutputObj(LeadMgmtRoot lead, String message) {
		LeadBulkUploadOutput obj = new LeadBulkUploadOutput();
		obj.setErrorMsg(message);
		obj.setBranch(getBranchName(lead.getDmsLeadDto().getBranchId()));
		obj.setCustomerType(lead.getDmsContactDto().getCustomerType());
		obj.setEmail(lead.dmsContactDto.getEmail());
		obj.setEnqSource(getEnqSourceName(lead.getDmsContactDto().getEnquirySource(),lead.getDmsContactDto().getOrgId()));
		obj.setFirstName(lead.getDmsContactDto().getFirstName());
		obj.setLastName(lead.getDmsContactDto().getLastName());
		obj.setMobileNo(lead.getDmsContactDto().getPhone());

		obj.setPinCode(lead.getDmsLeadDto().getDmsAddresses().get(0).getPincode());
		obj.setSegment(lead.getDmsLeadDto().getEnquirySegment());
		obj.setOrgId(lead.getDmsContactDto().getOrgId());
		obj.setModel(lead.getDmsLeadDto().getModel());
		return obj;
	}

	private String getEnqSourceName(int enquirySource, int orgId) {
		log.debug("getEnqSourceName(){} " + enquirySource);
		if (null != enqList && enqList.isEmpty()) {
			enqList = getEnqList(orgId);
		}
		log.debug("enqList " + enqList);
		Optional<EnqSource> opt =  enqList.stream().filter(x -> x.getId() == enquirySource).findAny();
		String val = null;
		if(opt.isPresent()) {
			val = opt.get().getName();
			return val;
		}
		//String val = enqList.stream().filter(x -> x.getId() == enquirySource).findAny().get().getName();
		else {
			return "";
		}
	}

	private String getBranchName(int branchId) {
		log.info("Inside getBranchName,Given Branch ID : " + branchId);
		String res = null;
		String deptQuery = "SELECT name FROM dms_branch where branch_id=<ID>;";
		try {

			Object obj = entityManager.createNativeQuery(deptQuery.replaceAll("<ID>", String.valueOf(branchId)))
					.getSingleResult();
			res = (String) obj;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	private LeadErrorMsg restInvoke(String url, LeadMgmtRoot lead) throws DynamicFormsServiceException {
		LeadErrorMsg lm = null;
		String leadJsonString = new Gson().toJson(lead);
		log.debug("leadJsonString " + leadJsonString);
		try {
			lm = restTemplate.postForObject(url, lead, LeadErrorMsg.class);
		} catch (HttpClientErrorException | HttpServerErrorException e) {
			// e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(), e.getStatusCode());
		}
		return lm;
	}

	private List<LeadMgmtRoot> readExcelFile(String filePath) {
		List<LeadMgmtRoot> list = new ArrayList<>();
		try {

			FileInputStream excelFile = new FileInputStream(new File(filePath));
			Workbook workbook = new XSSFWorkbook(excelFile);
			Sheet datatypeSheet = workbook.getSheetAt(0);
			Iterator<Row> iterator = datatypeSheet.iterator();
			int cnt = 0;
			DataFormatter formatter = new DataFormatter();
			while (iterator.hasNext()) {
				Row currentRow = iterator.next();
				System.out.println("cnt " + cnt);
				if (cnt != 0) {
					String customerType = getStringCellValue(currentRow.getCell(10));
					String emailId = getStringCellValue(currentRow.getCell(7));
					
					String firstName = getStringCellValue(currentRow.getCell(3));
					String lastName = getStringCellValue(currentRow.getCell(4));
					String phone = formatter.formatCellValue(currentRow.getCell(5));
					// Integer phone = getIntCellValue(currentRow.getCell(5));
					String enqSegment = getStringCellValue(currentRow.getCell(9));
					String model = getStringCellValue(currentRow.getCell(8));
					String pinCode = formatter.formatCellValue(currentRow.getCell(13));
					Integer branchId = getBranchId(getStringCellValue(currentRow.getCell(1)));
					Integer orgId = getIntCellValue(currentRow.getCell(0));
					Integer enqSource = getEnqValue(getStringCellValue(currentRow.getCell(11)),orgId);
					String uploadedBy = formatter.formatCellValue(currentRow.getCell(14));
					log.debug("customerType:" + customerType + ",emailId:" + emailId + ",enqSource:" + enqSource
							+ ",firstName:" + firstName + ",lastName" + lastName);
					LeadMgmtRoot leadMgmtRoot = new LeadMgmtRoot();
					DmsContactDto contactDto = new DmsContactDto();
					DmsLeadDto dmsLeadDto = new DmsLeadDto();

					contactDto.setCreatedBy(uploadedBy);
					contactDto.setBranchId(branchId);
					contactDto.setCustomerType(customerType);
					contactDto.setEmail(emailId);
					contactDto.setEnquirySource(enqSource);
					contactDto.setFirstName(firstName);
					contactDto.setLastName(lastName);
					contactDto.setOrgId(orgId);
					contactDto.setPhone(String.valueOf(phone));
					contactDto.setStatus("PREENQUIRY");

					dmsLeadDto.setCreatedBy(uploadedBy);
					dmsLeadDto.setBranchId(branchId);
					dmsLeadDto.setEnquirySegment(enqSegment);
					dmsLeadDto.setFirstName(firstName);
					dmsLeadDto.setLastName(lastName);
					dmsLeadDto.setLeadStage("PREENQUIRY");
					dmsLeadDto.setModel(model);
					dmsLeadDto.setPhone(String.valueOf(phone));
					dmsLeadDto.setOrganizationId(orgId);
					dmsLeadDto.setSourceOfEnquiry(enqSource);
					dmsLeadDto.setEmail(emailId);

					LeadCustRefReq custRefReq = new LeadCustRefReq();
					custRefReq.setBranchid(branchId);
					custRefReq.setLeadstage("PREENQUIRY");
					custRefReq.setOrgid(1);

					dmsLeadDto.setReferencenumber(getRefNumber(custRefReq));

					DmsAddress address1 = new DmsAddress();
					address1.setAddressType("Communication");
					address1.setPincode(String.valueOf(pinCode));
					address1.setCounty("India");
					address1.setRural(false);
					address1.setUrban(true);

					DmsAddress address2 = new DmsAddress();
					address2.setAddressType("Permanent");
					address2.setPincode(String.valueOf(pinCode));
					address2.setCounty("India");
					address2.setRural(false);
					address2.setUrban(true);

					List<DmsAddress> addList = new ArrayList<>();
					addList.add(address1);
					addList.add(address2);

					dmsLeadDto.setDmsAddresses(addList);
					leadMgmtRoot.setDmsContactDto(contactDto);
					leadMgmtRoot.setDmsLeadDto(dmsLeadDto);
					list.add(leadMgmtRoot);
				}
				cnt++;
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return list;
	}

	private String getRefNumber(LeadCustRefReq custRefReq) {
		LeadCustomerRefRoot res = restTemplate.postForObject(leadCustRefUrl, custRefReq, LeadCustomerRefRoot.class);
		log.debug("LeadCustomerRefRoot " + res);
		return res.getDmsEntity().getLeadCustomerReference().getReferencenumber();
	}

	private Integer getEnqValue(String enq, Integer orgId) throws JsonMappingException, JsonProcessingException {
		log.debug("Given enq " + enq);
		if (null != enqList && enqList.isEmpty()) {
			enqList = getEnqList(orgId);
		}
		log.debug("enqList " + enqList);
		String val = enqList.stream().filter(x -> x.getName().equalsIgnoreCase(enq)).findAny().get().getValue();
		if (null != val) {
			return Integer.parseInt(val);
		} else {
			return 0;
		}
	}

	private List<EnqSource> getEnqList(Integer orgId) {
		try {
			String tmp = sourceOfEnqUrl;
			tmp = tmp.replaceAll("{ORG_ID}", String.valueOf(orgId));
			ResponseEntity<String> response = restTemplate.getForEntity(sourceOfEnqUrl, String.class);
			
			EnqSource[] arr = om.readValue(response.getBody(), EnqSource[].class);
			enqList = Arrays.asList(arr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return enqList;
	}

	private String getStringCellValue(Cell cell) {
		if (null != cell) {
			int cellType = cell.getCellType();
			if (cellType == 1) {
				return cell.getStringCellValue();

			}
		}
		return "";
	}

	private Integer getIntCellValue(Cell cell) {
		if (null != cell) {
			int cellType = cell.getCellType();
			if (cellType == 0) {
				Double d = cell.getNumericCellValue();

				System.out.println("phone " + d + " int " + String.valueOf(d));
				return d.intValue();

			}
		}
		return 0;
	}

	private Integer getBranchId(String branchId) {
		log.info("Inside getBranchName,Given Branch ID : " + branchId);
		Integer res = null;
		branchId = "\'" + branchId + "\'";
		String query = "SELECT branch_id FROM dms_branch where name=" + branchId;
		try {
			if (null != branchId) {
				List<Object> objArr = entityManager.createNativeQuery(query.replaceAll("<ID>", branchId))
						.getResultList();
				res = (Integer) objArr.get(0);
			} else {
				res = 0;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}

	@Override
	public List<AutoLeadAllocationRes> mapEmpLeads(LeadMapReq req) throws DynamicFormsServiceException {
		// List<LeadMapRoot> list = new ArrayList<>();
		List<AutoLeadAllocationRes> list = new ArrayList<>();
		try {
			if (req != null) {

				for (String leadId : req.getUniversalIdList()) {
					LeadMapSalesConsultantReq salesReq = buildSalesReq(getLeadMapDataByUniversalId(leadId),
							req.getToMapEmpName(), req.getLoggedInEmpName());
					log.debug("salesReq for lead  " + leadId);
					
					String jsonReq = new Gson().toJson(salesReq);
					
					log.debug("salesReq :" + jsonReq);

					HttpHeaders manualheaders = new HttpHeaders();
					manualheaders.setContentType(MediaType.APPLICATION_JSON);
					log.debug("making call to leadSalesConsultantUrl");
					HttpEntity<String> entity = new HttpEntity<String>(jsonReq, manualheaders);
					LeadMapRoot res = restTemplate.postForObject(leadSalesConsultantUrl, entity, LeadMapRoot.class);

					AutoLeadAllocationRes autoRes = new AutoLeadAllocationRes();
					log.debug("auto allociation  for manual starts here::: " + leadId);

					log.debug("Making First call of auto alloction");
					ResponseEntity<AutoLeadTask> autoLeadEntity = restTemplate
							.getForEntity(autoLeadFirstUrl.replace("<LEAD_ID>", leadId), AutoLeadTask.class);

					log.debug("RESPONSEE OF FIRST CALL ");
					log.debug("OBJECT " + autoLeadEntity.getBody());
					String firstResponseInString = new Gson().toJson(autoLeadEntity.getBody());
					log.debug("STRIN FORMAT OF FIRST " + firstResponseInString);
					log.debug("" + autoLeadEntity.getBody());
					if (autoLeadEntity.getStatusCode().is2xxSuccessful()) {
						AutoLeadTask autoLead = autoLeadEntity.getBody();
						List<Task> taskList = autoLead.getDmsEntity().getTasks();
						log.debug("Avialable tasks are " + taskList.size());
						taskList = taskList.stream().filter(x -> x.getTaskName().equalsIgnoreCase("Create Enquiry"))
								.collect(Collectors.toList());
						log.debug("Avialable tasks are afeter " + taskList.size());
						ResponseEntity<AutoLeadSecondCallRoot> secondRootEntity = null;
						for (Task task : taskList) {
							task.setTaskStatus("CLOSED");
							String secondJsonReq = new Gson().toJson(task);
							log.debug("Making Second call of auto alloction");
							log.debug("secondJsonReq:" + secondJsonReq);
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							HttpEntity<String> requestUpdate = new HttpEntity<>(secondJsonReq, headers);
							secondRootEntity = restTemplate.exchange(autoLeadSecondUrl, HttpMethod.PUT, requestUpdate,
									AutoLeadSecondCallRoot.class);

						}
						System.out.println(" Second status code " + secondRootEntity.getStatusCode().toString());
						String secondCallResponseString = new Gson().toJson(secondRootEntity);
						log.debug("SECOND RESPONSE STRING " + secondCallResponseString);
						;

						if (null != secondRootEntity && secondRootEntity.getStatusCode().toString().contains("200")) {
							log.debug("response of second call " + secondRootEntity.getBody());
							log.debug("Making Third call of auto alloction ");
							String thirdTmpUrl = autoLeadThridUrl.replace("<LEAD_ID>", leadId);
							System.out.println("third url  " + thirdTmpUrl);

							HttpHeaders headers3 = new HttpHeaders();
							headers3.setContentType(MediaType.APPLICATION_JSON);
							// headers3.set("auth-token", thirdTmpUrl)
							// headers3.set("auth-token","eyJraWQiOiI1Szd2U0VkMjRoQXJISlVkTWhLTUFJZkhUQVczN1RDZzhsMDU4K3A0VGMwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2MTk5M2UzMi0yY2U2LTQyNWMtOGE2My1mNThjYzA5NTBjYTMiLCJhZGRyZXNzIjp7ImZvcm1hdHRlZCI6Imh5ZCJ9LCJnZW5kZXIiOiJNIiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmFwLXNvdXRoLTEuYW1hem9uYXdzLmNvbVwvYXAtc291dGgtMV92NWZnTk1pQm0iLCJwaG9uZV9udW1iZXJfdmVyaWZpZWQiOnRydWUsImNvZ25pdG86dXNlcm5hbWUiOiJzdXBlcmFkbWluIiwibWlkZGxlX25hbWUiOiJudWxsIiwiYXVkIjoiN2Rtcm1wb2F2Mm5sOHVubW91ZXB0dGUzN2UiLCJldmVudF9pZCI6IjFiNzE2MDkyLTJlZGEtNDEyOS1iM2Q4LTE2OGQ2OTMwZGM2YSIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjMyMjE2MzY0LCJjdXN0b206YnJhbmNoSWQiOiIxIiwibmFtZSI6InN1cGVyYWRtaW4iLCJwaG9uZV9udW1iZXIiOiIrOTE5OTAxMDk5OTQ0IiwiY3VzdG9tOm9yZ0lkIjoiMSIsImV4cCI6MTYzMjQxODcxOSwiaWF0IjoxNjMyNDE1MTE5LCJmYW1pbHlfbmFtZSI6Im51bGwiLCJlbWFpbCI6ImNoYW5kcmF2QGJoYXJhdGdyb3VwZS5jb20ifQ.U5nSXgTU1T0y7u8mhc30jTUMdURkZHFyjAtuW_ShygE7EMIp8eJTm11ymL6PyBCOUAXCHdeoXzvGkYvXIgqdj7Hz06scW7Bv6_wKSN0J5sNH4u-VdcavpvtCZKaZfDQnj8YjQK0DMawk1o48eF6SGUJ4UXcW5jt0ufihH0lMk84DeoWV2nP9P5aMKxCsTtLH3WG7avCiYjktC7ACSTxgLE9w1r4JsMFQ4bZN8W_88XDhy05itIdjHyRGmnSMK4bDPfQNP8J6fzrakHJqN9qmMXsrGYkzz2atEm8L6IRVwPK1has6PXqMglUgkYrDRTD3tSJxGi6FdDTBPuulGnfq0g");
							HttpEntity<String> requestUpdate3 = new HttpEntity<>("{}", headers3);
							// Thread.sleep(1000);
							String autoLeadThirdEntity = restTemplate.postForObject(thirdTmpUrl, requestUpdate3,
									String.class);
							// ResponseEntity<String> autoLeadThirdEntity =
							// restTemplate.exchange(thirdTmpUrl, HttpMethod.POST, requestUpdate3,
							// String.class);
							if (null != autoLeadThirdEntity) {
								String thirdresponse = autoLeadThirdEntity;
								System.out.println("thrid response " + thirdresponse);
								ObjectMapper om = new ObjectMapper();
								AutoLeadSecondCallRoot thridAutoLeadDmsEntity = om.readValue(thirdresponse,
										AutoLeadSecondCallRoot.class);
								// AutoLeadDmsEntity thridAutoLeadDmsEntity = autoLeadThirdEntity.getBody();
								System.out.println("thridAutoLeadDmsEntity " + thridAutoLeadDmsEntity);
								if (null != thridAutoLeadDmsEntity.getDmsEntity().getTask()) {
									autoRes.setAllocatedEmpName(
											thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee().getEmpName());

									LeadCustRefReq refReq = new LeadCustRefReq();
									refReq.setLeadstage("ENQUIRY");
									refReq.setBranchid(thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee()
											.getBranchId());
									refReq.setOrgid(
											thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee().getOrgId());

									autoRes.setGeneratedRefNumber(getRefNumber(refReq));
									list.add(autoRes);
								}
							}
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	private LeadMapSalesConsultantReq buildSalesReq(LeadMapRoot input, String empName, String loggedInEmpName) {
		LeadMapSalesConsultantReq req = new LeadMapSalesConsultantReq();
		try {

			DmsLeadMapDto leadMap = input.getDmsEntity().getDmsLeadDto();
			DmsContactMapDto contactMap = input.getDmsEntity().getDmsContactDto();
			req.setId(leadMap.getId());
			req.setFirstName(leadMap.getFirstName());
			req.setPhone(leadMap.getPhone());
			req.setLastName(leadMap.getLastName());
			req.setEmail(leadMap.getEmail());
			req.setCreateddatetime(leadMap.getCreateddatetime());
			req.setCrmUniversalId(leadMap.getCrmUniversalId());
			req.setDateOfEnquiry(leadMap.getDateOfEnquiry());
			req.setEnquirySegment(leadMap.getEnquirySegment());
			req.setLeadStage(leadMap.getLeadStage());
			req.setModel(leadMap.getModel());
			req.setOrganizationId(leadMap.getOrganizationId());
			req.setSalesConsultant(empName);
			req.setSourceOfEnquiry(leadMap.getSourceOfEnquiry());

			if (null != contactMap && null != contactMap.getCustomerType()
					&& contactMap.getCustomerType().equalsIgnoreCase("Individual")) {
				req.setDmscontactid(leadMap.getDmscontactid());
			} else {
				req.setDmsaccountid(leadMap.getDmsaccountid());
			}

			req.setEnquirySource(leadMap.getEnquirySource());
			req.setReferencenumber(leadMap.getReferencenumber());
			req.setBranchId(leadMap.getBranchId());
			System.out.println("Setting logged in name "+loggedInEmpName);
			req.setCreatedBy(loggedInEmpName);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return req;
	}

	private LeadMapRoot getLeadMapDataByUniversalId(String leadId) {
		log.debug("Inside getLeadMapDataByUniversalId(),fetching data for " + leadId);
		LeadMapRoot root = new LeadMapRoot();
		try {
			ResponseEntity<LeadMapRoot> entity = restTemplate.getForEntity(leadMapUrl.replaceAll("<LEAD_ID>", leadId),
					LeadMapRoot.class);
			root = entity.getBody();
			log.debug("LeadMap " + root);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return root;
	}

	@Override
	public List<AutoLeadAllocationRes> autoAllocationOfLeads(LeadMapReq req) throws DynamicFormsServiceException {
		List<AutoLeadAllocationRes> list = new ArrayList<>();
		log.debug("autoAllocationOfLeads{}");
		try {
			if (req != null) {

				for (String leadId : req.getUniversalIdList()) {
					AutoLeadAllocationRes autoRes = new AutoLeadAllocationRes();
					log.debug("auto allociation for " + leadId);

					log.debug("Making First call of auto alloction");
					ResponseEntity<AutoLeadTask> autoLeadEntity = restTemplate
							.getForEntity(autoLeadFirstUrl.replace("<LEAD_ID>", leadId), AutoLeadTask.class);

					if (autoLeadEntity.getStatusCode().is2xxSuccessful()) {
						AutoLeadTask autoLead = autoLeadEntity.getBody();
						List<Task> taskList = autoLead.getDmsEntity().getTasks();
						log.debug("Avialable tasks are " + taskList.size());
						taskList = taskList.stream().filter(x -> x.getTaskName().equalsIgnoreCase("Create Enquiry"))
								.collect(Collectors.toList());
						log.debug("Avialable tasks are afeter " + taskList.size());
						ResponseEntity<AutoLeadSecondCallRoot> secondRootEntity = null;
						for (Task task : taskList) {
							task.setTaskStatus("CLOSED");
							String secondJsonReq = new Gson().toJson(task);
							log.debug("Making Second call of auto alloction");
							log.debug("secondJsonReq:" + secondJsonReq);
							HttpHeaders headers = new HttpHeaders();
							headers.setContentType(MediaType.APPLICATION_JSON);
							HttpEntity<String> requestUpdate = new HttpEntity<>(secondJsonReq, headers);
							secondRootEntity = restTemplate.exchange(autoLeadSecondUrl, HttpMethod.PUT, requestUpdate,
									AutoLeadSecondCallRoot.class);

						}
						log.debug(" RESPONSE OF SECCOND CALL " + secondRootEntity.getStatusCode().toString());
						if (null != secondRootEntity && secondRootEntity.getStatusCode().toString().contains("200")) {
							log.debug("response of second call " + secondRootEntity.getBody());
							log.debug("Making Third call of auto alloction ");
							String thirdTmpUrl = autoLeadThridUrl.replace("<LEAD_ID>", leadId);
							System.out.println("third url  " + thirdTmpUrl);

							// String autoLeadThirdEntity =
							// restTemplate.postForObject(thirdTmpUrl,"{}",String.class);

							HttpHeaders headers3 = new HttpHeaders();
							headers3.setContentType(MediaType.APPLICATION_JSON);
							// headers3.set("auth-token", thirdTmpUrl);
							headers3.set("auth-token",
									"eyJraWQiOiI1Szd2U0VkMjRoQXJISlVkTWhLTUFJZkhUQVczN1RDZzhsMDU4K3A0VGMwPSIsImFsZyI6IlJTMjU2In0.eyJzdWIiOiI2MTk5M2UzMi0yY2U2LTQyNWMtOGE2My1mNThjYzA5NTBjYTMiLCJhZGRyZXNzIjp7ImZvcm1hdHRlZCI6Imh5ZCJ9LCJnZW5kZXIiOiJNIiwiaXNzIjoiaHR0cHM6XC9cL2NvZ25pdG8taWRwLmFwLXNvdXRoLTEuYW1hem9uYXdzLmNvbVwvYXAtc291dGgtMV92NWZnTk1pQm0iLCJwaG9uZV9udW1iZXJfdmVyaWZpZWQiOnRydWUsImNvZ25pdG86dXNlcm5hbWUiOiJzdXBlcmFkbWluIiwibWlkZGxlX25hbWUiOiJudWxsIiwiYXVkIjoiN2Rtcm1wb2F2Mm5sOHVubW91ZXB0dGUzN2UiLCJldmVudF9pZCI6IjFiNzE2MDkyLTJlZGEtNDEyOS1iM2Q4LTE2OGQ2OTMwZGM2YSIsInRva2VuX3VzZSI6ImlkIiwiYXV0aF90aW1lIjoxNjMyMjE2MzY0LCJjdXN0b206YnJhbmNoSWQiOiIxIiwibmFtZSI6InN1cGVyYWRtaW4iLCJwaG9uZV9udW1iZXIiOiIrOTE5OTAxMDk5OTQ0IiwiY3VzdG9tOm9yZ0lkIjoiMSIsImV4cCI6MTYzMjQxODcxOSwiaWF0IjoxNjMyNDE1MTE5LCJmYW1pbHlfbmFtZSI6Im51bGwiLCJlbWFpbCI6ImNoYW5kcmF2QGJoYXJhdGdyb3VwZS5jb20ifQ.U5nSXgTU1T0y7u8mhc30jTUMdURkZHFyjAtuW_ShygE7EMIp8eJTm11ymL6PyBCOUAXCHdeoXzvGkYvXIgqdj7Hz06scW7Bv6_wKSN0J5sNH4u-VdcavpvtCZKaZfDQnj8YjQK0DMawk1o48eF6SGUJ4UXcW5jt0ufihH0lMk84DeoWV2nP9P5aMKxCsTtLH3WG7avCiYjktC7ACSTxgLE9w1r4JsMFQ4bZN8W_88XDhy05itIdjHyRGmnSMK4bDPfQNP8J6fzrakHJqN9qmMXsrGYkzz2atEm8L6IRVwPK1has6PXqMglUgkYrDRTD3tSJxGi6FdDTBPuulGnfq0g");
							HttpEntity<String> requestUpdate3 = new HttpEntity<>("{}", headers3);
							Thread.sleep(100);
							String autoLeadThirdEntity = restTemplate.postForObject(thirdTmpUrl, requestUpdate3,
									String.class);
							// ResponseEntity<String> autoLeadThirdEntity =
							// restTemplate.exchange(thirdTmpUrl, HttpMethod.POST, requestUpdate3,
							// String.class);
							if (null != autoLeadThirdEntity) {
								String thirdresponse = autoLeadThirdEntity;
								System.out.println("thrid response " + thirdresponse);
								ObjectMapper om = new ObjectMapper();
								AutoLeadSecondCallRoot thridAutoLeadDmsEntity = om.readValue(thirdresponse,
										AutoLeadSecondCallRoot.class);
								// AutoLeadDmsEntity thridAutoLeadDmsEntity = autoLeadThirdEntity.getBody();
								System.out.println("thridAutoLeadDmsEntity " + thridAutoLeadDmsEntity);
								if (null != thridAutoLeadDmsEntity.getDmsEntity().getTask()) {
									autoRes.setAllocatedEmpName(
											thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee().getEmpName());

									LeadCustRefReq refReq = new LeadCustRefReq();
									refReq.setLeadstage("ENQUIRY");
									refReq.setBranchid(thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee()
											.getBranchId());
									refReq.setOrgid(
											thridAutoLeadDmsEntity.getDmsEntity().getTask().getAssignee().getOrgId());

									autoRes.setGeneratedRefNumber(getRefNumber(refReq));
									list.add(autoRes);
								}
							}
						}
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}
	
	@Autowired
	LeadStageRefDao leadStageRefDao;

	@Override
	public DMSResponse saveCustomerRef(LeadCustomerReferenceDto leadDto) throws DynamicFormsServiceException {
		DMSResponse res = new DMSResponse();
		try {
			String ref=null;
		
			if(null!=leadDto) {
			
				String stage = leadDto.getLeadstage();
				//if(stage.equalsIgnoreCase("PREENQUIRY")) {
					
				//}
				
				ref = getRefNumber(stage);
				LeadStageRefEntity leadStage = new LeadStageRefEntity();
				leadStage.setBranchId(leadDto.getBranchid());
				leadStage.setOrgId(leadDto.getOrgid());
				leadStage.setStageName(stage);
				leadStage.setLeadId(leadDto.getLeadId());
				leadStage.setRefNo(ref);
				leadStage.setStartDate(Timestamp.from(Instant.now()));
				leadStageRefDao.save(leadStage);
			}
			log.debug("ref:::"+ref);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private String getRefNumber(String stage) {
		String dealerCode = "S5235";
		String prefix = getPrefix(stage);
		int year = Calendar.getInstance().get(Calendar.YEAR);
		log.debug("year::" + year);
		String ref = null;
		try {
			List<LeadStageRefEntity> leadList = leadStageRefDao.getRecentRefByStage(stage);
			String lastRef = null;
			if (null == lastRef) {
				lastRef = "000001";
			}
			if (null != leadList && leadList.isEmpty()) {
				lastRef = "000001";
			} else {
				LeadStageRefEntity refEntity = leadList.get(0);
				refEntity.setEndDate(Timestamp.from(Instant.now()));
				leadStageRefDao.save(refEntity);
				String dbLastRf = refEntity.getRefNo();
				log.debug("dbLastRf::" + dbLastRf);
				String tmp = dbLastRf.substring(dbLastRf.length() - 6);
				log.debug("SEQ NO :::" + tmp);
				Integer i = Integer.parseInt(tmp);
				i = i + 1;
				lastRef = String.format("%06d", i);
				log.debug("Updated lastRef " + lastRef);
			}
			log.debug("lastRef:::" + lastRef);
			ref = buildRefNumber(dealerCode, prefix, year, lastRef);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return ref;
	}
	
	
	
	
	public String getPrefix(String stage) {
		
		 String prefix = null;
         switch (stage) {
             case "PREENQUIRY":
                 prefix = "PENQ";
                 break;
             case "ENQUIRY":
                 prefix = "ENQ";
                 break;
             case "PREBOOKING":
                 prefix = "PBK";
                 break;
             case "BOOKING":
                 prefix = "BOK";
                 break;
             case "INVOICE":
                 prefix = "INV";
                 break;
             case "PREDELIVERY":
                 prefix = "PDL";
                 break;
             case "DELIVERY":
                 prefix = "DEL";
                 break;
         }
		return prefix;
	}

	private String buildRefNumber(String dealerCode, String prefix, int year, String lastRef) {
		String ref = dealerCode+" - "+prefix+year+lastRef;
		return ref;
	}

	@Override
	public List<LeadStageRefEntity> getLeadStagesById(String id) throws DynamicFormsServiceException {
		List<LeadStageRefEntity> list = null;
		try {
			list = leadStageRefDao.getLeadStagesById(id);
		} catch (Exception e) {
			throw new DynamicFormsServiceException(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return list;
	}
}
