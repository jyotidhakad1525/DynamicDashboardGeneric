package com.automate.df.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.automate.df.entity.OrganizationEntity;

public interface OrganizationRepo extends JpaRepository<OrganizationEntity, Integer>{

}
