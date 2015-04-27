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

	public static void main(String[] args)  {
		
		String train = "data/ReutersCorn-train.arff";
		String test =  "data/ReutersCorn-test.arff";
		String cancer = "data/breast-cancer.arff";
		
		
		try{
		DataSource trainingSource = new DataSource(train);
		DataSource testSource = new DataSource(test);
		Instances testData = testSource.getDataSet();
		Instances trainingData = trainingSource.getDataSet();
		

		//Clustering
		 StringToNominal filter = new StringToNominal();
		 filter.setAttributeRange("first");
		 filter.setInputFormat(trainingData);
		 Instances filtered = Filter.useFilter(trainingData, filter); //new FilteredClusterer();
		 SimpleKMeans kmeans = new SimpleKMeans();
		 kmeans.buildClusterer(filtered);
		 System.out.println(kmeans);

		
		
		
			trainingData.setClassIndex(trainingData.numAttributes() - 1);
			testData.setClassIndex(testData.numAttributes() - 1);
			Remove rm = new Remove();
			rm.setAttributeIndices("1"); // remove 1st attribute
	
		 
		 // classifier
				 J48 j48 = new J48();
				 j48.setUnpruned(true);        // using an unpruned J48
				 // meta-classifier
				 FilteredClassifier fc = new FilteredClassifier();
				 fc.setFilter(rm);
				 fc.setClassifier(j48);
				 // train and make predictions
				 fc.buildClassifier(trainingData);
				 
				 
				 for (int i = 0; i < testData.numInstances(); i++) {
				/*
				   double pred = fc.classifyInstance(testData.instance(i));
				   System.out.print("ID: " + testData.instance(i).value(0));
				   System.out.print(", actual: " + testData.classAttribute().value((int) testData.instance(i).classValue()));
				   System.out.println(", predicted: " + testData.classAttribute().value((int) pred));
				   */
				 }
				 
				/* 
				//evaluation
				 Evaluation eval = new Evaluation(trainingData);
				 eval.crossValidateModel(j48, trainingData, 10, new Random(1));
				 eval.evaluateModel(j48, testData);
				 System.out.println(eval.toSummaryString("\nResults\n======\n", false));
				 eval.predictions(); // give ROC curve!!
				 
				 */
				 
				 System.out.println("*****************************************");
				
				 System.out.println(j48);
				 
				 System.out.println("*****************************************");
			
				 
				 DataSource source = new DataSource(cancer);
				 Instances dataSet = source.getStructure();
				 dataSet.setClassIndex(dataSet.numAttributes() - 1);
				 // train NaiveBayes
				 NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
				 nb.buildClassifier(dataSet);
				
				/*
				 * Brukes dersom det er flere dataset filer
				 Instance current;
				 while ((current = source.nextElement(dataSet)) != null){
				   nb.updateClassifier(current);
				 }
	*/
				 System.out.println(nb);
				 
				 System.out.println("*****************************************");
		
		}catch(IOException io){
			io.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
