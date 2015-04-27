import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;

import weka.classifiers.meta.FilteredClassifier;
import weka.classifiers.trees.J48;
import weka.core.Instances;
import weka.filters.unsupervised.attribute.Remove;

public class Unlabeled {

	public void unlabeled(Instances trainingData) throws Exception {

		Remove rm = new Remove();
		rm.setAttributeIndices("1"); // remove 1st attribute
		
		
		
		// load unlabeled data
		Instances unlabeled = new Instances(new BufferedReader(new FileReader(
				"data/unlabeled.arff")));

		// set class attribute
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);

		// create copy
		Instances labeled = new Instances(unlabeled);
		J48 j48 = new J48();

		// label instances
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = j48.classifyInstance(unlabeled.instance(i));
			labeled.instance(i).setClassValue(clsLabel);
		}
		// save labeled data
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"labeled.arff"));
		writer.write(labeled.toString());
		writer.newLine();
		writer.flush();
		writer.close();

	}

}
