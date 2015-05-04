package Main;

import supervised.SimpleKmeans;
import weka.core.converters.ConverterUtils.DataSource;

public class Main {

	public static void main(String[] args) throws Exception {

		String output = "data/output.arff";
		DataSource trainingSource = new DataSource(output);
		// Instances testData = testSource.getDataSet();
		// Må komme før evt seleksjon/fjerning av attributt
		SimpleKmeans kmeans = new SimpleKmeans();
		kmeans.kMeans();

		// algo. distributionForInstance(Instance).
		// trainingData.setClassIndex(trainingData.numAttributes() - 1);
		// testData.setClassIndex(testData.numAttributes() - 1);
		// Remove rm = new Remove();
		// rm.setAttributeIndices("1"); // remove 1st attribute

	}

}
