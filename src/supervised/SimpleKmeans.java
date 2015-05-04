package supervised;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.clusterers.ClusterEvaluation;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.Remove;
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
	
		DataSource source = new DataSource("new.arff");
		Instances data = source.getDataSet();
		

		
//		StringToWordVector filter = new StringToWordVector();
//		NumericToNominal filter1 = new NumericToNominal();
//		filter.setInputFormat(data);
//		Instances dataFiltered = Filter.useFilter(data, filter);
//		filter1.setInputFormat(dataFiltered);
//		Instances dataFiltered1 = Filter.useFilter(dataFiltered, filter1);
		
		
		
		//data.setClassIndex(dataFiltered.numAttributes() -1); 
		
		
		SimpleKMeans kmeans = new SimpleKMeans();
		
		FilteredClusterer fc = new FilteredClusterer(); //filtered clusterer to ignore attributes
//		SimpleKMeans clusterAlg = new SimpleKMeans(); //original clusterer
//		Instances data = getInstancesDataFromSomePlace(); // instances
		String[] options = new String[2];
		options[0] = "-R"; // "range"
		options[1] = "1"; // we want to ignore the attribute that is in the position '1'
		Remove remove = new Remove(); // new instance of filter
		remove.setOptions(options); // set options
		remove.setInputFormat(data); // inform filter about dataset
		fc.setFilter(remove); //add filter to remove attributes
		kmeans.setNumClusters(3);
		fc.setClusterer(kmeans); //bind FilteredClusterer to original clusterer
		
		fc.buildClusterer(data);
		
//		kmeans.buildClusterer(data);
	
		
		
		
		//System.out.println(kmeans.clusterInstance(dataFiltered.instance(0)));
		//System.out.println(data.instance(3));
		
		
		for(int i=0;i<data.numInstances();i++){
			int cluster = fc.clusterInstance(data.instance(i));
		//	System.out.println(cluster + " ***** " +dataFiltered.instance(i));
		//	System.out.println(cluster + " ***** " +data.instance(i));
			System.out.println(data.instance(i));
			String test = data.instance(i).toString();
			
			String work = test.substring(0, test.indexOf(","));
			
			
		
			if(cluster == 0){
				System.out.println("CLUSTER:  " + cluster+"  " + data.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/fail/"+work));
				pr.write(work);
				pr.close();
				
				
			}else if(cluster == 1){
				System.out.println("CLUSTER1:  " + cluster+"  " + data.instance(i));
//				data.instance(0).attribute(0);
				PrintWriter	pr = new PrintWriter(new File("class/assist/"+work));
				pr.write(work);
				pr.close();
			}
			else if(cluster == 2){
				System.out.println("CLUSTER2:  " + cluster+"  " + data.instance(i));
				PrintWriter	pr = new PrintWriter(new File("class/beer/"+work));
				pr.write(work);
				pr.close();
			}
			
		
		}
		
		 ClusterEvaluation eval = new ClusterEvaluation();
		 eval.setClusterer(kmeans);
		 eval.evaluateClusterer(data);
		// System.out.println(eval.clusterResultsToString());
		//System.out.println(kmeans);
		
		 
//		 NaiveBayesMultinomial nav = new NaiveBayesMultinomial();
//		 nav.buildClassifier(dataFiltered);
//		 System.out.println(nav);
		

	}

}
