package com.automate.df.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.automate.df.dao.EvaluationParametersRepository;
import com.automate.df.entity.EvaluationParameters;

@Service
@Transactional
public class EvaluationParametersService {
	
	private final EvaluationParametersRepository parametersRepository;
	
	public EvaluationParametersService(EvaluationParametersRepository parametersRepository) {
		this.parametersRepository=parametersRepository;
	}
	
	public Map<String, List> getAllParameters(String orgId) {
		
		List<EvaluationParameters> itemList = parametersRepository.getAllParameters(orgId, "Items");
		List<EvaluationParameters> expensesList = parametersRepository.getAllParameters(orgId, "Additional Expenses");
		
		Map<String, List> evaluationParamsMap = new HashMap();
		evaluationParamsMap.put("Items", itemList);
		evaluationParamsMap.put("AdditionalExpenses", expensesList);
		return evaluationParamsMap;
	}
}