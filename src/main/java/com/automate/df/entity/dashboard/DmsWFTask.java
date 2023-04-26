package com.automate.df.entity.dashboard;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name="dms_workflow_task")
@Entity
@Data
@NoArgsConstructor
public class DmsWFTask {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="task_id")
	private int taskId;
	
	@Column(name="task_name")
	private String taskName;
	
	
	@Column(name="task_created_time")
	private String taskCreatedTime;
	
	
	
	@Column(name="task_updated_time")
	private String taskUpdatedTime;
	
	
	
	@Column(name="task_expected_end_time")
	private String taskExceptedEndTime;
	
	@Column(name="assignee_id")
	private String assigneeId;

	
	@Column(name="universal_id")
	private String universalId;

	
	@Column(name="task_expected_start_time")
	private String taskExceptedStartTime;

	@Column(name="task_actual_start_time")
	private String taskActualStartTime;
	
	@Column(name="task_status")
	private String taskStatus;
	
	@Column(name="process_id")
	private String processId;
	
	@Column(name="entity_name")
	private String entityName;
	
	
	
	@Column(name="employee_remarks")
	private String employeeRemarks;
	
	


}
