package unsupervised;

import weka.clusterers.EM;
import weka.core.Instances;
public class EmCluster {

	
	 public void emCluster(Instances data) throws Exception{
	 String[] options = new String[2];
	 options[0] = "-I";                 // max. iterations
	 options[1] = "100";
	 EM clusterer = new EM();   // new instance of clusterer
	 clusterer.setOptions(options);     // set the options
	 clusterer.buildClusterer(data);    // buil
	 
	 }
}
