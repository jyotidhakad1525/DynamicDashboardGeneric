package com.automate.df.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.automate.df.entity.BulkUploadTemplate;

@Repository
public interface BulkUploadTemplateRepository extends JpaRepository<BulkUploadTemplate, Integer> {

	BulkUploadTemplate findByPageIdAndOrgId(int pageId, int orgId);

}
