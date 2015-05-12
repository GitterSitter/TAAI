package unsupervised;

import java.io.File;
import java.io.PrintWriter;

import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class kmeanscluster {
	
	public void test() throws Exception{
		// TODO Auto-generated constructor stub
//		TextDirectoryLoader tarff = new TextDirectoryLoader();
//
//		tarff.setSource(new File("class"));
//		Instances inst = tarff.getDataSet();
//		inst.setClass(inst.attribute(inst.numAttributes()-1));
//		
//		
//		PrintWriter pr = new PrintWriter(new File("newOutput.arff"));
//		
//		pr.write(inst.toString());
//		pr.close();
		ArffLoader arff = new ArffLoader();
		arff.setFile(new File("newOutputtest.arff"));
		Instances inst = new Instances(arff.getDataSet());
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(3);
		filter.setInputFormat(inst);
	
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setNumClusters(3);
//		dataset.setClass(dataset.attribute("last"));
		
		//**FilteredClusterer**//
		FilteredClusterer fc = new FilteredClusterer();
		fc.setClusterer(kmeans);
		fc.setFilter(filter);
		fc.buildClusterer(inst);
		System.out.println(fc);
	}
	

}
