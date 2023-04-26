package com.automate.df.dao;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import com.automate.df.entity.MenuRoleAction;

public interface MenuRoleActionRepo extends JpaRepository<MenuRoleAction, Integer>{
	
	@Query(value="SELECT * FROM menu_role_action where role_identifier=:id",nativeQuery=true)
	List<MenuRoleAction> getMenuRoleActions(@Param(value="id") int id);


}
