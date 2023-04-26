package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.automate.df.entity.EvaluationParameters;


public interface EvaluationParametersRepository extends CrudRepository<EvaluationParameters, Integer>  {

	@Query(value="SELECT * FROM ops.evalution_parameters where org_id=?1 and status='Active' and type = ?2",nativeQuery = true)
	List<EvaluationParameters> getAllParameters(String orgId, String type);


}