package com.automate.df.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.dao.SourceSubSourceRepository;
import com.automate.df.entity.Source;

@Service
@Transactional
public class SourceSubSourceService {
	private final SourceSubSourceRepository sourceSubSourceRepo;
	public SourceSubSourceService(SourceSubSourceRepository sourceSubSourceRepo) {
        this.sourceSubSourceRepo = sourceSubSourceRepo;  
    }
	public List<Source> getAllSubsourcedetails(String orgId) {
        return sourceSubSourceRepo.getAllSubsource(orgId);
    }

}
