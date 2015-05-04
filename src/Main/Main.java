package Main;

import supervised.SimpleKmeans;

public class Main {

	public static void main(String[] args) throws Exception {
		SimpleKmeans kmeans = new SimpleKmeans();
		kmeans.kMeans();
		// algo. distributionForInstance(Instance).
		// trainingData.setClassIndex(trainingData.numAttributes() - 1);
		// testData.setClassIndex(testData.numAttributes() - 1);
		// Remove rm = new Remove();
		// rm.setAttributeIndices("1"); // remove 1st attribute

	}

}
