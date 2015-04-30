package supervised;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.Filter;
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
		filter.setInputFormat(data);
		Instances dataFiltered = Filter.useFilter(data, filter);
		
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.setNumClusters(2);
		kmeans.buildClusterer(dataFiltered);
		
		//System.out.println(kmeans.clusterInstance(dataFiltered.instance(0)));
		//System.out.println(data.instance(3));
		
		
		for(int i=0;i<dataFiltered.numInstances();i++){
			int cluster = kmeans.clusterInstance(dataFiltered.instance(i));
		//	System.out.println(cluster + " ***** " +dataFiltered.instance(i));
		//	System.out.println(cluster + " ***** " +data.instance(i));
			
		//	System.out.println(data.instance(i));
			String test = data.instance(i).toString();
			
			String work = test.substring(0, test.indexOf(","));
			System.out.println(work);
			PrintWriter pr;
			if(cluster == 0){
			//	System.out.println("CLUSTER:  " + cluster+"  " + data.instance(i));
				pr = new PrintWriter(new File("Classified/Heart Failure"));
				pr.write(work);
				
			}else if(cluster == 1){
			//	System.out.println("CLUSTER1:  " + cluster+"  " + data.instance(i));
				//data.instance(0).attribute(0);
				pr = new PrintWriter(new File("Classified/Heart Transplant"));
				pr.write(work);
			}
//			else if(cluster == 2){
//				System.out.println("CLUSTER2:  " + cluster+"  " + data.instance(i));
//			}
			
		
		}
		
		//System.out.println(kmeans);
		

	}

}
