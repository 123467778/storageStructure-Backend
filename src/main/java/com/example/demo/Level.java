package com.example.demo;

import java.util.List;

import lombok.Data;

@Data
public class Level {
	    private Long id;
	    private Integer level;
	    private String nodeName;
	    private Integer quantity;
	    private Boolean isLeaf;
	    private Integer rows;
	    private Integer columns;
	    private Integer root_structure_id;
	    
	    private String icon;
	    
	    private String displayName;
	    
	    private List<Level> levels;

}
