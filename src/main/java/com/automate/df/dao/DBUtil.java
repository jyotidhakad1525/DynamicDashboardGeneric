package com.automate.df.dao;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import lombok.extern.slf4j.Slf4j;

@Repository
@Slf4j
public class DBUtil {
	
	
	 @Autowired
	 private JdbcTemplate jdbcTemplate;;
	

		public void save(String sql, Object[] arr) {
			
			int result = jdbcTemplate.update(sql, arr);
			if (result > 0) {
				System.out.println("A new row has been inserted.");
			}
			
		}
		
		
		public String update(String sql) {
			int result = jdbcTemplate.update(sql);
			if (result > 0) {

				System.out.println("Data has been updated");
				return "Updated Successfully";
			}
			return null;
		}
		
		
		public String delete(String sql) {
			int result = jdbcTemplate.update(sql);
			if (result > 0) {

				System.out.println("Data has been Deleted");
				return "Deleted Successfully";
			}
			return null;
		}


		public int get(String sql) {

			Integer parentId = jdbcTemplate.queryForObject(sql, Integer.class);
			
			
			
			
			return parentId.intValue();
		}
		
		public List<Integer> getList(String sql) {

			List<Integer> parentId = jdbcTemplate.queryForList(sql, Integer.class);
			
			
			
			
			return parentId;
		}
		
		public List<String> getListNames(String sql) {

			List<String> parentId = jdbcTemplate.queryForList(sql, String.class);
			
			
			
			
			return parentId;
		}
		
		

}
