package com.example.demo;
import java.util.Map;

import org.springframework.data.annotation.Transient;

import lombok.Data;

@Data
public class StructureMapping {
	
	private int nmapid;
	private String scontainername;
	private String  sdescription ;
	private Map<String , Object> nodedata ;
	private int nhierarchicalid;
	
	@Transient
    private String shierarchicalname;
    private Integer nstatus;

}
