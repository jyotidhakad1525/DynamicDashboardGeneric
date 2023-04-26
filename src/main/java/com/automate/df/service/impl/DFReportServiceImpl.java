package com.automate.df.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStream;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import org.apache.commons.lang3.StringUtils;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.automate.df.constants.GsAppConstants;
import com.automate.df.dao.AutoSaveRepo;
import com.automate.df.dao.DmsDeliveryDao;
import com.automate.df.dao.DmsExchangeBuyerDao;
import com.automate.df.dao.DmsInvoiceDao;
import com.automate.df.dao.LeadStageRefDao;
import com.automate.df.dao.ReportQueriesDAO;
import com.automate.df.dao.dashboard.DmsLeadDao;
import com.automate.df.dao.dashboard.DmsLeadDropDao;
import com.automate.df.dao.dashboard.DmsWfTaskDao;
import com.automate.df.dao.oh.DmsBranchDao;
import com.automate.df.dao.salesgap.DmsEmployeeRepo;
import com.automate.df.entity.AutoSaveEntity;
import com.automate.df.entity.LeadStageRefEntity;
import com.automate.df.entity.ReportQueries;
import com.automate.df.entity.dashboard.DmsLead;
import com.automate.df.entity.dashboard.DmsLeadDrop;
import com.automate.df.entity.dashboard.DmsWFTask;
import com.automate.df.entity.oh.DmsBranch;
import com.automate.df.entity.sales.employee.DmsExchangeBuyer;
import com.automate.df.entity.sales.lead.DmsDelivery;
import com.automate.df.entity.sales.lead.DmsInvoice;
import com.automate.df.entity.salesgap.DmsEmployee;
import com.automate.df.exception.DynamicFormsServiceException;
import com.automate.df.model.AutoSave;
import com.automate.df.model.DMSResponse;
import com.automate.df.model.DmsAccessoriesDto;
import com.automate.df.model.DmsAddress;
import com.automate.df.model.DmsAttachmentDto;
import com.automate.df.model.DmsBookingDto;
import com.automate.df.model.DmsContactDto;
import com.automate.df.model.DmsEntity;
import com.automate.df.model.DmsExchangeBuyerDto;
import com.automate.df.model.DmsFinanceDetailsDto;
import com.automate.df.model.DmsLeadDto;
import com.automate.df.model.DmsLeadProductDto;
import com.automate.df.model.ETVPreEnquiry;
import com.automate.df.model.ETVRequest;
import com.automate.df.model.QueryParam;
import com.automate.df.model.QueryRequestV2;
import com.automate.df.model.WhereRequest;
import com.automate.df.model.sales.lead.DmsAccountDto;
import com.automate.df.model.sales.lead.DmsOnRoadPriceDto;
import com.automate.df.model.salesgap.TargetRoleRes;
import com.automate.df.service.DFReportService;
//import com.automate.df.service.SalesFeignService;
import com.automate.df.service.SalesGapService;
import com.automate.df.util.ExcelUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.itextpdf.html2pdf.HtmlConverter;
import com.opencsv.CSVWriter;

import lombok.extern.slf4j.Slf4j;
/**
 * 
 * @author sruja
 *
 */

@Service
@Slf4j
public class DFReportServiceImpl implements DFReportService {

	@Autowired
	Environment env;

	@Value("${tmp.path}")
	String tmpPath;

	@Value("${file.controller.url}")
	String fileControllerUrl;
	
	@Value("${lead.enquiry.url}")
	String leadEnqUrl;
	
	@Value("${lead.onroadprice.url}")
	String leadOnRoadPriceUrl;
	
	@Autowired
	DmsInvoiceDao dmsInvoiceDao;

	@Autowired
	private EntityManager entityManager;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	ReportQueriesDAO reportQueriesDAO;
	
	@Autowired
	SalesGapService salesGapService;

	
	@Autowired
	DmsDeliveryDao dmsDeliveryDao;
	
	@Autowired
	ExcelUtil excelUtil;


	@Autowired
	RestTemplate restTemplate;
	
	/**
	 * @param empId
	 * @return
	 */
	private TargetRoleRes getPrecendenceRole(int empId) {
		List<TargetRoleRes> empRoles = salesGapService.getEmpRoles(empId);
		Collections.sort(empRoles);
		return empRoles.get(0);
	}

	@Override
	public String generateDynamicQueryV2(QueryRequestV2 request) throws DynamicFormsServiceException {
		String res = null;
		try {
			
			Integer empId = request.getEmpId();
			//TargetRoleRes empRoleData = getPrecendenceRole(empId);
			TargetRoleRes empRoleData = salesGapService.getEmpRoleData(empId);
			String roleIdentifier = null;
			if(null!=empRoleData) {
				if(null!=empRoleData.getHrmsRole()) {
				roleIdentifier = empRoleData.getHrmsRole();
				}else {
					throw new DynamicFormsServiceException("There is NO HRMS Role found in DB for the given emp id", HttpStatus.BAD_REQUEST);
					
				}
			}else {
				throw new DynamicFormsServiceException("No Emp records with given empID", HttpStatus.BAD_REQUEST);
			}
			log.debug("HRMS Role Identifier for emp id "+empId+" is "+roleIdentifier);
			log.info("HRMS Role Identifier for emp id "+empId+" is "+roleIdentifier);
			
			//roleIdentifier = "1"; //added for testing
			Optional<ReportQueries> rqOpt = reportQueriesDAO.findQuery(request.getReportIdentifier(), roleIdentifier);
			
			if (rqOpt.isPresent()) {
				StringBuilder query = new StringBuilder();
				ReportQueries rq = rqOpt.get();
				String selectQuery = rq.getQuery();
				String tableName = rq.getTableName();
				query.append(selectQuery);
			
				boolean whereflagInPayload = StringUtils.containsIgnoreCase(selectQuery, "where");
				log.debug("whereflagInPayload "+whereflagInPayload);
				if(null!=request.getWhere() && !whereflagInPayload) {
				String whereClasue = whereQueryBuilder(request.getWhere());
				query.append(GsAppConstants.SPACE).append("WHERE").append(GsAppConstants.SPACE)
						.append(whereClasue);
	
				}
				if(null!=request.getWhere() && whereflagInPayload) {
					String whereClasue = whereQueryBuilder(request.getWhere());
					query.append(GsAppConstants.SPACE).append("AND").append(GsAppConstants.SPACE)
							.append(whereClasue);
				}
				
				if (null != request.getGroupBy() && !request.getGroupBy().isEmpty()) {
					String groupBy = request.getGroupBy().toString();
					if (null != groupBy && groupBy.length() > 0) {
						groupBy = groupBy.substring(1, groupBy.length() - 1);
					}
					query.append(GsAppConstants.SPACE).append(GsAppConstants.GROUP_BY).append(GsAppConstants.SPACE)
							.append(groupBy);
				}

				if (null != request.getOrderBy() && !request.getOrderBy().isEmpty()) {
					String orderBy = request.getOrderBy().toString();
					if (null != orderBy && orderBy.length() > 0) {
						orderBy = orderBy.substring(1, orderBy.length() - 1);
					}
					query.append(GsAppConstants.SPACE).append(GsAppConstants.ORDER_BY).append(GsAppConstants.SPACE)
							.append(orderBy);

					if (null != request.getOrderByType() && request.getOrderByType().length() > 0) {
						query.append(GsAppConstants.SPACE).append(request.getOrderByType());
					}
				}

				log.debug("query::" + query);
				log.debug("tableName "+tableName);
				List<Object[]> colnHeadersList = new ArrayList<>();
				if(null!= tableName && !tableName.isEmpty()) {
					colnHeadersList = entityManager.createNativeQuery("DESCRIBE " + tableName).getResultList();
				}
				else {
					
					String tmp = selectQuery;
					if(tmp.contains("select") || tmp.contains("from")) {
						tmp = tmp.substring(tmp.indexOf("select"),tmp.indexOf("from"));
						tmp = tmp.replaceAll("select","");
					}
					if(tmp.contains("SELECT") || tmp.contains("FROM")) {
						tmp = tmp.substring(tmp.indexOf("SELECT"),tmp.indexOf("FROM"));
						tmp = tmp.replaceAll("SELECT","");
					}
					System.out.println("tmp ::"+tmp);
					String[] tmpArr = tmp.split(",");
					for(String s: tmpArr) {
						String[] a = new String[1];
						a[0]=s;
						colnHeadersList.add(a);
					}
				}
				log.debug("colnHeadersList "+colnHeadersList);
				
				List<String> headers = new ArrayList<>();
				for (Object[] arr : colnHeadersList) {

					String colName = (String) arr[0];
					if(StringUtils.containsIgnoreCase(colName, " as ")) {
						colName = colName.replaceAll("\"", "");
						colName = colName.replaceAll("\'","");
						colName = colName.substring(StringUtils.indexOfIgnoreCase(colName, " AS")+3, colName.length());
						colName=colName.trim();
						headers.add(colName);
					}
				}

				log.debug("Coln Headers ::" + headers);
				final List<Map<String, Object>> jObjList = new ArrayList<>();
				
				int maxItems = rq.getMaxItems();
				StringBuilder limitQuery = new StringBuilder();
				limitQuery.append("Selct * From ( ");
				limitQuery.append(query);
				limitQuery.append(" ) LIMIT ");
				limitQuery.append(maxItems + 1);
				
				Query q = entityManager.createNativeQuery(query.toString());
				List<Object[]> queryResults = q.getResultList();
				//List<PreEnquiry> preList = new ArrayList<>();
				for (int i = 0; i < queryResults.size(); i++) {
					Object[] objArr = queryResults.get(i);
					Map<String, Object> map = new LinkedHashMap<>();
					for (int j = 0; j < objArr.length; j++) {
						String colName = headers.get(j);
						map.put(colName, objArr[j]);
					}
					jObjList.add(map);
				}
				log.debug("jObjList size " + jObjList.size());
				
				
				
				
				 
				Map<String,Object> finalMap = new LinkedHashMap<>();
				//String csvLink = null;
				//String excelLink = null;
				//String pdfLink = null;
				List<Map<String, Object>> filterdList = new ArrayList<>();
				if(!jObjList.isEmpty()) {
				
					
					
					CompletableFuture<String> csvFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generateCsvLink(jObjList,request.getReportIdentifier());
					    }
					});
					CompletableFuture<String> excelFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generateExcelLink(jObjList,request.getReportIdentifier());
					    }
					});
					CompletableFuture<String> pdfFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generatePDFLinkV2(jObjList,request.getReportIdentifier());
					    }
					});
					/*
					csvLink = generateCsvLink(jObjList,request.getReportIdentifier());
					excelLink = generateExcelLink(jObjList,request.getReportIdentifier());
					pdfLink = generatePDFLinkV2(jObjList,request.getReportIdentifier());
					log.debug("pdfLink "+pdfLink);
					pdfLink = fileControllerUrl+"/downloadFile/"+pdfLink;
					excelLink = fileControllerUrl+"/downloadFile/"+excelLink;
					csvLink = fileControllerUrl+"/downloadFile/"+csvLink;
					*/
					boolean flag = request.isPaginationRequired();
					int totalCnt = jObjList.size();
					log.debug("jObjList ::"+totalCnt);
					finalMap.put("totalCnt", totalCnt);
					if(flag) {
						int size = request.getSize();
						int pageNo = request.getPageNo();
						
						finalMap.put("pageNo", pageNo);
						finalMap.put("size", size);
						
						pageNo = pageNo+1;
						int fromIndex = size * (pageNo - 1);
						int toIndex = size * pageNo;

						if (toIndex > totalCnt) {
							toIndex = totalCnt;
						}
						if (fromIndex > toIndex) {
							fromIndex = toIndex;
						}
						filterdList = jObjList.subList(fromIndex, toIndex);
					}else {
						
						finalMap.put("pageNo", 0);
						finalMap.put("size", jObjList.size());
						if(jObjList.size() > maxItems) {
							filterdList = jObjList.subList(0, maxItems);
							finalMap.put("MaxItems", maxItems);
						}else {
							filterdList=jObjList;
						}
						
					}
					log.debug("Size of filterdList "+filterdList.size());
					
					finalMap.put("excelLink", excelFuture.get());
					finalMap.put("csvLink", csvFuture.get());
					finalMap.put("pdfLink", pdfFuture.get());
				}
				
				
				finalMap.put("data", filterdList);
				res = objectMapper.writeValueAsString(finalMap);
			}else {
				throw new DynamicFormsServiceException("Invalid Report Identifier",
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(e.getMessage(),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}

	@Override
	public String generateDropdownQueryV2(QueryRequestV2 request) throws DynamicFormsServiceException {
		String res = null;
		try {
			
			Integer empId = request.getEmpId();
			TargetRoleRes empRoleData = getPrecendenceRole(empId);
			String roleIdentifier = empRoleData.getRoleId();
			log.debug("roleIdentifier "+roleIdentifier);
//			roleIdentifier = "1"; //added for testing
			Optional<ReportQueries> rqOpt = reportQueriesDAO.findQuery(request.getReportIdentifier(), roleIdentifier);
			
			if (rqOpt.isPresent()) {
				StringBuilder query = new StringBuilder();
				ReportQueries rq = rqOpt.get();
				String selectQuery = rq.getQuery();
				String tableName = rq.getTableName();
				query.append(selectQuery);
			
				boolean whereflagInPayload = StringUtils.containsIgnoreCase(selectQuery, "where");
				log.debug("whereflagInPayload "+whereflagInPayload);
				if(null!=request.getWhere() && !whereflagInPayload) {
				String whereClasue = whereQueryBuilder(request.getWhere());
				query.append(GsAppConstants.SPACE).append("WHERE").append(GsAppConstants.SPACE)
						.append(whereClasue);
	
				}
				if(null!=request.getWhere() && whereflagInPayload) {
					String whereClasue = whereQueryBuilder(request.getWhere());
					query.append(GsAppConstants.SPACE).append("AND").append(GsAppConstants.SPACE)
							.append(whereClasue);
				}
				
				if (null != request.getGroupBy() && !request.getGroupBy().isEmpty()) {
					String groupBy = request.getGroupBy().toString();
					if (null != groupBy && groupBy.length() > 0) {
						groupBy = groupBy.substring(1, groupBy.length() - 1);
					}
					query.append(GsAppConstants.SPACE).append(GsAppConstants.GROUP_BY).append(GsAppConstants.SPACE)
							.append(groupBy);
				}

				if (null != request.getOrderBy() && !request.getOrderBy().isEmpty()) {
					String orderBy = request.getOrderBy().toString();
					if (null != orderBy && orderBy.length() > 0) {
						orderBy = orderBy.substring(1, orderBy.length() - 1);
					}
					query.append(GsAppConstants.SPACE).append(GsAppConstants.ORDER_BY).append(GsAppConstants.SPACE)
							.append(orderBy);

					if (null != request.getOrderByType() && request.getOrderByType().length() > 0) {
						query.append(GsAppConstants.SPACE).append(request.getOrderByType());
					}
				}

				log.debug("query::" + query);
				log.debug("tableName "+tableName);
				List<Object[]> colnHeadersList = new ArrayList<>();
				if(null!= tableName && !tableName.isEmpty()) {
					colnHeadersList = entityManager.createNativeQuery("DESCRIBE " + tableName).getResultList();
				}
				else {
					
					String tmp = selectQuery;
					if(tmp.contains("select") || tmp.contains("from")) {
						tmp = tmp.substring(tmp.indexOf("select"),tmp.indexOf("from"));
						tmp = tmp.replaceAll("select","");
					}
					if(tmp.contains("SELECT") || tmp.contains("FROM")) {
						tmp = tmp.substring(tmp.indexOf("SELECT"),tmp.indexOf("FROM"));
						tmp = tmp.replaceAll("SELECT","");
					}
					System.out.println("tmp ::"+tmp);
					String[] tmpArr = tmp.split(",");
					for(String s: tmpArr) {
						String[] a = new String[1];
						a[0]=s;
						colnHeadersList.add(a);
					}
				}
				log.debug("colnHeadersList "+colnHeadersList);
				
				List<String> headers = new ArrayList<>();
				for (Object[] arr : colnHeadersList) {

					String colName = (String) arr[0];
					if(StringUtils.containsIgnoreCase(colName, " as ")) {
						colName = colName.replaceAll("\"", "");
						colName = colName.replaceAll("\'","");
						colName = colName.substring(StringUtils.indexOfIgnoreCase(colName, " AS")+3, colName.length());
						colName=colName.trim();
						headers.add(colName);
					}
				}

				log.debug("Coln Headers ::" + headers);
				final List<Map<String, Object>> jObjList = new ArrayList<>();
				
				int maxItems = rq.getMaxItems();
				StringBuilder limitQuery = new StringBuilder();
				limitQuery.append("Selct * From ( ");
				limitQuery.append(query);
				limitQuery.append(" ) LIMIT ");
				limitQuery.append(maxItems + 1);
				
				Query q = entityManager.createNativeQuery(query.toString());
				List<Object[]> queryResults = q.getResultList();
				//List<PreEnquiry> preList = new ArrayList<>();
				for (int i = 0; i < queryResults.size(); i++) {
					Object[] objArr = queryResults.get(i);
					Map<String, Object> map = new LinkedHashMap<>();
					for (int j = 0; j < objArr.length; j++) {
						String colName = headers.get(j);
						map.put(colName, objArr[j]);
					}
					jObjList.add(map);
				}
				log.debug("jObjList size " + jObjList.size());
				
				
				
				
				 
				Map<String,Object> finalMap = new LinkedHashMap<>();
				//String csvLink = null;
				//String excelLink = null;
				//String pdfLink = null;
				List<Map<String, Object>> filterdList = new ArrayList<>();
				if(!jObjList.isEmpty()) {
				
					
					
					CompletableFuture<String> csvFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generateCsvLink(jObjList,request.getReportIdentifier());
					    }
					});
					CompletableFuture<String> excelFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generateExcelLink(jObjList,request.getReportIdentifier());
					    }
					});
					CompletableFuture<String> pdfFuture = CompletableFuture.supplyAsync(new Supplier<String>() {
						public String get() {
					    	return generatePDFLinkV2(jObjList,request.getReportIdentifier());
					    }
					});
					/*
					csvLink = generateCsvLink(jObjList,request.getReportIdentifier());
					excelLink = generateExcelLink(jObjList,request.getReportIdentifier());
					pdfLink = generatePDFLinkV2(jObjList,request.getReportIdentifier());
					log.debug("pdfLink "+pdfLink);
					pdfLink = fileControllerUrl+"/downloadFile/"+pdfLink;
					excelLink = fileControllerUrl+"/downloadFile/"+excelLink;
					csvLink = fileControllerUrl+"/downloadFile/"+csvLink;
					*/
					
					if(jObjList.size() > maxItems) {
						filterdList = jObjList.subList(0, maxItems);
						finalMap.put("MaxItems", maxItems);
					}else {
						filterdList=jObjList;
					}
					finalMap.put("excelLink", excelFuture.get());
					finalMap.put("csvLink", csvFuture.get());
					finalMap.put("pdfLink", pdfFuture.get());
				}
				
				
				finalMap.put("data", filterdList);
				res = objectMapper.writeValueAsString(filterdList);
			}else {
				throw new DynamicFormsServiceException("Invalid Report Identifier",
						HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new DynamicFormsServiceException(env.getProperty("InternalServerError"),
					HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return res;
	}
	
	
	private String generatePDFLinkV2(List<Map<String, Object>> jObjList, String reportId) {

		String tableCell = "<td style=\"width: CALCULATED_WIDTH%;\">CELL_VALUE</td>";
    	String htmlString = "<html>\r\n <head>\r\n"
    			+ "<style>\r\n"
    			+ "tr {\r\n"
    			+ "  font-size: 8px;\r\n"
    			+ "}\r\n"
    			+ "\r\n"
    			+ "td {\r\n"
    			+ "  font-size: 8px !important;\r\n"
    			+ "}\r\n"
    			+ "</style>\r\n"
    			+ "</head>"
    			
    			+ "<body>\r\n"
    			+ "<table style=\"border-collapse: collapse; width: 100%;\" border=\"1\">\r\n"
    			+ "<tbody>\r\n"
    			+ "<tr>\r\n"
    			+ "TABLE_HEADERS\r\n"
    			+ "</tr>\r\n"
    			+ "TABLE_CONTENT\r\n"
    			+ "</tbody>\r\n"
    			+ "</table>\r\n"
    			+ "</body>\r\n"
    			+ "</html>";
    	String fileName = reportId + "_" + System.currentTimeMillis() + ".pdf";
		try {

			if (null != jObjList) {
				Map<String, Object> map = jObjList.get(1);
				Object[] objArr = map.keySet().toArray();
				String headerString="";
				int colns = objArr.length;
				int width = (100/colns);
				log.debug("Excepted table cell width "+width);
				for (int k = 0; k < objArr.length; k++) {
					String tmp = tableCell;
					tmp = StringUtils.replaceAll(tmp, "CALCULATED_WIDTH", String.valueOf(width));
					tmp = StringUtils.replaceAll(tmp, "CELL_VALUE", objArr[k]!=null?objArr[k].toString():"");
					headerString  += tmp;
				}
				//log.debug("headerString "+headerString);
		
				String valueString="";
				for (Map<String, Object> dataMap : jObjList) {
					String rowString = "";
					Object[] valArr = dataMap.values().toArray();
					for (int z = 0; z < valArr.length; z++) {
						String tmp = tableCell;
						tmp = StringUtils.replaceAll(tmp, "CALCULATED_WIDTH", String.valueOf(width));
						tmp = StringUtils.replaceAll(tmp, "CELL_VALUE", valArr[z]!=null?valArr[z].toString():"");
						rowString  += tmp;
					}
					rowString = "<tr>"+rowString+"</tr>";
					valueString+=rowString;
				}
				htmlString = htmlString.replaceAll("TABLE_HEADERS", headerString);
				htmlString = htmlString.replaceAll("TABLE_CONTENT", valueString);
				//log.debug("htmlString "+htmlString);
				OutputStream fileOutputStream = new FileOutputStream(tmpPath + fileName);
				HtmlConverter.convertToPdf(htmlString, fileOutputStream);
			}
			
			log.debug("Created File Successfully " + (tmpPath + fileName));
			fileName = fileControllerUrl+"/downloadFile/"+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	
	}

	private List<Map<String, Object>> getPaginatedList(List<Map<String, Object>> jObjList, int size, int pageNo) {
		int totalCnt = jObjList.size();
		int fromIndex = size * (pageNo - 1);
		int toIndex = size * pageNo;

		if (toIndex > totalCnt) {
			toIndex = totalCnt;
		}
		if (fromIndex > toIndex) {
			fromIndex = toIndex;
		}
		return jObjList.subList(fromIndex, toIndex);
	}

	/*
	 * private String generatePDFLink(String csvLink, String reportId) { String
	 * fileName = reportId + "_" + System.currentTimeMillis() + "_pdf" + ".pdf";
	 * com.aspose.cells.Workbook book; log.debug("csvLink in generatePDF " +
	 * csvLink); try { book = new com.aspose.cells.Workbook(tmpPath + csvLink);
	 * book.save(tmpPath + fileName, SaveFormat.AUTO); } catch (Exception e) {
	 * 
	 * e.printStackTrace(); }
	 * 
	 * return fileName; }
	 */
	private String generateExcelLink(List<Map<String, Object>> jObjList, String reportIdentifier) {
		String fileName = reportIdentifier + "_" + System.currentTimeMillis() + "_excel" + ".xlsx";
		try {

			Workbook workbook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file
			CreationHelper createHelper = workbook.getCreationHelper();
			Sheet sheet = workbook.createSheet("Data");
			Map<String, Object> map = jObjList.get(1);
			Object[] objArr = map.keySet().toArray();
			String[] headers = new String[objArr.length];
			for (int k = 0; k < objArr.length; k++) {

				headers[k] = objArr[k].toString();
			}
			CellStyle headerCellStyle = workbook.createCellStyle();
			Row headerRow = sheet.createRow(0);
			for (int i = 0; i < headers.length; i++) {
				org.apache.poi.ss.usermodel.Cell cell = headerRow.createCell(i);
				cell.setCellValue(headers[i]);
			}
			int rowNum = 1;
			for (Map<String, Object> dataMap : jObjList) {
				Object[] obj = dataMap.values().toArray();
				Row row = sheet.createRow(rowNum++);
				for (int j = 0; j < obj.length; j++) {
					row.createCell(j).setCellValue(getValue(obj[j]));
				}
			}
			for (int i = 0; i < headers.length; i++) {
				sheet.autoSizeColumn(i);
			}
			FileOutputStream fileOut = new FileOutputStream(tmpPath + fileName);
			workbook.write(fileOut);
			fileOut.close();
			// workbook.close();
			log.debug("Generated Excel Successfully " + fileName);
			fileName = fileControllerUrl+"/downloadFile/"+fileName;
		} catch (Exception e) {
			e.printStackTrace();

		}
		return fileName;
	}

	private String getValue(Object obj) {
		if (null != obj) {
			return obj.toString();
		} else {
			return "";
		}

	}

	private String generateCsvLink(List<Map<String, Object>> jObjList, String reportId) {
		String fileName = reportId + "_" + System.currentTimeMillis() + ".csv";
		try {
			FileWriter outputfile = new FileWriter(tmpPath + fileName);
			CSVWriter writer = new CSVWriter(outputfile);
			if (null != jObjList) {
				Map<String, Object> map = jObjList.get(1);
				Object[] objArr = map.keySet().toArray();
				String[] headers = new String[objArr.length];
				for (int k = 0; k < objArr.length; k++) {

					headers[k] = objArr[k].toString();
				}
				writer.writeNext(headers);

				for (Map<String, Object> dataMap : jObjList) {
					Object[] valArr = dataMap.values().toArray();
					String[] values = new String[valArr.length];
					for (int z = 0; z < valArr.length; z++) {
						if (valArr[z] != null) {
							values[z] = valArr[z].toString();
						} else {
							values[z] = "";
						}
					}
					writer.writeNext(values);

				}
				writer.close();
			}

			log.debug("Created File Successfully " + (tmpPath + fileName));
			fileName = fileControllerUrl+"/downloadFile/"+fileName;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fileName;
	}

	private String whereQueryBuilder(List<WhereRequest> whereList) {
		StringBuilder whereQueryMain = new StringBuilder();
		int cnt = 1;
	
		if (whereList != null && !whereList.isEmpty()) {
			log.debug("whereList Size ::"+whereList.size());
			
			for(WhereRequest wr : whereList) {
				String tmp = "";
				StringBuilder whereQuery = new StringBuilder();
				List<QueryParam> valList = wr.getValues();
				System.out.println("valList:" + valList.size());
				
				if(null!=valList && !valList.isEmpty()) {
					
						if (wr.getType().equalsIgnoreCase(GsAppConstants.TEXT)) {
							for (QueryParam param : valList) {
								tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
							}
							tmp = tmp.substring(0, tmp.length() - 1);
							whereQuery.append(wr.getKey()).append(GsAppConstants.SPACE).append(GsAppConstants.IN_OP)
							.append(GsAppConstants.OPEN_BRACE).append(tmp).append(GsAppConstants.CLOSED_BRACE);
							
						}
						else if (wr.getType().equalsIgnoreCase(GsAppConstants.NUMBER)) {
							
							for (QueryParam param : valList) {
								tmp = tmp + param.getValue() + GsAppConstants.COMMA_SEPERATOR;
							}
							tmp = tmp.substring(0, tmp.length() - 1);
							whereQuery.append(wr.getKey()).append(GsAppConstants.SPACE).append(GsAppConstants.IN_OP)
							.append(GsAppConstants.OPEN_BRACE).append(tmp).append(GsAppConstants.CLOSED_BRACE);
						}
						
						// -- Date operations
						else if(GsAppConstants.DATE.equalsIgnoreCase(wr.getType())) {
							tmp = operateDateCondition(wr, tmp, whereQuery, valList);
						}
							
						else if (wr.getType().equalsIgnoreCase(GsAppConstants.FROMDATE)) {
							tmp = operateFromDate(wr, tmp, whereQuery, valList);
						}
						
						else if (wr.getType().equalsIgnoreCase(GsAppConstants.TODATE)) {
							tmp = operateToDate(wr, tmp, whereQuery, valList);
						}
						
						else if (wr.getType().equalsIgnoreCase(GsAppConstants.EQUALDATE)) {
							
							for (QueryParam param : valList) {
								tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
							}
							tmp = tmp.substring(0, tmp.length() - 1);
							whereQuery.append(wr.getKey()).append(GsAppConstants.SPACE).append("= ")
							.append(tmp);
						}
						
						
						log.debug("tmp::" + tmp);
					}
				log.debug("Cnt ::"+cnt);
				if (cnt < whereList.size()) {
					whereQuery.append(GsAppConstants.SPACE);
					whereQuery.append(GsAppConstants.AND);
					whereQuery.append(GsAppConstants.SPACE);
				}
				whereQueryMain.append(whereQuery);
				cnt++;

				
				}
				

				
			}
		
		return whereQueryMain.toString();
	}


	private String operateToDate(WhereRequest wr, String tmp, StringBuilder whereQuery, List<QueryParam> valList) {
		for (QueryParam param : valList) {
			tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
		}
		tmp = tmp.substring(0, tmp.length() - 1);
		
		String key = wr.getKey();
		String[] keys = key.split("to_date.to");
		key = keys.length >= 1 ? keys[1] : keys[0];
		
		whereQuery.append(key).append(GsAppConstants.SPACE).append("<= ")
		.append(tmp);
		return tmp;
	}


	private String operateFromDate(WhereRequest wr, String tmp, StringBuilder whereQuery, List<QueryParam> valList) {
		for (QueryParam param : valList) {
			tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
		}
		tmp = tmp.substring(0, tmp.length() - 1);
		
		String key = wr.getKey();
		String[] keys = key.split("from_date.from");						
		key = keys.length >= 1 ? keys[1] : keys[0];
		
		whereQuery.append(key).append(GsAppConstants.SPACE).append(">= ")
		.append(tmp);
		return tmp;
	}


	private String operateDateCondition(WhereRequest wr, String tmp, StringBuilder whereQuery,
			List<QueryParam> valList) {
		String dataKey = wr.getKey();

		int fromIndex = dataKey.indexOf("from_date.from");
		if(fromIndex != -1) {
			for (QueryParam param : valList) {
				tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
			}
			tmp = tmp.substring(0, tmp.length() - 1);

			String[] keys = dataKey.split("from_date.from");						

			whereQuery.append(keys[1]).append(GsAppConstants.SPACE).append(">= ")
			.append(tmp);
		}
		else {
			int toIndex = dataKey.indexOf("to_date.to");
			if(toIndex != -1) {
				for (QueryParam param : valList) {
					tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
				}
				tmp = tmp.substring(0, tmp.length() - 1);

				String[] keys = dataKey.split("to_date.to");
				whereQuery.append(keys[1]).append(GsAppConstants.SPACE).append("<= ")
				.append(tmp);
			}
			else {
				for (QueryParam param : valList) {
					tmp = tmp + "\"" + param.getValue() + "\"" + GsAppConstants.COMMA_SEPERATOR;
				}
				tmp = tmp.substring(0, tmp.length() - 1);
				whereQuery.append(wr.getKey()).append(GsAppConstants.SPACE).append("= ")
				.append(tmp);
			}
		}
		return tmp;
	}





	public Timestamp getCurrentTmeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	@Autowired
	AutoSaveRepo autoSaveRepo;

	@Autowired
	ModelMapper modelMapper;

	@Override
	public AutoSaveEntity saveAutoSave(AutoSave req) {
		AutoSaveEntity ase = new AutoSaveEntity();
		String jsonReq =  new Gson().toJson(req.getData());
		System.out.println("jsonReq "+jsonReq);
		ase.setData(jsonReq);
		ase.setStatus(req.getStatus());
		ase.setUniversalId(req.getUniversalId());
		
		List<AutoSaveEntity> dbList = autoSaveRepo.getDataByUniversalId(req.getUniversalId());
		if(dbList.isEmpty()) {
			return autoSaveRepo.save(ase);
		}else {
			dbList.forEach(x->{
				autoSaveRepo.delete(x);;
			});
			return	autoSaveRepo.save(ase);
		}
	}

	@Override
	public AutoSaveEntity updateAutoSsave(AutoSaveEntity req) {
		AutoSaveEntity db = autoSaveRepo.findById(req.getId()).get();
		db.setUniversalId(req.getUniversalId());
		db.setData(req.getData());
		db.setStatus(req.getStatus());
		return autoSaveRepo.save(db);
	}

	@Override
	public List<AutoSaveEntity> getAllAutoSave(String type, int pageNo, int size) {
		Pageable pageable = PageRequest.of(pageNo, size);
		return autoSaveRepo.getAutoSaveBasedOnType(type, pageable).toList();
	}

	@Override
	public String deleteAutoSave(int id) {
		autoSaveRepo.deleteById(id);
		return "Deleted Sucessfully";
	}

	@Override
	public String getAutoSaveByUid(String uid) throws DynamicFormsServiceException {
		Optional<AutoSaveEntity> opt = autoSaveRepo.getDataByUniversalIdV2(uid);
		String str = null;
		AutoSaveEntity auto = null;
		if(opt.isPresent()) {
			auto = opt.get();
			str =  new Gson().toJson(auto.getData());
			str = str.replace("\\", "");
			System.out.println("str "+str);
			auto.setData(str);
			//convertedObject = new Gson().fromJson(auto.getData(), JsonObject.class);
		}else {
			throw new DynamicFormsServiceException("Data Not found in sysem for given universalId", HttpStatus.BAD_REQUEST);
		}
		return str;
	}
	
	//ETVBRL Strats

	@Autowired
	LeadStageRefDao leadStageRefDao;
	
	@Autowired
	DmsLeadDao dmsLeadDao;
	
	@Autowired
	DmsBranchDao dmsBranchDao;
	
	@Autowired
	DmsLeadDropDao dmsLeadDropDao;
	
	@Autowired
	DmsEmployeeRepo dmsEmployeeRepo;
	
	@Autowired
	DmsWfTaskDao dmsWfTaskDao;
	
	@Autowired
	DmsExchangeBuyerDao dmsExchangeBuyerDao;
	

	
	
	String PREENQUIRY="PREENQUIRY";
	String DROPPED ="DROPPED";
	String ENQUIRY ="ENQUIRY";
	String HOME_VISIT ="Home Visit";
	String TEST_DRIVE ="Test Drive";
	
	@Override
	public Map<String,String> generateETVBRLReport(ETVRequest request) throws DynamicFormsServiceException {
		log.debug("Calling generateETVBRLReport(){}");
		Map<String,String> map = new HashMap<>();
		String preFileName = tmpPath + "PRE_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String enqFileName = tmpPath + "ENQ_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String bookFileName = tmpPath + "BOOKING_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String enqLiveFileName = tmpPath + "ENQ_LIVE_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String enqLostFileName = tmpPath + "ENQ_LOST_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String bookLiveFileName = tmpPath + "BOOKING_LIVE_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String bookLostFileName = tmpPath + "BOOKING_LOSTETVBRL_"+System.currentTimeMillis()+".xlsx";
		String retailFileName = tmpPath + "RETAIL_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String deliveryFileName = tmpPath + "DELIVERY_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String evalFileName = tmpPath + "EVALUATION_LOSTETVBRL_"+System.currentTimeMillis()+".xlsx";
		String hvFileName = tmpPath + "HV_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		String tdFileName = tmpPath + "TD_ETVBRL_"+System.currentTimeMillis()+".xlsx";
		List<String> fileNameList = new ArrayList<>();
		String combineFileName=null;
		try {
		
			
			/*fileNameList.add(new File(preFileName));
			fileNameList.add(new File(enqFileName));
			fileNameList.add(new File(bookFileName));
			fileNameList.add(new File(enqLiveFileName));
			fileNameList.add(new File(enqLostFileName));
			fileNameList.add(new File(bookLiveFileName));
			fileNameList.add(new File(bookLostFileName));
			fileNameList.add(new File(retailFileName));
			fileNameList.add(new File(deliveryFileName));
			fileNameList.add(new File(evalFileName));
			fileNameList.add(new File(hvFileName));
			fileNameList.add(new File(tdFileName));*/
			
			fileNameList.add(preFileName);
			fileNameList.add(enqFileName);
			fileNameList.add(bookFileName);
			fileNameList.add(enqLiveFileName);
			fileNameList.add(enqLostFileName);
			fileNameList.add(bookLiveFileName);
			fileNameList.add(bookLostFileName);
			fileNameList.add(retailFileName);
			fileNameList.add(deliveryFileName);
			fileNameList.add(evalFileName);
			fileNameList.add(hvFileName);
			fileNameList.add(tdFileName);
			
			String orgId = request.getOrgId();
			String startDate = request.getFromDate();
			String endDate = request.getToDate();
		//	List<String> branchIdList  =request.getBranchIdList();
			List<String> branchIdList  = new ArrayList<>();
			for(String str:request.getBranchIdList()) {
				branchIdList.add(String.valueOf(getBrachIdFromLocationID(str)));
			}
			log.debug("branchIdList "+branchIdList);
			String parentBranchId = request.getParentBranchId();
			
			if(null==orgId && null==startDate && null == endDate) {
				throw new DynamicFormsServiceException("ORG ID,START DATE AND END_DATE IS MISSING", HttpStatus.BAD_REQUEST);
			}
			log.debug("orgID:"+orgId+",StartDate:"+startDate+",endDate:"+endDate);
			
			startDate = startDate+" 00:00:00";
			endDate = endDate+" 23:59:59";
			
			log.debug("branchIdList::"+branchIdList);
			log.debug("parentBranchId:"+parentBranchId);
			
			//logic for preenquiry
			List<LeadStageRefEntity> leadRefDBList = getLeadRefDBList(orgId,startDate,endDate,PREENQUIRY,branchIdList);
			log.debug("leadRefDBList size for PREENQUIRY"+leadRefDBList.size());
			List<Integer> leadIdList = leadRefDBList.stream().map(x->x.getLeadId()).collect(Collectors.toList());
			List<DmsLead> leadDBList = dmsLeadDao.findAllById(leadIdList);
			
			if(null!=leadDBList) {
				log.debug("leadDBList size for PREENQUIRY "+leadDBList.size());
			}
			List<ETVPreEnquiry> etvList = buildPreEnqList(leadRefDBList,leadDBList);
			
		
			log.debug("fileName ::"+preFileName);
			log.debug("etvList::"+etvList.size());
			genearateExcelForPreEnq(etvList,preFileName);
			log.debug("Generated report for Pre Enq");
			
			
			
		
			

			//logic for enquiry
			List<LeadStageRefEntity> leadRefDBListEnq = getLeadRefDBList(orgId,startDate,endDate,ENQUIRY,branchIdList);
			log.debug("leadRefDBList size for ENQUIRY:"+leadRefDBListEnq.size());
			List<DmsLead> leadDBListEnq = dmsLeadDao.findAllById(leadRefDBListEnq.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseEnqList = new ArrayList(); 
			for(DmsLead dmsLead : leadDBListEnq) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseEnqList.add(dmsResponse);
			}

			if(null!=dmsResponseEnqList) {
				log.debug("dmsResponseEnqList size for ENQUIRY "+dmsResponseEnqList.size());
				genearateExcelForEnq(dmsResponseEnqList,leadRefDBListEnq,enqFileName,"Enquiry");
				log.debug("Generated report for ENQ");
				
			}
			
		
			
			//logic for LiveEnquiry
			List<LeadStageRefEntity> leadRefDBListPreBooking = getLeadRefDBList(orgId,startDate,endDate,"PREBOOKING",branchIdList);
			List<LeadStageRefEntity> leadRefDBListLiveEnq = removeDuplicates(leadRefDBListEnq,leadRefDBListPreBooking);
			log.debug("leadRefDBListLiveEnq size for LIVE ENQUIRY:::"+leadRefDBListLiveEnq.size());
			List<DmsLead> leadDBListLiveEnq = dmsLeadDao.findAllById(leadRefDBListLiveEnq.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseLiveEnqList = new ArrayList(); 
			for(DmsLead dmsLead : leadDBListLiveEnq) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseLiveEnqList.add(dmsResponse);
			}
			if(null!=dmsResponseLiveEnqList) {
				log.debug("dmsResponseEnqList size for LIVE ENQUIRY "+dmsResponseEnqList.size());
				genearateExcelForEnq(dmsResponseLiveEnqList,leadRefDBListLiveEnq,enqLiveFileName,"Live Enquiry");
				log.debug("Generated report for LIVE ENQ");
				
			}
		
			
			
			
			//logic for ENQUIRY LOST
			List<DmsLead> leadDBEnquiryList = dmsLeadDao.findAllById(leadRefDBListEnq.stream().map(x->x.getLeadId()).collect(Collectors.toList()));
			List<LeadStageRefEntity> leadDBEnquiryLostList = new ArrayList<>();
			for(DmsLead dmsLead : leadDBEnquiryList) {
				String leadStg = dmsLead.getLeadStage();
				if(leadStg.equalsIgnoreCase("DROPPED")) {
					String leadId = String.valueOf(dmsLead.getId());
					log.debug("LeadId "+leadId);
					for(LeadStageRefEntity ref : leadRefDBListEnq) {
						String refLeadId = String.valueOf(ref.getLeadId());
						log.debug("refLeadId "+refLeadId);
						if(refLeadId.equals(leadId)) {
							leadDBEnquiryLostList.add(ref);
						}
					}
				}
			}
			log.debug("leadDBEnquiryLostList size: "+leadDBEnquiryLostList.size());
			log.debug("leadRefDBList size for ENQUIRY:"+leadRefDBListEnq.size());
			
			log.debug("leadRefDBListLiveEnq size for LIVE ENQUIRY:::"+leadRefDBListLiveEnq.size());
			List<DmsLead> dmsLeadLostEnqList = dmsLeadDao.findAllById(leadDBEnquiryLostList.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseLostEnqList = new ArrayList<>(); 
			for(DmsLead dmsLead : dmsLeadLostEnqList) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseLostEnqList.add(dmsResponse);
			}
			if(null!=dmsResponseLostEnqList) {
				log.debug("dmsResponseEnqList size for LIVE ENQUIRY "+dmsResponseEnqList.size());
				genearateExcelForEnq(dmsResponseLostEnqList,leadDBEnquiryLostList,enqLostFileName,"Lost Enquiry");
				log.debug("Generated report for LOST ENQ");
				
			}
		
			
			
		
			//logic for BOOKING
			List<LeadStageRefEntity> leadRefDBListBooking = getLeadRefDBList(orgId,startDate,endDate,"BOOKING",branchIdList);
			log.debug("leadRefDBList size for BOOKING:"+leadRefDBListBooking.size());
			List<DmsLead> leadDBListBooking = dmsLeadDao.findAllById(leadRefDBListBooking.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseBookingList = new ArrayList(); 
			for(DmsLead dmsLead : leadDBListBooking) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseBookingList.add(dmsResponse);
			}

			if(null!=dmsResponseBookingList) {
				log.debug("dmsResponseEnqList size for BOOKING "+dmsResponseBookingList.size());
				genearateExcelForBooking(dmsResponseBookingList,leadRefDBListBooking,bookFileName,"Booking");
				log.debug("Generated report for BOOKING");
			}
		
			
					
			
			//logic for LIVE BOOKING
			List<LeadStageRefEntity> leadRefDBInvoice = getLeadRefDBList(orgId,startDate,endDate,"INVOICE",branchIdList);
			List<LeadStageRefEntity> leadRefDBLiveBookingList = removeDuplicates(leadRefDBListBooking,leadRefDBInvoice);
			log.debug("leadRefDBLiveBookingList size  after "+leadRefDBLiveBookingList.size());	
			List<DmsLead> leadDBLiveBookingList = dmsLeadDao.findAllById(leadRefDBLiveBookingList.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseLiveBookingList = new ArrayList(); 
			for(DmsLead dmsLead : leadDBLiveBookingList) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseLiveBookingList.add(dmsResponse);
			}

			if(null!=dmsResponseLiveBookingList) {
				log.debug("dmsResponseEnqList size for LIVE BOOKING "+dmsResponseLiveBookingList.size());
				genearateExcelForBooking(dmsResponseLiveBookingList,leadRefDBLiveBookingList,bookLiveFileName,"Live Booking");
				log.debug("Generated report for LIVE BOOKING");
			}
			
			
			//logic for BOOKING LOST
			List<DmsLead> leadDBBookingList = dmsLeadDao.findAllById(leadRefDBListBooking.stream().map(x->x.getLeadId()).collect(Collectors.toList()));
			List<LeadStageRefEntity> leadDBBookingLostList = new ArrayList<>();
			for(DmsLead dmsLead : leadDBBookingList) {
				String leadStg = dmsLead.getLeadStage();
				if(leadStg.equalsIgnoreCase("DROPPED")) {
					String leadId = String.valueOf(dmsLead.getId());
					log.debug("LeadId "+leadId);
					for(LeadStageRefEntity ref : leadRefDBListBooking) {
						String refLeadId = String.valueOf(ref.getLeadId());
						log.debug("refLeadId "+refLeadId);
						if(refLeadId.equals(leadId)) {
							leadDBBookingLostList.add(ref);
						}
					}
				}
			}
			log.debug("leadDBBookingLostList size: "+leadDBBookingLostList.size());
			
			List<DmsLead> dmsLeadDropBookingList = dmsLeadDao.findAllById(leadDBBookingLostList.stream().map(x->x.getLeadId()).collect(Collectors.toList()));
			List<DMSResponse> dmsResponseLostBookingList = new ArrayList<>(); 
			for(DmsLead dmsLead : dmsLeadDropBookingList) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseLostBookingList.add(dmsResponse);
			}

			if(null!=dmsResponseLostBookingList) {
				log.debug("dmsResponseEnqList size for LIVE BOOKING "+dmsResponseLiveBookingList.size());
				genearateExcelForBooking(dmsResponseLostBookingList,leadDBBookingLostList,bookLostFileName,"Lost Booking");
				log.debug("Generated report for LOST BOOKING");
			}
			
			
		
			//lost for RETAIL
			
			List<LeadStageRefEntity> leadRefDBListInvoice = getLeadRefDBList(orgId,startDate,endDate,"INVOICE",branchIdList);
			log.debug("leadRefDBList size for INVOICE:"+leadRefDBListInvoice.size());
			List<DmsLead> dmsLeadInvoiceDbList = dmsLeadDao.findAllById(leadRefDBListInvoice.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseInvoiceList = new ArrayList(); 
			for(DmsLead dmsLead : dmsLeadInvoiceDbList) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseInvoiceList.add(dmsResponse);
			}

			if(null!=dmsResponseInvoiceList) {
				log.debug("dmsResponseEnqList size for INVOICE "+dmsResponseInvoiceList.size());
				genearateExcelForInvoice(dmsResponseInvoiceList,leadRefDBListInvoice,retailFileName);
				log.debug("Generated report for INVOICE");
			}
			
			//lost for DELIVERY
			
			List<LeadStageRefEntity> leadRefDBListDelivery = getLeadRefDBList(orgId,startDate,endDate,"DELIVERY",branchIdList);
			log.debug("leadRefDBList size for DELIVERY:"+leadRefDBListDelivery.size());
			List<DmsLead> dmsLeadDeliveryDbList = dmsLeadDao.findAllById(leadRefDBListDelivery.stream().map(x->x.getLeadId()).collect(Collectors.toList()));

			List<DMSResponse> dmsResponseDeliveryList = new ArrayList(); 
			for(DmsLead dmsLead : dmsLeadDeliveryDbList) {
				String universalId = dmsLead.getCrmUniversalId();
				String tmp = leadEnqUrl.replace("universal_id",universalId);
				DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
				dmsResponseDeliveryList.add(dmsResponse);
			}

			if(null!=dmsResponseDeliveryList) {
				log.debug("dmsResponseEnqList size for DELIVERY "+dmsResponseDeliveryList.size()+" deliveryFileName "+deliveryFileName);
				
				genearateExcelForDelivery(dmsResponseDeliveryList,leadRefDBListDelivery,deliveryFileName);
				log.debug("Generated report for DELIVERY");
			}
		
			
			
			//  TestDrive
			
			List<DmsWFTask> wfTaskListTD = dmsWfTaskDao.getWfTaskByTaskName("Test Drive", startDate, endDate);
			List<DMSResponse> dmsResponseTDList = new ArrayList(); 
			for(DmsWFTask task : wfTaskListTD) {
				String universalId = task.getUniversalId();
				if(null!=task) {
					String tmp = leadEnqUrl.replace("universal_id",universalId);
					DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
					dmsResponseTDList.add(dmsResponse);
				}
			}

			if(null!=dmsResponseTDList) {
				log.debug("dmsResponseEnqList size for TEST DRIVE "+dmsResponseTDList.size());
				genearateExcelForTD(dmsResponseTDList,wfTaskListTD,tdFileName,"Test Drive");
				log.debug("Generated report for Test Drive");
			}
		
			
		//  Home Visit
			
					List<DmsWFTask> wfTaskListHV = dmsWfTaskDao.getWfTaskByTaskName("Home Visit", startDate, endDate);
					List<DMSResponse> dmsResponseHVList = new ArrayList(); 
					for(DmsWFTask task : wfTaskListTD) {
						String universalId = task.getUniversalId();
						if(null!=task) {
							String tmp = leadEnqUrl.replace("universal_id",universalId);
							DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
							dmsResponseHVList.add(dmsResponse);
						}
					}

					if(null!=dmsResponseTDList) {
						log.debug("dmsResponseEnqList size for Home Visit "+dmsResponseTDList.size());
						genearateExcelForTD(dmsResponseHVList,wfTaskListHV,hvFileName,"Home Visit");
						log.debug("Generated report for Home Visit");
					}
				
			
			
				
			
			// Evalutation
			List<DmsWFTask> wfTaskListEval = dmsWfTaskDao.getWfTaskByTaskName("Evaluation", startDate, endDate);
			List<DMSResponse> dmsResponseEvalList = new ArrayList(); 
			for(DmsWFTask task : wfTaskListEval) {
				String universalId = task.getUniversalId();
				if(null!=task) {
					String tmp = leadEnqUrl.replace("universal_id",universalId);
					DMSResponse dmsResponse = restTemplate.getForEntity(tmp, DMSResponse.class).getBody();
					dmsResponseEvalList.add(dmsResponse);
				}
			}

			if(null!=dmsResponseEvalList) {
				log.debug("dmsResponseEnqList size for Evaluation "+dmsResponseEvalList.size());
				genearateExcelForEval(dmsResponseEvalList,wfTaskListEval,evalFileName,"Evaluation");
				log.debug("Generated report for Evaluationt");
			}
			
			combineFileName = excelUtil.mergeFiles(fileNameList);
			combineFileName = fileControllerUrl+"/downloadFile/"+combineFileName;
			
			map.put("downloadUrl", combineFileName);
			
		}catch(DynamicFormsServiceException e) {
			throw new DynamicFormsServiceException(e.getMessage(), e.getStatusCode());
		}catch(Exception e) {
			e.printStackTrace();
		}
		return map;
	}

	
	private Integer getBrachIdFromLocationID(String str) {
		
		return dmsBranchDao.getBranchByOrgMpId(Integer.parseInt(str)).getBranchId();
		
	}

	private void genearateExcelForEval(List<DMSResponse> dmsResponseList, List<DmsWFTask> wfTaskList,
			String fileName, String sheetName) {

		XSSFWorkbook workbook = new XSSFWorkbook();
		XSSFSheet sheet = workbook.createSheet(sheetName);
		int rowNum = 0;
		Row row = sheet.createRow(rowNum++);
		List<String> rowHeaders = null;
		rowHeaders = getEvalutionRowHeaders();

		try {
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}

			for (DMSResponse res : dmsResponseList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if (null != dmsEntity) {
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto = dmsEntity.getDmsContactDto();
					DmsAccountDto dmsAccountDto = dmsEntity.getDmsAccountDto();
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList = dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts();
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					List<DmsAttachmentDto> dmsAttachments = dmsLeadDto.getDmsAttachments();

					List<DmsAccessoriesDto> dmsAccessoriesList = dmsLeadDto.getDmsAccessories();
					DmsWFTask dmsWFTask = null;

					DmsBookingDto dmsBookingDto = dmsLeadDto.getDmsBooking();
					DMSResponse dmsResponseOnRoadPrice = null;
					List<DmsExchangeBuyer> dmsExchangeBuyerList = null;
					if (dmsLeadDto != null) {
						int leadId = dmsLeadDto.getId();
						
						LeadStageRefEntity leadRef = new LeadStageRefEntity();
						String preEnqId = "";
						String preEnqDate = "";
						String preEnqMonthYear = "";
						String enqId = "";
						String enqDate = "";
						String enqMonthYear = "";
						try {
							List<LeadStageRefEntity> leadRefList = leadStageRefDao.findLeadsByLeadId(leadId);
							if (leadRefList != null && !leadRefList.isEmpty()) {
								leadRef = leadRefList.get(0);
							}
							for (LeadStageRefEntity tmpLeadRef : leadRefList) {
								if (tmpLeadRef.getStageName().equalsIgnoreCase(PREENQUIRY)) {
									preEnqId = tmpLeadRef.getRefNo();
									preEnqDate = tmpLeadRef.getStartDate().toString();
									preEnqMonthYear = getMonthAndYear(tmpLeadRef.getStartDate());
								}
								if (tmpLeadRef.getStageName().equalsIgnoreCase(ENQUIRY)) {
									enqId = tmpLeadRef.getRefNo();
									enqDate = tmpLeadRef.getStartDate().toString();
									enqMonthYear = getMonthAndYear(tmpLeadRef.getStartDate());
								}
							}

						} catch (Exception e) {
							e.printStackTrace();
							log.error("exception:", e);
						}
						try {
							dmsResponseOnRoadPrice = restTemplate
									.getForEntity(leadOnRoadPriceUrl + leadId, DMSResponse.class).getBody();
						} catch (Exception e) {
							e.printStackTrace();
							log.error("exception:", e);
						}
						try {
							dmsExchangeBuyerList = dmsExchangeBuyerDao.getDmsExchangeBuyersByLeadId(leadId);
						} catch (Exception e) {
							e.printStackTrace();
							log.error("exception:", e);
						}

						try {
							Optional<DmsWFTask> dmsWFTaskOpt = wfTaskList.stream()
									.filter(x -> x.getUniversalId().equalsIgnoreCase(dmsLeadDto.getCrmUniversalId()))
									.findAny();
							if (dmsWFTaskOpt.isPresent()) {
								dmsWFTask = dmsWFTaskOpt.get();

							}

						} catch (Exception e) {
							e.printStackTrace();
							log.error("exception:", e);
						}
						String branchName = "";

						if (leadRef != null && leadRef.getBranchId() != null) {
							System.out.println("leadRef.getBranchId() " + leadRef.getBranchId());
							Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());

							if (optBranch.isPresent()) {
								DmsBranch branch = optBranch.get();
								branchName = branch.getName();
							}
						}
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, dmsWFTask != null ? dmsWFTask.getProcessId() : "", cellNum++);
						writeIntoCell(detailsRow, dmsWFTask != null ? dmsWFTask.getUniversalId() : "", cellNum++);
						writeIntoCell(detailsRow, dmsWFTask != null ? dmsWFTask.getTaskActualStartTime() : "",
								cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(dmsWFTask.getTaskActualStartTime()), cellNum++);
						writeIntoCell(detailsRow, enqId, cellNum++);
						writeIntoCell(detailsRow, enqDate, cellNum++);
						writeIntoCell(detailsRow, enqMonthYear, cellNum++);
						writeIntoCell(detailsRow, dmsWFTask != null ? dmsWFTask.getTaskStatus() : "", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto != null ? dmsContactDto.getPhone() : "", cellNum++);
						cellNum = addSingleAddress(addressList, detailsRow, cellNum);
						//
						final List<Map<String, Object>> evalutionList = buildEvaluation(dmsLeadDto.getCrmUniversalId());

						String vehicleType = "";
						String oldCarRegNo = "";
						String oldCarMake = "";
						String oldCarModel = "";
						String oldCarVariant = "";
						String oldCarColor = "";
						String oldCarTransmission = "";
						String vinNo = "";
						String makeYear = "";
						String oldCarManufacturDt = "";
						Date regExpiryDate = null;
						Integer kmsdriven = 0;
						Double expectedPrice = 0D;
						Double offeredPrice = 0D;
						Double gapPrice = 0D;
						Integer evaluatorName = 0;
						Integer evaluatorMgr = 0;

						for (Map<String, Object> m : evalutionList) {
							vehicleType = (String) m.get("vehicle_type");
							oldCarRegNo = (String) m.get("rc_no");
							oldCarMake = (String) m.get("make");
							oldCarModel = (String) m.get("model");
							oldCarVariant = (String) m.get("varient");
							oldCarColor = (String) m.get("colour");
							oldCarTransmission = (String) m.get("transmission");
							vinNo = (String) m.get("chassis_no");
							oldCarManufacturDt = (String) m.get("year_month_of_manufacturing");
							makeYear = (String) m.get("year_month_of_manufacturing");
							regExpiryDate = (Date) m.get("reg_validity");
							kmsdriven = (Integer) m.get("km_driven");
							expectedPrice = (Double) m.get("cust_expected_price");
							offeredPrice = (Double) m.get("evaluator_offer_price");
							evaluatorName = (Integer) m.get("evalutor_id");
							evaluatorMgr = (Integer) m.get("manager_id");
							if(offeredPrice!=null && expectedPrice!=null) {
								gapPrice = offeredPrice - expectedPrice;
							}
						}
						writeIntoCell(detailsRow, vehicleType, cellNum++);
						writeIntoCell(detailsRow, oldCarRegNo, cellNum++);
						writeIntoCell(detailsRow, oldCarMake, cellNum++);
						writeIntoCell(detailsRow, oldCarModel, cellNum++);
						writeIntoCell(detailsRow, oldCarVariant, cellNum++);
						writeIntoCell(detailsRow, oldCarColor, cellNum++);
						writeIntoCell(detailsRow, oldCarTransmission, cellNum++);
						writeIntoCell(detailsRow, vinNo, cellNum++);
						writeIntoCell(detailsRow, oldCarManufacturDt, cellNum++);
						writeIntoCell(detailsRow, makeYear, cellNum++);
						writeIntoCell(detailsRow, regExpiryDate!=null?regExpiryDate.toString():"", cellNum++);
						writeIntoCell(detailsRow, String.valueOf(kmsdriven), cellNum++);
						writeIntoCell(detailsRow, String.valueOf(expectedPrice), cellNum++);
						writeIntoCell(detailsRow, String.valueOf(offeredPrice), cellNum++);
						writeIntoCell(detailsRow, String.valueOf(gapPrice), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLeadStage(), cellNum++);

						writeIntoCell(detailsRow, dmsLeadDto.getEnquiryCategory(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getAging(), cellNum++);
						writeIntoCell(detailsRow, evaluatorName, cellNum++);
						writeIntoCell(detailsRow, evaluatorMgr, cellNum++);

						writeIntoCell(detailsRow, dmsLeadDto != null ? dmsLeadDto.getSalesConsultant() : "", cellNum++);
						String empId = getEmpName(dmsLeadDto.getSalesConsultant());
						writeIntoCell(detailsRow, getEmpName(dmsLeadDto.getSalesConsultant()), cellNum++);
						String teamLeadName = getTeamLead(empId);
						writeIntoCell(detailsRow, teamLeadName, cellNum++); // Team Leader
						writeIntoCell(detailsRow, getManager(teamLeadName), cellNum++); // Manager

						writeIntoCell(detailsRow, dmsWFTask != null ? dmsWFTask.getEmployeeRemarks() : "", cellNum++);
					}
				}
			}
			FileOutputStream out;
			out = new FileOutputStream(new File(fileName));
			workbook.write(out);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("exception:", e);
		}

	}

	private void genearateExcelForTD(List<DMSResponse> dmsResponseList, List<DmsWFTask> wfTaskListTD,
			String fileName, String sheetName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = null;
			
			if(sheetName.equalsIgnoreCase("Test Drive")) {
				rowHeaders = getTestDriveRowHeaders();
			}
			
			if(sheetName.equalsIgnoreCase("Home Visit")) {
				rowHeaders = getHomeVisitRowHeaders();
			}
			
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
			for (DMSResponse res : dmsResponseList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if(null!=dmsEntity) {
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto =dmsEntity.getDmsContactDto();
					DmsAccountDto dmsAccountDto = dmsEntity.getDmsAccountDto();		
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList =  dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts(); 
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					List<DmsAttachmentDto> dmsAttachments = dmsLeadDto.getDmsAttachments();
					
					List<DmsAccessoriesDto> dmsAccessoriesList = dmsLeadDto.getDmsAccessories();
					DmsWFTask dmsWFTask=null;
				
					
					
					DmsBookingDto dmsBookingDto = dmsLeadDto.getDmsBooking();
					DMSResponse dmsResponseOnRoadPrice =null;
					List<DmsExchangeBuyer> dmsExchangeBuyerList = null;
					if (dmsLeadDto != null ) {
						int leadId = dmsLeadDto.getId();
					
						LeadStageRefEntity leadRef  = new LeadStageRefEntity();
						String preEnqId="";
						String preEnqDate="";
						String preEnqMonthYear="";
						String enqId="";
						String enqDate="";
						String enqMonthYear="";
						try {
							List<LeadStageRefEntity> leadRefList = leadStageRefDao.findLeadsByLeadId(leadId);
							if (leadRefList != null && !leadRefList.isEmpty()) {
								leadRef = leadRefList.get(0);
							}
							for (LeadStageRefEntity tmpLeadRef : leadRefList) {
								if (tmpLeadRef.getStageName().equalsIgnoreCase(PREENQUIRY)) {
									preEnqId = tmpLeadRef.getRefNo();
									preEnqDate = tmpLeadRef.getStartDate().toString();
									preEnqMonthYear = getMonthAndYear(tmpLeadRef.getStartDate());
								}
								if (tmpLeadRef.getStageName().equalsIgnoreCase(ENQUIRY)) {
									enqId = tmpLeadRef.getRefNo();
									enqDate = tmpLeadRef.getStartDate().toString();
									enqMonthYear = getMonthAndYear(tmpLeadRef.getStartDate());
								}
							}

						}catch(Exception e) {
							e.printStackTrace();
							log.error("exception:",e);
						}
						try {
							dmsResponseOnRoadPrice = restTemplate.getForEntity(leadOnRoadPriceUrl+leadId, DMSResponse.class).getBody();
						}catch(Exception e) {
							e.printStackTrace();
							log.error("exception:",e);
						}
						try {
							dmsExchangeBuyerList=  dmsExchangeBuyerDao.getDmsExchangeBuyersByLeadId(leadId);
						}
						catch(Exception e) {
							e.printStackTrace();
							log.error("exception:",e);
						}
				
						try {
							Optional<DmsWFTask> dmsWFTaskOpt = wfTaskListTD.stream()
									.filter(x -> x.getUniversalId().equalsIgnoreCase(dmsLeadDto.getCrmUniversalId()))
									.findAny();
							if (dmsWFTaskOpt.isPresent()) {
								dmsWFTask = dmsWFTaskOpt.get();

							}
							
						}catch(Exception e) {
							e.printStackTrace();
							log.error("exception:",e);
						}
						String branchName = "";
						
						if(leadRef!=null && leadRef.getBranchId()!=null) {
							System.out.println("leadRef.getBranchId() "+leadRef.getBranchId());
						Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
					
						if (optBranch.isPresent()) {
							DmsBranch branch = optBranch.get();
							branchName = branch.getName();
						}
						}
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, dmsWFTask!=null?dmsWFTask.getProcessId():"", cellNum++);
						writeIntoCell(detailsRow,  dmsWFTask!=null?dmsWFTask.getUniversalId():"", cellNum++);
						writeIntoCell(detailsRow,  dmsWFTask!=null?dmsWFTask.getTaskActualStartTime():"", cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(dmsWFTask.getTaskActualStartTime()), cellNum++);
						
						writeIntoCell(detailsRow, preEnqId, cellNum++);
						writeIntoCell(detailsRow, preEnqDate, cellNum++);
						writeIntoCell(detailsRow, preEnqMonthYear, cellNum++);
						writeIntoCell(detailsRow, enqId, cellNum++);
						writeIntoCell(detailsRow, enqDate, cellNum++);
						writeIntoCell(detailsRow, enqMonthYear, cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLastName(), cellNum++);
						writeIntoCell(detailsRow,  dmsContactDto!=null?dmsContactDto.getPhone():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEmail():"", cellNum++);
					
						cellNum = addSingleAddress(addressList,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getModel():"", cellNum++);
						cellNum = addDmsLeadProducts(dmsLeadProductDtoList,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSourceOfEnquiry():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSubSource():"", cellNum++);
						
						if(sheetName.equalsIgnoreCase("Test Drive")) {
							writeIntoCell(detailsRow, dmsWFTask!=null?dmsWFTask.getEntityName():"", cellNum++);
							writeIntoCell(detailsRow, dmsWFTask!=null?dmsWFTask.getTaskStatus():"", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);//Test drive veh. Reg.No
							writeIntoCell(detailsRow, "", cellNum++);//Test drive Model");
							writeIntoCell(detailsRow, "", cellNum++);//Test drive variant
						}
						
						if(sheetName.equalsIgnoreCase("Home Visit")) {
							writeIntoCell(detailsRow, dmsWFTask!=null?dmsWFTask.getTaskStatus():"", cellNum++);
						}
						
						//
							writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSalesConsultant():"", cellNum++); 
							String empId =getEmpName(dmsLeadDto.getSalesConsultant());
							writeIntoCell(detailsRow, getEmpName(dmsLeadDto.getSalesConsultant()), cellNum++); 
							String teamLeadName = getTeamLead(empId);
							writeIntoCell(detailsRow, teamLeadName, cellNum++); // Team Leader
							writeIntoCell(detailsRow, getManager(teamLeadName), cellNum++); // Manager

					
							writeIntoCell(detailsRow, dmsWFTask!=null?dmsWFTask.getEmployeeRemarks():"", cellNum++);
					}
				}
			}
				FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
				workbook.write(out);
				out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}

	private Object getMonthAndYear(String taskActualStartTime) {
		// TODO Auto-generated method stub
		return null;
	}

	
	
	private String zipFiles(List<File> fileList) {
		String zipFileName = tmpPath + "ETVBRL_" + System.currentTimeMillis() + ".zip";
		try {

			FileOutputStream fos = new FileOutputStream(zipFileName);
			ZipOutputStream zos = new ZipOutputStream(fos);

			for (File file : fileList) {
				if (null != file && file.exists()) {
					FileInputStream fis = new FileInputStream(file);

					ZipEntry zipEntry = new ZipEntry(file.getName());
					zos.putNextEntry(zipEntry);

					byte[] bytes = new byte[1024];
					int length;
					while ((length = fis.read(bytes)) >= 0) {
						zos.write(bytes, 0, length);
					}

					zos.closeEntry();
					fis.close();
				}

			}

			zos.close();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception ", e);
		}

		return zipFileName;

	}
	

	private void genearateExcelForDelivery(List<DMSResponse> dmsResponseDeliveryList,
			List<LeadStageRefEntity> leadRefDBList, String fileName) {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Delivery");
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = getDeliveryRowHeaders();
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
			for (DMSResponse res : dmsResponseDeliveryList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if(null!=dmsEntity) {
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto =dmsEntity.getDmsContactDto();
					DmsAccountDto dmsAccountDto = dmsEntity.getDmsAccountDto();		
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList =  dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts(); 
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					List<DmsAttachmentDto> dmsAttachments = dmsLeadDto.getDmsAttachments();
					List<DmsAccessoriesDto> dmsAccessoriesList = dmsLeadDto.getDmsAccessories();
					
					DmsBookingDto dmsBookingDto = dmsLeadDto.getDmsBooking();
					DMSResponse dmsResponseOnRoadPrice =null;
					List<DmsExchangeBuyer> dmsExchangeBuyerList = null;
					List<DmsDelivery> deliveryList = null;
					DmsDelivery dmsDelivery = null;
					List<DmsInvoice> dmsInvoiceList = null;
					DmsInvoice dmsInvoice=null;
					if (dmsLeadDto != null ) {
						int leadId = dmsLeadDto.getId();
						
						
						log.debug("leadId:::"+leadId);
						try {
							dmsResponseOnRoadPrice = restTemplate.getForEntity(leadOnRoadPriceUrl+leadId, DMSResponse.class).getBody();
						}catch(Exception e) {
							e.printStackTrace();
						}
						try {
							dmsExchangeBuyerList=  dmsExchangeBuyerDao.getDmsExchangeBuyersByLeadId(leadId);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
						
						try {
							deliveryList = dmsDeliveryDao.getDeliveriesWithLeadId(leadId);			
							if(null!=deliveryList && !deliveryList.isEmpty()) {
								dmsDelivery = deliveryList.get(0);
							}
						}catch(Exception e) {
							e.printStackTrace();
							log.error("e",e);
						}
						List<LeadStageRefEntity> leadRefList = leadRefDBList.stream()
								.filter(x -> x.getLeadId() != null && (x.getLeadId()) == leadId)
								.collect(Collectors.toList());
						LeadStageRefEntity leadRef = new LeadStageRefEntity();
						if (null != leadRefList && !leadRefList.isEmpty()) {
							leadRef = leadRefList.get(0);
						}
						log.debug("leadRef::"+leadRef);
						Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
						String branchName = "";
						if (optBranch.isPresent()) {
							DmsBranch branch = optBranch.get();
							branchName = branch.getName();
						}
						
						try {
							dmsInvoiceList= dmsInvoiceDao.getInvoiceDataWithLeadId(leadId);
							if(dmsInvoiceList!=null && !dmsInvoiceList.isEmpty()) {
								dmsInvoice = dmsInvoiceList.get(0);
							}
						}catch(Exception e) {
							e.printStackTrace();
							
						}
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, leadRef.getStartDate(), cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(leadRef.getStartDate()), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getSalutation():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLastName(), cellNum++);
						writeIntoCell(detailsRow,  dmsContactDto!=null?dmsContactDto.getPhone():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEmail():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getDateOfBirth():"", cellNum++);
						cellNum = addSingleAddress(addressList,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getModel():"", cellNum++);
						cellNum = addDmsLeadProducts(dmsLeadProductDtoList,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getVinNo():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getEngineNo():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getChassisNo():"", cellNum++);
						
						
						cellNum = addAttachments(dmsAttachments,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getGstNumber():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getLeadId():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCreatedDate():"", cellNum++);
						
						writeIntoCell(detailsRow, getRefNo(leadRef.getLeadId(),ENQUIRY), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEnquiryDate():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEnquirySegment():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getCustomerCategoryType():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSourceOfEnquiry():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSubSource():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCorporateName():"", cellNum++);
						cellNum = addEventDetails(detailsRow,cellNum,leadId,dmsLeadDto.getEventCode());
						
						writeIntoCell(detailsRow, buildRetailToDeliveryConvesionDays(leadId,"INVOICE","DELIVERY"), cellNum++);//Retail to Delivery Conversion days
						
						
						cellNum = addBookingDetails(dmsResponseOnRoadPrice,dmsBookingDto,detailsRow,cellNum,dmsInvoice);
						writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getInvoiceDate():"",cellNum++); //Vehicle Purchase Date
						writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getTotalAmount():"", cellNum++); //Vehicle Purchase Amount
						if(null!=dmsExchagedetailsList && dmsExchagedetailsList.isEmpty()) {
							writeIntoCell(detailsRow, "NA", cellNum++);
						}else {
							writeIntoCell(detailsRow, "Yes", cellNum++);
						}
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getBuyerType():"", cellNum++);  
						
						final List<Map<String, Object>> evalutionList  = buildEvaluation (dmsLeadDto.getCrmUniversalId());
						
						String evalId="";
						Timestamp evalDate=null;
						for(Map<String,Object> m : evalutionList) {
							evalId =""+(Integer)m.get("evalutor_id");
							evalDate =(Timestamp)m.get("updated_date");
						}
						writeIntoCell(detailsRow, evalId, cellNum++);  //Evaluation ID
						writeIntoCell(detailsRow, evalDate!=null?evalDate.toString():"",cellNum++);  //Evaluation Date

						if (dmsExchagedetailsList != null && !dmsExchagedetailsList.isEmpty()) {
							DmsExchangeBuyerDto a1 = dmsExchagedetailsList.get(0);
							if (null != a1) {
								writeIntoCell(detailsRow, a1.getRegNo(), cellNum++);
								writeIntoCell(detailsRow, a1.getBrand(), cellNum++);
								writeIntoCell(detailsRow, a1.getModel(), cellNum++);
								writeIntoCell(detailsRow, a1.getVarient(), cellNum++);
								writeIntoCell(detailsRow, ""+a1.getOfferedPrice(), cellNum++);
							} else {
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								
							}
						} else {
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
						}
						
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getRelationName():"", cellNum++);
						
						writeIntoCell(detailsRow, "", cellNum++);//EW status
						writeIntoCell(detailsRow, "", cellNum++);//EW Number
						writeIntoCell(detailsRow, "", cellNum++);//EW Start date
						writeIntoCell(detailsRow, "", cellNum++);//EW End date"
						
						
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getInsuranceType():"", cellNum++);
						
						String insuranceNumber="";
						String insuranceCompany="";
						String insurStartDate="";
						String insurEndDate="";
						String insurPremium="";
						if(dmsDelivery!=null) {
							insuranceNumber = dmsDelivery.getInsurancePolicyNo();
							insuranceCompany = dmsDelivery.getInsuranceCompany();
							insurStartDate = dmsDelivery.getInsuranceDate()!=null?dmsDelivery.getInsuranceDate().toString():"";
							insurEndDate = dmsDelivery.getInsurenceExpDate()!=null?dmsDelivery.getInsurenceExpDate().toString():"";
						
						}
						writeIntoCell(detailsRow, insuranceNumber, cellNum++);//Insurane Number
						writeIntoCell(detailsRow, insuranceCompany, cellNum++);//Insurance Company
						writeIntoCell(detailsRow, insurStartDate, cellNum++);//Insurance Start date
						writeIntoCell(detailsRow, insurEndDate, cellNum++);//Insurance End date
						writeIntoCell(detailsRow, insurPremium, cellNum++);//Insurance premium
						
						Double accessoiresAmt =0D;
						String parts="";
						if(dmsAccessoriesList!=null && !dmsAccessoriesList.isEmpty()) {
							 DmsAccessoriesDto a =  dmsAccessoriesList.get(0);
							 for(DmsAccessoriesDto d :dmsAccessoriesList) {
								 accessoiresAmt = accessoiresAmt+d.getAmount();
								 parts = parts+","+a.getAccessoriesName();
							 }
							
						}
						
						 writeIntoCell(detailsRow, accessoiresAmt, cellNum++);
						 writeIntoCell(detailsRow, parts, cellNum++);
						
						 String finCategory="";
						 String finName="";
						 String finBranch="";
						 String finLoanAMt="";
						 String finRoI="";
						 String loanTenure="";
						 String emiAmt="";
						 String payout="";
						 String netpayout="";
						 if(dmsFinanceDetailsList!=null && !dmsFinanceDetailsList.isEmpty()) {
								DmsFinanceDetailsDto  dmsFinanceDetailsDto= dmsFinanceDetailsList.get(0);
								finCategory=dmsFinanceDetailsDto.getFinanceCategory();
								finName=dmsFinanceDetailsDto.getFinanceCompany();
								finBranch=dmsFinanceDetailsDto.getFinanceCompany();
								finLoanAMt=""+dmsFinanceDetailsDto.getLoanAmount();
								finRoI=dmsFinanceDetailsDto.getRateOfInterest();
								loanTenure=dmsFinanceDetailsDto.getExpectedTenureYears();
								emiAmt=""+dmsFinanceDetailsDto.getEmi();
								payout=""+dmsFinanceDetailsDto.getLoanAmount();
								netpayout=""+dmsFinanceDetailsDto.getLoanAmount();
								
						 }
						 writeIntoCell(detailsRow, finCategory, cellNum++);
						 writeIntoCell(detailsRow, finName, cellNum++);
						 writeIntoCell(detailsRow, finBranch, cellNum++);
						 writeIntoCell(detailsRow, finLoanAMt, cellNum++);
						 writeIntoCell(detailsRow, finRoI, cellNum++);
						 writeIntoCell(detailsRow, loanTenure, cellNum++);
						 writeIntoCell(detailsRow, emiAmt, cellNum++);
						 writeIntoCell(detailsRow, payout, cellNum++);
						 writeIntoCell(detailsRow, netpayout, cellNum++);
						 
						 
						 writeIntoCell(detailsRow, "", cellNum++);  //Disbursed date
						 writeIntoCell(detailsRow, "", cellNum++);  //Disbursed amount
						 
						 writeIntoCell(detailsRow, "", cellNum++);  //Payment ref number
						 
							String cancelDate ="";
							String lostReason="";
							String lostSubReason="";
							if (dmsLeadDto.getLeadStage().equalsIgnoreCase(DROPPED)) {
								List<DmsLeadDrop> dropList = dmsLeadDropDao.getByLeadId(leadId);
								if (dropList != null && !dropList.isEmpty()) {
									DmsLeadDrop droppedLead = dropList.get(0);
									cancelDate = droppedLead.getCreatedDateTime();
									lostReason = droppedLead.getLostReason();
									lostSubReason = droppedLead.getLostSubReason();
								}
							}
							  writeIntoCell(detailsRow, cancelDate, cellNum++);
							  writeIntoCell(detailsRow, lostReason, cellNum++);
							  writeIntoCell(detailsRow, lostSubReason, cellNum++);
							
						
							writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSalesConsultant():"", cellNum++); 
							String empId =getEmpName(dmsLeadDto.getSalesConsultant());
							writeIntoCell(detailsRow, getEmpName(dmsLeadDto.getSalesConsultant()), cellNum++); 
							String teamLeadName = getTeamLead(empId);
							writeIntoCell(detailsRow, teamLeadName, cellNum++); // Team Leader
							writeIntoCell(detailsRow, getManager(teamLeadName), cellNum++); // Manager

							writeIntoCell(detailsRow, "", cellNum++);   //Finance Executive
							writeIntoCell(detailsRow, "", cellNum++);  //Last Remarks
					}
				}
			}
				FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
				workbook.write(out);
				out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static String quote(String s) {
	    return new StringBuilder()
	        .append('\'')
	        .append(s)
	        .append('\'')
	        .toString();
	}

	private List<Map<String, Object>> buildEvaluation(String crmUniversalId) {
		String q = "select * from ops.vehicle_evalutions where customer_id=";
		q =q+quote(crmUniversalId);
		log.debug("vehicle_evalutions query :"+q );
		
		
		List<Object[]> colnHeadersList = new ArrayList<>();
		colnHeadersList = entityManager.createNativeQuery("DESCRIBE ops.vehicle_evalutions").getResultList();
		
		
		List<String> headers = new ArrayList<>();
		for (Object[] arr : colnHeadersList) {

			String colName = (String) arr[0];
			headers.add(colName);
		
		}

		final List<Map<String, Object>> jObjList = new ArrayList<>();
		
		List<Object[]> queryResults = entityManager.createNativeQuery(q).getResultList();
		
		for (int i = 0; i < queryResults.size(); i++) {
			Object[] objArr = queryResults.get(i);
			Map<String, Object> map = new LinkedHashMap<>();
			for (int j = 0; j < objArr.length; j++) {
				String colName = headers.get(j);
				map.put(colName, objArr[j]);
			}
			jObjList.add(map);
		}
		return jObjList;
	}
	
	private List<Map<String, Object>> buildEvents(String eventId) {
		String q = "select * from ops.event_details where event_id=";
		q =q+quote(eventId);
		log.debug("buildEvents query :"+q );
		
		
		List<Object[]> colnHeadersList = new ArrayList<>();
		colnHeadersList = entityManager.createNativeQuery("DESCRIBE ops.event_details").getResultList();
		
		
		List<String> headers = new ArrayList<>();
		for (Object[] arr : colnHeadersList) {

			String colName = (String) arr[0];
			headers.add(colName);
		
		}

		final List<Map<String, Object>> jObjList = new ArrayList<>();
		
		List<Object[]> queryResults = entityManager.createNativeQuery(q).getResultList();
		
		for (int i = 0; i < queryResults.size(); i++) {
			Object[] objArr = queryResults.get(i);
			Map<String, Object> map = new LinkedHashMap<>();
			for (int j = 0; j < objArr.length; j++) {
				String colName = headers.get(j);
				map.put(colName, objArr[j]);
			}
			jObjList.add(map);
		}
		return jObjList;
	}

	private Object buildRetailToDeliveryConvesionDays(int leadId,String stage1,String stage2) {
		// TODO Auto-generated method stub
		List<LeadStageRefEntity> list=  leadStageRefDao.findLeadsByLeadId(leadId);
		
		Timestamp ts1=null;
		Timestamp ts2=null;
		if(list!=null && !list.isEmpty()) {
			for(LeadStageRefEntity ref :list) {
				
				if(ref.getStageName().equals(stage1)) {
					ts1 = ref.getStartDate();
				}
				if(ref.getStageName().equals(stage2)) {
					ts2 = ref.getStartDate();
				}
			}
		}
		long diff=0L;
		System.out.println("ts1 "+ts1+" ts2"+ts2);
		
		if(ts1!=null && ts2!=null) {
		 diff = ts2.getTime() - ts1.getTime();
		 diff = TimeUnit.MILLISECONDS.toDays(diff);
		}
		return diff;
	}
	




	private Object getRefNo(Integer leadId, String stage) {
		String refNo ="";
		try {
			refNo = leadStageRefDao.findRefByLeadIdStge(leadId,stage);
		}catch(Exception e) {
			e.printStackTrace();
		}
		return refNo;
	}

	private void genearateExcelForInvoice(List<DMSResponse> dmsResponseInvoiceList,
			List<LeadStageRefEntity> leadRefDBList, String fileName) {
		
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Invoice");
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = getRetailRowHeaders();
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
			for (DMSResponse res : dmsResponseInvoiceList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if(null!=dmsEntity) {
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto =dmsEntity.getDmsContactDto();
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList =  dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts(); 
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					List<DmsAttachmentDto> dmsAttachments = dmsLeadDto.getDmsAttachments();
					
					DmsBookingDto dmsBookingDto = dmsLeadDto.getDmsBooking();
					DMSResponse dmsResponseOnRoadPrice =null;
					
					List<DmsInvoice> dmsInvoiceList = null;
					DmsInvoice dmsInvoice=null;
					if (dmsLeadDto != null ) {
						int leadId = dmsLeadDto.getId();
						
						try {
							dmsResponseOnRoadPrice = restTemplate.getForEntity(leadOnRoadPriceUrl+leadId, DMSResponse.class).getBody();
						}catch(Exception e) {
							e.printStackTrace();
						}
					
						List<LeadStageRefEntity> leadRefList = leadRefDBList.stream()
								.filter(x -> x.getLeadId() != null && (x.getLeadId()) == leadId)
								.collect(Collectors.toList());
						LeadStageRefEntity leadRef = new LeadStageRefEntity();
						if (null != leadRefList && !leadRefList.isEmpty()) {
							leadRef = leadRefList.get(0);
						}
						log.debug("leadRef::"+leadRef);
						Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
						String branchName = "";
						if (optBranch.isPresent()) {
							DmsBranch branch = optBranch.get();
							branchName = branch.getName();
						}
						try {
							dmsInvoiceList= dmsInvoiceDao.getInvoiceDataWithLeadId(leadId);
							if(dmsInvoiceList!=null && !dmsInvoiceList.isEmpty()) {
								dmsInvoice = dmsInvoiceList.get(0);
							}
						}catch(Exception e) {
							e.printStackTrace();
							
						}
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, leadRef.getStartDate(), cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(leadRef.getStartDate()), cellNum++);
						writeIntoCell(detailsRow,  leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getSalutation():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLastName(), cellNum++);
						writeIntoCell(detailsRow,  dmsContactDto!=null?dmsContactDto.getPhone():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEmail():"", cellNum++);
						
						cellNum = addSingleAddress(addressList,detailsRow,cellNum);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getModel():"", cellNum++);
						cellNum = addDmsLeadProducts(dmsLeadProductDtoList,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getVinNo():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getEngineNo():"", cellNum++);
						
						
						cellNum = addAttachments(dmsAttachments,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getGstNumber():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getId():"", cellNum++);//Booking ID
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCreatedDate():"", cellNum++);//Booking Date
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEnquiryDate():"", cellNum++); //Enquiry ID
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEnquiryDate():"", cellNum++);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEnquirySegment():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getCustomerCategoryType():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSourceOfEnquiry():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSubSource():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCorporateName():"", cellNum++);

						cellNum = addEventDetails(detailsRow,cellNum,leadId,dmsLeadDto.getEventCode());
						writeIntoCell(detailsRow,buildRetailToDeliveryConvesionDays(leadId, "BOOKING", "RETAIL"), cellNum++);  //Booking to Retail Conversion days
						
						cellNum = addBookingDetails(dmsResponseOnRoadPrice,dmsBookingDto,detailsRow,cellNum,dmsInvoice);
				
						if(dmsFinanceDetailsList!=null && !dmsFinanceDetailsList.isEmpty()) {
							DmsFinanceDetailsDto  dmsFinanceDetailsDto= dmsFinanceDetailsList.get(0);		
							writeIntoCell(detailsRow, dmsFinanceDetailsDto.getFinanceCategory(),cellNum++);
							writeIntoCell(detailsRow, dmsFinanceDetailsDto.getFinanceCompany(),cellNum++);
						}else {
							writeIntoCell(detailsRow, "",cellNum++);
							writeIntoCell(detailsRow, "",cellNum++);
						}
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getBuyerType():"", cellNum++);
						
						//
						
						
						if (dmsExchagedetailsList != null && !dmsExchagedetailsList.isEmpty()) {
							DmsExchangeBuyerDto a1 = dmsExchagedetailsList.get(0);
							if (null != a1) {
								writeIntoCell(detailsRow, a1.getRegNo(), cellNum++);
								writeIntoCell(detailsRow, a1.getBrand(), cellNum++);
								writeIntoCell(detailsRow, a1.getModel(), cellNum++);
								writeIntoCell(detailsRow, a1.getVarient(), cellNum++);
								writeIntoCell(detailsRow, ""+a1.getOfferedPrice(), cellNum++);
							} else {
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
								
							}
						} else {
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);
						}
						
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getRelationName():"",cellNum++); // relation with customer
						String cancelDate ="";
						String lostReason="";
						String lostSubReason="";
						if (dmsLeadDto.getLeadStage().equalsIgnoreCase(DROPPED)) {
							List<DmsLeadDrop> dropList = dmsLeadDropDao.getByLeadId(leadId);
							if (dropList != null && !dropList.isEmpty()) {
								DmsLeadDrop droppedLead = dropList.get(0);

								cancelDate = droppedLead.getCreatedDateTime();
								lostReason = droppedLead.getLostReason();
								lostSubReason = droppedLead.getLostSubReason();

							}
						}
						
						  writeIntoCell(detailsRow, cancelDate, cellNum++);
						  writeIntoCell(detailsRow, lostReason, cellNum++);
						  writeIntoCell(detailsRow, lostSubReason, cellNum++);
						
					
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSalesConsultant():"", cellNum++); 
						String empId =getEmpName(dmsLeadDto.getSalesConsultant());
						writeIntoCell(detailsRow, getEmpName(dmsLeadDto.getSalesConsultant()), cellNum++); 
						writeIntoCell(detailsRow, getTeamLead(empId), cellNum++);  //Team Leader
						writeIntoCell(detailsRow, getManager(empId), cellNum++);   //Manager
						writeIntoCell(detailsRow, "", cellNum++);   //Finance Executive
						writeIntoCell(detailsRow, "", cellNum++);  //evaluator name
						
						
					}
				}
			}
				FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
				workbook.write(out);
				out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int addBookingDetails(DMSResponse dmsResponseOnRoadPrice, DmsBookingDto dmsBookingDto, Row detailsRow, int cellNum, DmsInvoice dmsInvoice) {
		String exShowroomPrice="";
		String tcs="";
		String corporateOffer="";
		String specialOffer="";
		String exchnageOffer="";
		String additionalOffer1="";
		String additionalOffer2="";
		String cashDiscount="";
		if(dmsResponseOnRoadPrice!=null) {
			if(null!=dmsResponseOnRoadPrice.getDmsEntity()) {
				DmsOnRoadPriceDto dmsOnRoadPriceDto =   dmsResponseOnRoadPrice.getDmsEntity().getDmsOnRoadPriceDto();
				if(dmsOnRoadPriceDto!=null) {
					exShowroomPrice = ""+dmsOnRoadPriceDto.getExShowroomPrice();
					tcs = ""+dmsOnRoadPriceDto.getTcs();
					corporateOffer =""+ dmsOnRoadPriceDto.getCorporateOffer();
					specialOffer =""+dmsOnRoadPriceDto.getSpecialScheme();	
					exchnageOffer =""+dmsOnRoadPriceDto.getExchangeOffers();
					cashDiscount = ""+dmsOnRoadPriceDto.getCashDiscount();
					additionalOffer1 = ""+dmsOnRoadPriceDto.getAdditionalOffer1();
					additionalOffer2 = ""+dmsOnRoadPriceDto.getAdditionalOffer2();
				}
			}
		
		}
		writeIntoCell(detailsRow, exShowroomPrice, cellNum++);
		writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getStateType():"", cellNum++);//GST Type
		writeIntoCell(detailsRow, "14%", cellNum++);//CGST %
		writeIntoCell(detailsRow, "14^", cellNum++);//SGST %
		writeIntoCell(detailsRow, "14%", cellNum++);//IGST %
		writeIntoCell(detailsRow, "14%", cellNum++);//UTGST %
		writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getCessPercentage():"", cellNum++);//CESS %
		writeIntoCell(detailsRow, tcs, cellNum++);//TCS %
		writeIntoCell(detailsRow, tcs, cellNum++); 
		writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getGst_rate():"", cellNum++);//Total GST % %
	
		writeIntoCell(detailsRow, "", cellNum++);  //consumerOffer
		writeIntoCell(detailsRow, exchnageOffer, cellNum++);
		writeIntoCell(detailsRow, corporateOffer, cellNum++);
		writeIntoCell(detailsRow, specialOffer, cellNum++);
		writeIntoCell(detailsRow, additionalOffer1, cellNum++);
		writeIntoCell(detailsRow, additionalOffer2, cellNum++);
		writeIntoCell(detailsRow, cashDiscount, cellNum++);
		writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getFocAccessories():"", cellNum++);
		writeIntoCell(detailsRow, dmsInvoice!=null?dmsInvoice.getTotalAmount():"", cellNum++);//INVOICE AMNT
	
		return cellNum;
	}

	private int addEventDetails(Row detailsRow, int cellNum, int leadId, String eventCode) {
		final List<Map<String, Object>> evalutionList = buildEvents(eventCode);
		String evtName = "";
		String evtId = "";
		String evtStartDt = "";
		String evtEndDt = "";
		String evtCategory = "";
		for (Map<String, Object> m : evalutionList) {
			evtName = (String) m.get("name");
			evtId = (String) m.get("event_id");
			evtStartDt = (String) m.get("startdate");
			evtEndDt = (String) m.get("enddate");
			evtCategory = (String) m.get("category_id");
		}
		writeIntoCell(detailsRow, evtName, cellNum++); // Event name
		writeIntoCell(detailsRow, evtId, cellNum++); // Event ID
		writeIntoCell(detailsRow, evtStartDt, cellNum++); // Event Start Date
		writeIntoCell(detailsRow, evtEndDt, cellNum++); // Event End date
		writeIntoCell(detailsRow, evtCategory, cellNum++); // Event Cateogry

		return cellNum;
	}

	private List<LeadStageRefEntity> removeDuplicates(List<LeadStageRefEntity> leadRefDBListBooking,
			List<LeadStageRefEntity> leadRefDBInvoice) {
		log.debug("leadRefDBInvoice size "+leadRefDBInvoice.size() +", leadRefDBInvoice :"+leadRefDBInvoice);	
		log.debug("leadRefDBListBooking size "+leadRefDBListBooking.size() +", leadRefDBListBooking :"+leadRefDBListBooking);	
		List<LeadStageRefEntity> leadRefDBLiveBookingList = new ArrayList<>();
		
		for(LeadStageRefEntity book: leadRefDBListBooking) {
			String bookID = String.valueOf(book.getLeadId());
			System.out.println("book "+bookID);
			
			//int size = leadRefDBInvoice.stream().filter(x->x.getLeadId()==book.getLeadId()).collect(Collectors.toList()).size();
			for(LeadStageRefEntity inv: leadRefDBInvoice) {
				String invId = String.valueOf(inv.getLeadId());
				System.out.println("invId "+invId);
				if(bookID.equals(invId)) {
					System.out.println("equals true");
				}else {
					System.out.println("equals false");
					leadRefDBLiveBookingList.add(book);
				}
			}
		}
		
		return leadRefDBLiveBookingList;
	}

	private void genearateExcelForBooking(List<DMSResponse> dmsResponseBookingList,
			List<LeadStageRefEntity> leadRefDBList, String fileName,String sheetName) {

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = getBookingRowHeaders();
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
			for (DMSResponse res : dmsResponseBookingList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if(null!=dmsEntity) {
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto =dmsEntity.getDmsContactDto();
					DmsAccountDto dmsAccountDto = dmsEntity.getDmsAccountDto();		
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList =  dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts(); 
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					List<DmsAttachmentDto> dmsAttachments = dmsLeadDto.getDmsAttachments();
					List<DmsAccessoriesDto> dmsAccessoriesList = dmsLeadDto.getDmsAccessories();
					
					DmsBookingDto dmsBookingDto = dmsLeadDto.getDmsBooking();
					DMSResponse dmsResponseOnRoadPrice =null;
					List<DmsExchangeBuyer> dmsExchangeBuyerList = null;
					if (dmsLeadDto != null ) {
						int leadId = dmsLeadDto.getId();
						
						try {
							dmsResponseOnRoadPrice = restTemplate.getForEntity(leadOnRoadPriceUrl+leadId, DMSResponse.class).getBody();
						}catch(Exception e) {
							e.printStackTrace();
						}
						try {
							dmsExchangeBuyerList=  dmsExchangeBuyerDao.getDmsExchangeBuyersByLeadId(leadId);
						}
						catch(Exception e) {
							e.printStackTrace();
						}
						List<LeadStageRefEntity> leadRefList = leadRefDBList.stream()
								.filter(x -> x.getLeadId() != null && (x.getLeadId()) == leadId)
								.collect(Collectors.toList());
						LeadStageRefEntity leadRef = new LeadStageRefEntity();
						if (null != leadRefList && !leadRefList.isEmpty()) {
							leadRef = leadRefList.get(0);
						}
						log.debug("leadRef::"+leadRef);
						Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
						String branchName = "";
						if (optBranch.isPresent()) {
							DmsBranch branch = optBranch.get();
							branchName = branch.getName();
						}
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, leadRef.getStartDate(), cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(leadRef.getStartDate()), cellNum++);
						writeIntoCell(detailsRow,  leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getSalutation():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLastName(), cellNum++);
						writeIntoCell(detailsRow,  dmsContactDto!=null?dmsContactDto.getPhone():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEmail():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getDateOfBirth():"", cellNum++);
						cellNum = addSingleAddress(addressList,detailsRow,cellNum);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getModel():"", cellNum++);
				
						cellNum = addDmsLeadProducts(dmsLeadProductDtoList,detailsRow,cellNum);
						
						cellNum = addAttachments(dmsAttachments,detailsRow,cellNum);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getGstNumber():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getInsuranceType():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getAddOnCovers():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getWarranty():"", cellNum++);
						
						if(dmsAccessoriesList!=null && !dmsAccessoriesList.isEmpty()) {
							DmsAccessoriesDto  dmsAccessoriesDto= dmsAccessoriesList.get(0);		
							writeIntoCell(detailsRow, dmsAccessoriesDto.getAmount(),cellNum++);
						}else {
							writeIntoCell(detailsRow, "",cellNum++);
						}
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getFocAccessories():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCashDiscount():"", cellNum++);
						cellNum = addOnRoadPriceDetails(detailsRow,cellNum,dmsLeadDto.getId(),dmsResponseOnRoadPrice);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getReceivedBookingAmount():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getModeOfPayment():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getBookingAmount():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getTotalPaid():"", cellNum++);
						writeIntoCell(detailsRow, "", cellNum++);//Pending Amount
						
						if(dmsFinanceDetailsList!=null && !dmsFinanceDetailsList.isEmpty()) {
							DmsFinanceDetailsDto  dmsFinanceDetailsDto= dmsFinanceDetailsList.get(0);		
							writeIntoCell(detailsRow, dmsFinanceDetailsDto.getFinanceCategory(),cellNum++);
						}else {
							writeIntoCell(detailsRow, "",cellNum++);
						}
						
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCreatedDate():"", cellNum++);//Vehicle Allocation Date
						writeIntoCell(detailsRow, "", cellNum++);//Vehicle Allocation Age
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getVinNo():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getDmsExpectedDeliveryDate():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getCommitmentDeliveryPreferredDate():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getDeliveryLocation():"", cellNum++);
						writeIntoCell(detailsRow, buildRetailToDeliveryConvesionDays(leadId, "ENQUIRY", "BOOKING"), cellNum++);//Enquiry to booking Conversion days
						writeIntoCell(detailsRow, "", cellNum++);//Booking age
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEnquiryDate():"", cellNum++);
						writeIntoCell(detailsRow, leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEnquirySegment():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getCustomerCategoryType():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSourceOfEnquiry():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSubSource():"", cellNum++);
						writeIntoCell(detailsRow, dmsBookingDto!=null?dmsBookingDto.getCorporateName():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEventCode():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEventCode():"", cellNum++);  //Event Category
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getBuyerType():"", cellNum++);  
						
						if (dmsExchagedetailsList != null && !dmsExchagedetailsList.isEmpty()) {
							DmsExchangeBuyerDto a1 = dmsExchagedetailsList.get(0);
							if (null != a1) {
								writeIntoCell(detailsRow, a1.getRegNo(), cellNum++);
								writeIntoCell(detailsRow, a1.getModel(), cellNum++);
							} else {
								writeIntoCell(detailsRow, "", cellNum++);
								writeIntoCell(detailsRow, "", cellNum++);
							}
						} else {

							writeIntoCell(detailsRow, "", cellNum++);
							writeIntoCell(detailsRow, "", cellNum++);

						}
						

						final List<Map<String, Object>> evalutionList  = buildEvaluation (dmsLeadDto.getCrmUniversalId());
						
						String evalName="";
						Timestamp evalDate=null;
						String evalNumber="";
						String evalStatus="";
						String custExpectedPrice="";
						for(Map<String,Object> m : evalutionList) {
							
							evalName = ""+(Integer)m.get("evalutor_id");
							evalDate =(Timestamp)m.get("updated_date");;
							evalNumber = m.get("mobile_num")!=null?(String)m.get("mobile_num"):"";
							evalStatus = m.get("evalution_status")!=null?(String)m.get("evalution_status"):"";
							custExpectedPrice = m.get("cust_expected_price")!=null?(String)m.get("cust_expected_price"):"";
						}
						writeIntoCell(detailsRow, evalName, cellNum++);  //Evaluation ID
						writeIntoCell(detailsRow, evalDate!=null?evalDate.toString():"",cellNum++);  //Evaluation Date
						writeIntoCell(detailsRow, evalNumber, cellNum++);  //Evaluation ID
						writeIntoCell(detailsRow, evalStatus, cellNum++); 
						writeIntoCell(detailsRow, custExpectedPrice, cellNum++);  //Customer Exp. Price
						
				
						
						writeIntoCell(detailsRow, "", cellNum++);  //Customer Exp. Price
						
						String offredPrice ="";
						String finalPrice="";
						String exchangeStatus="";
				
						if (null != dmsExchangeBuyerList && !dmsExchangeBuyerList.isEmpty()) {
							DmsExchangeBuyer dmsExchangeBuyer = dmsExchangeBuyerList.get(0);
							if (dmsExchangeBuyer != null) {
								offredPrice = "" + dmsExchangeBuyer.getOfferedPrice();
								finalPrice = "" + dmsExchangeBuyer.getFinalPrice();
								exchangeStatus = dmsExchangeBuyer.getEvaluationStatus();
							}
						}
						writeIntoCell(detailsRow, offredPrice, cellNum++);  //Offered Price
						writeIntoCell(detailsRow, finalPrice, cellNum++); //Approved Price
						writeIntoCell(detailsRow, exchangeStatus, cellNum++);  //Exchnage price Approval status
						
						String cancelDate ="";
						String lostReason="";
						String lostSubReason="";
						if(dmsLeadDto.getLeadStage().equalsIgnoreCase(DROPPED)) {
							  List<DmsLeadDrop> dropList = dmsLeadDropDao.getByLeadId(leadId);
							  if(dropList!=null && !dropList.isEmpty()) {
								  DmsLeadDrop droppedLead =dropList.get(0);
							
								  cancelDate = droppedLead.getCreatedDateTime();
								  lostReason = droppedLead.getLostReason();
								  lostSubReason=droppedLead.getLostSubReason();
								  
								
							  }
								
						}
						
						  writeIntoCell(detailsRow, cancelDate, cellNum++);
						  writeIntoCell(detailsRow, lostReason, cellNum++);
						  writeIntoCell(detailsRow, lostSubReason, cellNum++);
						
					
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSalesConsultant():"", cellNum++); 
						String empId =getEmpName(dmsLeadDto.getSalesConsultant());
						writeIntoCell(detailsRow, getEmpName(dmsLeadDto.getSalesConsultant()), cellNum++); 
						writeIntoCell(detailsRow, getTeamLead(empId), cellNum++);  //Team Leader
						writeIntoCell(detailsRow, getManager(empId), cellNum++);   //Manager
						writeIntoCell(detailsRow, "", cellNum++);   //Finance Executive
						writeIntoCell(detailsRow, "", cellNum++);  //Last Remarks
						
						
					}
				}
			}
				FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
				workbook.write(out);
				out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	
		
	}

	private Object getManager(String teamLeadName) {
		String mgrId ="";
		if(teamLeadName!=null ) {
			String teamLeadId = getEmpName(teamLeadName);
			mgrId = getTeamLead(teamLeadId);
		}
		return mgrId;
	}
	public String getEmpNameWithEmpID(String id) {
		String res = null;
		String empNameQuery = "SELECT emp_name FROM dms_employee where emp_id=<ID>;";
		try {
			if (null != id && !id.equalsIgnoreCase("string")) {
				Object obj = entityManager.createNativeQuery(empNameQuery.replaceAll("<ID>", id)).getSingleResult();
				res = (String) obj;
			} else {
				res = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	private String getTeamLead(String empId) {
		String teamLead = "";
		String reportingTo ="";
		if(empId!=null ) {
			Optional<DmsEmployee> opt = dmsEmployeeRepo.findById(Integer.valueOf(empId));
			if(opt.isPresent()) {
				reportingTo  = opt.get().getReportingTo();
			}
		}
		if (reportingTo != null && !reportingTo.isEmpty()) {
			teamLead = getEmpNameWithEmpID(reportingTo);
		}
		
		return teamLead;
	}

	private int addOnRoadPriceDetails(Row detailsRow, int cellNum, int id,DMSResponse dmsResponseOnRoadPrice) {
		// TODO Auto-generated method stub
		
		String exShowroomPrice="";
		String onRoadPrice="";
	
		if(dmsResponseOnRoadPrice!=null) {
			DmsEntity dmsEntity = dmsResponseOnRoadPrice.getDmsEntity();
			if(null!=dmsEntity) {
				DmsOnRoadPriceDto dmsOnRoadPriceDto =   dmsEntity.getDmsOnRoadPriceDto();
				if(dmsOnRoadPriceDto!=null) {
					exShowroomPrice = ""+dmsOnRoadPriceDto.getExShowroomPrice();
					onRoadPrice = ""+dmsOnRoadPriceDto.getOnRoadPrice();
					
				}
			}
		
		}
		writeIntoCell(detailsRow, exShowroomPrice, cellNum++);
		writeIntoCell(detailsRow, onRoadPrice, cellNum++);
		return cellNum;
	}

	private int addAttachments(List<DmsAttachmentDto> dmsAttachmentsList, Row detailsRow, int cellNum) {
		// TODO Auto-generated method stub
		String aadarNumber ="";
		String panNumber="";
	
		if (dmsAttachmentsList != null && !dmsAttachmentsList.isEmpty()) {
			for (DmsAttachmentDto dmsAttachmentDto : dmsAttachmentsList) {

				if (dmsAttachmentDto.getDocumentType().equalsIgnoreCase("aadhar")) {
					aadarNumber = dmsAttachmentDto.getDocumentNumber();
				}
				if (dmsAttachmentDto.getDocumentType().equalsIgnoreCase("pan")) {
					panNumber = dmsAttachmentDto.getDocumentNumber();
				}
			}

		}
		writeIntoCell(detailsRow, panNumber, cellNum++); 
		writeIntoCell(detailsRow, aadarNumber, cellNum++); 
		return cellNum;
	}

	private void genearateExcelForEnq(List<DMSResponse> dmsResponseList, List<LeadStageRefEntity> leadRefDBList, String fileName,String sheetName) throws DynamicFormsServiceException {
		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet(sheetName);
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = getEnquiryRowHeaders();
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
			for (DMSResponse res : dmsResponseList) {
				DmsEntity dmsEntity = res.getDmsEntity();
				if(null!=dmsEntity) {
					log.debug("dmsEntity is not null");
					Row detailsRow = sheet.createRow(rowNum++);
					cellNum = 0;
					DmsContactDto dmsContactDto =dmsEntity.getDmsContactDto();
					DmsAccountDto dmsAccountDto = dmsEntity.getDmsAccountDto();		
					DmsLeadDto dmsLeadDto = dmsEntity.getDmsLeadDto();
					List<DmsAddress> addressList =  dmsLeadDto.getDmsAddresses();
					List<DmsLeadProductDto> dmsLeadProductDtoList = dmsLeadDto.getDmsLeadProducts(); 
					List<DmsFinanceDetailsDto> dmsFinanceDetailsList = dmsLeadDto.getDmsfinancedetails();
					List<DmsExchangeBuyerDto> dmsExchagedetailsList = dmsLeadDto.getDmsExchagedetails();
					if (dmsLeadDto != null ) {
						int leadId = dmsLeadDto.getId();

						List<LeadStageRefEntity> leadRefList = leadRefDBList.stream()
								.filter(x -> x.getLeadId() != null && (x.getLeadId()) == leadId)
								.collect(Collectors.toList());
						LeadStageRefEntity leadRef = new LeadStageRefEntity();
						if (null != leadRefList && !leadRefList.isEmpty()) {
							leadRef = leadRefList.get(0);
						}

						Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
						String branchName = "";
						if (optBranch.isPresent()) {
							DmsBranch branch = optBranch.get();
							branchName = branch.getName();
						}
					
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, branchName, cellNum++);
						writeIntoCell(detailsRow, leadRef.getRefNo(), cellNum++);
						writeIntoCell(detailsRow, leadRef.getStartDate(), cellNum++);
						writeIntoCell(detailsRow, getMonthAndYear(leadRef.getStartDate()), cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getSalutation():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getFirstName(), cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto.getLastName(), cellNum++);
						writeIntoCell(detailsRow,  dmsContactDto!=null?dmsContactDto.getPhone():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getEmail():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getDateOfBirth():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getAnniversaryDate():"", cellNum++);
						cellNum = addAddress(addressList,detailsRow,cellNum);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getModel():"", cellNum++);
				
						cellNum = addDmsLeadProducts(dmsLeadProductDtoList,detailsRow,cellNum);
					
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEnquirySegment():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getCustomerCategoryType():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSourceOfEnquiry():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSubSource():"", cellNum++);
						writeIntoCell(detailsRow, dmsContactDto!=null?dmsContactDto.getCompany():"", cellNum++);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getEnquiryCategory():"", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getLeadStatus():"", cellNum++);
						
						
						cellNum = addEventDetails(detailsRow,cellNum,leadId,dmsLeadDto.getEventCode());
		
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getDmsExpectedDeliveryDate():"", cellNum++); 
						
						cellNum = addDmsFinanceDetails(dmsFinanceDetailsList,detailsRow,cellNum);
						writeIntoCell(detailsRow, "", cellNum++);
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getBuyerType():"", cellNum++);
						//writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getBuyerType():"", cellNum++);
						
						
						cellNum = addDmsExchangeDetails(dmsExchagedetailsList,detailsRow,cellNum);
					
					
						
						
						String evalName="";
						Timestamp evalDate=null;
						String evalNumber="";
						String evalStatus="";
						String evalOfferPrice="";
						String custExpectedPrice="";
						String approviedPrice="";
						final List<Map<String, Object>> evalutionList  = buildEvaluation (dmsLeadDto.getCrmUniversalId());
					
						for(Map<String,Object> m : evalutionList) {
						
							
							evalName = ""+(Integer)m.get("evalutor_id");
							evalDate =(Timestamp)m.get("updated_date");
							evalNumber = (String)m.get("mobile_num");
							evalStatus = m.get("evalution_status")!=null?(String)m.get("evalution_status"):"";
							evalOfferPrice = m.get("evaluator_offer_price")!=null?(String)m.get("evaluator_offer_price"):"";
							custExpectedPrice = m.get("cust_expected_price")!=null?(String)m.get("cust_expected_price"):"";
							approviedPrice = m.get("manager_offer_price")!=null?(String)m.get("manager_offer_price"):"";
							
							
							
						}
						
						log.debug("evalName:::"+evalName+",evalDate:"+evalDate);
						writeIntoCell(detailsRow, evalStatus, cellNum++); //Eval Status
						writeIntoCell(detailsRow, evalName, cellNum++);  //Evaluator Name
						writeIntoCell(detailsRow, evalDate!=null?evalDate.toString():"",cellNum++);  //Evaluation Date
						writeIntoCell(detailsRow, evalNumber, cellNum++);  //Evaluation number
						writeIntoCell(detailsRow, evalOfferPrice, cellNum++);  //Offered Price
						writeIntoCell(detailsRow, custExpectedPrice, cellNum++);  //Customer Exp. Price
						writeIntoCell(detailsRow, approviedPrice, cellNum++);  //Approved price
						
						
						
						cellNum = addTestDriveDetails(dmsLeadDto.getCrmUniversalId(),detailsRow,cellNum);
						cellNum =addHomeVisitDetails(dmsLeadDto.getCrmUniversalId(),detailsRow,cellNum);
						
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getAging():"", cellNum++);
						writeIntoCell(detailsRow, dmsAccountDto!=null?dmsAccountDto.getKmsTravelledInMonth():"", cellNum++);
						writeIntoCell(detailsRow, dmsAccountDto!=null?dmsAccountDto.getWhoDrives():"", cellNum++);
						writeIntoCell(detailsRow, dmsAccountDto!=null?dmsAccountDto.getMembersInFamily():"", cellNum++);
						writeIntoCell(detailsRow, dmsAccountDto!=null?dmsAccountDto.getPrimeExpectationFromCar():"", cellNum++);
						
						
						writeIntoCell(detailsRow, "", cellNum++);  //Looking for another make
						writeIntoCell(detailsRow, "", cellNum++);  //Looking for another model
						writeIntoCell(detailsRow, "", cellNum++);  //Looking for another Variant
						writeIntoCell(detailsRow, "", cellNum++);  //CO-Dealership/Competetor Name
						writeIntoCell(detailsRow, "", cellNum++);  //CO-Dealership/Competetor Location
						writeIntoCell(detailsRow, "", cellNum++);  //Enquiry Lost date
						writeIntoCell(detailsRow, "", cellNum++);  //Enquiry Lost Reason
						writeIntoCell(detailsRow, "", cellNum++);  //Lost to Co-delaer Name
						writeIntoCell(detailsRow, "", cellNum++);  //Lost to Co-dealer Location
						writeIntoCell(detailsRow, "", cellNum++);  //Lost to Competetor
						
						writeIntoCell(detailsRow, "", cellNum++);  //Competetor Model
					
						writeIntoCell(detailsRow, dmsLeadDto!=null?dmsLeadDto.getSalesConsultant():"", cellNum++); 
						String empID=getEmpName(dmsLeadDto.getSalesConsultant());
						writeIntoCell(detailsRow, empID, cellNum++); 
						String teamLeadName = getTeamLead(empID);
						writeIntoCell(detailsRow, teamLeadName, cellNum++); // Team Leader
						writeIntoCell(detailsRow, getManager(teamLeadName), cellNum++); // Manager

						writeIntoCell(detailsRow, "", cellNum++);  //Last Remarks
						
						
					}
				}
			}
				FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
				workbook.write(out);
				out.close();
			
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	
	private int addTestDriveDetails(String crmUniversalId, Row detailsRow, int cellNum) {
		
		try {

		List<DmsWFTask> dmsWFTaskListHomeVisit = dmsWfTaskDao.getWfTaskByUniversalIdandTask(crmUniversalId,TEST_DRIVE);
		if(null!=dmsWFTaskListHomeVisit && !dmsWFTaskListHomeVisit.isEmpty()) {
			DmsWFTask wfTask = dmsWFTaskListHomeVisit.get(0);
			writeIntoCell(detailsRow, wfTask.getTaskExceptedStartTime(), cellNum++); 
			writeIntoCell(detailsRow, wfTask.getTaskId(), cellNum++); 
			writeIntoCell(detailsRow, wfTask.getTaskActualStartTime(), cellNum++); 
		}else {
			writeIntoCell(detailsRow, "", cellNum++); 
			writeIntoCell(detailsRow, "", cellNum++); 
			writeIntoCell(detailsRow, "", cellNum++); 
		}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cellNum;
	}

	private int addHomeVisitDetails(String crmUniversalId, Row detailsRow, int cellNum) {
		try {
		List<DmsWFTask> dmsWFTaskListHomeVisit = dmsWfTaskDao.getWfTaskByUniversalIdandTask(crmUniversalId,HOME_VISIT);
		if(null!=dmsWFTaskListHomeVisit && !dmsWFTaskListHomeVisit.isEmpty()) {
			DmsWFTask wfTask = dmsWFTaskListHomeVisit.get(0);
			writeIntoCell(detailsRow, wfTask.getTaskStatus(), cellNum++); 
			writeIntoCell(detailsRow, wfTask.getTaskActualStartTime(), cellNum++); 
		}else {
			writeIntoCell(detailsRow, "", cellNum++); 
			writeIntoCell(detailsRow, "", cellNum++); 
		}
		}catch(Exception e) {
			e.printStackTrace();
		}
		return cellNum;
	}

	private int addDmsExchangeDetails(List<DmsExchangeBuyerDto> dmsExchagedetailsList, Row detailsRow, int cellNum) {
		if (dmsExchagedetailsList != null && !dmsExchagedetailsList.isEmpty()) {

			DmsExchangeBuyerDto a1 = dmsExchagedetailsList.get(0);
			if (null != a1) {
				writeIntoCell(detailsRow, a1.getRegNo(), cellNum++);
				writeIntoCell(detailsRow, a1.getBrand(), cellNum++);
				writeIntoCell(detailsRow, a1.getModel(), cellNum++);
				writeIntoCell(detailsRow, a1.getVarient(), cellNum++);
				writeIntoCell(detailsRow, a1.getColor(), cellNum++);
				writeIntoCell(detailsRow, a1.getFuelType(), cellNum++);
				writeIntoCell(detailsRow, a1.getTransmission(), cellNum++);
				writeIntoCell(detailsRow, a1.getYearofManufacture(), cellNum++);
			

			} else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);

			}

		}else {
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
		}
		return cellNum;
	}

	private int addDmsFinanceDetails(List<DmsFinanceDetailsDto> dmsFinanceDetailsList, Row detailsRow, int cellNum) {
		if (dmsFinanceDetailsList != null && !dmsFinanceDetailsList.isEmpty()) {

			DmsFinanceDetailsDto a1 = dmsFinanceDetailsList.get(0);
			if (null != a1) {
				writeIntoCell(detailsRow, a1.getFinanceType(), cellNum++);
				writeIntoCell(detailsRow, a1.getFinanceCategory(), cellNum++);
				writeIntoCell(detailsRow, a1.getDownPayment(), cellNum++);
				writeIntoCell(detailsRow, a1.getLoanAmount(), cellNum++);
				writeIntoCell(detailsRow, a1.getFinanceCompany(), cellNum++);
				writeIntoCell(detailsRow, a1.getRateOfInterest(), cellNum++);
				writeIntoCell(detailsRow, a1.getExpectedTenureYears(), cellNum++);
				writeIntoCell(detailsRow, a1.getAnnualIncome(), cellNum++);
			

			} else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
			}

		}else {
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
		}
		return cellNum;
	}

	
	private int addDmsLeadProducts(List<DmsLeadProductDto> dmsLeadProductDtoList, Row detailsRow, int cellNum) {
		if (dmsLeadProductDtoList != null && !dmsLeadProductDtoList.isEmpty()) {

			DmsLeadProductDto a1 = dmsLeadProductDtoList.get(0);
			if (null != a1) {
				writeIntoCell(detailsRow, a1.getVariant(), cellNum++);
				writeIntoCell(detailsRow, a1.getColor(), cellNum++);
				writeIntoCell(detailsRow, a1.getFuel(), cellNum++);
				writeIntoCell(detailsRow, a1.getTransimmisionType(), cellNum++);

			} else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);

			}

		} else {

			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);

		}
		return cellNum;
	}

	private int addSingleAddress(List<DmsAddress> addressList, Row detailsRow, int cellNum) {
		if(addressList!=null && !addressList.isEmpty()) {
			
			DmsAddress a1 = addressList.get(0);
			if(null!=a1) {
				writeIntoCell(detailsRow, a1.getHouseNo(), cellNum++);
				writeIntoCell(detailsRow, a1.getCity(), cellNum++);
				writeIntoCell(detailsRow, a1.getDistrict(), cellNum++);
				writeIntoCell(detailsRow, a1.getState(), cellNum++);
				writeIntoCell(detailsRow, a1.getPincode(), cellNum++);
			}else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
			}
			
		}else {
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
		}
		return cellNum;
	}
	private int addAddress(List<DmsAddress> addressList, Row detailsRow, int cellNum) {
		if(addressList!=null && !addressList.isEmpty()) {
			
			DmsAddress a1 = addressList.get(0);
			if(null!=a1) {
				writeIntoCell(detailsRow, a1.getAddressType(), cellNum++);
				writeIntoCell(detailsRow, a1.getCity(), cellNum++);
				writeIntoCell(detailsRow, a1.getDistrict(), cellNum++);
				writeIntoCell(detailsRow, a1.getState(), cellNum++);
				writeIntoCell(detailsRow, a1.getPincode(), cellNum++);
			}else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
			}
			
			DmsAddress a2 = addressList.get(1);
			if(null!=a2) {
				writeIntoCell(detailsRow, a2.getAddressType(), cellNum++);
				writeIntoCell(detailsRow, a2.getCity(), cellNum++);
				writeIntoCell(detailsRow, a2.getDistrict(), cellNum++);
				writeIntoCell(detailsRow, a2.getState(), cellNum++);
				writeIntoCell(detailsRow, a2.getPincode(), cellNum++);
			}else {
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
				writeIntoCell(detailsRow, "", cellNum++);
			}
		}else {
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
			writeIntoCell(detailsRow, "", cellNum++);
		}
		return cellNum;
	}

	private void genearateExcelForPreEnq(List<ETVPreEnquiry> etvList, String fileName) {

		try {
			XSSFWorkbook workbook = new XSSFWorkbook();
			XSSFSheet sheet = workbook.createSheet("Pre Enquiry");
			int rowNum = 0;
			Row row = sheet.createRow(rowNum++);
			List<String> rowHeaders = getPreEnqRowHeaders();
			int cellNum = 0;
			for (String rowHeader : rowHeaders) {
				Cell cell = row.createCell(cellNum);
				cell.setCellValue(rowHeader);
				cellNum++;
			}
 
		
			for (ETVPreEnquiry pre : etvList) {
				Row detailsRow = sheet.createRow(rowNum++);
				cellNum = 0;
				writeIntoCell(detailsRow, pre.getLocation(), cellNum++);
				writeIntoCell(detailsRow, pre.getDealerCode(), cellNum++);
				writeIntoCell(detailsRow, pre.getPreEnqId(), cellNum++);
				writeIntoCell(detailsRow, pre.getPreEnqDate(), cellNum++);
				writeIntoCell(detailsRow, pre.getPreEnqMonthYear(), cellNum++);
				writeIntoCell(detailsRow, pre.getFirstName(), cellNum++);
				writeIntoCell(detailsRow, pre.getLastName(), cellNum++);
				writeIntoCell(detailsRow, pre.getMobileNo(), cellNum++);
				writeIntoCell(detailsRow, pre.getEmailId(), cellNum++);
				writeIntoCell(detailsRow, pre.getModel(), cellNum++);
				writeIntoCell(detailsRow, pre.getEnqSegment(), cellNum++);
				writeIntoCell(detailsRow, pre.getCustomerType(), cellNum++);
				writeIntoCell(detailsRow, pre.getSourceOfPreEnquiry(), cellNum++);
				writeIntoCell(detailsRow, pre.getSubSoruceOfPreEnquiry(), cellNum++);
				writeIntoCell(detailsRow, pre.getPincode(), cellNum++);
				writeIntoCell(detailsRow, pre.getDropReason(), cellNum++);
				writeIntoCell(detailsRow, pre.getSubDropReason(), cellNum++);
				writeIntoCell(detailsRow, pre.getAssignedBy(), cellNum++);
				writeIntoCell(detailsRow, pre.getSalesExecutive(), cellNum++);
				writeIntoCell(detailsRow, pre.getSalesExecutiveEmpId(), cellNum++);
				
				
				
				String teamLeadName = getTeamLead(pre.getSalesExecutiveEmpId());
				writeIntoCell(detailsRow, teamLeadName, cellNum++); // Team Leader
				writeIntoCell(detailsRow, getManager(teamLeadName), cellNum++); // Manager


				writeIntoCell(detailsRow, pre.getRemarks(), cellNum++);
		
			}

			FileOutputStream out = new FileOutputStream(new File(fileName)); // file name with path
			workbook.write(out);
			out.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	private void writeIntoCell(Row row, Object value, int cellNum) {
		Cell cell = row.createCell(cellNum++);
 
		if (value instanceof String) {
			cell.setCellType(Cell.CELL_TYPE_STRING);
			cell.setCellValue((String) value);
		} else if (value instanceof Long) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Long) value);
		} else if (value instanceof Integer) {
			cell.setCellType(Cell.CELL_TYPE_NUMERIC);
			cell.setCellValue((Integer) value);
		}
 
	}

	private List<String> getPreEnqRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Pre enquiry ID");
		list.add("Pre enquiry Date");
		list.add("Pre enquiry Month & Year");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Model");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of pre enquiry");
		list.add("Subsource of Pre enquiry");
		list.add("Pincode");
		list.add("Drop date");
		list.add("Drop Reason");
		list.add("Sub Drop Reason");
		list.add("Assigned by");
		list.add("Sales Executive");
		list.add("Sales Executive EMP ID");
		list.add("Manager");
		list.add("Last remarks");
		return list;
	}

	private List<ETVPreEnquiry> buildPreEnqList(List<LeadStageRefEntity> leadRefDBList, List<DmsLead> leadDBList) {
		List<ETVPreEnquiry> list = new ArrayList<>();
		for(DmsLead lead: leadDBList) {
			ETVPreEnquiry pre = new ETVPreEnquiry();
			
			List<LeadStageRefEntity> tmpleadRefList = leadRefDBList.stream().filter(x->x.getLeadId()!=null && x.getLeadId().equals(lead.getId())).collect(Collectors.toList());
			LeadStageRefEntity leadRef = tmpleadRefList.get(0);
			
			pre.setLocation("");
			Optional<DmsBranch> optBranch = dmsBranchDao.findById(leadRef.getBranchId());
			if(optBranch.isPresent()) {
				DmsBranch branch = optBranch.get();
				pre.setDealerCode(branch.getName());
				pre.setLocation(branch.getName());
				}
				pre.setPreEnqId(leadRef.getRefNo());
				pre.setPreEnqDate(leadRef.getStartDate().toString());
				pre.setPreEnqDate(getMonthAndYear(leadRef.getStartDate()));
				pre.setFirstName(lead.getFirstName());
				pre.setLastName(lead.getLastName());
				pre.setLastName(lead.getPhone());
				pre.setEmailId(lead.getEmail());
				pre.setModel(lead.getModel());
				pre.setEnqSegment(lead.getEnquirySegment());
				//pre.setCustomerType(lead.getCustomerCategoryType());
				pre.setSourceOfPreEnquiry(lead.getDmsSourceOfEnquiry().getName());
				pre.setSubSoruceOfPreEnquiry(lead.getSubSource());
				
				if(lead.getLeadStage().equalsIgnoreCase(DROPPED)) {
				  List<DmsLeadDrop> dropList = dmsLeadDropDao.getByLeadId(lead.getId());
				  if(dropList!=null && !dropList.isEmpty()) {
					  DmsLeadDrop droppedLead =dropList.get(0);
				
					  pre.setDropDate(droppedLead.getCreatedDateTime());
					  pre.setDropReason(droppedLead.getLostReason());
					  pre.setSubDropReason(droppedLead.getLostSubReason());
				  }
					
				}
				
				pre.setSalesExecutive(lead.getSalesConsultant());
				pre.setSalesExecutiveEmpId(getEmpName(lead.getSalesConsultant()));
				list.add(pre);
		}
		
		return list;
	}

	private String getEmpName(String salesConsultant) {
		String empId="";
		empId =  dmsEmployeeRepo.findEmpIdByName(salesConsultant);
		return empId;
		
	}

	private String getMonthAndYear(Timestamp startDate) {
		// TODO Auto-generated method stub
		return null;
	}

	private List<LeadStageRefEntity> getLeadRefDBList(String orgId, String startDate, String endDate,
			String stageName, List<String> branchIdList) {
		if (null != branchIdList && branchIdList.isEmpty()) {
			log.debug("branchIdList is empty");
			return leadStageRefDao.getLeadsBasedOnStage(orgId, startDate, endDate, stageName);
		} else {
			log.debug("branchIdList is not empty");
			return leadStageRefDao.getLeadsBasedOnStageBranch(orgId, startDate, endDate, stageName, branchIdList);
		}

	}
	
	private List<String> getEnquiryRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Month & Year");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Date of birth");
		list.add("Date of aniversary");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Permenent Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Enquiry segment");
		list.add("Customer Type");
		list.add("Source of enquiry");
		list.add("Sub source");
		list.add("Company/Institution");
		list.add("Enquiry Category");
		list.add("Enquiry Status");
		list.add("Event Name");
		list.add("Event ID");
		list.add("Event Start date");
		list.add("Event End date");
		list.add("Event Category");
		list.add("Expected Delivery date");
		list.add("Retail Finance");
		list.add("Finance Category");
		list.add("Down Payment");
		list.add("Loan Amount");
		list.add("Bank/Financier Name");
		list.add("Rate of intrest");
		list.add("Loan tenure");
		list.add("Approx annual Income");
		list.add("Leasing Name");
		list.add("Buyer type");
		list.add("Old car Reg.Number");
		list.add("Old car Make");
		list.add("Old car Model");
		list.add("Old car Variant");
		list.add("Old car Colour");
		list.add("Old car Fuel");
		list.add("Old car Transmission");
		list.add("Old car Month & Year of manufacture");
		list.add("Eval Status");
		list.add("Evaluator Name");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Offered Price");
		list.add("Customer Exp. Price");
		list.add("Approved price");
		list.add("Test Drive Given");
		list.add("Test drive ID");
		list.add("Test Drive Date");
		list.add("Home Visit Status");
		list.add("Home visit date");
		list.add("Enq Ageing");
		list.add("KM's Travelled in Month");
		list.add("Who drives");
		list.add("How many members in home");
		list.add("Pimary expectation");
		list.add("Looking for another make");
		list.add("Looking for another model");
		list.add("Looking for another Variant");
		list.add("CO-Dealership/Competetor Name ");
		list.add("CO-Dealership/Competetor Location");
		list.add("Enquiry Lost date");
		list.add("Enquiry Lost Reason");
		list.add("Lost to Co-delaer Name");
		list.add("Lost to Co-dealer Location");
		list.add("Lost to Competetor");
		list.add("Competetor Model");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Last Remarks");
		return list;
		}

		//////////////////////////////////////////

		//Boking

		private List<String> getBookingRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Booking ID");
		list.add("Booking Date");
		list.add("Booking Month & Year");
		list.add("Customer ID");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile Number");
		list.add("Email id");
		list.add("Date of birth");
		list.add("Booking address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Pan Number");
		list.add("Aadhar Number");
		list.add("GST Number");
		list.add("Selected Insurance Type");
		list.add("Selected Add on-Covers");
		list.add("Selected Ex-warranty Type");
		list.add("Paid accessories amount");
		list.add("Foc accessories amount");
		list.add("Cash Discount");
		list.add("Ex-Showroom Price");
		list.add("On Road price");
		list.add("Booking Status");
		list.add("Booking payment Mode");
		list.add("Booking Amount");
		list.add("Total Payment received");
		list.add("Pending Amount");
		list.add("Retail Finance");
		list.add("Vehicle Allocation Date");
		list.add("Vehicle Allocation Age");
		list.add("Allocated VIN Number");
		list.add("Preferred Delivery date");
		list.add("Promissed delivery date");
		list.add("Delivery Location");
		list.add("Enquiry to booking Conversion days");
		list.add("Booking age");
		list.add("Enquiry Date");
		list.add("Enquiry Number");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of Enquiry");
		list.add("Subsource of Enquiry");
		list.add("Corporate Name");
		list.add("Event Name");
		list.add("Event Category");
		list.add("Buyer type");
		list.add("Old Car Reg.Number");
		list.add("Old car Model");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Evaluator Name");
		list.add("Eval Status");
		list.add("Customer Exp. Price");
		list.add("Offered Price");
		list.add("Approved Price");
		list.add("Exchnage price Approval status");
		list.add("Booking Cancel date");
		list.add("Booking Cancel Reason");
		list.add("Booking Cancel Sub Reason");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Finance Executive");
		list.add("Last Remarks");
		return list;
		}
		//////////////////////////////////////////////////////////////////
		//Retail


		private List<String> getRetailRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Invoice ID");
		list.add("Invoice Date");
		list.add("Invoice Month & Year");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile Number");
		list.add("Email id");
		list.add("Confirm billing address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("VIN Number");
		list.add("Chassis Number");
		list.add("Engine Number");
		list.add("Pan Number");
		list.add("Aadhar Number");
		list.add("GST Number");
		list.add("Booking ID");
		list.add("Booking date");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of Enquiry");
		list.add("Subsource of Enquiry");
		list.add("Corporate Name");
		list.add("Event Name");
		list.add("Event ID");
		list.add("Event Start date");
		list.add("Event End date");
		list.add("Event Category");
		list.add("Booking to Retail Conversion days");
		list.add("Exshowroom Price");
		list.add("GST Type");
		list.add("CGST %");
		list.add("SGST %");
		list.add("IGST %");
		list.add("UTGST %");
		list.add("CESS %");
		list.add("Total GST %");
		list.add("TCS %");
		list.add("TCS Amount");
		list.add("Consumer offer");
		list.add("Exchange Offer");
		list.add("Corporate Offer");
		list.add("Special Offer");
		list.add("Additional offer 1");
		list.add("Additional offer 2");
		list.add("Cash Discount");
		list.add("FOC accessories amount");
		list.add("Invoice amount");
		list.add("Retail Finance");
		list.add("Finance Name");
		list.add("Buyer Type");
		list.add("Old Car Reg.number");
		list.add("Old car Make");
		list.add("Old car Model");
		list.add("Old car Variant");
		list.add("Old car approved Price");
		list.add("Relationship with customer");
		list.add("Invoice cancel date");
		list.add("Invoice Cancel Reason");
		list.add("Invoice cancel Sub Lost reason");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Finance Executive");
		list.add("Evaluator Name");
		return list;
		}


		////////////////////////////


		//Delivery


		private List<String> getDeliveryRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Delivery Challan ID");
		list.add("Delivery Date");
		list.add("Delivery Month & Year");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile Number");
		list.add("Email id");
		list.add("Confirm billing address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Engine CC");
		list.add("VIN Number");
		list.add("Chassis Number");
		list.add("Engine Number");
		list.add("Pan Number");
		list.add("Aadhar Number");
		list.add("GST Number");
		list.add("Booking ID");
		list.add("Booking date");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of Enquiry");
		list.add("Subsource of Enquiry");
		list.add("Corporate Name");
		list.add("Event Name");
		list.add("Event ID");
		list.add("Event Start date");
		list.add("Event End date");
		list.add("Event Category");
		list.add("Retail to Delivery Conversion days");
		list.add("Exshowroom Price");
		list.add("GST Type");
		list.add("CGST %");
		list.add("SGST %");
		list.add("IGST %");
		list.add("UTGST %");
		list.add("CESS %");
		list.add("Total GST %");
		list.add("TCS %");
		list.add("TCS Amount");
		list.add("Consumer offer");
		list.add("Exchange Offer");
		list.add("Corporate Offer");
		list.add("Special Offer");
		list.add("Additional offer 1");
		list.add("Additional offer 2");
		list.add("Cash Discount");
		list.add("FOC accessories amount");
		list.add("Invoice amount");
		list.add("Vehicle Purchase date");
		list.add("Vehicle Purchase amount");
		list.add("Exchange status");
		list.add("Buyer Type");
		list.add("Evaluation ID");
		list.add("Evaluation date");
		list.add("Old Car Reg.number");
		list.add("Old car Make");
		list.add("Old car Model");
		list.add("Old car Variant");
		list.add("Old car approved Price");
		list.add("Relationship with customer");
		list.add("EW status");
		list.add("EW Number");
		list.add("EW Start date");
		list.add("EW End date");
		list.add("Insurance Type");
		list.add("Insurane Number");
		list.add("Insurance Company");
		list.add("Insurance Start date");
		list.add("Insurance End date");
		list.add("Insurance premium");
		list.add("Paid accessories amount");
		list.add("Accessories fitting pening parts amount");
		list.add("Retail Finance");
		list.add("Financier Name");
		list.add("Financier branch");
		list.add("Loan amount");
		list.add("Rate of intrest");
		list.add("Loan tenure");
		list.add("EMI Amount");
		list.add("Payout %");
		list.add("Net payout");
		list.add("Disbursed date");
		list.add("Disbursed amount");
		list.add("Payment ref number");
		list.add("Delivery challan cancel date");
		list.add("Delivery challan Cancel Reason");
		list.add("Delivery challan cancel Sub Lost reason");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Finance Executive");
		list.add("Evaluator Name");
		return list;
		}
		///////////////////////////////////
		//Evalution

		private List<String> getEvalutionRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Evaluation Id");
		list.add("Evaluation date");
		list.add("Evaluation Month & Year");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Month & Year");
		list.add("Evaluation Status");
		list.add("Customer Name");
		list.add("Mobile No");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("VehicleType");
		list.add("Old car Reg.Number");
		list.add("Old car Make");
		list.add("Old car Model");
		list.add("Old car Variant");
		list.add("Old car Colour");
		list.add("Old car Fuel");
		list.add("Old car Transmission");
		list.add("Vin No");
		list.add("Make Year");
		list.add("Old car Month & Year of manufacture");
		list.add("Registartion expiry date");
		list.add("Kms driven");
		list.add("Expected Price");
		list.add("Offered Price");
		list.add("Gap amount");
		list.add("Lead Stage");
		list.add("Enquiry Category");
		list.add("Buyer type");
		list.add("Enq Ageing");
		list.add("Evaluator Name");
		list.add("Evaluation Manager");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Last Remarks");
		return list;
		}

		////////////////////////////////////

		//TestDrive

		private List<String> getTestDriveRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Test Drive ID");
		list.add("Test drive Number");
		list.add("Test drive Date");
		list.add("Pre enquiry ID");
		list.add("Pre enquiry Date");
		list.add("Pre enquiry Month & Year");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Month & Year");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Enquiry Source");
		list.add("Sub source");
		list.add("Test Drive At");
		list.add("Test drive status");
		list.add("Test drive veh. Reg.No");
		list.add("Test drive Model");
		list.add("Test drive Variant");
		list.add("Driver Name");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Test drive remarks");
		return list;
		}
		//////////////////////////////////////////////////////////////////////////


		//HomeVisit



		private List<String> getHomeVisitRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Home Visit ID");
		list.add("Home visit date");
		list.add("Pre enquiry ID");
		list.add("Pre enquiry Date");
		list.add("Pre enquiry Month & Year");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Pre enquiry Month & Year");
		list.add("Customer Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Enquiry Source");
		list.add("Sub source");
		list.add("Home Visit status");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Home visit remarks");
		return list;
		}
		//////////////////////////////////////////////////////////

		//LiveEnquiry


		private List<String> getLiveEnquiryRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Month & Year");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Date of birth");
		list.add("Date of aniversary");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Permenent Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Enquiry segment");
		list.add("Customer Type");
		list.add("Source of enquiry");
		list.add("Sub source");
		list.add("Company/Institution");
		list.add("Enquiry Category");
		list.add("Enquiry Status");
		list.add("Event Name");
		list.add("Event ID");
		list.add("Event Start date");
		list.add("Event End date");
		list.add("Event Category");
		list.add("Expected Delivery date");
		list.add("Retail Finance");
		list.add("Finance Category");
		list.add("Down Payment");
		list.add("Loan Amount");
		list.add("Bank/Financier Name");
		list.add("Rate of intrest");
		list.add("Loan tenure");
		list.add("Approx annual Income");
		list.add("Leasing Name");
		list.add("Buyer type");
		list.add("Old car Reg.Number");
		list.add("Old car Make");
		list.add("Old car Model");
		list.add("Old car Variant");
		list.add("Old car Colour");
		list.add("Old car Fuel");
		list.add("Old car Transmission");
		list.add("Old car Month & Year of manufacture");
		list.add("Eval Status");
		list.add("Evaluator Name");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Offered Price");
		list.add("Customer Exp. Price");
		list.add("Approved price");
		list.add("Test Drive Given");
		list.add("Test drive ID");
		list.add("Test Drive Date");
		list.add("Home Visit Status");
		list.add("Home visit date");
		list.add("Enq Ageing");
		list.add("KM's Travelled in Month");
		list.add("Who drives");
		list.add("How many members in home");
		list.add("Pimary expectation");
		list.add("Looking for another make");
		list.add("Looking for another model");
		list.add("Looking for another Variant");
		list.add("CO-Dealership/Competetor Name ");
		list.add("CO-Dealership/Competetor Location");
		list.add("Enquiry Lost date");
		list.add("Enquiry Lost Reason");
		list.add("Lost to Co-delaer Name");
		list.add("Lost to Co-dealer Location");
		list.add("Lost to Competetor");
		list.add("Competetor Model");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Last Remarks");
		return list;
		}
		////////////////////////////
		///Live Booking

		private List<String> getLiveBookingRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Booking ID");
		list.add("Booking Date");
		list.add("Booking Month & Year");
		list.add("Customer ID");
		list.add("Salutation");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile Number");
		list.add("Email id");
		list.add("Date of birth");
		list.add("Booking address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Pan Number");
		list.add("Aadhar Number");
		list.add("GST Number");
		list.add("Selected Insurance Type");
		list.add("Selected Add on-Covers");
		list.add("Selected Ex-warranty Type");
		list.add("Paid accessories amount");
		list.add("Foc accessories amount");
		list.add("Cash Discount");
		list.add("Ex-Showroom Price");
		list.add("On Road price");
		list.add("Booking Status");
		list.add("Booking payment Mode");
		list.add("Booking Amount");
		list.add("Total Payment received");
		list.add("Pending Amount");
		list.add("Retail Finance");
		list.add("Vehicle Allocation Date");
		list.add("Vehicle Allocation Age");
		list.add("Allocated VIN Number");
		list.add("Preferred Delivery date");
		list.add("Promissed delivery date");
		list.add("Delivery Location");
		list.add("Enquiry to booking Conversion days");
		list.add("Booking age");
		list.add("Enquiry Date");
		list.add("Enquiry Number");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of Enquiry");
		list.add("Subsource of Enquiry");
		list.add("Corporate Name");
		list.add("Event Name");
		list.add("Event Category");
		list.add("Buyer type");
		list.add("Old Car Reg.Number");
		list.add("Old car Model");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Evaluator Name");
		list.add("Eval Status");
		list.add("Customer Exp. Price");
		list.add("Offered Price");
		list.add("Approved Price");
		list.add("Exchnage price Approval status");
		list.add("Booking Cancel date");
		list.add("Booking Cancel Reason");
		list.add("Booking Cancel Sub Reason");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Finance Executive");
		list.add("Last Remarks");
		return list;
		}

		////////////////////////////

		//EnquiryLost


		private List<String> getEnquiryLostRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Enquiry Lost Date");
		list.add("Enquiry Lost Month & Year");
		list.add("Enquiry ID");
		list.add("Enquiry Date");
		list.add("Enquiry Month & Year");
		list.add("First Name");
		list.add("Last Name");
		list.add("Mobile No");
		list.add("Email Id");
		list.add("Communication Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Permenent Address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Enquiry segment");
		list.add("Customer Type");
		list.add("Source of enquiry");
		list.add("Sub source");
		list.add("Company/Institution");
		list.add("Enquiry Category");
		list.add("Event Name");
		list.add("Event ID");
		list.add("Event Start date");
		list.add("Event End date");
		list.add("Event Category");
		list.add("Expected Delivery date");
		list.add("Retail Finance");
		list.add("Finance Category");
		list.add("Buyer type");
		list.add("Old car Reg.Number");
		list.add("Eval Status");
		list.add("Evaluator Name");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Offered Price");
		list.add("Customer Exp. Price");
		list.add("Approved price");
		list.add("Test Drive Given");
		list.add("Test drive ID");
		list.add("Test Drive Date");
		list.add("Home Visit");
		list.add("Home visit date");
		list.add("Enq Ageing");
		list.add("Lost reason");
		list.add("Sub lost reason");
		list.add("Lost to co-delaer Name");
		list.add("Lost to co-delaer Location");
		list.add("Lost to model");
		list.add("Lost to variant");
		list.add("Lost to compitetor Name");
		list.add("Lost to compitetor Location");
		list.add("Lost to model");
		list.add("Lost to variant");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Last Remarks");
		return list;
		}

		////////////////////////////////////

		//BookingLost



		private List<String> getBookingLostRowHeaders() {
		List<String> list = new ArrayList<>();
		list.add("Location");
		list.add("Dealer Code");
		list.add("Booking Cancel Date");
		list.add("Booking Lost Month & Year");
		list.add("Booking ID");
		list.add("Booking Date");
		list.add("Booking Month & Year");
		list.add("Customer ID");
		list.add("Customer Name");
		list.add("Mobile No");
		list.add("Email id");
		list.add("Booking address");
		list.add("City");
		list.add("District");
		list.add("State");
		list.add("Pincode");
		list.add("Model");
		list.add("Variant");
		list.add("Colour");
		list.add("Fuel");
		list.add("Transmission");
		list.add("Pan Number");
		list.add("Aadhar Number");
		list.add("GST Number");
		list.add("Selected Insurance Type");
		list.add("Selected Add on-Covers");
		list.add("Selected Ex-warranty Type");
		list.add("Retail Finance");
		list.add("Paid accessories amount");
		list.add("Foc accessories amount");
		list.add("Cash Discount");
		list.add("On Road price");
		list.add("Booking Status");
		list.add("Booking payment Mode");
		list.add("Booking Amount");
		list.add("Total Payment received");
		list.add("Pending Amount");
		list.add("Preferred Delivery date");
		list.add("Promissed delivery date");
		list.add("Delivery Location");
		list.add("Booking age");
		list.add("Enquiry Date");
		list.add("Enquiry Number");
		list.add("Enquiry Segment");
		list.add("Customer Type");
		list.add("Source of pre enquiry");
		list.add("Subsource of Pre enquiry");
		list.add("Corporate Name");
		list.add("Event Name");
		list.add("Event Category");
		list.add("Buyer type");
		list.add("Old Car Reg.Number");
		list.add("Old car Model");
		list.add("Evaluation Date");
		list.add("Evaluation number");
		list.add("Evaluator Name");
		list.add("Eval Status");
		list.add("Customer Exp. Price");
		list.add("Offered Price");
		list.add("Sales Executive");
		list.add("Sales Executive Emp Id");
		list.add("Team Leader");
		list.add("Manager");
		list.add("Finance Executive");
		list.add("Last Remarks");
		return list;
		}







		///////////////////////////////

}
