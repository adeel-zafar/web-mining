package phase4;

import global.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.simple.JSONObject;

import com.representqueens.lingua.en.Fathom;
import com.representqueens.lingua.en.Fathom.Stats;
import com.representqueens.lingua.en.Readability;

public class MergePh1Ph3 {
	static String inputFolderPath = "output/phase1";
	static String outputFolderPath = "output/phase4";
	static String docTopicFile = "output/phase3_1/documentid_topics";
	
	static Map<String, Integer> docTopic = new HashMap<String, Integer>();
	
	@SuppressWarnings("unchecked")
	public static void main(String []args){
		BufferedReader br = null;
		
		try {
			File f = new File(docTopicFile);
			br = new BufferedReader(new FileReader(f));
			String line;
			while ((line = br.readLine()) != null) {
				String []fs = line.split("\\s+");
				docTopic.put(fs[0], Integer.parseInt(fs[1]));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if(br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		File input = new File(inputFolderPath);
		File []jsons = input.listFiles();
		
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		for(File f: jsons) {
			JSONObject obj = Utils.fileToJSON(f.getAbsolutePath());
			
			String id = (String)obj.get("id");
			
			// Set topic
			obj.put("topic", docTopic.get(id));
			
			String type = (String)obj.get("type");
			
			String timeAsked = (String)obj.get("timestamp");
			String timeAnswered = (String)obj.get("answer");
			String cat = "10";
			Calendar c = Calendar.getInstance(); 
			if(type.compareToIgnoreCase("answered") == 0){
				long t1 = Long.parseLong(timeAsked);
				long t2 = Long.parseLong(timeAnswered);
				
				long diff = t2 - t1;
				long sec = (diff / 1000 ) % 60;
				long min = (diff / (60 * 1000)) % 60;
				long hr = diff / (60 * 60 * 1000);
		        long day = (diff / (60 * 60 * 1000 * 24));
				
				if (day > 2) {
					cat = "15";
				} else if (day > 1) {
					cat = "14";
				} else if (hr > 8) {
					cat = "13";
				} else if (hr > 4) {
					cat = "12";
				} else if (hr > 2) {
					cat = "11";
				} else if (hr > 1) {
					cat = "10";
				} else if (min > 30) {
					cat = "9";
				} else if (min > 15) {
					cat = "8";
				} else if (min > 10) {
					cat = "7";
				} else if (min > 5) {
					cat = "6";
				} else if (min > 3) {
					
					cat = "5";
				} else if (min > 2) {
					cat = "4";
				} else if (min > 1) {
					cat = "3";
				} else if (sec > 30) {
					cat = "2";
				} else if (sec > 10) {
					cat = "1";
				} else {
					cat = "0";
				}
				
				
				/*if(cat.compareTo("10") == 0)
					System.out.println(id + "|" + day + ":" + hr + ":" + min + ":" + sec);*/
			}
			
			// set category
			obj.put("bin", cat);
			
			// set the Day and month of asking the question
			String date = (String)obj.get("date");
			
			Calendar c1 = Calendar.getInstance();
			try {
				Date d = dateFormat.parse(date);
				c1.setTime(d);
				
				int dow = c1.get(Calendar.DAY_OF_WEEK);
				int month = c1.get(Calendar.MONTH);
				
				obj.put("day", ""+dow);
				obj.put("month", ""+month);
				
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			// set question length 
			String ques = (String)obj.get("subject");
			ques = ques.trim().replace("\n", " ");
			if(ques != null && ques.length() > 0){
				Stats  st = Fathom.analyze(ques);
				double flesch = Readability.calcFlesch(st);
				double fog = Readability.calcFog(st);
				
				obj.put("sub_len", ""+ques.length());
				obj.put("sub_fog", "" + fog);
				obj.put("sub_flesch", "" + flesch);
			}
			else{
				obj.put("sub_len", "0");
				obj.put("sub_fog", "100");
				obj.put("sub_flesch", "0");
			}
			
			// set Description length
			String dec = (String)obj.get("content");
			dec = dec.trim().replace("\n", " ");
			if(dec != null && dec.length() > 0) {
				Stats  st = Fathom.analyze(dec);
				double flesch = Readability.calcFlesch(st);
				double fog = Readability.calcFog(st);
				
				Readability.calcFlesch(st);
				obj.put("con_len", ""+dec.length());
				obj.put("con_fog", "" + fog);
				obj.put("con_flesch", "" + flesch);
			} else {
				obj.put("con_len", "0");
				obj.put("con_fog", "100");
				obj.put("con_flesch", "0");
			}
			
			String outputPath = outputFolderPath + File.separator + f.getName();
			
			File op = new File(outputPath);
			
			if(!op.getParentFile().exists()){
				op.getParentFile().mkdirs();
			}
			

			FileWriter fr = null;
			try {
				fr = new FileWriter(op);
				fr.write(obj.toString());
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
}
