package com.automate.df.service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.dao.LostSubLostRepository;
import com.automate.df.entity.LostReasons;
import com.automate.df.entity.SubLostReasons;

@Service
@Transactional
public class LostSubLostServices {
	private final LostSubLostRepository lostsublostRepo;
	public LostSubLostServices(LostSubLostRepository lostsublostRepo) {
        this.lostsublostRepo = lostsublostRepo;  
    }
	public List<LostReasons> getAllSubLostAllDetails(String orgId,String stageName) {
		 List<LostReasons> lr=lostsublostRepo.getAllSubLost(orgId,stageName);
	        return lr.stream().map(i->{
	        	Set<SubLostReasons> d=i.getSublostreasons().stream().filter(j->j.getOrgId().equals(orgId)).
	        			collect(Collectors.toSet());
	        	i.setSublostreasons(d);
	        	return i;
	        }).collect(Collectors.toList());
    }

}