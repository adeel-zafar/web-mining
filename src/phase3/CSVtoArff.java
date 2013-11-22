package phase3;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class CSVtoArff {	
	static String vectorFileName = "output/phase2/docVectors.csv";
	static String wordFileName = "output/phase2/allWords";
	
	static String outputFileName = "output/phase3/vectors.arff";
	
	public static void main(String[] args) {
		File vectorFile = new File(vectorFileName);
		File wordFile = new File(wordFileName);
		
		File outputFile = new File(outputFileName);
		
		if(!outputFile.getParentFile().exists()){
			outputFile.getParentFile().mkdirs();
		}
		
		FileWriter fw = null;
		try {
			fw = new FileWriter(outputFile);
			fw.write("% Normalized document vectors for yahoo answers\n");
			fw.write("@RELATION yahoo\n\n");
			fw.write("@ATTRIBUTE\tdocid\tstring");
			
			BufferedReader bufferedReader = null;
			
			// Read the words as dimensions
			try {
				bufferedReader = new BufferedReader(new FileReader(wordFile));
				String line=null;
				
				while((line=bufferedReader.readLine()) != null)
					fw.write("\n@ATTRIBUTE\t" + line + "\tNUMERIC");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally{
				if(bufferedReader != null)
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
			fw.write("\n\n@DATA");
			
			// copy each line of vector file
			try {
				bufferedReader = new BufferedReader(new FileReader(vectorFile));
				String line=null;
				
				while((line=bufferedReader.readLine()) != null)
					fw.write("\n{"+line.substring(0, line.length()-3)+"}");
				
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				if(bufferedReader != null)
					try {
						bufferedReader.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
			}
			
			fw.write("\n");
		} catch (IOException e) {
			e.printStackTrace();
		} finally{
			if(fw != null){
				try {
					fw.close();
				} catch (IOException e) {
					e.printStackTrace();
				}			
			}
		}

	}

}
