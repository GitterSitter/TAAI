package supervised;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;


public class NaiveBayes {

	public void Bayes(String dataPath) throws Exception{
	 System.out.println("*********** NAIVE BAyeS **************");
	 DataSource source = new DataSource(dataPath);
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
	 
	}
}
