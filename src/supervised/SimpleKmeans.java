package supervised;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import Util.TextDirectoryToArff;

public class SimpleKmeans {

	public void kMeans() throws Exception {

		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("docs/2");
		// TextDirectoryLoader loader = new TextDirectoryLoader();
		// loader.setDirectory(new File(""));
		// Instances dataset = loader.getDataSet();
	
		String content = dataset.toString();// dataset.toString();
		FileWriter wr = new FileWriter(new File("output.arff"));
		wr.write(content);
		wr.close();
	
		DataSource source = new DataSource("output.arff");
		Instances data = source.getDataSet();
		
		 
			StringToWordVector filter = new StringToWordVector();
			filter.setIDFTransform(true);
			filter.setTFTransform(true);
			filter.setAttributeIndices("2-last");
		//	filter.setNormalizeDocLength(newType);¨
			filter.setUseStoplist(true);
			filter.setWordsToKeep(5);
			filter.setInputFormat(data);
			Instances dataFiltered = Filter.useFilter(data, filter);
		
	//	NumericToNominal nm = new NumericToNominal();
		
		//data.setClassIndex(dataFiltered.numAttributes() -1); 
		//FilteredClusterer fc = new FilteredClusterer();
	 //    data.setClassIndex(data.numAttributes() -1); 
	     
		SimpleKMeans kmeans = new SimpleKMeans();
		FilteredClusterer fc = new FilteredClusterer();
	
	String[] options = new String[2];
	options[0] = "-R";
	options[1] = "1";
	Remove remove = new Remove();
	remove.setOptions(options);
	remove.setInputFormat(dataFiltered);
	fc.setFilter(remove);
	
		kmeans.setNumClusters(3);
		fc.setClusterer(kmeans);
		//kmeans.buildClusterer(data);//Filtered);
	fc.buildClusterer(dataFiltered);
		
		for(int i=0;i<dataFiltered.numInstances();i++){
			int cluster = fc.clusterInstance(dataFiltered.instance(i));
		//	System.out.println(cluster + " ***** " +dataFiltered.instance(i));
		//	System.out.println(cluster + " ***** " +data.instance(i));
		//	System.out.println(data.instance(i));
			String test = data.instance(i).toString();
			String work = test.substring(0, test.indexOf(","));
			
		
			if(cluster == 0){
			System.out.println("CLUSTER:  " + cluster+"  " + dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/fail/"+work));
				pr.write(work);
				pr.close();
				
				
			}else if(cluster == 1){
				System.out.println("CLUSTER1:  " + cluster+"  " + dataFiltered.instance(i));
				//data.instance(0).attribute(0);
				PrintWriter	pr = new PrintWriter(new File("class/assist/"+work));
				pr.write(work);
				pr.close();
			}
			else if(cluster == 2){
				System.out.println("CLUSTER2:  " + cluster+"  " + dataFiltered.instance(i) );
				PrintWriter	pr = new PrintWriter(new File("class/beer/"+work));
				pr.write(work);
				pr.close();
			}
			
		
		}
		
	//	 ClusterEvaluation eval = new ClusterEvaluation();
	//	 eval.setClusterer(kmeans);
	//	 eval.evaluateClusterer(data);
		// System.out.println(eval.clusterResultsToString());
		//System.out.println(kmeans);
		
		 
//		 NaiveBayesMultinomial nav = new NaiveBayesMultinomial();
//		 nav.buildClassifier(dataFiltered);
//		 System.out.println(nav);
		

	}

}
