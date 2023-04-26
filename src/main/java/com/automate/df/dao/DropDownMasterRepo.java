package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.DropDown;
import com.automate.df.model.DropDownRes;

public interface DropDownMasterRepo extends JpaRepository<DropDown, Integer> {

	@Query(value="SELECT * FROM dropdown_master where business_unit=:bu and dropdown_type=:type",nativeQuery=true)
	public List<DropDown> getDropDownData(@Param(value="bu") String bu,@Param(value="type") String type);
	
	@Query(value="SELECT * FROM dropdown_master where business_unit=:bu and dropdown_type=:type and parent=:parent",nativeQuery=true)
	public List<DropDown> getDropDownWithParent(@Param(value="bu") String bu,@Param(value="type") String type,@Param(value="parent") int parentId);

	@Query(value="SELECT * FROM dropdown_master where id=:parent",nativeQuery=true)
	public List<DropDown> getDropDownWithParentId(@Param(value="parent") int parentId);
}
