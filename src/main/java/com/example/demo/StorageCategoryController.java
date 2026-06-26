package com.example.demo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@CrossOrigin(origins = "http://localhost:3000")
public class StorageCategoryController {
	
	private StorageCategoryRepo storageRepo;
	
	public StorageCategoryController( StorageCategoryRepo storageRepo ) {
		this.storageRepo=storageRepo;
	}
	
    @GetMapping("/categories")
	public ResponseEntity<?> getAllCategory(){
		List<String> categories = storageRepo.getAllCategory();
		
		return ResponseEntity.ok(categories);
		
		
				}
}
