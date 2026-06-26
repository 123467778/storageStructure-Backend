package com.example.demo;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/structure")
@CrossOrigin(origins="http://localhost:3000")
public class StorageStructureController {
	
	private final StructureService structureService ;
	
	public StorageStructureController(final StructureService structureService ) {
		this.structureService =structureService;
	}
	
//	@PostMapping("/create")
//    public ResponseEntity<String> addStructure(@RequestBody Structure struct){
//        structureService.addStructure(struct);
//        return ResponseEntity.ok("Structure created successfully...");
//    }
	
	
	@PostMapping("/getNode")
	public ResponseEntity<?> createStructure(@RequestBody final Hierarchical struct) {
//	  List<Level> levels = struct.getData().getLevels();
//
//	    levels.forEach(System.out::println);
	    structureService.createStructure(struct);

	    return ResponseEntity.ok("Structure Created ...");
	}
	
	
	@GetMapping("/getHierarchical")
	public ResponseEntity<List<Hierarchical>> getHierarchical(){
		final List<Hierarchical> structures =structureService.getHierarchical();
		return ResponseEntity.ok(structures);
	}
	
	@GetMapping("/getStructure/{hierarchicalName}")
	public ResponseEntity<List<Level>> getHierarchicalByName(@PathVariable final String hierarchicalName ){
		
		final List<Level> struct = structureService.getHierarchicalByName(hierarchicalName);
		
		return ResponseEntity.ok(struct);
		
		
	}
	
	

}
