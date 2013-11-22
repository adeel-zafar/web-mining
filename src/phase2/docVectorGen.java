package phase2;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

public class docVectorGen {
	
	public static final String InputPath = "output/phase2/";
	private static final String intermediateFileName = "intermediate.txt";
	private static final String allWordsFile = "allWords";
	private static final String docVector = "docVectors.csv";
	
	public static Vector<String> dim;
	public static BufferedReader bReader;
	public static PrintWriter fw;
	
	public static void main(String args[]){
		getDimensions();
		try {
			bReader = new BufferedReader(new FileReader(InputPath+intermediateFileName));
			fw = new PrintWriter(new File(InputPath+docVector));
			int sz;
			float cnt;
			HashMap<String, Double> hm = new HashMap<String, Double>();
			String str="";
			while((str=bReader.readLine())!=null){
				long colNo = 0;
				String arr[] = str.split(",");
				sz = arr.length;
				cnt=0;
				for(int i=1;i<sz;i++){
					String s[] = arr[i].split(":");
					hm.put(s[0], Double.parseDouble(s[1]));
					cnt+=(Double.parseDouble(s[1])*Double.parseDouble(s[1]));
				}
				cnt=(float) Math.sqrt(cnt);
//				printing file number
				fw.print("0 "+arr[0]+",");
				int sz1 = dim.size(),i;
				String s;
//				StringBuilder mainStr=new StringBuilder();
//				boolean flag=false;
				for(i=0;i<sz1;i++){
					s = dim.elementAt(i);
					if(hm.containsKey(s)){
						double ans=hm.get(s)/cnt;
						fw.print((i+1)+" "+ans+", ");
//						mainStr.append(ans+",");
//						flag=true;
//						fw.print(ans+",");
					}
//					else{
//						mainStr.append("0,");
//						fw.print("0,");
//					}
				}
//				s = dim.elementAt(i);
//				if(hm.containsKey(s)){
//					double ans=hm.get(s)/cnt;
//					fw.print((i+1)+" "+ans+", ");
//					mainStr.append(ans);
//					flag=true;
//					fw.print(ans);
//				}
//				else{
//					mainStr.append("0");
//					fw.print("0");
//				}
//				if(flag)
//					fw.println(mainStr.toString());
				fw.println();
				hm.clear();
			}
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	private static void getDimensions() {
		dim = new Vector<String>();
		BufferedReader br = null;
		try {
			br = new BufferedReader(new FileReader(InputPath+allWordsFile));
			String str;
			while((str=br.readLine())!=null){
				dim.add(str.toLowerCase());
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
	
	
}
