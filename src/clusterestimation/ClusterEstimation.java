package clusterestimation;

import global.PorterStemmer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;



public class ClusterEstimation {
	private static final String jsonFolder = "output/phase1";
	
	private static HashMap<String, Integer> wordFreq;
	private static int freq[];
	private static int temp;
	private static HashSet<String> stopWords;
	private static int maxFreq;
	private static PorterStemmer porterStemmer;
	
	public static void main(String args[]){
		getMap();
	}
	private static void init(){
		wordFreq = new HashMap<String, Integer>();
		stopWords = new HashSet<String>();
		porterStemmer = new PorterStemmer();
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
		freq = new int[maxFreq+1];
		try {
			PrintWriter fw = new PrintWriter(new File("output/wordFreq.txt"));
			for(Entry<String, Integer> e : wordFreq.entrySet()){
				freq[e.getValue()]++;
				//System.out.println(e.getKey() +" "+ e.getValue());
				//fw.println(e.getKey() +" "+ e.getValue());
			}
			for(int i=0;i<maxFreq+1;i++){
				System.out.println(i + " " + freq[i]);
				fw.println(i + " " + freq[i]);
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
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
				String str = (String) obj.get("subject") + " " + obj.get("content");
				String[] strArr = str.split("[^\\w']+");
				for(String s : strArr){
					s=s.toLowerCase();
					porterStemmer.add(s.toCharArray(), s.length());
					porterStemmer.stem();
					s=porterStemmer.toString();
					if(s.length()!=1 && !stopWords.contains(s)){
						if(wordFreq.containsKey(s)){
							temp = wordFreq.get(s);
							temp++;
							if(temp>maxFreq){
								maxFreq = temp; 
							}
							wordFreq.put(s,temp);
							
						}
						else {
							wordFreq.put(s, 1);
						}
					}
				}
				
			}
		}
}
