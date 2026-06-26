package com.example.demo;

import java.util.List;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;


@Repository
public class StorageCategoryRepo {
	
	private final JdbcTemplate jdbcTemplate;
	
	public StorageCategoryRepo (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate=jdbcTemplate;
	}

//	public List<String> getAllCategory() {
//        String sql = "select categoryname from storageCategory";
//
//        return jdbcTemplate.query(sql, (rs, rowNum) -> {
//            StorageCategory category = new StorageCategory();
//            
//            
//            category.setCategory_name(rs.getString("categoryname"));
//
//            return category;
//        });
//    }
	
	public List<String> getAllCategory() {
	    String sql = "select categoryname from storageCategory";

	    return jdbcTemplate.query(sql,
	        (rs, rowNum) -> rs.getString("categoryname")
	    );
	}
	
}
