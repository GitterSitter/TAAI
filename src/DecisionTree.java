import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;


public class DecisionTree {

	public void j48(Instances trainingData,Instances testData) throws Exception{
		
		System.out.println("******* DECISION TREE J48 ************");
		
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
			 System.out.println(j48);
	}
	
}
