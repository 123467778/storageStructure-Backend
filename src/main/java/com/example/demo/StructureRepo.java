package com.example.demo;

import java.util.List;
import java.util.Map;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;




@Repository
public class StructureRepo {

private final JdbcTemplate jdbcTemplate;
private final JsonUtility jsonUtility;
	
	public StructureRepo (final JdbcTemplate jdbcTemplate ,final JsonUtility jsonUtility ) {
		this.jdbcTemplate=jdbcTemplate;
		this.jsonUtility=jsonUtility;
	}

//	public int createStructure(Structure struct ,Level level ) {
//		String sql = "INSERT INTO storage_structure (structure_name,node_id,node_name,level,quantity,rows,columns,isleaf,category_id) VALUES (?,?,?,?,?,?,?,?,?)";
//
//		return jdbcTemplate.update(sql, struct.getStructureName(),level.getNodeId(),level.getNodeName(),level.getLevel(),level.getQuantity(),level.getRows(),
//				level.getColumns(),level.isLeaf(),struct.getCategoryId());
//		
//	}
	
	

	public int createStructure(final Hierarchical struct) {
		
		

		
		final String sql = "INSERT INTO hierarchical ( shierarchicalname , sdescription) VALUES (?,?)";
//		String structure = "Insert into structure (snodename,nlevel,ncount,nrows,ncolumns,isleaf,nhierarchicalid) values(?,?,?,?,?,?,?)";
  
		int count=0;
		
		 count+= jdbcTemplate.update(sql,struct.getHierarchicalName(),struct.getDescription());
		 final String selectSql =
				    "SELECT nhierarchicalid FROM hierarchical WHERE shierarchicalname = ?";
		 
		 final Integer hierarchicalId = jdbcTemplate.queryForObject(
			        selectSql,
			        Integer.class,
			        struct.getHierarchicalName()
			      
			);
		 final String structure = "Insert into structure (snodename,displayName,nlevel,ncount,nrows,ncolumns,isleaf,icon,nhierarchicalid) values(?,?,?,?,?,?,?,?,?)";
		 
		 for (final Level level : struct.getData().getLevels()) {

			  


			    count += jdbcTemplate.update(
			            structure,
			            level.getNodeName(),
			            level.getDisplayName(),
			            level.getLevel(),
			            level.getQuantity(),
			            level.getRows(),
			            level.getColumns(),
			            level.getIsLeaf(),
			            level.getIcon(),
			            hierarchicalId
			            
			           
			          
			            
			    );
			   
			}

		
		   
		    return count;
		   
	}

	public List<Hierarchical> getHierarchical() {
	    final String sql = "select nhierarchicalid, shierarchicalname, sdescription from hierarchical";

	    return jdbcTemplate.query(sql, (rs, rowNum) -> {
	        final Hierarchical h = new Hierarchical();
	        h.setHierarchicalName(rs.getString("shierarchicalname"));
	        h.setDescription(rs.getString("sdescription"));
	        h.setId(rs.getInt("nhierarchicalid"));
	        System.out.println(h);
	        return h;
	    });
	}

	public List<Level> getHierarchicalByName(final String hierarchicalName) {
		final String selectSql =
			    "SELECT nhierarchicalid FROM hierarchical WHERE shierarchicalname = ?";
	 
	 final Integer hierarchicalId = jdbcTemplate.queryForObject(
		        selectSql,
		        Integer.class,
		        hierarchicalName
		     
		);
	 
	 final String getStruct = "select * from structure where nhierarchicalid=?";
	 
	 
	 return jdbcTemplate.query(
			    getStruct,
			    (rs, rowNum) -> {
			        final Level l = new Level();
			        l.setNodeName(rs.getString("snodename"));
			        l.setDisplayName(rs.getString("displayName"));
			        l.setQuantity(rs.getInt("ncount"));
			        l.setLevel(rs.getInt("nlevel"));
			        l.setRows(rs.getInt("nrows"));
			        l.setColumns(rs.getInt("ncolumns"));
			        l.setIsLeaf(rs.getBoolean("isleaf"));
			        l.setIcon(rs.getString("icon"));
			        return l;
			    },
			    hierarchicalId
			);

	
	}

	public int mapStructure(final StructureMapping map) {
		
	
		
		final String insert = "Insert into structuremapping(scontainername,sdescription,nhierarchicalid,nodedata)values(?,?,?, ?::jsonb)";
		
		return jdbcTemplate.update(insert,map.getScontainername(),map.getSdescription(),map.getNhierarchicalid(),jsonUtility.toJsonString(map.getNodedata()));
		
	}

	public List<StructureMapping> getMapStructures() {

	    final String query =
	        "SELECT sm.scontainername, " +
	        "sm.sdescription, " +
	        "h.shierarchicalname " +
	        "FROM structuremapping sm " +
	        "INNER JOIN hierarchical h " +
	        "ON sm.nhierarchicalid = h.nhierarchicalid " +
	        "WHERE sm.nstatus = 1 order by  1 desc";

	    return jdbcTemplate.query(query, (rs, rowNum) -> {

	        final StructureMapping map = new StructureMapping();

	        map.setScontainername(rs.getString("scontainername"));
	        map.setSdescription(rs.getString("sdescription"));
	        map.setShierarchicalname(rs.getString("shierarchicalname"));

	        return map;
	    });
	}

	public Map<String,Object> getNodeData(final String scontainername) {
		
		final String sql = "select nodedata from structuremapping where scontainername =? ";
		
		final String json = jdbcTemplate.queryForObject(sql,String.class,scontainername );
		
		System.out.println("json" +json);
		System.out.println(scontainername);
		
		try {
			return jsonUtility.fromJson(json);
		}
		catch(final Exception e) {
			throw new RuntimeException("error in json data");
		}
	}

	public int editNode(final String scontainername,final StructureMapping struct) {
		final String sql = "update structuremapping set nodedata=?::jsonb where scontainername = ? ";
		System.out.println("Updated Data" +jsonUtility.toJsonString(struct.getNodedata()));
		System.out.println("Struct object: " + struct);
		System.out.println("Node Data: " + struct.getNodedata());
		System.out.println(
		    "JSON: " + jsonUtility.toJsonString(struct.getNodedata())
		);
		return jdbcTemplate.update(sql,jsonUtility.toJsonString(struct.getNodedata()),scontainername);
	}

//	public int editNode( final StructureMapping struct,final String containerName ) {
//		
//		System.out.println(jsonUtility.toJsonString(struct.getNodedata()));
//		
//		final String select ="select nmapid from structuremapping where scontainername=?";
//		final int containerId = jdbcTemplate.queryForObject(select,Integer.class,containerName);
//		
//		
//		System.out.println("Id" +containerId);
//		
//		
//		
//		final String sql = "update structuremapping set nodedata=?::jsonb where nmapid = ? ";
//		
//		return jdbcTemplate.update(sql,jsonUtility.toJsonString(struct.getNodedata()),containerId);
//		
//		
//	}

//	public Map<String, Object> getEditNode(final String containerName) {
//
//	    final String sql = "SELECT nodedata FROM structuremapping WHERE scontainername = ? order by  1 desc";
//
//	    final List<Map<String, Object>> list = jdbcTemplate.query(
//	        sql,
//	        (rs, rowNum) -> jsonUtility.fromJson(rs.getString("nodedata")),
//	        containerName
//	    );
//
//	    System.out.println(list.get(0));
//	    
//	    
//	    return list.isEmpty() ? null : list.get(0);
//	}
//	
//	public StructureMapping getEditNode(final String scontainerName) {
//	    final String sql = "select nodedata from structuremapping where scontainername = ?";
//
//	    return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
//	        final StructureMapping s = new StructureMapping();
//
//	        final String nodeDataJson = rs.getString("nodedata");
//
//	        final Map<String, Object> nodeData = jsonUtility.fromJson(nodeDataJson);
//
//	        s.setNodedata(nodeData);
//	        System.out.println(nodeData);
//
//	        return s;
//	    }, scontainerName);
//	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}