package com.automate.df.util;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.env.Environment;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import com.automate.df.constants.DynamicFormConstants;
import com.automate.df.dao.AdressEntityRepo;
import com.automate.df.dao.AutFileColmnProcRepo;
import com.automate.df.dao.AutFileExtractRepo;
import com.automate.df.dao.AutFileImportRepo;
import com.automate.df.dao.DBUtil;
import com.automate.df.entity.AutomateFileColumnProcessor;
import com.automate.df.entity.AutomateFileExtract;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.Accessory;
import com.automate.df.model.AccessoryMapping;
import com.automate.df.model.AutModuleMenuExcelBO;
import com.automate.df.model.BaseResponse;
import com.automate.df.model.BulkUploadChildChild;
import com.automate.df.model.BulkUploadParent;
import com.automate.df.model.BulkUploadParentChild;
import com.automate.df.model.BulkUploadReq;
import com.automate.df.model.DynamicFormFieldBO;
import com.automate.df.model.DynamicRoleFormBO;
import com.automate.df.model.ErrorDetails;
import com.automate.df.model.ExtendedWaranty;
import com.automate.df.model.InsuranceAddOn;
import com.automate.df.model.InsuranceAddonRequest;
import com.automate.df.model.InsuranceDetails;
import com.automate.df.model.InsuranceDetailsRequest;
import com.automate.df.model.OfferCorporateDetails;
import com.automate.df.model.OfferDetails;
import com.automate.df.model.OfferMapping;
import com.automate.df.model.ResponseJson;
import com.automate.df.model.VehicleDetails;
import com.automate.df.model.VehicleImage;
import com.automate.df.model.VehicleOnRoadPrice;
import com.automate.df.model.VehicleVarient;
import com.fasterxml.jackson.databind.ObjectMapper;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Component
public class DynamicFormExcelUtils {
	
	@Autowired
    Environment env;
	
	@Autowired
	AutFileColmnProcRepo autFileColmnProcRepo;

	@Autowired
	AutFileExtractRepo autFileExtractRepo;
	
	@Autowired
	AutFileImportRepo autFileImportRepo;
	
	@Autowired
	AdressEntityRepo adressEntityRepo;
	
	
	@Autowired
	DBUtil dbUtil;
	
	@Autowired
    private RestTemplate restTemplate;
	
	@Autowired
    RestTemplateUtil restTemplateUtil;
	
	@Value("${file.controller.url}")
	String fileControllerUrl;
	
	@Value("${tmp.path}")
	String tmpPath;
	
	public static List<DynamicFormFieldBO> processDynamicFormExcelCSV(MultipartFile dynamicFormExcelCSV) throws DynamicFormsServiceException {

		CellProcessor[] processors = new CellProcessor[] {
				new Optional(), // sno
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), //
				new Optional()

		};

		ICsvBeanReader beanReader;
		List<DynamicFormFieldBO> dynamicFormExcelList = new ArrayList<>();

		try {
			Path tmpDir = Files.createTempDirectory("temp");
			Path tempFilePath = tmpDir.resolve(dynamicFormExcelCSV.getOriginalFilename());
			Files.write(tempFilePath, dynamicFormExcelCSV.getBytes());
			beanReader = new CsvBeanReader(new FileReader(tempFilePath.toString()), CsvPreference.STANDARD_PREFERENCE);

			String[] header = beanReader.getHeader(true);
			DynamicFormFieldBO dynamicFormExcelDO = null;
			while ((dynamicFormExcelDO = beanReader.read(DynamicFormFieldBO.class, header, processors)) != null) {
				
				dynamicFormExcelList.add(dynamicFormExcelDO);
				

			}

			

		} catch (IOException e) {
			log.error("Failed to process Excel sheet ", e);
			throw new DynamicFormsServiceException("INTERNAL_SERVER_ERROR",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return dynamicFormExcelList;

	}

	public static List<DynamicRoleFormBO> processDynamicRoleFormExcelCSV(MultipartFile dynamicFormExcelCSV) throws DynamicFormsServiceException {

		CellProcessor[] processors = new CellProcessor[] {
				new Optional(), // sno
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional(), // 
				new Optional()

		};

		ICsvBeanReader beanReader;
		List<DynamicRoleFormBO> dynamicFormExcelList = new ArrayList<>();

		try {
			Path tmpDir = Files.createTempDirectory("temp");
			Path tempFilePath = tmpDir.resolve(dynamicFormExcelCSV.getOriginalFilename());
			Files.write(tempFilePath, dynamicFormExcelCSV.getBytes());
			beanReader = new CsvBeanReader(new FileReader(tempFilePath.toString()), CsvPreference.STANDARD_PREFERENCE);

			String[] header = beanReader.getHeader(true);
			DynamicRoleFormBO dynamicFormExcelDO = null;
			while ((dynamicFormExcelDO = beanReader.read(DynamicRoleFormBO.class, header, processors)) != null) {
				
				dynamicFormExcelList.add(dynamicFormExcelDO);
				

			}

			

		} catch (IOException e) {
			log.error("Failed to process Excel sheet ", e);
			throw new DynamicFormsServiceException("INTERNAL_SERVER_ERROR",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return dynamicFormExcelList;

	}
	
	public static List<AutModuleMenuExcelBO> processAutomateMenuExcelCSV(MultipartFile dynamicMenuExcelCSV)
			throws DynamicFormsServiceException {

		CellProcessor[] processors = new CellProcessor[] { new Optional(), // sno
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional(), //
				new Optional() };

		ICsvBeanReader beanReader;
		List<AutModuleMenuExcelBO> dynamicMenuExcelList = new ArrayList<>();

		try {
			Path tmpDir = Files.createTempDirectory("temp");
			Path tempFilePath = tmpDir.resolve(dynamicMenuExcelCSV.getOriginalFilename());
			Files.write(tempFilePath, dynamicMenuExcelCSV.getBytes());
			beanReader = new CsvBeanReader(new FileReader(tempFilePath.toString()), CsvPreference.STANDARD_PREFERENCE);

			String[] header = beanReader.getHeader(true);
			AutModuleMenuExcelBO dynamicMenuExcelDO = null;
			while ((dynamicMenuExcelDO = beanReader.read(AutModuleMenuExcelBO.class, header, processors)) != null) {

				dynamicMenuExcelList.add(dynamicMenuExcelDO);

			}

		} catch (IOException e) {
			log.error("Failed to process Excel sheet ", e);
			throw new DynamicFormsServiceException("INTERNAL_SERVER_ERROR",
					HttpStatus.INTERNAL_SERVER_ERROR);
		}

		return dynamicMenuExcelList;

	}
	
	
	public  ErrorDetails processBulkExcel(MultipartFile bulkExcel,BulkUploadReq bulkUploadReq)
			throws DynamicFormsServiceException {
		
	          Resource file = null;
		
		if (bulkExcel.isEmpty()) {
			log.error("Failed to process Excel sheet ");
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
		try {
 
			Path tmpDir = Files.createTempDirectory("temp");
			Path tempFilePath = tmpDir.resolve(bulkExcel.getOriginalFilename());
			Files.write(tempFilePath, bulkExcel.getBytes());
			String fileName = bulkExcel.getOriginalFilename();
			  fileName = fileName.substring(0, fileName.indexOf("."));
			 
				  
//			  if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("enquiry")) {
//				  Enquiry enq = new Enquiry();
//				  return  uploadFileData(tempFilePath.toString(), enq,bulkUploadReq);
//			  } 
//			  if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("preEnquiry")) {
//				  Class c = Class.forName("com.automate.df.model."+bulkUploadReq.getFileuploadName());
//					Object preenq = c.newInstance();
//				  PreEnquiry preenq = new PreEnquiry();
				  return uploadFileData(tempFilePath.toString(), bulkUploadReq);
//			  }
//			  if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("address")) {
//				  AddressEntity addrs = new AddressEntity();
//				  return uploadMultiFileData(tempFilePath.toString(), addrs,bulkUploadReq);
//			  }
			// Get the file and save it somewhere
			/*
			 * byte[] bytes = file.getBytes(); Path path = Paths.get("C://temp//" +
			 * file.getOriginalFilename()); Files.write(path, bytes);
			 */
			
			  
		} catch (Exception e) {
			log.error("Failed to process Excel sheet ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
				

		/*
		 * CellProcessor[] processors = new CellProcessor[] { new Optional(), // sno new
		 * Optional(), // new Optional(), // new Optional(), // new Optional(), // new
		 * Optional(), // new Optional(), // new Optional(), // new Optional() };
		 * 
		 * ICsvBeanReader beanReader; List<AutModuleMenuExcelBO> dynamicMenuExcelList =
		 * new ArrayList<>();
		 * 
		 * try { Path tmpDir = Files.createTempDirectory("temp"); Path tempFilePath =
		 * tmpDir.resolve(dynamicMenuExcelCSV.getOriginalFilename());
		 * Files.write(tempFilePath, dynamicMenuExcelCSV.getBytes()); beanReader = new
		 * CsvBeanReader(new FileReader(tempFilePath.toString()),
		 * CsvPreference.STANDARD_PREFERENCE);
		 * 
		 * String[] header = beanReader.getHeader(true); AutModuleMenuExcelBO
		 * dynamicMenuExcelDO = null; while ((dynamicMenuExcelDO =
		 * beanReader.read(AutModuleMenuExcelBO.class, header, processors)) != null) {
		 * 
		 * dynamicMenuExcelList.add(dynamicMenuExcelDO);
		 * 
		 * }
		 * 
		 * } catch (IOException e) { log.error("Failed to process Excel sheet ", e);
		 * throw new
		 * DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
		 * HttpStatus.INTERNAL_SERVER_ERROR); }
		 * 
		 * return dynamicMenuExcelList;
		 */
		
	}
	
	public  boolean processBulkExcelVehicles(MultipartFile bulkExcel,BulkUploadReq bulkUploadReq)
			throws DynamicFormsServiceException {
		
	          Resource file = null;
		
		if (bulkExcel.isEmpty()) {
			log.error("Failed to process Excel sheet ");
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
 
		try {
 
			Path tmpDir = Files.createTempDirectory("temp");
			Path tempFilePath = tmpDir.resolve(bulkExcel.getOriginalFilename());
			Files.write(tempFilePath, bulkExcel.getBytes());
			String fileName = bulkExcel.getOriginalFilename();
			  fileName = fileName.substring(0, fileName.indexOf("."));
			 
				  

				  return uploadVehicleData(tempFilePath.toString(), bulkUploadReq);

			
			  
		} catch (Exception e) {
			log.error("Failed to process Excel sheet ", e);
			throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
				
		
	}
	
	
	public Timestamp getCurrentTmeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}
		
	
	@SuppressWarnings("deprecation")
	private  boolean uploadVehicleData(String inputFilePath,BulkUploadReq bulkUploadReq) throws DynamicFormsServiceException, IOException{

		
		Workbook workbook = null;
		Workbook workbookError = new XSSFWorkbook();
		MultipartFile fi = null;
		Sheet sheet = null;
		List<Object> list = new ArrayList<Object>();
		List<Object> childList = new ArrayList<Object>();
		InputStreamResource file = null;
		 byte[] bytes = new byte[1024];
		 ByteArrayOutputStream out = new ByteArrayOutputStream();
		 Map<String, String> rowList = new HashMap<>();
		 boolean response = false;
//		 BulkUploadParent finalParent = new BulkUploadParent();
		try
		{
			 AutomateFileExtract fileExtrct =  autFileExtractRepo.findByPageIdentifier(bulkUploadReq.getPageIdentifier());
			 bulkUploadReq.setFileuploadName(fileExtrct.getName()); 
			 workbook = getWorkBook(new File(inputFilePath));
			sheet = workbook.getSheetAt(0);
			XSSFRow header_row = (XSSFRow) sheet.getRow(0);
			 int rowCount = sheet.getLastRowNum();
			Sheet sheetError = workbookError.createSheet(bulkUploadReq.getFileuploadName());
			
			/*Build the header portion of the Output File*/
			/*
			 * String headerDetails= "EmployeeId,EmployeeName,Address,Country"; String
			 * headerNames[] = headerDetails.split(",");
			 */
 
			/*Read and process each Row*/
			
			//Collection<?> employeeList = new ArrayList<>();
			boolean firstTime = true;
			 Object childParentTempObject = null;
			Iterator<Row> rowIterator = sheet.iterator();
			 Map<Object, List<Object>> childChildList = new HashMap<>();
			 Map<String, Object> parentMap = new HashMap<>();
			 Map<String, Object> parentChildMap = new HashMap<>();
			 Map<String, Object> childChildMap = new HashMap<>();
			 List<Object> childTempList = new ArrayList<Object>();
			 List<BulkUploadParent> parentList = new ArrayList<BulkUploadParent>();
			 List<BulkUploadParentChild> parentChilList = new ArrayList<BulkUploadParentChild>();
			 List<BulkUploadChildChild> childChilList = new ArrayList<BulkUploadChildChild>();
			while(rowIterator.hasNext())
			{
				Row row = rowIterator.next();
				
				if(row.getRowNum() !=0) {
				//Read and process each column in row
				//Class<T> excelTemplateVO = new Class<T>();
				 Object newObject = null;
				 Object childObject = null;
				 Object childChildObject = null;
				 Object roadPriceEndObject = null;
				 Object roadTaxEndObject = null;
				 Object exWarrEndObject = null;
				 Object insAddEndObject = null;
				 Object insVarMapEndObject = null;
//				  if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("enquiry")) {
//					  Enquiry enq = new Enquiry();
//				int count=0;
//				while(count < header_row.getLastCellNum()){
//					String methodName = "set"+header_row.getCell(count);
//					String inputCellValue = getCellValueBasedOnCellType(row,count++);
//					newObject = setValueIntoObject(enq, obj.getClass(), methodName, "java.lang.String", inputCellValue);
//				}
//              
//				list.add(newObject);
//			}else   if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("preEnquiry")) {
				
				Class c = Class.forName("com.automate.df.model."+bulkUploadReq.getFileuploadName());
				Object preenq = c.newInstance();
				Class onRoadClass = Class.forName("com.automate.df.model.VehicleOnRoadPrice");
				Object onRoadObject = onRoadClass.newInstance();
				Class roadTaxClass = Class.forName("com.automate.df.model.RoadTax");
				Object roadTaxObject = roadTaxClass.newInstance();
				Class exWarrantyClass = Class.forName("com.automate.df.model.ExtendedWaranty");
				Object exWarrantyObject = exWarrantyClass.newInstance();
				Class insAddOnClass = Class.forName("com.automate.df.model.InsuranceAddOn");
				Object insAddOnObject = insAddOnClass.newInstance();
				Class insVarMapClass = Class.forName("com.automate.df.model.InsuranceVarientMapping");
				Object insVarMapObject = insVarMapClass.newInstance();
				Class corporateOnClass = Class.forName("com.automate.df.model.OfferCorporateDetails");
				Object corporate = corporateOnClass.newInstance();
				Object enq = null;
				 Object childChildObj = null;
//				PreEnquiry preenq = new PreEnquiry();
				if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
					Class child1Class = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
					 enq = child1Class.newInstance();
					
//						PreEnquiry preenq = new PreEnquiry();
						if(StringUtils.isNotBlank(fileExtrct.getChildChildPojo())) {
							Class childChildClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
							childChildObj = childChildClass.newInstance();
						}
//				Enquiry enq = new Enquiry();
				}
			int count=0;
			int rowcunt = 1;
			int maxRow = sheet.getLastRowNum();
//			String parentName = null;
//			String parentExcName = null;
//			
//			while(count < row.getLastCellNum()){
//				String methodName = row.getCell(count).toString();
//				String inputCellValue = getCellValueBasedOnCellType(row,count++);
//				if(methodName.contains("parent")) {
//				parentExcName = inputCellValue;
//				}
//			}
//
//			for(int i =1; i<= maxRow;i++) {
//				if(parentExcName.contains("parent"+i)) {
//					
//					parentName = "parent"+rowcunt;
//					if(i==rowcunt+1) {
//						rowcunt++;
//						Map<String, String> rowTempList = saveDFForm(list,childList,bulkUploadReq);
//					rowList.putAll(rowTempList);
//					}
//				}
//				}
			
//			if(parentName.equalsIgnoreCase(parentExcName)) {
			String snum = getCellValueBasedOnCellType(row,0);
			String parentIdenfier = getCellValueBasedOnCellType(row,1);
			String childParentIdentifier = null;
			while(count < header_row.getLastCellNum()){
//				if(header_row.getCell(count).toString().equalsIgnoreCase("ParentIdentifier")){
//					String identifierValue = getCellValueBasedOnCellType(row,count);
//					parentIdenfier = identifierValue;
//				}
//				String snum = null;
				
//				if(null != header_row.getCell(count)) {
//				 snum = header_row.getCell(count).toString();
//				}
				if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("parentchild")) {
					Object objChld = null;
//					PreEnquiry preenq = new PreEnquiry();
					if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
						Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
						objChld = childClass.newInstance();
//					Enquiry enq = new Enquiry();
					}
					
//					Enquiry objChld = new Enquiry();
							String child = header_row.getCell(count).toString();
//					if(child.equalsIgnoreCase("sno")) {
//						String methodName = "set"+header_row.getCell(count);
//						String inputCellValue = getCellValueBasedOnCellType(row,count++);
//						childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
//					}else {
							String[] parts = child.split("-");
					
					String header = parts[1]; // 034556
					String methodName = "set"+header;
					String inputCellValue = getCellValueBasedOnCellType(row,count++);
					if(StringUtils.isNotBlank(inputCellValue) && !(inputCellValue.equalsIgnoreCase("n"))) {
					childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
					childParentTempObject = childObject;											
//					}
//					if(header_row.getCell(count).toString().equalsIgnoreCase("ParentIdentifier")){
//						parentIdenfier = inputCellValue;
//					}
					}
					
					}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild")) {
						Object objChild = null;
//						PreEnquiry preenq = new PreEnquiry();
						if(StringUtils.isNotBlank(fileExtrct.getChildChildPojo())) {
							Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
							objChild = childClass.newInstance();
//						Enquiry enq = new Enquiry();
						}
						
//						Enquiry objChld = new Enquiry();
								String childChld = header_row.getCell(count).toString();
//						if(child.equalsIgnoreCase("sno")) {
//							String methodName = "set"+header_row.getCell(count);
//							String inputCellValue = getCellValueBasedOnCellType(row,count++);
//							childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
//						}else {
								String[] partsChild = childChld.split("-");
						
						String headerChild = partsChild[1]; // 034556
						String methodNameChild = "set"+headerChild;
						String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
						if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
							childChildObject = setValueIntoObject(childChildObj, objChild.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);
//							childTempList.add(childChildObject);
							
							
//						}
						}
						
						}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild1")) {
//							Object objChild = null;

							Class onRoadchildClass = Class.forName("com.automate.df.model.VehicleOnRoadPrice");
							Object onRoadchildObj = onRoadchildClass.newInstance();
//								Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
//								objChild = childClass.newInstance();

									String childChld = header_row.getCell(count).toString();
									String[] partsChild = childChld.split("-");
							
							String headerChild = partsChild[1]; // 034556
							String methodNameChild = "set"+headerChild;
							String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
							if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
								roadPriceEndObject = setValueIntoObject(onRoadObject, onRoadchildObj.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);
								
								
							
							}
							
							}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild2")) {
//								Object objChild = null;
								Class roadTaxObjClass = Class.forName("com.automate.df.model.RoadTax");
								Object roadTaxObj = roadTaxObjClass.newInstance();
//									Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
//									objChild = childClass.newInstance();

								
//								Enquiry objChld = new Enquiry();
										String childChld = header_row.getCell(count).toString();
//								if(child.equalsIgnoreCase("sno")) {
//									String methodName = "set"+header_row.getCell(count);
//									String inputCellValue = getCellValueBasedOnCellType(row,count++);
//									childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
//								}else {
										String[] partsChild = childChld.split("-");
								
								String headerChild = partsChild[1]; // 034556
								String methodNameChild = "set"+headerChild;
								String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
								if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
									roadTaxEndObject = setValueIntoObject(roadTaxObject, roadTaxObj.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);
//									childTempList.add(childChildObject);
									
									
//								}
								}
								
								}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild3")) {
								
										
										Class exWarrantyObjClass = Class.forName("com.automate.df.model.ExtendedWarranty");
										Object exWarrantyObj = exWarrantyObjClass.newInstance();

									

											String childChld = header_row.getCell(count).toString();

											String[] partsChild = childChld.split("-");
									
									String headerChild = partsChild[1]; // 034556
									String methodNameChild = "set"+headerChild;
									String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
									if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
										exWarrEndObject = setValueIntoObject(childChildObj, exWarrantyObj.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);

									}
									
									}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild4")) {

										Class insAddOnObjClass = Class.forName("com.automate.df.model.InsuranceAddOn");
										Object insAddOnObj = insAddOnObjClass.newInstance();
										

												String childChld = header_row.getCell(count).toString();

												String[] partsChild = childChld.split("-");
										
										String headerChild = partsChild[1]; // 034556
										String methodNameChild = "set"+headerChild;
										String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
										if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
											insAddEndObject = setValueIntoObject(childChildObj, insAddOnObj.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);

										}
										
										}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild5")) {
											Class insVarMapObjClass = Class.forName("com.automate.df.model.InsuranceVarientMapping");
											Object insVarMapObj = insVarMapObjClass.newInstance();
											

													String childChld = header_row.getCell(count).toString();

													String[] partsChild = childChld.split("-");
											
											String headerChild = partsChild[1]; // 034556
											String methodNameChild = "set"+headerChild;
											String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
											if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
												insVarMapEndObject = setValueIntoObject(childChildObj, insVarMapObj.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);

											}
											
											} else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("primary")) { 
							Object obj = null;
							if(StringUtils.isNotBlank(fileExtrct.getPojo())) {
								Class mainClass = Class.forName("com.automate.df.model."+fileExtrct.getPojo());
								obj = mainClass.newInstance();

							}
							
							
							String parr = header_row.getCell(count).toString();
							String[] partsprr = parr.split("-");
							
							String headerPrr = partsprr[1]; // 034556
							String methodNamePrr = "set"+headerPrr;
//							String methodName = "set"+header_row.getCell(count);
				String inputCellValue = getCellValueBasedOnCellType(row,count++);
				if(StringUtils.isNotBlank(inputCellValue) && !(inputCellValue.equalsIgnoreCase("n"))) {
				newObject = setValueIntoObject(preenq, obj.getClass(), methodNamePrr, "java.lang.String", inputCellValue);
				}
				}else {
					count++;
				}
//				if(header_row.getCell(count).toString().contains("child")) {
//					Enquiry objChld = new Enquiry();
//							String child = header_row.getCell(count).toString();
//					String[] parts = child.split("-");
//					
//					String header = parts[1]; // 034556
//					String methodChildName = "set"+header;
//					if(row.toString().contains("child")) {
//					String inputChildCellValue = getCellValueBasedOnCellType(row,count++);
//					childObject = setValueIntoObject(enq, objChld.getClass(), methodChildName, "java.lang.String", inputChildCellValue);
//					}
//					}
				rowcunt++;
					}
//					}
			
			
			if(null != childObject) {
//			childList.add(childObject);
//				parentChildMap.put(snum, childObject);
				BulkUploadParentChild perCh = new BulkUploadParentChild();
				perCh.setSno(snum);
				perCh.setParentIdentifier(parentIdenfier);
				perCh.setParentChild(childObject);
				parentChilList.add(perCh);
			}
			if(null != newObject) {
//			list.add(newObject);
//				parentMap.put(snum, newObject);
				BulkUploadParent per = new BulkUploadParent();
				per.setSno(snum);
				per.setParentIdentifier(parentIdenfier);
				per.setParent(newObject);
				parentList.add(per);
				
			}
			if(null != childChildObject) {
//				childTempList.add(childChildObject);
//				childChildList.put(childParentTempObject, childTempList);
//				childChildMap.put(snum, childChildObject);
				BulkUploadChildChild chCh = new BulkUploadChildChild();
				chCh.setSno(snum);
				chCh.setParentIdentifier(parentIdenfier);
				chCh.setChildChild(childChildObject);
				childChilList.add(chCh);
				}

			}
		}
			 for(BulkUploadParent bulkPar:parentList) {
					List<BulkUploadParentChild> tempPrChldLst = new ArrayList<BulkUploadParentChild>();
					for(BulkUploadParentChild  bulkPrCl:parentChilList) {
						List<BulkUploadChildChild> tempChChldLst = new ArrayList<BulkUploadChildChild>();
						for(BulkUploadChildChild  bulkchCh:childChilList) {
							if(bulkchCh.getParentIdentifier().equalsIgnoreCase(bulkPrCl.getSno())) {
								tempChChldLst.add(bulkchCh);
							}
						}
						bulkPrCl.setChildChildList(tempChChldLst);
						if(bulkPrCl.getParentIdentifier().equalsIgnoreCase(bulkPar.getSno())) {
							tempPrChldLst.add(bulkPrCl);
						}
						
					}
					bulkPar.setParentChildList(tempPrChldLst);
				}
			 
			 if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("VehicleOnRoadPrice")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 VehicleOnRoadPrice onRoadPrice =  (VehicleOnRoadPrice)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(onRoadPrice.getOrganization_id());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(onRoadPrice.getModel())) {
							onRoadPrice.setVehicle_id(vehi.getVehicleId());
						}
						for(VehicleVarient vehVer:vehi.getVarients()) {
							if(vehVer.getName().equalsIgnoreCase(onRoadPrice.getVariant())) {
								onRoadPrice.setVarient_id(vehVer.getId());
							}
						}
					}
					 createVehicleOnRoadPrice(onRoadPrice);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("Accessory")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 Accessory accesor =  (Accessory)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(accesor.getOriganistionId());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(accesor.getModel())) {
							accesor.setVehicleId(vehi.getVehicleId());
						}
//						for(VehicleVarient vehVer:vehi.getVarients()) {
//							if(vehVer.getName().equalsIgnoreCase(onRoadPrice.getVariant())) {
//								onRoadPrice.setVarientId(vehVer.getId());
//							}
//						}
					}
					 createVehicleAccessory(accesor);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("AccessoryMapping")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 AccessoryMapping accesor =  (AccessoryMapping)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(accesor.getOrganisationId());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(accesor.getModel())) {
							accesor.setVehicleId(vehi.getVehicleId());
						}
//						for(VehicleVarient vehVer:vehi.getVarients()) {
//							if(vehVer.getName().equalsIgnoreCase(onRoadPrice.getVariant())) {
//								onRoadPrice.setVarientId(vehVer.getId());
//							}
//						}
					}
					 createAccessorMapping(accesor);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("OfferDetails")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 OfferDetails offer =  (OfferDetails)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(offer.getOrganizationId());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(offer.getModel())) {
							offer.setVehicleId(vehi.getVehicleId());
						}
						for(VehicleVarient vehVer:vehi.getVarients()) {
							if(vehVer.getName().equalsIgnoreCase(offer.getVariant())) {
								offer.setVariantId(vehVer.getId());
							}
						}
					}
					 offer = createVehicleOffers(offer);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("OfferCorporateDetails")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 OfferCorporateDetails offer1 =  (OfferCorporateDetails)bulkPar.getParent();
					List<VehicleDetails> xyz = getVehicleAndVariants(offer1.getOrganizationId());
					for(VehicleDetails details:xyz) {
						if(details.getModel().equalsIgnoreCase(offer1.getModel())) {
							offer1.setVariantId(details.getVehicleId());
						}
						for(VehicleVarient veh : details.getVarients()) {
							if(veh.getName().equalsIgnoreCase(offer1.getVariant())) {
								offer1.setVariantId(veh.getId());
							}
						}
					}
					}
			 
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("ExtendedWaranty")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 ExtendedWaranty offer =  (ExtendedWaranty)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(offer.getOrganization_id());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(offer.getModel())) {
							offer.setVehicle_id(vehi.getVehicleId());
						}
						for(VehicleVarient vehVer:vehi.getVarients()) {
							if(vehVer.getName().equalsIgnoreCase(offer.getVariant())) {
								offer.setVarient_id(vehVer.getId());
							}
						}
					}
					 createExtWarranty(offer);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("InsuranceAddOn")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 InsuranceAddOn insAddon =  (InsuranceAddOn)bulkPar.getParent();
					 List<VehicleDetails>  vehList = getVehicleAndVariants(insAddon.getOrganization_id());
					for(VehicleDetails vehi:vehList) {
						if(vehi.getModel().equalsIgnoreCase(insAddon.getModel())) {
							insAddon.setVehicle_id(vehi.getVehicleId());
						}
						for(VehicleVarient vehVer:vehi.getVarients()) {
							if(vehVer.getName().equalsIgnoreCase(insAddon.getVariant())) {
								insAddon.setVarient_id(vehVer.getId());
							}
						}
					}
					 createInsuraAddon(insAddon);
					 
						
						
					}
			 }else if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("InsuranceDetails")) {
				 for(BulkUploadParent bulkPar:parentList) {
					 InsuranceDetails insuran =  (InsuranceDetails)bulkPar.getParent();
					
					 createInsuraDetails(insuran);
					 
						
						
					}
			 }else {
				 for(BulkUploadParent bulkPar:parentList) {
				 VehicleDetails model =  (VehicleDetails)bulkPar.getParent();
				 String vehicleId = validateVehicleBooiking(model);
					for(BulkUploadParentChild  bulkPrCl:bulkPar.getParentChildList()) {
						VehicleVarient vehVar = (VehicleVarient)bulkPrCl.getParentChild();
						vehVar.setVehicleId(vehicleId);
						List<VehicleImage> vehColorList = new ArrayList<VehicleImage>();
						for(BulkUploadChildChild  bulkchCh:bulkPrCl.getChildChildList()) {
							VehicleImage vehImg = (VehicleImage)bulkchCh.getChildChild();
							vehImg.setVehicleId(vehicleId);
							
							vehColorList.add(vehImg);
						}
						String vehVarId = createVehicleVariant(vehVar, vehColorList);
						
					}
					
				}
			 }
//			 rowList = saveDFForm(parentList,bulkUploadReq);
		
      
        if(!CollectionUtils.isEmpty(rowList)) {
        	response = true;
//        	Row header = sheet.getRow(0);
//        	int headerCellIndex = 0;
//        	Iterator<Cell> cellHeadIterator = header_row.cellIterator();
//    		while (cellHeadIterator.hasNext()) {
//    		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
//    		String cellData = cellHeadIterator.next().toString();
//    		if (headerCellIndex == 0)
//    			sheetError.createRow(0).createCell(headerCellIndex).setCellValue(cellData);
//    		else
//    			sheetError.getRow(0).createCell(headerCellIndex).setCellValue(cellData);
//    		headerCellIndex++;
//    		}
        	Iterator<Row> rowIteratorNew = sheet.iterator();
        	int rowIdx = 0;
        	boolean headerFlag = false;
        	while(rowIteratorNew.hasNext())
			{
				Row rowIt = rowIteratorNew.next();
				for (Map.Entry<String,String> row : rowList.entrySet()) {
//			 for(String row : rowList) {
        	//XSSFSheet hssheet = (XSSFSheet) workbook.getSheetAt(0);
        	int rowIndex = Integer.valueOf(row.getKey());
             if(!headerFlag) {
        	if(rowIt.getCell(0).toString().equalsIgnoreCase("sno")) {
        		// sheetError.createRow(rowIdx++);
        		headerFlag= true;
        		int currentCellIndex = 0;
        		Iterator<Cell> cellIterator = rowIt.cellIterator();
        		while (cellIterator.hasNext()) {
        		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
        			
        			String cellData = cellIterator.next().toString();
//					Cell nextCell = cellIterator.next();
//					if (null != nextCell) {
//						cellData = nextCell.toString();
//					}
        		if (currentCellIndex == 0)
        			sheetError.createRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
        		else
        			sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
        		currentCellIndex++;
        		}
//        		sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(row.getValue());
        		rowIdx++;
        		}
             }else {
        	if(rowIt.getRowNum() == rowIndex) {
        		// sheetError.createRow(rowIdx++);
        		int currentCellIndex = 0;
        		Iterator<Cell> cellIterator = rowIt.cellIterator();
        		while (cellIterator.hasNext()) {
        		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
				String cellData = "";
				
				Cell nextCell = cellIterator.next();
                if(null != nextCell && nextCell.getCellType()==Cell.CELL_TYPE_STRING) {
                	 cellData = nextCell.toString();
				}
                if(null != nextCell && nextCell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
                	Double tempCellVal = nextCell.getNumericCellValue();
                	Integer cellDataInt = tempCellVal.intValue();
                	cellData = cellDataInt.toString();
				}
//				if (null != nextCell) {
//					cellData = nextCell.toString();
//				}
        			
        			
        		if (currentCellIndex == 0)
        			sheetError.createRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
        		else
        			sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
        		currentCellIndex++;
        		}
        		sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(row.getValue());
        		rowIdx++;
        		}
             }
        	//removeRow(workbook.getSheetAt(0), rowIt.getRowNum());
        	}
			}
        }
		/*
		 * String xls = ".xls"; String fileName = name+xls;
		 * 
		 * 
		 * 
		 * FileOutputStream fos = write(workbookError, fileName); fos.write(bytes);
		 * fos.flush(); fos.close();
		 */
		     
//     Row rw = sheetError.createRow(0);
//     rw.createCell(0).setCellValue("Hello World");
		  
        
        
     File currDir = new File(".");
     String path = currDir.getAbsolutePath();
     String fileLocation = path.substring(0, path.length() - 1) +bulkUploadReq.getFileuploadName()+".xlsx";

     FileOutputStream outputStream = new FileOutputStream(fileLocation);
     workbookError.write(outputStream);
     
  //  ((FileOutputStream) workbook).close();
				
//     File currDirjj = new File(fileLocation);
//     
//       // workbookError.write(out);
//         fi = (MultipartFile) currDirjj;
				// file = new InputStreamResource( new ByteArrayInputStream(out.toByteArray()));
//         ClassLoader classLoader = getClass().getClassLoader();
//         File fildde = new File(classLoader.getResource(fileLocation).getFile());
//         InputStream inputStream = new FileInputStream(fildde);		 
//          bytes = IOUtils.toByteArray(inputStream);
		} catch(Exception ex){
			ex.printStackTrace();
		}
        return   response;
	}
	
	
	
		@SuppressWarnings("deprecation")
		private  ErrorDetails uploadFileData(String inputFilePath,BulkUploadReq bulkUploadReq) throws DynamicFormsServiceException, IOException{
		
			Workbook workbook = null;
			Workbook workbookError = new XSSFWorkbook();
			MultipartFile fi = null;
			Sheet sheet = null;
			List<Object> list = new ArrayList<Object>();
			List<Object> childList = new ArrayList<Object>();
			InputStreamResource file = null;
			 byte[] bytes = new byte[1024];
			 ByteArrayOutputStream out = new ByteArrayOutputStream();
			 Map<String, String> rowList = new HashMap<>();
			 boolean response = false;
			 String excelFilePath= "";
			 String fileNameError="";
//			 BulkUploadParent finalParent = new BulkUploadParent();
			try
			{
				 AutomateFileExtract fileExtrct =  autFileExtractRepo.findByPageIdentifier(bulkUploadReq.getPageIdentifier());
				 bulkUploadReq.setFileuploadName(fileExtrct.getName()); 
				 workbook = getWorkBook(new File(inputFilePath));
				sheet = workbook.getSheetAt(0);
				XSSFRow header_row = (XSSFRow) sheet.getRow(0);
				 int rowCount = sheet.getLastRowNum();
				Sheet sheetError = workbookError.createSheet(bulkUploadReq.getFileuploadName());
				
				/*Build the header portion of the Output File*/
				/*
				 * String headerDetails= "EmployeeId,EmployeeName,Address,Country"; String
				 * headerNames[] = headerDetails.split(",");
				 */
	 
				/*Read and process each Row*/
				
				//Collection<?> employeeList = new ArrayList<>();
				boolean firstTime = true;
				 Object childParentTempObject = null;
				Iterator<Row> rowIterator = sheet.iterator();
				 Map<Object, List<Object>> childChildList = new HashMap<>();
				 Map<String, Object> parentMap = new HashMap<>();
				 Map<String, Object> parentChildMap = new HashMap<>();
				 Map<String, Object> childChildMap = new HashMap<>();
				 List<Object> childTempList = new ArrayList<Object>();
				 List<BulkUploadParent> parentList = new ArrayList<BulkUploadParent>();
				 List<BulkUploadParentChild> parentChilList = new ArrayList<BulkUploadParentChild>();
				 List<BulkUploadChildChild> childChilList = new ArrayList<BulkUploadChildChild>();
				while(rowIterator.hasNext())
				{
					Row row = rowIterator.next();
					
					if(row.getRowNum() !=0) {
					//Read and process each column in row
					//Class<T> excelTemplateVO = new Class<T>();
					 Object newObject = null;
					 Object childObject = null;
					 Object childChildObject = null;
//					  if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("enquiry")) {
//						  Enquiry enq = new Enquiry();
//					int count=0;
//					while(count < header_row.getLastCellNum()){
//						String methodName = "set"+header_row.getCell(count);
//						String inputCellValue = getCellValueBasedOnCellType(row,count++);
//						newObject = setValueIntoObject(enq, obj.getClass(), methodName, "java.lang.String", inputCellValue);
//					}
//	              
//					list.add(newObject);
//				}else   if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("preEnquiry")) {
					
					Class c = Class.forName("com.automate.df.model."+bulkUploadReq.getFileuploadName());
					Object preenq = c.newInstance();
					Object enq = null;
					 Object childChildObj = null;
//					PreEnquiry preenq = new PreEnquiry();
					if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
						Class child1Class = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
						 enq = child1Class.newInstance();
						
//							PreEnquiry preenq = new PreEnquiry();
							if(StringUtils.isNotBlank(fileExtrct.getChildChildPojo())) {
								Class childChildClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
								childChildObj = childChildClass.newInstance();
							}
//					Enquiry enq = new Enquiry();
					}
				int count=0;
				int rowcunt = 1;
				int maxRow = sheet.getLastRowNum();
//				String parentName = null;
//				String parentExcName = null;
//				
//				while(count < row.getLastCellNum()){
//					String methodName = row.getCell(count).toString();
//					String inputCellValue = getCellValueBasedOnCellType(row,count++);
//					if(methodName.contains("parent")) {
//					parentExcName = inputCellValue;
//					}
//				}
//
//				for(int i =1; i<= maxRow;i++) {
//					if(parentExcName.contains("parent"+i)) {
//						
//						parentName = "parent"+rowcunt;
//						if(i==rowcunt+1) {
//							rowcunt++;
//							Map<String, String> rowTempList = saveDFForm(list,childList,bulkUploadReq);
//						rowList.putAll(rowTempList);
//						}
//					}
//					}
				
//				if(parentName.equalsIgnoreCase(parentExcName)) {
				String snum = getCellValueBasedOnCellType(row,0);
				String parentIdenfier = getCellValueBasedOnCellType(row,1);
				String childParentIdentifier = null;
				while(count < header_row.getLastCellNum()){
//					if(header_row.getCell(count).toString().equalsIgnoreCase("ParentIdentifier")){
//						String identifierValue = getCellValueBasedOnCellType(row,count);
//						parentIdenfier = identifierValue;
//					}
//					String snum = null;
					
//					if(null != header_row.getCell(count)) {
//					 snum = header_row.getCell(count).toString();
//					}
					if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("parentchild")) {
						Object objChld = null;
//						PreEnquiry preenq = new PreEnquiry();
						if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
							Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
							objChld = childClass.newInstance();
//						Enquiry enq = new Enquiry();
						}
						
//						Enquiry objChld = new Enquiry();
								String child = header_row.getCell(count).toString();
//						if(child.equalsIgnoreCase("sno")) {
//							String methodName = "set"+header_row.getCell(count);
//							String inputCellValue = getCellValueBasedOnCellType(row,count++);
//							childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
//						}else {
								String[] parts = child.split("-");
						
						String header = parts[1]; // 034556
						String methodName = "set"+header;
						String inputCellValue = getCellValueBasedOnCellType(row,count++);
						if(StringUtils.isNotBlank(inputCellValue) && !(inputCellValue.equalsIgnoreCase("n"))) {
						childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
						childParentTempObject = childObject;											
//						}
//						if(header_row.getCell(count).toString().equalsIgnoreCase("ParentIdentifier")){
//							parentIdenfier = inputCellValue;
//						}
						}
						
						}else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("childchild")) {
							Object objChild = null;
//							PreEnquiry preenq = new PreEnquiry();
							if(StringUtils.isNotBlank(fileExtrct.getChildChildPojo())) {
								Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
								objChild = childClass.newInstance();
//							Enquiry enq = new Enquiry();
							}
							
//							Enquiry objChld = new Enquiry();
									String childChld = header_row.getCell(count).toString();
//							if(child.equalsIgnoreCase("sno")) {
//								String methodName = "set"+header_row.getCell(count);
//								String inputCellValue = getCellValueBasedOnCellType(row,count++);
//								childObject = setValueIntoObject(enq, objChld.getClass(), methodName, "java.lang.String", inputCellValue);
//							}else {
									String[] partsChild = childChld.split("-");
							
							String headerChild = partsChild[1]; // 034556
							String methodNameChild = "set"+headerChild;
							String inputCellValueChild = getCellValueBasedOnCellType(row,count++);
							if(StringUtils.isNotBlank(inputCellValueChild) && !(inputCellValueChild.equalsIgnoreCase("n"))) {
								childChildObject = setValueIntoObject(childChildObj, objChild.getClass(), methodNameChild, "java.lang.String", inputCellValueChild);
//								childTempList.add(childChildObject);
								
								
//							}
							}
							
							} else if(null != header_row.getCell(count) && header_row.getCell(count).toString().contains("primary")) { 
								Object obj = null;
								if(StringUtils.isNotBlank(fileExtrct.getPojo())) {
									Class mainClass = Class.forName("com.automate.df.model."+fileExtrct.getPojo());
									obj = mainClass.newInstance();
//								Enquiry enq = new Enquiry();
								}
								
								
								String parr = header_row.getCell(count).toString();
								String[] partsprr = parr.split("-");
								
								String headerPrr = partsprr[1]; // 034556
								String methodNamePrr = "set"+headerPrr;
//								String methodName = "set"+header_row.getCell(count);
					String inputCellValue = getCellValueBasedOnCellType(row,count++);
					if(StringUtils.isNotBlank(inputCellValue) && !(inputCellValue.equalsIgnoreCase("n"))) {
					newObject = setValueIntoObject(preenq, obj.getClass(), methodNamePrr, "java.lang.String", inputCellValue);
					}
					}else {
						count++;
					}
//					if(header_row.getCell(count).toString().contains("child")) {
//						Enquiry objChld = new Enquiry();
//								String child = header_row.getCell(count).toString();
//						String[] parts = child.split("-");
//						
//						String header = parts[1]; // 034556
//						String methodChildName = "set"+header;
//						if(row.toString().contains("child")) {
//						String inputChildCellValue = getCellValueBasedOnCellType(row,count++);
//						childObject = setValueIntoObject(enq, objChld.getClass(), methodChildName, "java.lang.String", inputChildCellValue);
//						}
//						}
					rowcunt++;
						}
//						}
				
				
				if(null != childObject) {
//				childList.add(childObject);
//					parentChildMap.put(snum, childObject);
					BulkUploadParentChild perCh = new BulkUploadParentChild();
					perCh.setSno(snum);
					perCh.setParentIdentifier(parentIdenfier);
					perCh.setParentChild(childObject);
					parentChilList.add(perCh);
				}
				if(null != newObject) {
//				list.add(newObject);
//					parentMap.put(snum, newObject);
					BulkUploadParent per = new BulkUploadParent();
					per.setSno(snum);
					per.setParentIdentifier(parentIdenfier);
					per.setParent(newObject);
					parentList.add(per);
					
				}
				if(null != childChildObject) {
//					childTempList.add(childChildObject);
//					childChildList.put(childParentTempObject, childTempList);
//					childChildMap.put(snum, childChildObject);
					BulkUploadChildChild chCh = new BulkUploadChildChild();
					chCh.setSno(snum);
					chCh.setParentIdentifier(parentIdenfier);
					chCh.setChildChild(childChildObject);
					childChilList.add(chCh);
					}
				
				
				
				
//				Iterator<Entry<String, Object>> itr = parentMap.entrySet().iterator();
//		         
//			        while(itr.hasNext())
//			        {
//			             Entry<String, Object> entry = itr.next();
//			             BulkUploadParent finalParent = new BulkUploadParent();
//			             finalParent.setSno(entry.getKey());
//			             finalParent.setParent(entry.getValue());
//			             Iterator<Entry<String, Object>> itrChil = parentMap.entrySet().iterator();
//				         
//					        while(itr.hasNext())
//					        {
//					             Entry<String, Object> entryChil = itrChil.next();
//					        }
//			             
//			        }
//				 Iterator<Entry<Object, List<Object>>> itr = childChildList.entrySet().iterator();
//		         
//			        while(itr.hasNext())
//			        {
//			             Entry<Object, List<Object>> entry = itr.next();
//			             if(childParentTempObject == entry.getKey()) {
//			            	 childTempList.add(childChildObject);
//			             }else {
//			            	 childTempList = new ArrayList<Object>();
//			            	 childTempList.add(childChildObject);
//			             }
//			        }
//				childChildList.put(childParentTempObject, childTempList);
				
//			}
//                   int nextRowNum = row.getRowNum();
					  
//					  if((row.getRowNum() != 0)  && ((row.getRowNum() == sheet.getLastRowNum()) || !(sheet.getRow(nextRowNum+1).getCell(1)).toString().equalsIgnoreCase("n"))){
////						 if(firstTime) {
////							 list.remove(0);
////							 if(!CollectionUtils.isEmpty(childList)) {
////							 childList.remove(0);
////							 }
////							firstTime =false;
////						 }
//						 
//							  
//							  Map<String, String> rowListNew = saveDFForm(list,childList,childChildList,bulkUploadReq);
//							  rowList.putAll(rowListNew);
//							  list = new ArrayList<Object>();
//							 childList = new ArrayList<Object>();
//							 childTempList = new ArrayList<Object>();
//							 childChildList = new HashMap<>();
//							  
//
//						}
					 
					  
//					  list.remove(0);
//					  childList.remove(0);
//					  rowList = saveDFForm(list,childList,bulkUploadReq);
				}
			}
				 for(BulkUploadParent bulkPar:parentList) {
						List<BulkUploadParentChild> tempPrChldLst = new ArrayList<BulkUploadParentChild>();
						for(BulkUploadParentChild  bulkPrCl:parentChilList) {
							List<BulkUploadChildChild> tempChChldLst = new ArrayList<BulkUploadChildChild>();
							for(BulkUploadChildChild  bulkchCh:childChilList) {
								if(bulkchCh.getParentIdentifier().equalsIgnoreCase(bulkPrCl.getSno())) {
									tempChChldLst.add(bulkchCh);
								}
							}
							bulkPrCl.setChildChildList(tempChChldLst);
							if(bulkPrCl.getParentIdentifier().equalsIgnoreCase(bulkPar.getSno())) {
								tempPrChldLst.add(bulkPrCl);
							}
							
						}
						bulkPar.setParentChildList(tempPrChldLst);
					}
				 rowList = saveDFForm(parentList,bulkUploadReq);
			
	      
	        if(!CollectionUtils.isEmpty(rowList)) {
	        	response = true;
//	        	Row header = sheet.getRow(0);
//	        	int headerCellIndex = 0;
//	        	Iterator<Cell> cellHeadIterator = header_row.cellIterator();
//        		while (cellHeadIterator.hasNext()) {
//        		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
//        		String cellData = cellHeadIterator.next().toString();
//        		if (headerCellIndex == 0)
//        			sheetError.createRow(0).createCell(headerCellIndex).setCellValue(cellData);
//        		else
//        			sheetError.getRow(0).createCell(headerCellIndex).setCellValue(cellData);
//        		headerCellIndex++;
//        		}
	        	Iterator<Row> rowIteratorNew = sheet.iterator();
	        	int rowIdx = 0;
	        	boolean headerFlag = false;
	        	while(rowIteratorNew.hasNext())
				{
					Row rowIt = rowIteratorNew.next();
					for (Map.Entry<String,String> row : rowList.entrySet()) {
//				 for(String row : rowList) {
	        	//XSSFSheet hssheet = (XSSFSheet) workbook.getSheetAt(0);
	        	int rowIndex = Integer.valueOf(row.getKey());
                 if(!headerFlag) {
	        	if(rowIt.getCell(0).toString().equalsIgnoreCase("sno")) {
	        		// sheetError.createRow(rowIdx++);
	        		headerFlag= true;
	        		int currentCellIndex = 0;
	        		Iterator<Cell> cellIterator = rowIt.cellIterator();
	        		while (cellIterator.hasNext()) {
	        		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
	        			
	        			String cellData = cellIterator.next().toString();
//						Cell nextCell = cellIterator.next();
//						if (null != nextCell) {
//							cellData = nextCell.toString();
//						}
	        		if (currentCellIndex == 0)
	        			sheetError.createRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
	        		else
	        			sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
	        		currentCellIndex++;
	        		}
//	        		sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(row.getValue());
	        		rowIdx++;
	        		}
                 }else {
	        	if(rowIt.getRowNum() == rowIndex) {
	        		// sheetError.createRow(rowIdx++);
	        		int currentCellIndex = 0;
	        		Iterator<Cell> cellIterator = rowIt.cellIterator();
	        		while (cellIterator.hasNext()) {
	        		// Step #3: Creating new Row, Cell and Input value in the newly created sheet.
					String cellData = "";
					
					Cell nextCell = cellIterator.next();
                    if(null != nextCell && nextCell.getCellType()==Cell.CELL_TYPE_STRING) {
                    	 cellData = nextCell.toString();
					}
                    if(null != nextCell && nextCell.getCellType()==Cell.CELL_TYPE_NUMERIC) {
                    	Double tempCellVal = nextCell.getNumericCellValue();
                    	Integer cellDataInt = tempCellVal.intValue();
                    	cellData = cellDataInt.toString();
					}
//					if (null != nextCell) {
//						cellData = nextCell.toString();
//					}
	        			
	        			
	        		if (currentCellIndex == 0)
	        			sheetError.createRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
	        		else
	        			sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(cellData);
	        		currentCellIndex++;
	        		}
	        		sheetError.getRow(rowIdx).createCell(currentCellIndex).setCellValue(row.getValue());
	        		rowIdx++;
	        		}
                 }
	        	//removeRow(workbook.getSheetAt(0), rowIt.getRowNum());
	        	}
				}
	        }
			/*
			 * String xls = ".xls"; String fileName = name+xls;
			 * 
			 * 
			 * 
			 * FileOutputStream fos = write(workbookError, fileName); fos.write(bytes);
			 * fos.flush(); fos.close();
			 */
			     
//	     Row rw = sheetError.createRow(0);
//	     rw.createCell(0).setCellValue("Hello World");
			  
	        
	        
	   //  File currDir = new File(".");
	   //  String path = currDir.getAbsolutePath();
	    // String fileLocation = path.substring(0, path.length() - 1) +bulkUploadReq.getFileuploadName()+".xlsx";
	      fileNameError = bulkUploadReq.getFileuploadName()+ System.currentTimeMillis() +".xlsx";
	     excelFilePath = tmpPath  + File.separator + fileNameError;
	     fileNameError = fileControllerUrl + "/downloadFile/" + fileNameError;
			log.debug("ErrorfileGeneated at  " + excelFilePath);
			try (FileOutputStream outputStream = new FileOutputStream(excelFilePath)) {
				workbookError.write(outputStream);
			}
			

			} catch(Exception ex){
				ex.printStackTrace();
			}
			ErrorDetails errorDetails;
			 if(!CollectionUtils.isEmpty(rowList)) {
			 errorDetails = new ErrorDetails(new Date(), "Some records failed in bulk upload, Please verify the Error excel file",fileNameError, "400");
			}else {
				 errorDetails = new ErrorDetails(null, null,null,null);
			}
			return   errorDetails;
		}
	 
		

		
		public static Workbook getWorkBook(File fileName)
		{
			Workbook workbook = null;
			try {
				String myFileName=fileName.getName();
				String extension = myFileName.substring(myFileName.lastIndexOf("."));
				if(extension.equalsIgnoreCase(".xls")){
					workbook = new HSSFWorkbook(new FileInputStream(fileName));
				}
				else if(extension.equalsIgnoreCase(".xlsx")){
					workbook = new XSSFWorkbook(new FileInputStream(fileName));
				}
			}
			catch(Exception ex)
			{
				ex.printStackTrace();
			}
			return workbook;
		}
	 
		public static String getCellValueBasedOnCellType(Row rowData,int columnPosition)
		{
			String cellValue=null;
			Cell cell = rowData.getCell(columnPosition);
			if(cell!=null){
				if(cell.getCellType()==Cell.CELL_TYPE_STRING)
				{
					String inputCellValue=cell.getStringCellValue();
					if(inputCellValue.endsWith(".0")){
						inputCellValue=inputCellValue.substring(0, inputCellValue.length()-2);
					}
					cellValue=inputCellValue;
				}
				else if (cell.getCellType()==Cell.CELL_TYPE_NUMERIC)
				{
					if(DateUtil.isCellDateFormatted(cell)) {
						 
						 DateFormat df = new SimpleDateFormat("yyyy-mm-dd");
	    
						    Date today = cell.getDateCellValue() ;      
						   
						    cellValue = df.format(today);
					        		
					    }else {
					Integer doubleVal = (int) cell.getNumericCellValue();
					cellValue= Integer.toString(doubleVal);
					    }
				}
				
			}
			return cellValue;
		}
	 
		private static <T> Object setValueIntoObject(Object obj, Class<T> clazz, String methodNameForField, String dataType, String inputCellValue)
				throws SecurityException, NoSuchMethodException, ClassNotFoundException, NumberFormatException, IllegalArgumentException, IllegalAccessException, InvocationTargetException{
	 
//			if(!methodNameForField.equalsIgnoreCase("setsno")) {
			Method meth = clazz.getMethod(methodNameForField, Class.forName(dataType));
			T t = clazz.cast(obj);
			meth.invoke(t, inputCellValue);
//			}
			return obj;
			/*
			 * if ("java.lang.Double".equalsIgnoreCase(dataType)) { meth.invoke(t,
			 * Double.parseDouble(inputCellValue)); } else if
			 * (!"java.lang.Integer".equalsIgnoreCase(dataType)) { meth.invoke(t,
			 * inputCellValue); } else { meth.invoke(t, Integer.parseInt(inputCellValue)); }
			 */
			
			 
		}
		
		
		@SuppressWarnings("deprecation")
		private Map<String, String> saveDFForm(List<BulkUploadParent> parentBulkList,BulkUploadReq bulkUploadReq) throws DynamicFormsServiceException
		{

			
//			AutomateFileImport autBulkUpload = new AutomateFileImport();
//			autBulkUpload.setFileName(bulkUploadReq.getFileuploadName());
//			autBulkUpload = autFileImportRepo.save(autBulkUpload);
				AutomateFileExtract fileExtrct =  autFileExtractRepo.findByName(bulkUploadReq.getFileuploadName());
				List<AutomateFileColumnProcessor> columnProcList = autFileColmnProcRepo.findByFileExtractId(fileExtrct.getId());
				Map<String, Object> paramValMapping = new LinkedHashMap<>();
				Map<String, Object> paramChildValMapping = new LinkedHashMap<>();
				List<String> rowNumList = new ArrayList<String>();
				Map<String, String> rowList = new HashMap<>();
				int parentDbId = 0;
//				int parentDbDelId = 0;
				List<Integer> parentDelIdList =  new ArrayList<Integer>();
//				int parentChildDbDelId = 0;
				List<Integer> parentChildDbDelIdList =  new ArrayList<Integer>();
				List<Object>  childChildTempList = new ArrayList<Object>();
				
				
				
				if(bulkUploadReq.isFlushAndFill()) {
					try {
						
						StringBuilder sbselForDel = new StringBuilder();
						sbselForDel.append("SELECT ID FROM ");
						sbselForDel.append(fileExtrct.getTableName())
						.append(" where bulk_upload_id = ")
						 .append('\'')
						 .append(bulkUploadReq.getBulkUploadIdentifier())
						 .append('\'');
						parentDelIdList = dbUtil.getList(sbselForDel.toString());
						for(Integer parIdTemp:parentDelIdList) {
							if(StringUtils.isNotBlank(fileExtrct.getChildTableName())) {
						StringBuilder sbselChldForDel = new StringBuilder();
						sbselChldForDel.append("SELECT ID FROM ");
						sbselChldForDel.append(fileExtrct.getChildTableName())
						.append(" where parentId = ")
						 .append('\'')
						 .append(parIdTemp)
						 .append('\'');
						parentChildDbDelIdList = dbUtil.getList(sbselChldForDel.toString());
						for(Integer parChldIdTemp:parentChildDbDelIdList) {
							if(StringUtils.isNotBlank(fileExtrct.getChildChildTableName())) {
						StringBuilder sbDelChldChild = new StringBuilder();
						sbDelChldChild.append("DELETE FROM ")
						.append(fileExtrct.getChildChildTableName())
						.append(" where parentId = ")
						 .append('\'')
						 .append(parChldIdTemp)
						 .append('\'');
						dbUtil.delete(sbDelChldChild.toString());
							}
						StringBuilder sbDelChild = new StringBuilder();
						sbDelChild.append("DELETE FROM ")
						.append(fileExtrct.getChildTableName())
						.append(" where parentId = ")
						 .append('\'')
						 .append(parIdTemp)
						 .append('\'');
						dbUtil.delete(sbDelChild.toString());
						}
							}
						StringBuilder sbDelParent = new StringBuilder();
						sbDelParent.append("DELETE FROM ")
						.append(fileExtrct.getTableName())
						.append(" where bulk_upload_id = ")
						 .append('\'')
						 .append(bulkUploadReq.getBulkUploadIdentifier())
						 .append('\'');
						dbUtil.delete(sbDelParent.toString());
						}

					}catch (Exception e) {
						String res="There is an error while deleting the record, please check id ";
						log.error("delete bulk uplod flush and fill ", e);
						throw new DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
								HttpStatus.INTERNAL_SERVER_ERROR);
					}

				}
				

				 
				for (BulkUploadParent parentbulkUp : parentBulkList) {
					boolean errorRecord = false;
					String parentErrorMsg="";
					Object parentField = parentbulkUp.getParent();
//					paramValMapping.put("bulk_upload_id", autBulkUpload.getId());
					String rowNumParent = null;
					rowNumParent = parentbulkUp.getSno();
					try {
						
						StringBuilder sbselForDel = new StringBuilder();
						sbselForDel.append("SELECT ID FROM ");
						sbselForDel.append(fileExtrct.getTableName())
						.append(" where bulk_upload_id = ")
						 .append('\'')
						 .append(bulkUploadReq.getBulkUploadIdentifier())
						 .append('\'');
//						parentDelIdList = dbUtil.getList(sbselForDel.toString());
					Class c = Class.forName("com.automate.df.model."+fileExtrct.getPojo());
					Object enqParent = c.newInstance();
					
					  
					  enqParent =parentField ;
					  
					  Field[] fields = enqParent.getClass().getDeclaredFields();
				for(AutomateFileColumnProcessor colum :columnProcList) {
					for(Field field : fields) {
						String fieldName = field.getName();
			            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			            
			                Method method = enqParent.getClass().getMethod(methodName);
			                Object value = method.invoke(enqParent);
							/*
							 * System.out.println("field name ---------"+field.getName());
							 * System.out.println("field value ---------"+value);
							 */
			                
			               
			                if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("employee")) {
			                	paramValMapping.remove("bulk_upload_id");
			                	
			                	if(field.getName().equalsIgnoreCase("reportingTo")) {
			                		List<Integer> reporting =  new ArrayList<Integer>();
			                		StringBuilder sbemp = new StringBuilder();
			                		sbemp.append("SELECT emp_id FROM  ");
			                		sbemp.append(" dms_employee" )
									.append(" where emp_name = ")
									 .append('\'')
									 .append(value)
									 .append('\'');
			                		reporting = dbUtil.getList(sbemp.toString());
			                		if(!CollectionUtils.isEmpty(reporting)) {
			                			value = reporting.get(0);
			                		}
			                	}
			                	if(field.getName().equalsIgnoreCase("approverId")) {
			                		List<Integer> approver =  new ArrayList<Integer>();
			                		StringBuilder sbappr = new StringBuilder();
			                		sbappr.append("SELECT emp_id FROM  ");
			                		sbappr.append(" dms_employee" )
									.append(" where emp_name = ")
									 .append('\'')
									 .append(value)
									 .append('\'');
			                		approver = dbUtil.getList(sbappr.toString());
			                		if(!CollectionUtils.isEmpty(approver)) {
			                			value = approver.get(0);
			                		}
			                	}
			                	if(field.getName().equalsIgnoreCase("hrmsRole")) {
			                		List<Integer> hrmsroleLst =  new ArrayList<Integer>();
			                		StringBuilder sbrole = new StringBuilder();
			                		sbrole.append("SELECT role_id FROM  ");
			                		sbrole.append(" dms_role" )
									.append(" where role_name = ")
									 .append('\'')
									 .append(value)
									 .append('\'');
			                		hrmsroleLst = dbUtil.getList(sbrole.toString());
			                		if(!CollectionUtils.isEmpty(hrmsroleLst)) {
			                			value = hrmsroleLst.get(0);
			                		}
			                	}
			                	if(field.getName().equalsIgnoreCase("org")) {
			                		List<Integer> orgidLst =  new ArrayList<Integer>();
			                		StringBuilder sborg = new StringBuilder();
			                		sborg.append("SELECT org_id FROM  ");
			                		sborg.append(" dms_organization" )
									.append(" where name = ")
									 .append('\'')
									 .append(value)
									 .append('\'');
			                		orgidLst = dbUtil.getList(sborg.toString());
			                		if(!CollectionUtils.isEmpty(orgidLst)) {
			                			value = orgidLst.get(0);
			                		}
			                	}
			                	if(field.getName().equalsIgnoreCase("branch")) {
			                		List<Integer> brnchidList =  new ArrayList<Integer>();
			                		StringBuilder sbemp = new StringBuilder();
			                		sbemp.append("SELECT branch_id FROM  ");
			                		sbemp.append(" dms_branch" )
									.append(" where name = ")
									 .append('\'')
									 .append(value)
									 .append('\'');
			                		brnchidList = dbUtil.getList(sbemp.toString());
			                		if(!CollectionUtils.isEmpty(brnchidList)) {
			                			value = brnchidList.get(0);
			                		}
			                	}
			                }
			                
					if(colum.getColumnName().equalsIgnoreCase(field.getName())) {
						boolean parentError = false;
						

						if(StringUtils.isNotBlank(colum.getValidationColumn()) && colum.getValidationColumn().equalsIgnoreCase("true")) {
							StringBuilder sb1 = new StringBuilder();
							sb1.append("SELECT "+colum.getTableColumn()+" FROM");
							sb1.append(fileExtrct.getTableName())
							.append(" where "+colum.getTableColumn()+" = ")
							 .append('\'')
							 .append(value)
							 .append('\'');
							List<String>  names= dbUtil.getListNames(sb1.toString());
							for(String namee:names) {
								if(value.toString().equalsIgnoreCase(namee)) {
									parentErrorMsg = parentErrorMsg+"\n"+ "The name you entered for "+field.getName() + " already exists.";
									parentError = true;
								}
							}
						}
						
						if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("int")) {
							if(!NumberUtils.isCreatable(value.toString())) {
//								rowNumParent = parentbulkUp.getSno();
//								rowNumList.add(rowNumParent);
								parentErrorMsg = parentErrorMsg+"\n"+ "The type you entered for "+field.getName() + " should be Integer value.";
								parentError = true;
//								rowList.put(rowNumParent, parentErrorMsg);
							}
						}
							if(StringUtils.isNotBlank(colum.getRequired()) &&  colum.getRequired().equalsIgnoreCase("y")) {
								if(null == value || StringUtils.isBlank(value.toString())) {
//									rowNumParent = parentbulkUp.getSno();
//								rowNumList.add(rowNumParent);
								parentErrorMsg = parentErrorMsg+"\n"+ "The field "+field.getName() + " should not be empty.";
								parentError = true;
//								rowList.put(rowNumParent, parentErrorMsg);
								}
							}
							if(colum.getMaxLength() != null) {
								if(value.toString().length() > colum.getMaxLength()) {
//									rowNumParent = parentbulkUp.getSno();
//								rowNumList.add(rowNumParent);
								parentErrorMsg = parentErrorMsg+"\n"+ "The entered value for field "+field.getName() + " exceeds max lenght of "+colum.getMaxLength();
								parentError = true;
//								rowList.put(rowNumParent, parentErrorMsg);
								}
							}
							if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("date")) {
								if(!isValidDate(value.toString())) {
//									rowNumParent = parentbulkUp.getSno();
//									rowNumList.add(rowNumParent);
									parentErrorMsg = parentErrorMsg+"\n"+ "The type you entered for "+field.getName() + " should be valid date in DD/MM/YYYY format.";
									parentError = true;
																	}
							}
							if(parentError) {
								
								rowList.put(rowNumParent, parentErrorMsg);
								errorRecord = true;
								if(!CollectionUtils.isEmpty(parentbulkUp.getParentChildList())) {
									for (BulkUploadParentChild childBulkTemp : parentbulkUp.getParentChildList()) {
										rowList.put(childBulkTemp.getSno(), "");
										if(!CollectionUtils.isEmpty(childBulkTemp.getChildChildList())) {
											for (BulkUploadChildChild childChildBulkTemp : childBulkTemp.getChildChildList()) {
												rowList.put(childChildBulkTemp.getSno(), "");
											}
										}
									}
								}
							}
						 paramValMapping.put(colum.getTableColumn() ,value);
						
						
			} 
					 if(colum.getColumnName().equalsIgnoreCase("createdat") || colum.getColumnName().equalsIgnoreCase("updateat")) {
						 paramValMapping.put(colum.getTableColumn() ,getCurrentTmeStamp());
					}
					 if(colum.getColumnName().equalsIgnoreCase("createdBy") || colum.getColumnName().equalsIgnoreCase("updatedBy")) {
						 paramValMapping.put(colum.getTableColumn() ,bulkUploadReq.getEmpId());
					}
//					paramValMapping.put("" ,value);
					/*
				 * else if(colum.getColumnName().equalsIgnoreCase("lastName")) {
				 * paramValMapping.put(colum.getTableColumn() ,enq.getLastName()); }else
				 * if(colum.getColumnName().equalsIgnoreCase("address")) {
				 * paramValMapping.put(colum.getTableColumn() ,enq.getAddress()); }else
				 * if(colum.getColumnName().equalsIgnoreCase("type")) {
				 * paramValMapping.put(colum.getTableColumn() ,enq.getType()); }else
				 * if(colum.getColumnName().equalsIgnoreCase("colour")) {
				 * paramValMapping.put(colum.getTableColumn() ,enq.getColour()); }else
				 * if(colum.getColumnName().equalsIgnoreCase("model")) {
				 * paramValMapping.put(colum.getTableColumn() ,enq.getModel()); }
				 */
					}
				}
//			}
					if(!errorRecord) {
				log.debug("paramValMapping::"+paramValMapping);
				StringBuilder sb = new StringBuilder();
				sb.append("INSERT INTO ");
				sb.append(fileExtrct.getTableName());
				sb.append(" (");
				//sb.append("id");
				//sb.append(DynamicFormConstants.COMMA_SEP);
				Object[] arr = new Object[paramValMapping.size()];
				//arr[0] = dfSave.getPageId();
				AtomicInteger cnt = new AtomicInteger(0);
				paramValMapping.forEach((k, v) -> {

					sb.append(k);
					sb.append(DynamicFormConstants.COMMA_SEP);
					arr[cnt.getAndIncrement()] = v;
				});
				StringBuilder query = new StringBuilder(sb.substring(0, sb.length() - 1));
				query.append(") values(");
				
				String placeholder ="";
				for(Object obj : arr ) {
					placeholder = placeholder+"?,";
				}
				placeholder = placeholder.substring(0,placeholder.length()-1);
				query.append(placeholder);
				query.append(")");
				log.debug("query ::" + query.toString());
				dbUtil.save(query.toString(), arr);
				StringBuilder sb1 = new StringBuilder();
				sb1.append("SELECT LAST_INSERT_ID() FROM ");
				sb1.append(fileExtrct.getTableName()+" limit 1");
				parentDbId = dbUtil.get(sb1.toString());

					}
			} catch (Exception e) {
				//parentErrorMsg = parentErrorMsg+"\n"+	 e.getCause().getMessage(); 
					e.printStackTrace();
			

					rowList.put(rowNumParent, parentErrorMsg);
					errorRecord = true;
					if(!CollectionUtils.isEmpty(parentbulkUp.getParentChildList())) {
						for (BulkUploadParentChild childBulkTemp : parentbulkUp.getParentChildList()) {
							rowList.put(childBulkTemp.getSno(), "");
							if(!CollectionUtils.isEmpty(childBulkTemp.getChildChildList())) {
								for (BulkUploadChildChild childChildBulkTemp : childBulkTemp.getChildChildList()) {
									rowList.put(childChildBulkTemp.getSno(), "");
								}
							}
						}
					}
				
			}
					if(null != parentbulkUp.getParentChildList() && !errorRecord) {
					for (BulkUploadParentChild childBulk : parentbulkUp.getParentChildList()) {
						Object childField = childBulk.getParentChild();
						//paramValMapping.put("bulk_upload_id", autBulkUpload.getId());
						boolean errorRecordChild = false;
						int parentChildDbId = 0;
						Map<String, Object> paramChildChldValMapping = new LinkedHashMap<>();
						
							String childErrorMsg="";

						String rowNumChild = childBulk.getSno();
						
						try {
					Object enq = null;
							if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
								Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
								 enq = childClass.newInstance();
							}
							 
						  enq = childField ;
						  
						  Field[] fields = enq.getClass().getDeclaredFields();
					for(AutomateFileColumnProcessor colum :columnProcList) {
						for(Field field : fields) {
							String fieldName = field.getName();
				            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				            
				                Method method = enq.getClass().getMethod(methodName);
				                Object value = method.invoke(enq);
				               
				                String child = colum.getColumnName();
				                if(child.contains("child")) {
								String[] parts = child.split("-");
								
								String header = parts[1];
						if(header.equalsIgnoreCase(field.getName())) {
							boolean parentChildError = false;
							if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("int")) {
								if(!NumberUtils.isCreatable(value.toString())) {
//									rowNumChild = childBulk.getSno();
//									rowNumList.add(rowNumChild);
									childErrorMsg = childErrorMsg+"\n"+ "The type you entered for "+field.getName() + " should be Integer value.";
									parentChildError = true;
//									rowList.put(rowNumChild, childErrorMsg);
								}
							}
								if(StringUtils.isNotBlank(colum.getRequired()) &&  colum.getRequired().equalsIgnoreCase("y")) {
									if(null == value || StringUtils.isBlank(value.toString())) {
//									rowNumChild = childBulk.getSno();
//									rowNumList.add(rowNumChild);
									childErrorMsg = childErrorMsg+"\n"+ "The field "+field.getName() + " should not be empty.";
									parentChildError = true;
//									rowList.put(rowNumChild, childErrorMsg);
									}
								}
								if(colum.getMaxLength() != null) {
									if(value.toString().length() > colum.getMaxLength()) {
//										rowNumChild = childBulk.getSno();
//									rowNumList.add(rowNumChild);
									childErrorMsg = childErrorMsg+"\n"+ "The entered value for field "+field.getName() + " exceeds max lenght of "+colum.getMaxLength();
									parentChildError = true;
//									rowList.put(rowNumChild, childErrorMsg);
									}
								}
								if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("date")) {
									if(!isValidDate(value.toString())) {
//										rowNumChild = childBulk.getSno();
//										rowNumList.add(rowNumChild);
										childErrorMsg = childErrorMsg+"\n"+ "The type you entered for "+field.getName() + " should be valid date in DD/MM/YYYY format.";
										parentChildError = true;
//										rowList.put(rowNumChild, childErrorMsg);
									}
								}
								if(parentChildError) {
									
									rowList.put(rowNumChild, childErrorMsg);
									errorRecordChild = true;
											if(!CollectionUtils.isEmpty(childBulk.getChildChildList())) {
												for (BulkUploadChildChild childChildBulkTemp1 : childBulk.getChildChildList()) {
													rowList.put(childChildBulkTemp1.getSno(), "");
												}
											}
										
									
								}
							
							paramChildValMapping.put(colum.getTableColumn() ,value);
							paramChildValMapping.put("parentId" ,parentDbId);
							  if(header.equalsIgnoreCase("createdat") || header.equalsIgnoreCase("updateat")) {
									 paramValMapping.put(colum.getTableColumn() ,getCurrentTmeStamp());
								}
							  if(colum.getColumnName().equalsIgnoreCase("createdBy") || colum.getColumnName().equalsIgnoreCase("updatedBy")) {
									 paramValMapping.put(colum.getTableColumn() ,bulkUploadReq.getEmpId());
								}
							

				} 
				                }
//				                if(colum.getColumnName().equalsIgnoreCase("createdat") || colum.getColumnName().equalsIgnoreCase("updateat")) {
//									 paramValMapping.put(colum.getTableColumn() ,getCurrentTmeStamp());
//								}
						/*
					 * else if(colum.getColumnName().equalsIgnoreCase("lastName")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getLastName()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("address")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getAddress()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("type")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getType()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("colour")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getColour()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("model")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getModel()); }
					 */
						}
					}
					if(parentDbId > 0) {
					if(!errorRecordChild) {
									log.debug("paramValMapping::"+paramChildValMapping);
					StringBuilder sb = new StringBuilder();
					sb.append("INSERT INTO ");
					sb.append(fileExtrct.getChildTableName());
					sb.append(" (");
					//sb.append("id");
					//sb.append(DynamicFormConstants.COMMA_SEP);
					Object[] arr = new Object[paramChildValMapping.size()];
					//arr[0] = dfSave.getPageId();
					AtomicInteger cnt = new AtomicInteger(0);
					paramChildValMapping.forEach((k, v) -> {

						sb.append(k);
						sb.append(DynamicFormConstants.COMMA_SEP);
						arr[cnt.getAndIncrement()] = v;
					});
					StringBuilder query = new StringBuilder(sb.substring(0, sb.length() - 1));
					query.append(") values(");
					
					String placeholder ="";
					for(Object obj : arr ) {
						placeholder = placeholder+"?,";
					}
					placeholder = placeholder.substring(0,placeholder.length()-1);
					query.append(placeholder);
					query.append(")");
					log.debug("query ::" + query.toString());
					dbUtil.save(query.toString(), arr);
					StringBuilder sbChldPrnt = new StringBuilder();
					sbChldPrnt.append("SELECT LAST_INSERT_ID() FROM");
					sbChldPrnt.append(fileExtrct.getChildTableName());
					parentChildDbId = dbUtil.get(sbChldPrnt.toString());
					}
					}
//					 Iterator<Entry<Object, List<Object>>> itr = childChildList.entrySet().iterator();
//			         
//				        while(itr.hasNext())
//				        {
//				             Entry<Object, List<Object>> entry = itr.next();
//					
//				             Object childObjec = null;
//								if(StringUtils.isNotBlank(fileExtrct.getChildPojo())) {
//									Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildPojo());
//									 childObjec = childClass.newInstance();
////								Enquiry enq = new Enquiry();
//								}
//								 
//							  childObjec = entry.getKey() ;
					if(null != childBulk.getChildChildList() && !errorRecordChild) {
					for (BulkUploadChildChild childChildBulk : childBulk.getChildChildList()) {
						Object objChlTmp = childChildBulk.getChildChild();
						boolean errorRecordChildChild = false;
//				             if(childField == childObjec) {
				            	 
				             
						//paramValMapping.put("bulk_upload_id", autBulkUpload.getId());
						
							String childChildErrMsg="";

						String rowNumChldCld = childChildBulk.getSno();
						try {
							
//						 if(bulkUploadReq.getFileuploadName().equalsIgnoreCase("enquiry")) {
//							  Enquiry enq = new Enquiry();
//							 
//							  enq =(Enquiry) dField ;
//							  
//							  Field[] fields = enq.getClass().getDeclaredFields();
//						for(AutomateFileColumnProcessor colum :columnProcList) {
//							for(Field field : fields) {
//								String fieldName = field.getName();
//					            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
//					            
//					                Method method = enq.getClass().getMethod(methodName);
//					                Object value = method.invoke(enq);
//									/*
//									 * System.out.println("field name ---------"+field.getName());
//									 * System.out.println("field value ---------"+value);
//									 */
//					                if(fieldName.equalsIgnoreCase("sno")) {
//					                	rowNum = value.toString();
//					                }
////					                if(colum.getProcessorType().equalsIgnoreCase("int")) {
////					                	 if (!value.toString().matches("\\d+")) {
////					                		 throw new Exception();
////					                	 }
////					                }else if(colum.getProcessorType().equalsIgnoreCase("int")) {
////					                	 if (!value.toString().matches("\\d{4}[-]\\d{2}[-]\\d{2}")) {
////					                		 throw new Exception();
////					                	 }
////					                }
//					                	
//							if(colum.getColumnName().equalsIgnoreCase(field.getName())) {
//								 paramValMapping.put(colum.getTableColumn() ,value);
//					} 						}
//						}
//				}else  
//							List<Object> childChlTempList = entry.getValue();
//							childChildTempList = entry.getValue();
//							for(Object objChlTmp:childChlTempList) {
					Object enqChild = null;
							if(StringUtils.isNotBlank(fileExtrct.getChildChildPojo())) {
								Class childClass = Class.forName("com.automate.df.model."+fileExtrct.getChildChildPojo());
								 enqChild = childClass.newInstance();
//							Enquiry enq = new Enquiry();
							}
							 
						  enqChild = objChlTmp ;
						  
						  Field[] fieldsChild = enqChild.getClass().getDeclaredFields();
					for(AutomateFileColumnProcessor colum :columnProcList) {
						for(Field field : fieldsChild) {
							String fieldName = field.getName();
				            String methodName = "get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
				            
				                Method method = enqChild.getClass().getMethod(methodName);
				                Object value = method.invoke(enqChild);
//				                if(fieldName.equalsIgnoreCase("sno")) {
//				                	if(null != value && !(value.toString().equalsIgnoreCase("n"))) {
//				                	rowNumChldCld = value.toString();
//				                	}
//				                	rowNumList.add(rowNumChldCld);
////				                	else {
////				                		if(StringUtils.isNotBlank(rowNumChild)) {
////				                		Integer i = Integer.valueOf(rowNumChild);
////				                		i++;
////				                		rowNumChild = i.toString();
////				                		}
////				                	}
//				                }
								/*
								 * System.out.println("field name ---------"+field.getName());
								 * System.out.println("field value ---------"+value);
								 */
//				                if(fieldName.equalsIgnoreCase("sno")) {
//				                	rowNum = value.toString();
//				                }

//				                if(colum.getProcessorType().equalsIgnoreCase("int")) {
//				                	 Integer.parseInt(value.toString());
////				                		 throw new Exception("datatype mismatch");
////				                	 }
//				                }else if(colum.getProcessorType().equalsIgnoreCase("double")) {
//				                	 Double.parseDouble(value.toString());
//				                }else if(colum.getProcessorType().equalsIgnoreCase("date")) {
//				                	    	
//				                	if (isValidDate(value.toString())) {
//						                		 throw new Exception("datatype mismatch");
//						                	 }
//				                }
				               
				                String child = colum.getColumnName();
				                if(child.contains("childchild")) {
								String[] parts = child.split("-");
								
								String header = parts[1];
						if(header.equalsIgnoreCase(field.getName())) {
							if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("int")) {
								if(!NumberUtils.isCreatable(value.toString())) {
//									rowNumChldCld = childChildBulk.getSno();
//									rowNumList.add(rowNumChldCld);
									childChildErrMsg = childChildErrMsg+"\n"+ "The type you entered for "+field.getName() + " should be Integer value.";
									rowList.put(rowNumChldCld, childChildErrMsg);
									errorRecordChildChild = true;
								}
							}
								if(StringUtils.isNotBlank(colum.getRequired()) &&  colum.getRequired().equalsIgnoreCase("y")) {
									if(null == value || StringUtils.isBlank(value.toString())) {
//										rowNumChldCld = childChildBulk.getSno();
//									rowNumList.add(rowNumChldCld);
									childChildErrMsg = childChildErrMsg+"\n"+ "The field "+field.getName() + " should not be empty.";
									rowList.put(rowNumChldCld, childChildErrMsg);
									errorRecordChildChild = true;
									}
								}
								if(colum.getMaxLength() != null) {
									if(value.toString().length() > colum.getMaxLength()) {
//										rowNumChldCld = childChildBulk.getSno();
//									rowNumList.add(rowNumChldCld);
									childChildErrMsg = childChildErrMsg+"\n"+ "The entered value for field "+field.getName() + " exceeds max lenght of "+colum.getMaxLength();
									rowList.put(rowNumChldCld, childChildErrMsg);
									errorRecordChildChild = true;
									}
								}
								if(StringUtils.isNotBlank(colum.getProcessorType()) && colum.getProcessorType().equalsIgnoreCase("date")) {
									if(!isValidDate(value.toString())) {
//										rowNumChldCld = childChildBulk.getSno();
//										rowNumList.add(rowNumChldCld);
										childChildErrMsg = childChildErrMsg+"\n"+ "The type you entered for "+field.getName() + " should be valid date in DD/MM/YYYY format.";
										rowList.put(rowNumChldCld, childChildErrMsg);
										errorRecordChildChild = true;
									}
								}
						
							paramChildChldValMapping.put(colum.getTableColumn() ,value);
							paramChildChldValMapping.put("parentId" ,parentChildDbId);
							

				} 
				                }
				                if(colum.getColumnName().equalsIgnoreCase("createdat") || colum.getColumnName().equalsIgnoreCase("updateat")) {
									 paramValMapping.put(colum.getTableColumn() ,getCurrentTmeStamp());
								}
				                if(colum.getColumnName().equalsIgnoreCase("createdBy") || colum.getColumnName().equalsIgnoreCase("updatedBy")) {
									 paramValMapping.put(colum.getTableColumn() ,bulkUploadReq.getEmpId());
								}
						/*
					 * else if(colum.getColumnName().equalsIgnoreCase("lastName")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getLastName()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("address")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getAddress()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("type")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getType()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("colour")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getColour()); }else
					 * if(colum.getColumnName().equalsIgnoreCase("model")) {
					 * paramValMapping.put(colum.getTableColumn() ,enq.getModel()); }
					 */
						}
					}
					if(parentChildDbId > 0) {
					if(!errorRecordChildChild) {
					log.debug("paramValMapping::"+paramChildChldValMapping);
					StringBuilder sbChild = new StringBuilder();
					sbChild.append("INSERT INTO ");
					sbChild.append(fileExtrct.getChildChildTableName());
					sbChild.append(" (");
					//sb.append("id");
					//sb.append(DynamicFormConstants.COMMA_SEP);
					Object[] arrChild = new Object[paramChildChldValMapping.size()];
					//arr[0] = dfSave.getPageId();
					AtomicInteger cntChild = new AtomicInteger(0);
					paramChildChldValMapping.forEach((k, v) -> {

						sbChild.append(k);
						sbChild.append(DynamicFormConstants.COMMA_SEP);
						arrChild[cntChild.getAndIncrement()] = v;
					});
					StringBuilder queryChild = new StringBuilder(sbChild.substring(0, sbChild.length() - 1));
					queryChild.append(") values(");
					
					String placeholderChild ="";
					for(Object obj : arrChild ) {
						placeholderChild = placeholderChild+"?,";
					}
					placeholderChild = placeholderChild.substring(0,placeholderChild.length()-1);
					queryChild.append(placeholderChild);
					queryChild.append(")");
					log.debug("query ::" + queryChild.toString());
					dbUtil.save(queryChild.toString(), arrChild);
					}
					}
							
				
				} catch (Exception e) {
					log.error("save Bulk ecxel ", e);
//					rowNumList.add(rowNumParent);
//					List<String> listWithoutDuplicates = rowNumList.stream()
//						     .distinct()
//						     .collect(Collectors.toList());
					
					childChildErrMsg = childChildErrMsg +"\n"+	 e.getCause().getMessage();
					rowList.put(rowNumChldCld, childChildErrMsg);
					
//					rowList.put(rowNumChild, e.getCause().getMessage());
//					for(Object child:childList) {
//						Enquiry enqu = new Enquiry();
//						 
//						  enqu =(Enquiry) child ;
//						if(enqu.getSno().equalsIgnoreCase(rowNumChldCld)) {
//							rowList.put(enqu.getSno(), childErrMsg);
//						}else {
//							rowList.put(enqu.getSno(), "");
//						}
//					}
//					return rowList;
//					Enquiry childLast = new Enquiry();
//					childLast = (Enquiry) childField;
//					if(childLast.getSno().equals(childList.size())) {
//						
//						 Iterator<Map.Entry<String, String>> itr = rowList.entrySet().iterator();
//				         
//					        while(itr.hasNext())
//					        {
//					             Map.Entry<String, String> entry = itr.next();
//					             for(Object child:childList) {
//										Enquiry enqu = new Enquiry();
//										 
//										  enqu =(Enquiry) child ;
//					             if(!entry.getKey().equalsIgnoreCase(enqu.getSno())) {
//					            	 rowList.put(enqu.getSno(), "");
//					             }
//					             }
//					             
//					        }
//					
//					}
//					rowList.put(st, e.getCause().getMessage());
					
					/*
					 * throw new
					 * DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
				}
			
					}
						}	
				
				} catch (Exception e) {
					log.error("save Bulk ecxel ", e);
//					rowNumList.add(rowNumParent);
//					List<String> listWithoutDuplicates = rowNumList.stream()
//						     .distinct()
//						     .collect(Collectors.toList());
					
							childErrorMsg = childErrorMsg+"\n"+	 e.getCause().getMessage();
							rowList.put(rowNumChild, childErrorMsg);
							if(!CollectionUtils.isEmpty(childBulk.getChildChildList())) {
								for (BulkUploadChildChild childChildBulkTemp1 : childBulk.getChildChildList()) {
									rowList.put(childChildBulkTemp1.getSno(), "");
								}
							}
//					rowList.put(rowNumChild, e.getCause().getMessage());
//					for(Object child:childList) {
//						Enquiry enqu = new Enquiry();
//						 
//						  enqu =(Enquiry) child ;
//						if(enqu.getSno().equalsIgnoreCase(rowNumChild)) {
//							rowList.put(enqu.getSno(), childErrorMsg);
//						}else {
//							rowList.put(enqu.getSno(), "");
//						}
//					}
//					return rowList;
//					Enquiry childLast = new Enquiry();
//					childLast = (Enquiry) childField;
//					if(childLast.getSno().equals(childList.size())) {
//						
//						 Iterator<Map.Entry<String, String>> itr = rowList.entrySet().iterator();
//				         
//					        while(itr.hasNext())
//					        {
//					             Map.Entry<String, String> entry = itr.next();
//					             for(Object child:childList) {
//										Enquiry enqu = new Enquiry();
//										 
//										  enqu =(Enquiry) child ;
//					             if(!entry.getKey().equalsIgnoreCase(enqu.getSno())) {
//					            	 rowList.put(enqu.getSno(), "");
//					             }
//					             }
//					             
//					        }
//					
//					}
//					rowList.put(st, e.getCause().getMessage());
					
					/*
					 * throw new
					 * DynamicFormsServiceException(env.getProperty("INTERNAL_SERVER_ERROR"),
					 * HttpStatus.INTERNAL_SERVER_ERROR);
					 */
				}
					}
				}
					
			}
				 
			
//				if(!CollectionUtils.isEmpty(childList)) {
//					if(!CollectionUtils.isEmpty(childChildTempList)) {
//						if(childList.size() > childChildTempList.size()) {
//							for(int i=1;i<=childList.size();i++) {
//								Integer inte = (Integer)i;
//								 Iterator<Map.Entry<String, String>> itrcc = rowList.entrySet().iterator();
//						         
//							        while(itrcc.hasNext())
//							        {
//							             Map.Entry<String, String> entrycc = itrcc.next();	
//							             if(!entrycc.getKey().equals(inte.toString())) {
//							            	 rowList.put(inte.toString(), "");
//							             }
//							        }
//								}
//							}else if(childChildTempList.size() > childList.size()) {
//								for(int i=1;i<=childChildTempList.size();i++) {
//									Integer inte = (Integer)i;
//									Map<String, String> rowListTemp = rowList;
//									 Iterator<Map.Entry<String, String>> itrchc = rowListTemp.entrySet().iterator();
//							         
//								        while(itrchc.hasNext())
//								        {
//								             Map.Entry<String, String> entrychc = itrchc.next();	
//								             if(!entrychc.getKey().equals(inte.toString())) {
//								            	 rowList.put(inte.toString(), "");
//								             }
//								        }
//									}
//								}
//						}else {
//							for(int i=1;i<=childList.size();i++) {
//								Integer inte = (Integer)i;
//								Map<String, String> rowListTemp1 = new HashMap<>();
//								rowListTemp1.putAll(rowList);
//								 Iterator<Map.Entry<String, String>> itrCh = rowListTemp1.entrySet().iterator();
//						         
//							        while(itrCh.hasNext())
//							        {
//							             Map.Entry<String, String> entryCh = itrCh.next();	
//							             if(!entryCh.getKey().equals(inte.toString())) {
//							            	 rowList.put(inte.toString(), "");
//							             }
//							        }
//								}
//						}
//					}
				
				
			return rowList;
			
		}

		
		 /**
		 * Remove a row by its index
		 * @param sheet a Excel sheet
		 * @param rowIndex a 0 based index of removing row
		 */
		public static void removeRow(Sheet sheet, int rowIndex) {
		    int lastRowNum=sheet.getLastRowNum();
		    if(rowIndex>=0&&rowIndex<lastRowNum){
		        sheet.shiftRows(rowIndex+1,lastRowNum, -1);
		    }
		    if(rowIndex==lastRowNum){
		        XSSFRow removingRow=(XSSFRow) sheet.getRow(rowIndex);
		        if(removingRow!=null){
		            sheet.removeRow(removingRow);
		        }
		    }
		}


		

		
		  private FileOutputStream write(final Workbook workbook, final String filename) throws IOException {
		      FileOutputStream fos = new FileOutputStream(filename);
		      workbook.write(fos);
		      fos.close();
		      return fos;
		  }  
		  
//		  public static boolean isValidDate(String inDate) {
//		        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
//		        dateFormat.setLenient(false);
//		        try {
//		            dateFormat.parse(inDate.trim());
//		        } catch (ParseException pe) {
//		            return false;
//		        }
//		        return true;
//		    }
		  
		  public static boolean isValidDate(String d)
		    {
		        String regex = "^(1[0-2]|0[1-9])/(3[01]"
		                       + "|[12][0-9]|0[1-9])/[0-9]{4}$";
		        Pattern pattern = Pattern.compile(regex);
		        Matcher matcher = pattern.matcher((CharSequence)d);
		        return matcher.matches();
		    }
		  
		  public String validateVehicleBooiking(VehicleDetails appointment) {
//		        VehicleAllotmentRequest request = new VehicleAllotmentRequest();
//		        DemoTestdriveVehicleAllotment allotment = new DemoTestdriveVehicleAllotment();
//
//		        allotment.setPlannedStartDatetime(appointment.getStartTime());
//		        allotment.setPlannedEndDatetime(appointment.getEndTime());
//		        allotment.setDemoVehicleId(appointment.getVehicleId().intValue());
//		        allotment.setEventType("TESTDRIVE");
//		        request.setAllotment(allotment);
//			  appointment.setModel("srinivas");
//			  appointment.setBooking_amount(998);
//			  appointment.setOrganizationId(1);
//			  appointment.setPriceRange("9-10");
//			  appointment.setVehicleId(0);
//			  appointment.setStatus("Active");
//			  appointment.setType(appointment.getType().Car);
//			  appointment.setCreatedDate("01-01-2020");
			  
			  
		        // TODO : do not hardcode properties as below. Instead, read from external properties.
		        String url = env.getProperty("dms.vehicle.details.create.vehicle");
//		        logger.info("Url :{}", url);
		       ResponseJson responseJson = new ResponseJson();
		        responseJson = restTemplateUtil.postForObject(url, null, null, appointment,
		        		ResponseJson.class, null);
//		        if (ErrorMessages.SUCCESS.code().equals(response.getStatusCode()) && Utils.isEmpty(response.getVehicleAllotments())) {
//		            logger.info("Demo vehicle Booking logging {}", Utils.ObjectToJson(response));
//		        } else if (Utils.isNotEmpty(response.getVehicleAllotments())) {
//		            throw new BaseException(ErrorMessages.SLOT_NOT_AVAILABLE);
//		        }
//		        VehicleDetails vv = (VehicleDetails)responseJson.getResult();
		        LinkedHashMap ll = (LinkedHashMap) responseJson.getResult();
		        String vehId = ll.get("vehicleId").toString();
		       return vehId;
		    }
		  
		  public String createVehicleVariant(VehicleVarient variant,List<VehicleImage> vehColorList) {
//		        VehicleAllotmentRequest request = new VehicleAllotmentRequest();
//		        DemoTestdriveVehicleAllotment allotment = new DemoTestdriveVehicleAllotment();
//
//		        allotment.setPlannedStartDatetime(appointment.getStartTime());
//		        allotment.setPlannedEndDatetime(appointment.getEndTime());
//		        allotment.setDemoVehicleId(appointment.getVehicleId().intValue());
//		        allotment.setEventType("TESTDRIVE");
//		        request.setAllotment(allotment);
//			  variant.setName("srinivas");
			  Set<VehicleImage> vehicleImages = new HashSet<VehicleImage>();
			for(VehicleImage vehColor:vehColorList) {
				if(vehColor.getDualColor().equalsIgnoreCase("0")) {
				vehColor.setIs_dual_color(false);
				}else {
					vehColor.setIs_dual_color(true);
				}
			  vehicleImages.add(vehColor);
			}
			  variant.setVehicleImages(vehicleImages);
		        
		        String url = env.getProperty("dms.vehicle.details.create.vehicleVariant");
//		        logger.info("Url :{}", url);
		       ResponseJson responseJson = new ResponseJson();
		        responseJson = restTemplateUtil.postForObject(url, null, null, variant,
		        		ResponseJson.class, null);
//		        if (ErrorMessages.SUCCESS.code().equals(response.getStatusCode()) && Utils.isEmpty(response.getVehicleAllotments())) {
//		            logger.info("Demo vehicle Booking logging {}", Utils.ObjectToJson(response));
//		        } else if (Utils.isNotEmpty(response.getVehicleAllotments())) {
//		            throw new BaseException(ErrorMessages.SLOT_NOT_AVAILABLE);
//		        }
		        LinkedHashMap ll = (LinkedHashMap) responseJson.getResult();
		        String vehVarId = ll.get("id").toString();
		        return vehVarId;
		    }

		  
		  public void createVehicleOnRoadPrice(VehicleOnRoadPrice vehOnRoadPrice) {

//			  Set<RoadTax> roadTaxSet = new HashSet<RoadTax>();
//			  roadTaxSet.add(roadTax);
//			  vehOnRoadPrice.setRoadtax(roadTaxSet);
//			  Set<ExtendedWaranty> exWarSet = new HashSet<ExtendedWaranty>();
//			  exWarSet.add(exWarr);
//			  vehOnRoadPrice.setExtended_waranty(exWarSet);
//			  Set<InsuranceAddOn> insAddOnSet = new HashSet<InsuranceAddOn>();
//			  Set<InsuranceVarientMapping> insVarMapSet = new HashSet<InsuranceVarientMapping>();
//			  vehOnRoadPrice.setInsuranceAddOn(insAddOnSet);
//			  vehOnRoadPrice.setInsurance_vareint_mapping(insVarMapSet);
			  
		        String url = env.getProperty("dms.vehicle.details.create.vehicleOnRoadPrice");
//		        logger.info("Url :{}", url);
		      
		         restTemplateUtil.postForObject(url, null, null, vehOnRoadPrice,
		        		 BaseResponse.class, null);
//		        if (ErrorMessages.SUCCESS.code().equals(response.getStatusCode()) && Utils.isEmpty(response.getVehicleAllotments())) {
//		            logger.info("Demo vehicle Booking logging {}", Utils.ObjectToJson(response));
//		        } else if (Utils.isNotEmpty(response.getVehicleAllotments())) {
//		            throw new BaseException(ErrorMessages.SLOT_NOT_AVAILABLE);
//		        }
		       
		    }
		  
		  public OfferDetails createVehicleOffers(OfferDetails offer) {

			
		        String url = env.getProperty("dms.vehicle.details.create.vehicleOffer");
		        Map<String, String> headersMap = new HashMap<String, String>();
                headersMap.put("orgId", offer.getOrganizationId());
		       
                restTemplateUtil.postForObject(url, headersMap,null, offer,
                		String.class, null);
//                HttpEntity<?> entity = null;
//                headers.add("orgId", offer.getOrganisationId());
//
//               
//                    entity = new HttpEntity<>(offer, headers);
//		        ResponseEntity<OfferDetails> responseEntity = 
//		        		  restTemplate.exchange(
//		        		    url,
//		        		    HttpMethod.POST,
//		        		    entity,
//		        		    new ParameterizedTypeReference<OfferDetails>() {}
//		        		  );
//		        
//		        OfferDetails offerDetails = responseEntity.getBody();
		       return offer;
		    }
		  
		  public OfferMapping createVehicleOfferMapping(OfferMapping offerMap) {

//			  HttpHeaders headers = new HttpHeaders();
//				headers.setContentType(MediaType.APPLICATION_JSON);
//                headers.add("orgId", offerMap.getOrganisationId());
//                headers.add("Accept","application/json");
                Map<String, String> headersMap = new HashMap<String, String>();
                headersMap.put("orgId", offerMap.getOrganisationId());
		        // TODO : do not hardcode properties as below. Instead, read from external properties.
		        String url = env.getProperty("dms.vehicle.details.create.vehicleOffer");
		        offerMap = restTemplateUtil.postForObject(url, headersMap, null, offerMap,
		        		OfferMapping.class, null);
//		        ResponseEntity<OfferMapping> responseEntity = 
//		        		  restTemplate.exchange(
//		        		    url,
//		        		    HttpMethod.POST,
//		        		    null,
//		        		    new ParameterizedTypeReference<OfferMapping>() {}
//		        		  );
//		        
//		        OfferMapping offerDetails = responseEntity.getBody();
		       return offerMap;
		    }
		  
		  public  List<VehicleDetails> getVehicleAndVariants(String orgId) {
		  
		        // TODO : do not hardcode properties as below. Instead, read from external properties.
		        String url = env.getProperty("dms.vehicle.details.create.vehicleDetails");

//		        ResponseEntity<List<VehicleDetails>> entity;
		        ResponseEntity<List<VehicleDetails>> responseEntity = 
		        		  restTemplate.exchange(
		        		    url,
		        		    HttpMethod.GET,
		        		    null,
		        		    new ParameterizedTypeReference<List<VehicleDetails>>() {}
		        		  );
//		        entity = restTemplate.getForEntity(url,VehicleDetails[].class);

		        List<VehicleDetails> vehiList = responseEntity.getBody();
		        
		       return vehiList;
		    }
		  
		  public void createExtWarranty(ExtendedWaranty offer) {

			  ObjectMapper objectMapper = new ObjectMapper();
		      try {
				List<HashMap<String, Object>> addOnPr = objectMapper.readValue(offer.getExtendedWarranty(),List.class);
			  
		        String url = env.getProperty("dms.vehicle.details.create.vehicleExtWaranty");

		     
		       restTemplateUtil.postForObject(url, null, null, addOnPr,
		    		   BaseResponse.class, null);
		      }catch(Exception e) {
		    	  e.printStackTrace();
		      }

		    }
		  
		  public void createInsuraDetails(InsuranceDetails insDet) {

			  InsuranceDetailsRequest insReq = new InsuranceDetailsRequest();
			  
			  insReq.setInsuranceDetails(insDet);
		        String url = env.getProperty("dms.vehicle.details.create.vehicleInsuranceDetails");

		     
		       restTemplateUtil.postForObject(url, null, null, insReq,
		    		   BaseResponse.class, null);

		    }
		
		  public void createInsuraAddon(InsuranceAddOn insAddon) {
		      ObjectMapper objectMapper = new ObjectMapper();
		      try {
		    	  List<HashMap<String, Object>> addOnPr = objectMapper.readValue(insAddon.getAddOnPrice(), List.class);
 
			  insAddon.setAdd_on_price(addOnPr);
			  InsuranceAddonRequest insReq = new InsuranceAddonRequest();
			  
			  insReq.setInsuranceAddOn(insAddon);
		        String url = env.getProperty("dms.vehicle.details.create.vehicleInsuranceAddOn");

		     
		       restTemplateUtil.postForObject(url, null, null, insReq,
		    		   BaseResponse.class, null);
		      }catch(Exception e) {
		    	  e.printStackTrace();
		      }

		    }
		  
		  public void createVehicleAccessory(Accessory accessory) {


			  
		        String url = env.getProperty("dms.vehicle.details.create.vehicleAccessory");

		      
		         restTemplateUtil.postForObject(url, null, null, accessory,
		        		 BaseResponse.class, null);

		       
		    }
		  
		  public void createAccessorMapping(AccessoryMapping accessoryMap) {


			  
		        String url = env.getProperty("dms.vehicle.details.create.vehicleAccessoryMapping");

		      
		         restTemplateUtil.postForObject(url, null, null, accessoryMap,
		        		 BaseResponse.class, null);

		       
		    }
		  
}