package phase2;

import global.PorterStemmer;
import global.Utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map.Entry;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

public class VectorSpaceGen {
	private static final String jsonFolder = "output/phase1";
	private static final String intermediateFilePath = "output/phase2/";
	private static final String intermediateFileName = "intermediate.txt";
	private static final String allWordsFile = "allWords.csv";
	private static HashMap<String, Integer> wordFreq;
	private static int freq[];
	private static int currFreqCnt;
	private static HashSet<String> stopWords;
	private static int maxFreq;
	private static PorterStemmer porterStemmer;
	private static PrintWriter intermediateFW;
	
	public static void main(String args[]){
		getMap();
	}
	private static void init(){
		wordFreq = new HashMap<String, Integer>();
		stopWords = Utils.getStopWords();
		porterStemmer = new PorterStemmer();
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
		freq = new int[maxFreq+1];
		try {
			PrintWriter fw = new PrintWriter(new File(intermediateFilePath+allWordsFile));
			for(Entry<String, Integer> e : wordFreq.entrySet()){
				freq[e.getValue()]++;
//				System.out.println(e.getKey() +" "+ e.getValue());
//				if(e.getValue()>1)
//					fw.println(e.getKey());
			}
			for(int i=0;i<maxFreq+1;i++){
				System.out.println(i + " " + freq[i]);
				fw.println(i + "," + freq[i]);
			}
			fw.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		intermediateFW.close();
	}
	

	private static void getFreq(File file) {
		HashMap<String, Integer> docWordFreq = new HashMap<String, Integer>();
		int currDocFreqCnt;
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
					s=s.replace("'", "");
					if(s.length()>1 && !stopWords.contains(s)){
						if(wordFreq.containsKey(s)){
							currFreqCnt = wordFreq.get(s);
							currFreqCnt++;
							if(currFreqCnt>maxFreq){
								maxFreq = currFreqCnt; 
							}
							wordFreq.put(s,currFreqCnt);
						}
						else {
							wordFreq.put(s, 1);
							
						}
						if(docWordFreq.containsKey(s)){
							currDocFreqCnt = docWordFreq.get(s);
							currDocFreqCnt++;	
							docWordFreq.put(s, currDocFreqCnt);		
						}
						else{
							docWordFreq.put(s, 1);	
						}
					}
				}
				intermediateFW.print(file.getName()+",");
				for(Entry<String, Integer> e: docWordFreq.entrySet()){
//					if(e.getValue()>1)
						intermediateFW.print(e.getKey()+":"+e.getValue()+",");
				}
				intermediateFW.println();
			}
		}
}
