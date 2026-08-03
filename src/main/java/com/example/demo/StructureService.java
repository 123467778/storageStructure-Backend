package com.example.demo;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ObjectMapper;

@Service
public class StructureService {
	
	private final StructureRepo structRepo;
	
	
	@Autowired
	private JdbcTemplate jdbcTemplate;

	@Autowired
	private ObjectMapper objectMapper;
	
	
	
	public StructureService(final StructureRepo structRepo ) {
		this.structRepo=structRepo;
	}

//	public void addStructure(Structure struct) {
//		structRepo.addStructure(struct);
//		
//	}



	public int createStructure( final Hierarchical struct) {
		
		
		
		
		 final int count = structRepo.createStructure(struct);	
		 return count;
	}

	public List<Hierarchical> getHierarchical() {
		
		final List <Hierarchical>structures =structRepo.getHierarchical();
		
		return structures;
	}

	public List<Level> getHierarchicalByName(final String hierarchicalName) {
		final List<Level> struct = structRepo.getHierarchicalByName(hierarchicalName);
		System.out.println(struct);

		return struct;
		
	}

	public int  mapStructure(final StructureMapping map) {
		
		return structRepo.mapStructure(map);
		
	}

	public List<StructureMapping> getMapStructures() {
		
		return structRepo.getMapStructures();
	}

//	public int editNode( final StructureMapping struct,final String containerName) {
//		return structRepo.editNode(struct,containerName);
//		
//	}

	
//	public ResponseEntity<?> getEditNode(final String scontainerName, final String shierarchicalName) {
//
//	    final Map<String, Object> edited =
//	            structRepo.getEditNode(scontainerName);
//
//	    if (edited != null) {
//	        return ResponseEntity.ok(edited);
//	    }
//
//	    final List<Level> originalTree =
//	            structRepo.getHierarchicalByName(shierarchicalName);
//
//	    return ResponseEntity.ok(originalTree);
//	}
	
//	public StructureMapping getEditNode(final String scontainerName){
//		return structRepo.getEditNode(scontainerName);
//	}

	public Map<String,Object> getNodeData(final String scontainername) {
		return structRepo.getNodeData(scontainername);
	}

	public int editNode(final String scontainername,final StructureMapping struct) {
		
		
		
		return structRepo.editNode(scontainername,struct);
	}

	public List<Map<String, Object>> getAllTreeStructure() {
		return structRepo.getAllTreeStructure();
	}

//	public int approveStructure(final String scontainername) {
//		return structRepo.approveStructure(scontainername);
//	}
//	
	
//	public int approveStructure(final String scontainername, final StructureMapping struct) {
//		return structRepo.approveStructure(scontainername,struct);
//	

//	public int approveStructure(final String scontainername) {
//		
//		final List<Map<String,Object>> getAllTree = structRepo.getAllTreeStructure();
//		final Map<String,Integer> nodeNameCount = new HashMap<>();
//		
//		for(final Map<String,Object> tree :getAllTree) {
//			final List<String> duplicates = NodeValidator.findDuplicateName(getAllTree);
//			
//			for(final String name : duplicates) {
//				nodeNameCount.put(name,nodeNameCount.getOrDefault(name,0)+1);
//			}
//			
//		}
//		
//		if(!nodeNameCount.isEmpty()) {
//			throw new IllegalArgumentException("Duplicate node name found:" +nodeNameCount.keySet());
//		}
//		
//		
//		
//		return structRepo.approveStructure(scontainername);
//	}
	
//	public int approveStructure(final String scontainername) {
//
//	    final List<Map<String,Object>> allTrees =
//	            structRepo.getAllTreeStructure();
//
//
//	    final List<String> duplicates =
//	            NodeValidator.findDuplicateName(allTrees);
//
//
//	    if (!duplicates.isEmpty()) {
//
//	        throw new IllegalArgumentException(
//	            "Duplicate node name found: " + duplicates
//	        );
//	    }
//
//
//	    return structRepo.approveStructure(scontainername);
//	}
	
//	public int approveStructure(final String scontainername) {
//
//	    final List<Map<String,Object>> allTrees =
//	            structRepo.getAllTreeStructure();
//
//
//	    final List<String> duplicates =
//	            NodeValidator.findDuplicateName(allTrees,scontainername);
//	    
//	    System.out.println(duplicates);
//
//
//	    if (!duplicates.isEmpty()) {
//	    	
//	    	
//	        throw new IllegalArgumentException(
//	                "Duplicate node found: " + duplicates
//	        );
//	    }
//
//
//	    return structRepo.approveStructure(scontainername);
//	}
	
	
//	
//	public int approveStructure(final String scontainername) {
//
//	    final List<Map<String, Object>> allTrees =
//	            structRepo.getAllTreeStructure();
//
//	    final List<String> duplicates =
//	            NodeValidator.findDuplicateName(allTrees, scontainername);
//
//	    if (!duplicates.isEmpty()) {
//	        throw new IllegalArgumentException(
//	                "Duplicate node found: " + duplicates);
//	    }
//
//	    return structRepo.approveStructure(scontainername);
//	}
	
	
	
	
	
	
	


	public int approveStructure(final String scontainername) throws Exception {


	    final String currentTreeJson = jdbcTemplate.queryForObject(
	            "SELECT nodedata FROM structuremapping WHERE scontainername = ?",
	            String.class,
	            scontainername
	    );
		


	    final Set<String> currentNames = new HashSet<>();

	    final JsonNode currentTree =
	            objectMapper.readTree(currentTreeJson);


	    collectNames(
	            currentTree.get("tree"),
	            currentNames
	    );


	    // Get all other structures
	    final List<String> otherTrees = jdbcTemplate.queryForList(
	            "SELECT nodedata FROM structuremapping WHERE scontainername <> ?",
	            String.class,
	            scontainername
	    );


	    final Set<String> duplicate = new HashSet<>();


	    for(final String json : otherTrees) {

	        final JsonNode tree =
	                objectMapper.readTree(json);


	        checkDuplicate(
	                tree.get("tree"),
	                currentNames,
	                duplicate
	        );
	    }


	    if(!duplicate.isEmpty()) {

	        throw new IllegalArgumentException(
	                "Duplicate node found: " + duplicate
	        );
	    }


	    // Approve after validation
	    final String sql =
	            "UPDATE structuremapping " +
	            "SET status='approved' " +
	            "WHERE scontainername=?";


	    return jdbcTemplate.update(sql, scontainername);
	}
	
	
	
	
	
	private void collectNames(
	        final JsonNode nodes,
	        final Set<String> names) {


	    if(nodes == null || !nodes.isArray()) {
			return;
		}


	    for(final JsonNode node : nodes) {


	        final JsonNode displayName =
	                node.get("displayName");


	        if(displayName != null) {

	            names.add(
	                displayName.asText()
	                .trim()
	                .toLowerCase()
	            );
	        }


	        collectNames(
	                node.get("children"),
	                names
	        );
	    }
	}
	
	
	
	private void checkDuplicate(
	        final JsonNode nodes,
	        final Set<String> currentNames,
	        final Set<String> duplicate) {


	    if(nodes == null || !nodes.isArray()) {
			return;
		}


	    for(final JsonNode node : nodes) {


	        final JsonNode displayName =
	                node.get("displayName");


	        if(displayName != null) {

	            final String name =
	                    displayName.asText()
	                    .trim()
	                    .toLowerCase();


	            if(currentNames.contains(name)) {

	                duplicate.add(
	                    name.toUpperCase()
	                );
	            }
	        }


	        checkDuplicate(
	                node.get("children"),
	                currentNames,
	                duplicate
	        );
	    }
	}
	
	
}
