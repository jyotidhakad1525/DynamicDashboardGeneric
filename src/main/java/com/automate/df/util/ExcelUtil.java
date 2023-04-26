package com.automate.df.util;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.spire.xls.ExcelVersion;
import com.spire.xls.Workbook;
import com.spire.xls.Worksheet;
import com.spire.xls.WorksheetCopyType;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class ExcelUtil {

	@Value("${tmp.path}")
	String tmpPath;

	public String mergeFiles(List<String> fileNamesList) {
		String fn =  "ETVBRL_" + System.currentTimeMillis() + ".xlsx";
		try {
			Workbook newBook = new Workbook();
			newBook.getWorksheets().clear();
			Workbook tempBook = new Workbook();
			for (String file : fileNamesList) {
				File f = new File(file);
				if (null != f && f.exists()) {
					tempBook.loadFromFile(file);
					for (Worksheet sheet : (Iterable<Worksheet>) tempBook.getWorksheets()) {
						newBook.getWorksheets().addCopy(sheet, WorksheetCopyType.CopyAll);
					}
				}
			}
			newBook.saveToFile(tmpPath+fn, ExcelVersion.Version2013);

		} catch (Exception e) {
			e.printStackTrace();
			log.error("Exception while merging excel sheets ", e);
		}
		return fn;
	}
}
