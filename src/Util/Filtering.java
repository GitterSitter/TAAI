package Util;

import java.io.File;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Filtering {

	public void test(Classifier classifier) throws Exception {
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("class"));
		Instances trainingSet = loader.getDataSet();
		loader.setDirectory(new File("test"));
		Instances testSet = loader.getDataSet();
		
		
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
			
		Instances newTrain = Filter.useFilter(trainingSet, filter); 
		Instances newTest = Filter.useFilter(testSet, filter);
		
		System.out.println(classifier);
		
		 Evaluation eval = new Evaluation(trainingSet);
		 eval.evaluateModel(fc, testSet);
		 //System.out.println(eval.toSummaryString());
		
		 
		 for(int i =0;i< testSet.numInstances();i++){	
			double pred = fc.classifyInstance(testSet.instance(i));
			   System.out.print("ID: " + testSet.instance(i).value(0));
			   System.out.print(", actual: " + testSet.classAttribute().value((int) testSet.instance(i).classValue()));
			   System.out.println(", predicted: " + testSet.classAttribute().value((int) pred));
		 }
		 
		/*
		String content = newTrain.toString();
		FileWriter wr = new FileWriter(new File("trainDataFc.arff"));
		wr.write(content);
		wr.close();

		String content1 = newTest.toString();
		FileWriter wr1 = new FileWriter(new File("testDataFc.arff"));
		wr1.write(content1);
		wr1.close();
		*/

	}

}
