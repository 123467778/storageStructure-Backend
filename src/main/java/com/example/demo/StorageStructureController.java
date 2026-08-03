package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	

	
	
	@PostMapping("/getNode")
	public ResponseEntity<?> createStructure(@RequestBody final Hierarchical struct) {

		
	 try {
		 
		  final int row=  structureService.createStructure(struct);

		 if(row>0) {
			  return ResponseEntity.ok("Structure Created ..."); 
		  }
		 return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("failed attempt to map structure");	
	 }
	 catch(final RuntimeException e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());

	 }
	
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
	
	

	@PostMapping("/createMap")
	public ResponseEntity<?>  mapStructure(@RequestBody final StructureMapping map){
		try {
			final int row =structureService.mapStructure(map);
			if(row >0) {
				return ResponseEntity.ok("Structured got mapped");
				
			}
			
			return ResponseEntity.status(HttpStatus.CONFLICT)
	                .body("failed attempt to map structure");	
		}
		catch(final RuntimeException ex) {
			
			return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
		}
		
	}

	

    
	@GetMapping("/getMap")
	public ResponseEntity<?> getMapStructures(){
		final List<StructureMapping> structures = structureService.getMapStructures();
		return ResponseEntity.ok(structures);
	}
	

	
	@GetMapping("/getTree/{scontainername}")
	public Map<String,Object> getNodeData(final @PathVariable String scontainername){
		return structureService.getNodeData(scontainername);
	}
	
	@PutMapping("/editNode/{scontainername}")
	public ResponseEntity<?> editNode(final @PathVariable String scontainername,  final @RequestBody StructureMapping struct ){
		
		final int row  = structureService.editNode(scontainername,struct);
		if(row>0) {
			return ResponseEntity.ok("Node edited ...");
		}
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Node not edited ...") ;
	}
	
	
	@GetMapping("/getNodeData")
	public ResponseEntity<?> getAllTreeStructure (){
		
		final List<Map<String , Object>> nodes =  structureService.getAllTreeStructure();
		
		if(nodes.size() >0 ) {
			return ResponseEntity.ok(nodes) ;
		}
		
		
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Data failed to fetch ....") ;

		
	}
	
//	@PostMapping("/getApprove/{scontainername}")
//	public ResponseEntity<?> approveStructure (final @PathVariable String scontainername){
//		final int row =structureService.approveStructure(scontainername);
//		
//		if(row>0) {
//			return ResponseEntity.ok("Node approved ...");
//		}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Node not approved ...") ;
//
//	}
	
//	@PostMapping("/getApprove/{scontainername}")
//	public ResponseEntity<?> approveStructure (final @PathVariable String scontainername, final @RequestBody StructureMapping struct ){
//		
//		 final List<String> duplicateName =
//	NodeValidator.findDuplicateName(struct.getNodedata());
//		
//		if(!duplicateName.isEmpty()) {
//			return ResponseEntity.badRequest().body(Map.of("message","Duplicate node name found","duplicates",duplicateName));
//		}
//		
//		final int row =structureService.approveStructure(scontainername,struct);
//		
//		if(row>0) {
//			return ResponseEntity.ok("Node approved ...");
//		}
//		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Node not approved ...") ;
//
//	}
	
	
	@PutMapping("/getApprove/{scontainername}")
	public ResponseEntity<?> approveStructure(
	        @PathVariable final String scontainername) {

	    try {

	        final int row = structureService.approveStructure(scontainername);

	        if (row > 0) {
	            return ResponseEntity.ok("Node approved...");
	        }

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body("Node not approved...");

	    } catch (final IllegalArgumentException e) {

	        return ResponseEntity
	                .status(HttpStatus.BAD_REQUEST)
	                .body(e.getMessage());

	    } catch (final Exception e) {

	        return ResponseEntity
	                .status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body("Error while approving node");
	    }
	}
	
	
}
