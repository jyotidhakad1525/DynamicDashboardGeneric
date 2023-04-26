package com.automate.df.exception;
import com.automate.df.model.DynmFormExcelResponseDO;


public class DynmFormExcelException extends Exception {

	private static final long serialVersionUID = 1L;
	
	DynmFormExcelResponseDO dynmFormExcelResponseDO;
	
	public DynmFormExcelException(DynmFormExcelResponseDO dynmFormExcelResponseDO) {
		this.dynmFormExcelResponseDO = dynmFormExcelResponseDO;
	}
	
	public DynmFormExcelException(String message) {
		super(message);
	}

	public DynmFormExcelResponseDO getDynmFormExcelResponseDO() {
		return dynmFormExcelResponseDO;
	}

	public void setDynmFormExcelResponseDO(DynmFormExcelResponseDO dynmFormExcelResponseDO) {
		this.dynmFormExcelResponseDO = dynmFormExcelResponseDO;
	}

	
	
}
