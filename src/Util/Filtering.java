package Util;

import java.io.File;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Filtering {

	public void classify(Classifier classifier) throws Exception {
	
		
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("class"));
		Instances trainingSet = loader.getDataSet();
		
		TextDirectoryToArff source = new TextDirectoryToArff();
		//loader.setDirectory(new File("test"));
		Instances testSet = source.createDataset("unlabeled");  
		
		
		
		Add fil = new Add();
		testSet.deleteAttributeAt(0);
		fil.setAttributeIndex("2");
		fil.setNominalLabels("beers,heartFailure,heartTransplant");
		fil.setAttributeName("@@class@@");
		
		fil.setInputFormat(testSet);
		testSet.setClassIndex(0);	
		testSet = Filter.useFilter(testSet, fil);
		testSet.setClassIndex(1);
	
		System.out.println(testSet);
		
		PrintWriter pr = new PrintWriter(new File("outputTest.arff"));
		pr.write(testSet.toString());
		pr.flush();
		pr.close();
		pr = new PrintWriter(new File("outputTraining.arff"));
		pr.write(trainingSet.toString());
		pr.flush();
		pr.close();
		
		FilteredClassifier fc = new FilteredClassifier();
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(
				StringToWordVector.FILTER_NORMALIZE_ALL,
				StringToWordVector.TAGS_FILTER));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(2);
		filter.setInputFormat(trainingSet);
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		fc.buildClassifier(trainingSet);
		 
	//	Instances newTrain = Filter.useFilter(trainingSet, filter); 
	//	Instances newTest = Filter.useFilter(testSet, filter);
		
		System.out.println(fc);
		
		 
		 Evaluation eval = new Evaluation(trainingSet);
		 eval.evaluateModel(fc, testSet);
		 System.out.println(eval.toSummaryString());
		 
		 for(int i =0;i< testSet.numInstances();i++){	
			double pred = fc.classifyInstance(testSet.instance(i));
			  System.out.print("ID: " + testSet.instance(i).value(0));
			  // System.out.print(", actual: " + testSet.classAttribute().value((int) testSet.instance(i).classValue()) + testSet.instance(i).toString());
			   System.out.println(", predicted: " + testSet.classAttribute().value((int) pred));
		 }
		 
	

	}

}
