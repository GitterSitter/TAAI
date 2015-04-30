package Main;
import supervised.DecisionTree;
import supervised.NaiveBayes;
import supervised.SimpleKmeans;
import supervised.Unlabeled;
import unsupervised.EmCluster;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;

public class Main {

	public static void main(String[] args) throws Exception  {
		
		
		//generelt sett bruke supervised algo for mest mulig presis klassifisering
		String train = "data/ReutersCorn-train.arff";
		String test =  "data/ReutersCorn-test.arff";
		String cancer = "data/breast-cancer.arff";
		String unlabeled = "data/unlabeled.arff";
		String output = "data/output.arff";

		DataSource trainingSource = new DataSource(output);
		DataSource testSource = new DataSource(test);
	//	Instances testData = testSource.getDataSet();
	//	Instances trainingData = trainingSource.getDataSet();
		
		
		
		//System.out.println(trainingData.instance(0));
	//	System.out.println(trainingData.instance(1));
		
		//Må komme før evt seleksjon/fjerning av attributt
			SimpleKmeans kmeans = new SimpleKmeans();
			kmeans.kMeans();
			EmCluster cl = new EmCluster();
		//	cl.emCluster(trainingData);
				 
			
			//algo. distributionForInstance(Instance).

		//	trainingData.setClassIndex(trainingData.numAttributes() - 1);
		//	testData.setClassIndex(testData.numAttributes() - 1);
		//	Remove rm = new Remove();
		//	rm.setAttributeIndices("1"); // remove 1st attribute
		
		
			
		
	}

}
