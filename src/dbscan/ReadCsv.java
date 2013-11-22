package dbscan;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class ReadCsv {

	public static final String path="output/phase2";
	public static ArrayList< ArrayList<Double>> read(String file)
	{
		ArrayList< ArrayList<Double>> mainList=new ArrayList<ArrayList<Double>>();
		try{
		BufferedReader br = null;
		String line = "";
		String cvsSplitBy = ",";
		int count=0;
		ArrayList< Double> list=new ArrayList<Double>();
		br = new BufferedReader(new FileReader(file));
		while ((line = br.readLine()) != null) {
 			// use comma as separator
			
			String[] freq = line.split(cvsSplitBy);
			for (int i=0;i<freq.length;i++)
				list.add(Double.parseDouble(freq[i]));
			System.out.println(count++);
			mainList.add(list);
			list.clear();	
		}
		br.close();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return mainList;
	}
	
	
	public static void main(String args[])
	{
		
		ArrayList<ArrayList<Double>>list=ReadCsv.read(path+"/docVectors.csv");
		System.out.println(list.size());
	}
}
