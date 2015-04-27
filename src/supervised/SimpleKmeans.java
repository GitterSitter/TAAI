package supervised;
import weka.clusterers.SimpleKMeans;
import weka.core.Instances;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToNominal;

public class SimpleKmeans {

	public void kMeans(Instances trainingData) throws Exception {

		System.out.println("********* Simple KMeans *************");
		// Clustering
	
		StringToNominal filter = new StringToNominal();
		filter.setAttributeRange("first");
		filter.setInputFormat(trainingData);
		Instances filtered = Filter.useFilter(trainingData, filter); // new
																		// FilteredClusterer();
		SimpleKMeans kmeans = new SimpleKMeans();
		kmeans.buildClusterer(filtered);
		System.out.println(kmeans);

	}

}
