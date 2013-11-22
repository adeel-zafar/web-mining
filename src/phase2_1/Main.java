package phase2_1;

import global.Utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONObject;

public class Main {
	static String inputFolderPath = "output/phase1";
	static String outputFilePath = "output/phase2_1/mallet_ip";
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		File output = new File(outputFilePath);
		
		if(!output.getParentFile().exists()){
			output.getParentFile().mkdirs();
		}
		
		File inputFolder = new File(inputFolderPath);
		
		File []files = inputFolder.listFiles();
		
		FileWriter fr = null;
		try {
			fr = new FileWriter(output);
			
			for(File f: files){
				JSONObject ob = Utils.fileToJSON(f.getAbsolutePath());
				String op = "";
				String id = (String)ob.get("id");
				String subject = ((String)ob.get("subject")).replace("\n", " ").trim();
				String content = ((String)ob.get("content")).replace("\n", " ").trim();
				
				op += id + "\tX\t"; 
				op += subject + " " + content + "\n";
				
				fr.write(op);
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

}
