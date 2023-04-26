package com.automate.df.model;

import java.util.List;
import java.util.Map;

import com.automate.df.entity.sales.workflow.DMSTaskHistory;
import com.automate.df.model.sales.lead.*;
import com.automate.df.model.sales.master.DmsRoleDto;
import com.automate.df.model.sales.master.DmsRoleSourceEnquiryMappingDto;
import com.automate.df.model.sales.master.DmsSourceOfEnquiryDto;
import com.automate.df.model.sales.workflow.ActivityDef;
import com.automate.df.model.sales.workflow.ProcessDef;
import com.automate.df.model.sales.workflow.Workflow;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
public class DmsEntity {
	  public LeadCustomerReference leadCustomerReference;
	  public List<Task> tasks;
	List<ActivityDef> activityDefs;
	Page<Task> myTasks;
	List<ProcessDef> processDefs;
	private DmsContactDto dmsContactDto;
	private DmsAccountDto dmsAccountDto;
	private DmsLeadDto dmsLeadDto;
	private LeadDto leadDto;
	private DmsInvoiceDto dmsInvoiceDto;
	private DmsDeliveryDto dmsDeliveryDto;
	private DmsAllotmentDto dmsAllotmentDto;
	private DmsOnRoadPriceDto dmsOnRoadPriceDto;
	private List<DmsInvoiceDto> dmsInvoiceDtoList;
	private List<DmsDeliveryDto> dmsDeliveryDtoList;
	private List<DmsAllotmentDto> dmsAllotmentDtoList;
	private List<DmsOnRoadPriceDto> dmsOnRoadPriceDtoList;
	private DmsEmployeeAllocationDto dmsEmployeeAllocationDto;
	private DmsBookingAmountReceivedDto dmsBookingAmountReceivedDto;
	private DmsDeliveryCheckListDto dmsDeliveryCheckListDto;
	private List<DmsDeliveryCheckListDto> DmsDeliveryCheckListDtoList;
	private List<DmsBookingAmountReceivedDto> dmsBookingAmountReceivedDtoList;
	private DmsRoleSourceEnquiryMappingDto dmsrolesourceofEnquirymapping;
	private Workflow workflow;
	private Task task;
	private DmsPaymentReceiptDto dmsPaymentReceipt;
	private List<DmsEmployeeAllocationDto> dmsEmployeeAllocationDtos;
	private List<DmsLeadDto> dmsLeadDtoList;
	private List<BulkLeadData> bulkLeadDataErrorList;
	private List<DmsRoleDto> dmsRoles;
	private List<DMSAllocationStrategyDTO> dmsallocationStartegies;
	private List<DmsSourceOfEnquiryDto> DmsSourceofEnquiries;
	private List<SubSourceDto> subSourceDto;
	private List<Process> processes;
	private List<Task> empTasks;
	private List<EmployeeDTO> EmployeeDTOs;
	private List<TaskCategory> taskCategoryList;
	private Page<LeadDto> leadDtoPage;
	private Page<DmsContactDto> dmsContactDtoPage;
	private Page<EmployeeDTO> EmployeeDTOPage;
	private Map<Integer, List<Integer>> employeeHierrachy;
	private DmsTaskApproverModel taskApproverModel;
	private List<DmsTaskApproverModel> listTaskApproverModel;
	private List<LeadDto> leadDtos;
	private Page<HelpDeskDto> helpdesk;
	private List<HelpDeskDto> helpdesklist;
	private List<DMSTaskHistory> dmsTaskHistoryList;
	private LeadCustomerReferenceDto leadCustomerReferenceDto;
	private Chart chart;

	public DmsEntity() {
	}

	public List<EmployeeDTO> getEmployeeDTOs() {
		return EmployeeDTOs;
	}

	public void setEmployeeDTOs(List<EmployeeDTO> EmployeeDTOs) {
		this.EmployeeDTOs = EmployeeDTOs;
	}

	public List<DmsSourceOfEnquiryDto> getDmsSourceofEnquiries() {
		return DmsSourceofEnquiries;
	}

	public void setDmsSourceofEnquiries(List<DmsSourceOfEnquiryDto> dmsSourceofEnquiries) {
		DmsSourceofEnquiries = dmsSourceofEnquiries;
	}

	public Page<EmployeeDTO> getEmployeeDTOPage() {
		return EmployeeDTOPage;
	}

	public void setEmployeeDTOPage(Page<EmployeeDTO> EmployeeDTOPage) {
		this.EmployeeDTOPage = EmployeeDTOPage;
	}

	public List<DmsDeliveryCheckListDto> getDmsDeliveryCheckListDtoList() {
		return DmsDeliveryCheckListDtoList;
	}

	public void setDmsDeliveryCheckListDtoList(List<DmsDeliveryCheckListDto> dmsDeliveryCheckListDtoList) {
		DmsDeliveryCheckListDtoList = dmsDeliveryCheckListDtoList;
	}
}
