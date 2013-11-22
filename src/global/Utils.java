package global;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.HashSet;

public class Utils {
	public static JSONObject fileToJSON(String filePath) {
		JSONObject obj = null;
		
		JSONParser parser=new JSONParser();

		BufferedReader bufferedReader = null;
		try {
			bufferedReader = new BufferedReader(new FileReader(filePath));
			String line=null;
			StringBuilder JSONString= new StringBuilder();
			
			while(true){
				line=bufferedReader.readLine();
				
				if(line==null)
					break;
				
				JSONString.append(" "+line);
			}
			
			obj=(JSONObject)parser.parse(JSONString.toString());
			
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
		
		return obj;
	}
	
	public static HashSet<String> getStopWords(){
		HashSet<String> stopWords = new HashSet<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader("StopWords.txt"));
			String str;
			while((str=br.readLine())!=null){
				stopWords.add(str.toLowerCase());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally{
			try {
				if(br!=null)
					br.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return stopWords;
	}
	
}
