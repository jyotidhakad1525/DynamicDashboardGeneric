package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.automate.df.entity.OtherModel;
@Repository
public interface OtherMakerModelRepository extends CrudRepository<OtherModel, Integer> {
	 @Query(value = "SELECT * FROM dms_othermodel WHERE org_id =?1 and other_maker=?2", nativeQuery = true)
	    List<OtherModel> getModelByMaker(String orgId,String otherMaker);
}
