package com.example.demo;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class StructureService {
	
	private final StructureRepo structRepo;
	
	public StructureService(final StructureRepo structRepo ) {
		this.structRepo=structRepo;
	}

//	public void addStructure(Structure struct) {
//		structRepo.addStructure(struct);
//		
//	}



	public void createStructure( final Hierarchical struct) {
		 final int count = structRepo.createStructure(struct);	
		 System.out.println(count);
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



	
	
	

}
