package com.automate.df.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.dao.CheckListItemsRepository;
import com.automate.df.dao.CheckListTypesRepository;
import com.automate.df.entity.CheckListType;
import com.automate.df.model.ListOFCheckListTypeWithItems;

@Service
@Transactional
public class CheckListItemsService {
	private final CheckListTypesRepository checkListItemTypesRepository;
	private final CheckListItemsRepository checkListItemsRepository;

	public CheckListItemsService(CheckListTypesRepository checkListItemTypesRepository,
			CheckListItemsRepository checkListItemsRepository) {
		this.checkListItemTypesRepository = checkListItemTypesRepository;
		this.checkListItemsRepository = checkListItemsRepository;
	}

	public  List<ListOFCheckListTypeWithItems> getAllCheckListTypesDetails(int orgId) {
		List<CheckListType> checkListType=checkListItemTypesRepository.getAllCheckListType(orgId);
		List<ListOFCheckListTypeWithItems> listOFCheckListTypeWithItems=checkListType.stream().map(c-> new ListOFCheckListTypeWithItems(c,
				checkListItemsRepository.getByChecklisttypeId(c.getId()))).collect(Collectors.toList());
		return listOFCheckListTypeWithItems; 
    }

}
