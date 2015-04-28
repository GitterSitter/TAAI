package unsupervised;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.EM;
import weka.core.Instances;
import weka.filters.Filter;
public class EmCluster {

	
	 public void emCluster(Instances data) throws Exception{
	
		 /*
		 String[] options = new String[2];
	 options[0] = "-I";                 // max. iterations
	 options[1] = "100";
	
	 EM clusterer = new EM();   // new instance of clusterer
	 clusterer.setOptions(options);     // set the options
	 clusterer.buildClusterer(data);    // buil
	 System.out.println(clusterer);
	 
	 */
		 
		 data.setClassIndex(data.numAttributes() - 1);
		 weka.filters.unsupervised.attribute.Remove filter = new weka.filters.unsupervised.attribute.Remove();
		 filter.setAttributeIndices("" + (data.classIndex() + 1));
		 filter.setInputFormat(data);
		 Instances dataClusterer = Filter.useFilter(data, filter);
		 EM clusterer = new EM();
		 // set further options for EM, if necessary...
		 clusterer.buildClusterer(dataClusterer);
		 ClusterEvaluation eval = new ClusterEvaluation();
		 eval.setClusterer(clusterer);
		 eval.evaluateClusterer(data);
		 System.out.println(eval.clusterResultsToString());
	 
	 
	 
	 }
}
