package unsupervised;

import java.io.File;

import weka.clusterers.Clusterer;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.ArffLoader;
import weka.filters.unsupervised.attribute.StringToWordVector;
import Util.TextDirectoryToArff;

public class FilteredCluster {
	
	public void FilteredCluster() throws Exception {

		//**READ DATA**//
//		TextDirectoryToArff tdta = new TextDirectoryToArff();
//		Instances dataset = tdta.createDataset("docs");
		
		ArffLoader arffLoader = new ArffLoader();
		arffLoader.setFile(new File("TestDatanewUSE.arff"));
		Instances dataset = arffLoader.getDataSet();
//		dataset.setClass(dataset.attribute(1));

//		String content = dataset.toString();// dataset.toString();
//		FileWriter wr = new FileWriter(new File("output.arff"));
//		wr.write(content);
//		wr.close();

//		DataSource source = new DataSource("output.arff");
//		Instances data = source.getDataSet();
		//**FILTER= STRINGTOWORDVECTOR**//
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL, StringToWordVector.TAGS_FILTER));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(5);
		filter.setInputFormat(dataset);
//		Instances dataFiltered = filter.useFilter(dataset, filter);


//		String[] options = new String[2];
//		options[0] = "-R"; //Range
//		options[1] = "1"; //First attribute
//		Remove remove = new Remove();
//		remove.setOptions(options);
//		remove.setInputFormat(dataset);
		
		//**SimpleKMEANS**//
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setNumClusters(3);
//		dataset.setClass(dataset.attribute("last"));
		
		//**FilteredClusterer**//
		FilteredClusterer fc = new FilteredClusterer();
		fc.setClusterer(kmeans);
		fc.setFilter(filter);
		fc.buildClusterer(dataset);
		System.out.println(fc);

		
		
//		for (int i = 0; i < dataFiltered.numInstances(); i++) {
//			int cluster = fc.clusterInstance(dataFiltered.instance(i));
//			String test = data.instance(i).toString();
//			String work = test.substring(0, test.indexOf(","));
//
//			if (cluster == 0) {
//				System.out.println("CLUSTER:  " + cluster + "  "
//						+ dataFiltered.instance(i));
//				PrintWriter pr = new PrintWriter(new File("class/heartFailure/" + work));
//				pr.write(dataFiltered.instance(i).toString());
//				pr.close();
//
//			} else if (cluster == 1) {
//				System.out.println("CLUSTER1:  " + cluster + "  "
//						+ dataFiltered.instance(i));
//				PrintWriter pr = new PrintWriter(new File("class/beer/"
//						+ work));
//				pr.write(dataFiltered.instance(i).toString());
//				pr.close();
//			} else if (cluster == 2) {
//				System.out.println("CLUSTER2:  " + cluster + "  "
//						+ dataFiltered.instance(i));
//				PrintWriter pr = new PrintWriter(new File("class/heartTransplant/" + work));
//				pr.write(dataFiltered.instance(i).toString());
//				pr.close();
//			}
//
//		}

		// ClusterEvaluation eval = new ClusterEvaluation();
		// eval.setClusterer(kmeans);
		// eval.evaluateClusterer(data);
		// System.out.println(eval.clusterResultsToString());
	
	}
	
	

}
