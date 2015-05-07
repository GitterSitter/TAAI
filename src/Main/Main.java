package Main;

import weka.classifiers.bayes.NaiveBayes;
import weka.classifiers.functions.MultilayerPerceptron;
import weka.classifiers.functions.SMO;
import weka.clusterers.EM;
import weka.clusterers.SimpleKMeans;
import Util.Filtering;

public class Main {
	public static void main(String[] args) throws Exception {
		NaiveBayes naive = new NaiveBayes();
		SMO smo = new SMO();
		MultilayerPerceptron neural = new MultilayerPerceptron();
		Filtering filter = new Filtering();
	//	filter.classify(naive);
		//filter.test(smo);
		//filter.test(neural);
		SimpleKMeans km = new SimpleKMeans();
		//filter.cluster(km);
		EM em = new EM();
		filter.cluster(em);
		
	
	}

}
