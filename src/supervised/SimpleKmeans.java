package supervised;

import java.io.File;
import java.io.FileWriter;

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
		kmeans.setNumClusters(3);
		kmeans.buildClusterer(dataFiltered);
		System.out.println(kmeans);

	}

}
