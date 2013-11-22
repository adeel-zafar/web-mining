package phase4;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.regex.Pattern;

import cc.mallet.classify.Classification;
import cc.mallet.classify.Classifier;
import cc.mallet.classify.ClassifierTrainer;
import cc.mallet.classify.MaxEntTrainer;
import cc.mallet.classify.NaiveBayes;
import cc.mallet.classify.NaiveBayesTrainer;
import cc.mallet.classify.Trial;
import cc.mallet.pipe.CharSequence2TokenSequence;
import cc.mallet.pipe.CharSequenceLowercase;
import cc.mallet.pipe.FeatureSequence2FeatureVector;
import cc.mallet.pipe.Pipe;
import cc.mallet.pipe.SerialPipes;
import cc.mallet.pipe.Target2Label;
import cc.mallet.pipe.TokenSequence2FeatureSequence;
import cc.mallet.pipe.iterator.CsvIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;
import cc.mallet.util.Randoms;

public class Main {

	public static void main(String[] args) throws Exception {
		
		//input file
		String inputFilePath = "output/phase4_1/";
		String inputTrainFileName = "train.txt";
		String inputTestFileName = "test.txt";
		String inputFileName = "intermediate.txt";//"intermediate.txt";

		ArrayList<Pipe> pipeList = new ArrayList<Pipe>();

		pipeList.add( new CharSequenceLowercase() );
		pipeList.add( new CharSequence2TokenSequence(Pattern.compile("[\\p{L}\\p{N}_]+")));//\\p{L}[\\p{L}\\p{P}]+\\p{L}")) );
		pipeList.add(new TokenSequence2FeatureSequence());
		pipeList.add(new Target2Label());
		pipeList.add(new FeatureSequence2FeatureVector());

		InstanceList trainingInstanceList = new InstanceList (new SerialPipes(pipeList));
		Reader trainFileReader = new InputStreamReader(new FileInputStream(new File(inputFilePath+inputFileName)), "UTF-8");
		trainingInstanceList.addThruPipe(new CsvIterator (trainFileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
											   3, 2, 1)); // data, label, name fields
		
		/*InstanceList testingInstanceList = new InstanceList (trainingInstanceList.getPipe());        
		Reader testFileReader = new InputStreamReader(new FileInputStream(new File(inputFilePath+inputTestFileName)), "UTF-8");
		trainingInstanceList.addThruPipe(new CsvIterator (testFileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
				   3, 2, 1)); // data, label, name fields
*/
		InstanceList[] instanceLists =
				trainingInstanceList.split(new Randoms(),
		                    new double[] {0.5, 0.5});
		
		ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
		Classifier classifier = trainer.train(instanceLists[0]);
		
		
		for(Instance i:instanceLists[1])
		{
			if((Integer)i.getTarget()!=0)
			{	
				System.out.println(i.getName() +"			"+i.getTarget()	);
				int h=classifier.classify(i).getLabeling().getBestIndex();
				System.out.println(classifier.classify(i).getLabeling().getBestLabel());
			}
			
		}
//		ArrayList<Classification> arr = classifier.classify(instanceLists[1]);
//		for(Classification c : arr){
//			System.out.println(c.toString()+" "+c.getLabeling());
//		}
//		
		
		// Trial trail = new Trial(classifier, instanceLists[1]);
//        System.out.println(classifier.getAccuracy(instanceLists[1]));
//        System.out.println(instanceLists[1].size());
//        System.out.println(classifier.getLabelAlphabet().toString());
//      
            
//  System.out.println(classifier.getPrecision(instanceLists[1], labeling));
        /*ClassifierTrainer trainer = new NaiveBayesTrainer();
        Classifier classifier = trainer.train(trainingInstanceList);
        System.out.println("Accuracy: " + classifier.getAccuracy(testingInstanceList));*/
		
		
		
		
		
		
		
		
		
		
		//testInstances.addThruPipe(new CsvIterator (testfileReader, Pattern.compile("^(\\S*)[\\s,]*(\\S*)[\\s,]*(.*)$"),
		//		   3, 2, 1)); // data, label, name fields

		// Create a model with 100 topics, alpha_t = 0.01, beta_w = 0.01
		//  Note that the first parameter is passed as the sum over topics, while
		/*//  the second is 
		int numTopics = 20;
		int topWordsNum =25;
		String dataName= "topicModelDemo";
		
		//generate model path
		String modelPath="model/" + dataName + "_" + Integer.toString(numTopics) +"t_model.model";
		String topWordPath=dataName +"_"+Integer.toString(numTopics)+"t_top"+ Integer.toString(topWordsNum)+"word.csv" ;
		String topicDistrPath=dataName +"_"+Integer.toString(numTopics)+"t_dt.csv" ;
		
		ParallelTopicModel model = new ParallelTopicModel(numTopics, (double)50.0/numTopics , 0.01);
		
		//add training data into model
		model.addInstances(instances);

		// Use two parallel samplers, which each look at one half the corpus and combine
		//  statistics after every iteration.
		model.setNumThreads(2);

		// Run the model for 50 iterations and stop (this is for testing only, 
		//  for real applications, use 1000 to 2000 iterations)
		model.setNumIterations(1000);
		model.estimate();
		
*/		//write model to file
		try{
		//	model.write(new File(modelPath));
		}catch(Exception e){
			e.printStackTrace();
		}
		
		/*//get top words form model
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(topWordPath);
			  BufferedWriter out = new BufferedWriter(fstream);
			  
			  ArrayList<TreeSet<IDSorter>> topicSortedWords = model.getSortedWords();
			  Alphabet dataAlphabet = instances.getDataAlphabet();
			  
		      // Show top 5 words in topics with proportions for the first document
		      for (int topic = 0; topic < model.numTopics; topic++) {
		            Iterator<IDSorter> iterator = topicSortedWords.get(topic).iterator();
		            
		            int rank = 0;
		            while (iterator.hasNext() && rank < topWordsNum) {
		                IDSorter idCountPair = iterator.next();
		                out.write(topic+"," + dataAlphabet.lookupObject(idCountPair.getID()) + "," 
		                +idCountPair.getWeight() +"\n");
		                rank++;
		                
		            }
		        }		  
			  //Close the output stream
			  out.close();
			  
			  
	    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
	    }
		

		
		
		//get topic distribution for train data
		try{
			  // Create file 
			  FileWriter fstream = new FileWriter(topicDistrPath);
			  BufferedWriter out = new BufferedWriter(fstream);
			  
			  double[] topicsProb;
			  
			  for(int i=0; i < instances.size(); i++){
				  
				  topicsProb = model.getTopicProbabilities(i);
				  out.write((String)instances.get(i).getName() + ",");
				  
				  for(int j=0; j < topicsProb.length ; j++){
					  out.write(topicsProb[j] + ",");
				  }				  
				  out.write("\n");
		  
			  }
			  //Close the output stream
			  out.close();
			  
			  
	    }catch (Exception e){//Catch exception if any
			  System.err.println("Error: " + e.getMessage());
	    }*/
		/*	
		//run inference, get topic distribution for testing data
		try{
			// Create file 
		    FileWriter fstream = new FileWriter("2012-website-data-V5xxxxxxx.csv");
			BufferedWriter out = new BufferedWriter(fstream);
					  
			TopicInferencer inferencer = model.getInferencer();
			double[] topicsProb;
					  
			for(int i=0; i < testInstances.size(); i++){
						  
			    topicsProb = inferencer.getSampledDistribution(testInstances.get(i), 100, 1, 20);
				out.write((String)testInstances.get(i).getName() + ",");
						  
				for(int j=0; j < topicsProb.length ; j++){
							  out.write(topicsProb[j] + ",");
						  }				  
						  out.write("\n");
				  
					  }
					  //Close the output stream
					  out.close();
					  
					  
			    }catch (Exception e){//Catch exception if any
					  System.err.println("Error: " + e.getMessage());
			    }
		*/
	}

}