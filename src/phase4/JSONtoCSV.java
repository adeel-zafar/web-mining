package phase4;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class JSONtoCSV {
	
	private static final String jsonFolder = "output/phase4";
	private static final String intermediateFilePath = "output/phase4_1/";
	private static final String intermediateFileName = "intermediate.txt";
	
	private static PrintWriter intermediateFW;
	
	public static void main(String args[]){
		getMap();
	}
	private static void init(){
		
		try {
			intermediateFW = new PrintWriter(new File(intermediateFilePath+intermediateFileName));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static void getMap(){
		init();
		File folder = new File(jsonFolder);
		File[] listOfFiles = folder.listFiles();
		for (int i = 0; i < listOfFiles.length; i++) {
			
			if (listOfFiles[i].isFile()) {
				getFreq(listOfFiles[i]);
			}
		}
		
		intermediateFW.close();
		
	}
	

	private static void getFreq(File file) {
		JSONParser parser=new JSONParser();
		JSONObject obj = null;
			BufferedReader bufferedReader = null;
			try {
				bufferedReader = new BufferedReader(new FileReader(file));
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
			if(obj!=null){
				
				/*long topicNumber = (Long)obj.get("topic");
				double con_flesch = Double.parseDouble((String)obj.get("con_flesch"));
				double sub_fog = Double.parseDouble((String)obj.get("sub_fog"));
				double con_fog = Double.parseDouble((String)obj.get("con_fog"));
				String userid = (String)obj.get("userid");
				String id = (String)obj.get("id");
				double sub_flesch = Double.parseDouble((String)obj.get("sub_flesch"));
				int con_len = Integer.parseInt((String)obj.get("con_len"));
				int month = Integer.parseInt((String)obj.get("month"));
				int bin = Integer.parseInt((String)obj.get("bin"));
				int day = Integer.parseInt((String)obj.get("day"));
				int sub_len = Integer.parseInt((String)obj.get("sub_len"));*/
//				if(!((String) obj.get("bin")).equals("0"))
				{
					
				
			String str = ((String) obj.get("id") + " "
					+ (String) obj.get("bin") + " " + (Long) obj.get("topic")
					+ " " + (String) obj.get("con_flesch") + " "
					+ (String) obj.get("sub_fog") + " "
					+ (String) obj.get("con_fog") + " "
					+ (String) obj.get("userid") + " "
					+ (String) obj.get("sub_flesch") + " "
					+ (String) obj.get("con_len") + " "
					+ (String) obj.get("month") + " " + (String) obj.get("day")
					+ " " + (String) obj.get("sub_len"));

				intermediateFW.println(str);
				
			}
			}
	}
}

