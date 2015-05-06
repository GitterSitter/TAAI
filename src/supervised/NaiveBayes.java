package supervised;

import java.io.File;
import java.io.FileWriter;
import weka.classifiers.bayes.NaiveBayesUpdateable;
import weka.classifiers.meta.FilteredClassifier;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

public class NaiveBayes {

	public void Bayes() throws Exception {

		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("class"));
		Instances dataset = loader.getDataSet();
		dataset.setClassIndex(dataset.numAttributes() - 1);
	
		/*
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("2-last");
		// filter.setNormalizeDocLength(newType);¨
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(5);
		filter.setInputFormat(dataset);
		Instances dataFiltered = Filter.useFilter(dataset, filter);
		
		
		StringToNominal nm = new StringToNominal();
		nm.setAttributeRange("first-last");
		nm.setAttributeRange("first");
		nm.setInputFormat(dataset);
		Instances dataFiltered = Filter.useFilter(dataset, nm);
		*/
		
		NaiveBayesUpdateable nb = new NaiveBayesUpdateable();
		FilteredClassifier fc = new FilteredClassifier();
		fc.setClassifier(nb);
	//	fc.buildClassifier(dataset);
	
		String content = dataset.toString();
		FileWriter wr = new FileWriter(new File("trainingData.arff"));
		wr.write(content);
		wr.close();
		

		/*
		 * Brukes dersom det er flere dataset filer Instance current; while
		 * ((current = source.nextElement(dataSet)) != null){
		 * nb.updateClassifier(current); }
		 */
		System.out.println(nb);

	}
}
