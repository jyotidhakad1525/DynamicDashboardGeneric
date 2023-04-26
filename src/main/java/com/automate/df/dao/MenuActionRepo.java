package com.automate.df.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.automate.df.entity.MenuAction;

public interface MenuActionRepo extends JpaRepository<MenuAction, Integer>{
	
	@Query(value="SELECT * FROM menu_action where menu_identifier=:id",nativeQuery=true)
	List<MenuAction> getMenuRoles(@Param(value="id") String id);

}
