package com.example.demo;

import com.fasterxml.jackson.annotation.JsonProperty;

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

}
