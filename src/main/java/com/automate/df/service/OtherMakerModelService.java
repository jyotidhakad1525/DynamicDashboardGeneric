package com.automate.df.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.dao.OtherMakerModelRepository;
import com.automate.df.dao.OtherMakerRepository;
import com.automate.df.entity.OtherMaker;
import com.automate.df.entity.OtherModel;



@Service
@Transactional
public class OtherMakerModelService {
	private final OtherMakerModelRepository othermakermodelrepo;
	private final OtherMakerRepository otherMakerRepository;
	public OtherMakerModelService(OtherMakerModelRepository othermakermodelrepo,OtherMakerRepository otherMakerRepository) {
        this.othermakermodelrepo = othermakermodelrepo;
        this.otherMakerRepository=otherMakerRepository;
       
    }
	public List<OtherModel> getModelByMaker(String orgId, String otherMaker) {
        return othermakermodelrepo.getModelByMaker(orgId, otherMaker);
    }
	public List<OtherMaker> getAllOtherModels(String orgId) {
        return otherMakerRepository.getAllOtherModels(orgId);
    }

}
