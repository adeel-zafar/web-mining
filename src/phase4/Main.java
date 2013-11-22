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
import cc.mallet.classify.DecisionTree;
import cc.mallet.classify.DecisionTreeTrainer;
import cc.mallet.classify.MaxEnt;
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
import cc.mallet.types.CrossValidationIterator;
import cc.mallet.types.Instance;
import cc.mallet.types.InstanceList;

import cc.mallet.types.Label;
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
		                    new double[] {0.9, 0.1,0.0});
		CrossValidationIterator crossValidationIterator = new CrossValidationIterator(trainingInstanceList,10);
		while(crossValidationIterator.hasNext()){
			InstanceList[] instanceCrossLists = crossValidationIterator.next();
			ClassifierTrainer<DecisionTree> trainer = new DecisionTreeTrainer();
//			ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
//			ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
			Classifier classifier = trainer.train(instanceCrossLists[0]);
			
		/*	for(Instance i:instanceLists[1])
			{
				
					System.out.println(i.getName() +"			"+i.getTarget()	);
					int h=classifier.classify(i).getLabeling().getBestIndex();
					System.out.println(classifier.classify(i).getLabeling().getBestLabel());
//					System.out.println(classifier.classify(i).getLabelVector());
					
			}
		*/	/*ArrayList<Classification> arr = classifier.classify(instanceLists[1]);
			for(Classification c : arr){
				System.out.println(" "+c.getLabeling());
			}*/
			
			
			// Trial trail = new Trial(classifier, instanceLists[1]);
	        System.out.println(classifier.getAccuracy(instanceCrossLists[1]));
	    		
		}
//		InstanceList[] instanceLists =CrossValidationIterator(trainingInstanceList,10);
		//ClassifierTrainer<MaxEnt> trainer = new MaxEntTrainer();
//		ClassifierTrainer<NaiveBayes> trainer = new NaiveBayesTrainer();
		/*ClassifierTrainer<DecisionTree> trainer = new DecisionTreeTrainer();
		Classifier classifier = trainer.train(instanceLists[0]);
		
		for(Instance i:instanceLists[1])
		{
			
				System.out.println(i.getName() +"			"+i.getTarget()	);
				int h=classifier.classify(i).getLabeling().getBestIndex();
				System.out.println(classifier.classify(i).getLabeling().getBestLabel());
//				System.out.println(classifier.classify(i).getLabelVector());
				
		}
		ArrayList<Classification> arr = classifier.classify(instanceLists[1]);
		for(Classification c : arr){
			System.out.println(" "+c.getLabeling());
		}
		
		
		// Trial trail = new Trial(classifier, instanceLists[1]);
        System.out.println(classifier.getAccuracy(instanceLists[1]));*/
        /*ClassifierTrainer trainer = new NaiveBayesTrainer();
        Classifier classifier = trainer.train(trainingInstanceList);
        System.out.println("Accuracy: " + classifier.getAccuracy(testingInstanceList));*/
	
	}

}