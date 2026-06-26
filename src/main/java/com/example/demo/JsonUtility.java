package com.example.demo;

import java.util.Map;

import org.springframework.stereotype.Component;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

@Component
public class JsonUtility {
    private final ObjectMapper objectMapper;
    
    public JsonUtility (ObjectMapper objectMapper) {
    	this.objectMapper=objectMapper;
    }

    public String toJsonString(Map<String,Object> data){
        try{
            return objectMapper.writeValueAsString(data);
        }
        catch(Exception e){
            throw new RuntimeException(e);
        }
    }
    
    
    public Map<String,Object> fromJson(String json){
        try{
            return objectMapper.readValue(json,new TypeReference<Map<String,Object>>(){});
        }
        catch (Exception e){
            throw new RuntimeException(e);
        }
    }
}
