import java.io.IOException;

import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToNominal;

public class Main {

	public static void main(String[] args) throws Exception  {
		
		String train = "data/ReutersCorn-train.arff";
		String test =  "data/ReutersCorn-test.arff";
		String cancer = "data/breast-cancer.arff";
		String unlabeled = "data/unlabeled.arff";
		

		DataSource trainingSource = new DataSource(train);
		DataSource testSource = new DataSource(test);
		Instances testData = testSource.getDataSet();
		Instances trainingData = trainingSource.getDataSet();
		
		//Må komme før evt seleksjon/fjerning av attributt
			SimpleKmeans kmeans = new SimpleKmeans();
		//	kmeans.kMeans(trainingData);

			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			testData.setClassIndex(testData.numAttributes() - 1);
			Remove rm = new Remove();
			rm.setAttributeIndices("1"); // remove 1st attribute
		
			NaiveBayes nb = new NaiveBayes();
		//	nb.Bayes(cancer);
			
			DecisionTree dt = new DecisionTree();
			//dt.j48(trainingData, testData);
			
			Unlabeled lb = new Unlabeled();
			lb.unlabeled(trainingData);
				 
				 
		
	}

}
