package phase2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;


public class EntityMerger {
	static String outputFolder;
	static String inputFolder;
	
	static long fileNo = 0;
	
	static HashMap<String, Long> indexHash = new HashMap<String, Long>();
	
	public static void main(String args[]){
		outputFolder = "../output1/phase2";
		inputFolder = "../output1/phase1";
		String indexFileName = inputFolder + "/index";
		
		File index = new File(indexFileName);
		
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(index));
			String line;
			while ((line = br.readLine()) != null) {
			   String []params = line.split("###");
			   String entityType = params[0];
			   String []files = params[1].trim().split(" ");
			   
			   ArrayList<JSONObject> entities = getEntites(files);
			   
			   if(!entityType.equals("none_cat"))
				   mergeEntitiesToFile(entityType, entities);
			   
			   /*System.out.println(params[0]);
			   
			   for(String file : files){
				   System.out.println("\t|"+file+"|");
			   }*/
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			if(br != null){
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		// write index
		File f = new File(outputFolder +
						  File.separator +
						  "index");
		
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		
		FileWriter fr = null;
		try {
			fr = new FileWriter(f);
			
			for(Map.Entry<String, Long> entry : indexHash.entrySet()){
				String typ = entry.getKey();
				Long l = entry.getValue();
				
				String write = typ + "|" +l+"\n";
				fr.write(write);
			}			
			
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
	}
	
	private static ArrayList<JSONObject> getEntites(String []files){
		ArrayList<JSONObject> entities = new ArrayList<JSONObject>();
		JSONParser parser=new JSONParser();
		
		// open each file and load each into a JSONObject
		for(String str: files){
			
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(inputFolder + 
																	File.separator + 
																	str + 
																	".json"));
				String line=null;
				StringBuilder JSONString= new StringBuilder();
				
				while(true){
					line=bufferedReader.readLine();

					
					if(line==null)
						break;
					
					JSONString.append(" "+line);
				}
				
				JSONObject obj=(JSONObject)parser.parse(JSONString.toString());
				entities.add(obj);
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (ParseException e) {
				e.printStackTrace();
			}finally{
				if(bufferedReader != null)
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
		}
		
		return entities;
	}
	
	@SuppressWarnings("unchecked")
	private static void mergeEntitiesToFile(String entityType, 
												ArrayList<JSONObject> entities){
		TreeSet<String> ts = new TreeSet<String>();
		
		// get a set of occurring attributes 
		for(JSONObject json : entities){
			Set<String> s = json.keySet();
			ts.addAll(s);
		}
		
		// merge values of all attributes
		JSONObject json = new JSONObject();
		//json.put("entity_type", entityType);
		//json.put("type_count", entities.size());
		
		indexHash.put(entityType, fileNo);
		
		for(String s : ts){
			if(s.equals("ex_infobox"))
				continue;
			
			
			JSONArray arr = new JSONArray();
			
			for(JSONObject i : entities){
				if(i.containsKey(s)){
					arr.add(i.get(s).toString());
				}
			}
			
			json.put(s, arr);
		}
		
		// write JSON to file
		File f = new File(outputFolder +
							File.separator +
							fileNo++ + 
							".json");
		
		if(!f.getParentFile().exists()){
			f.getParentFile().mkdirs();
		}
		
		FileWriter fr = null;
		try {
			fr = new FileWriter(f);
			fr.write(json.toString());
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fr != null){
				try {
					fr.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}
		
	}
}
