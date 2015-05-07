package Util;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;

import weka.classifiers.Classifier;
import weka.classifiers.Evaluation;
import weka.classifiers.meta.FilteredClassifier;
import weka.clusterers.ClusterEvaluation;
import weka.clusterers.Clusterer;
import weka.clusterers.EM;
import weka.clusterers.FilteredClusterer;
import weka.clusterers.SimpleKMeans;
import weka.clusterers.XMeans;
import weka.core.Instances;
import weka.core.SelectedTag;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Filtering {

	public void cluster(Clusterer cluster) throws Exception {
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("docs");

		String content = dataset.toString();
		FileWriter wr = new FileWriter(new File("outputCluster.arff"));
		wr.write(content);
		wr.close();

		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("2-last");
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(5);
		filter.setInputFormat(dataset);
		Instances dataFiltered = Filter.useFilter(dataset, filter);

		FilteredClusterer fc = new FilteredClusterer();
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "1";
		Remove remove = new Remove();
		remove.setOptions(options);
		remove.setInputFormat(dataFiltered);
		fc.setFilter(remove);

		if (cluster.getClass() == weka.clusterers.EM.class) {
			((EM) cluster).setNumClusters(3);
		} else if (cluster.getClass() == weka.clusterers.SimpleKMeans.class) {
			((SimpleKMeans) cluster).setNumClusters(3);
		} else if (cluster.getClass() == weka.clusterers.XMeans.class) {
			((XMeans) cluster).setMinNumClusters(3);
			((XMeans) cluster).setMaxNumClusters(3);
		}

		fc.setClusterer(cluster);
		fc.buildClusterer(dataFiltered);

		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			int predictionClass = fc.clusterInstance(dataFiltered.instance(i));
			String test = dataset.instance(i).toString();
			String work = test.substring(0, test.indexOf(","));

			if (predictionClass == 0) {
				System.out.println("CLUSTER:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				// PrintWriter pr = new PrintWriter(new
				// File("class/heartFailure/" + work));
				// pr.write(dataFiltered.instance(i).toString());
				// pr.close();

			} else if (predictionClass == 1) {
				System.out.println("CLUSTER1:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				// PrintWriter pr = new PrintWriter(new File("class/beers/"+
				// work));
				// pr.write(dataFiltered.instance(i).toString());
				// pr.close();
			} else if (predictionClass == 2) {
				System.out.println("CLUSTER2:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				// PrintWriter pr = new PrintWriter(new
				// File("class/heartTransplant/" + work));
				// pr.write(dataFiltered.instance(i).toString());
				// pr.close();
			}

		}

		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(cluster);
		eval.evaluateClusterer(dataset);
		System.out.println(eval.clusterResultsToString());

	}

	public void classify(Classifier classifier) throws Exception {

		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("class"));
		Instances trainingSet = loader.getDataSet();

		TextDirectoryToArff source = new TextDirectoryToArff();
		// loader.setDirectory(new File("test"));
		Instances testSet = source.createDataset("unlabeled");

		Add fil = new Add();
		testSet.deleteAttributeAt(0);
		fil.setAttributeIndex("2");
		fil.setNominalLabels("beers,heartFailure,heartTransplant");
		fil.setAttributeName("@@class@@");

		fil.setInputFormat(testSet);
		testSet.setClassIndex(0);
		testSet = Filter.useFilter(testSet, fil);
		testSet.setClassIndex(1);

		System.out.println(testSet);

		PrintWriter pr = new PrintWriter(new File("outputTest.arff"));
		pr.write(testSet.toString());
		pr.flush();
		pr.close();
		pr = new PrintWriter(new File("outputTraining.arff"));
		pr.write(trainingSet.toString());
		pr.flush();
		pr.close();

		FilteredClassifier fc = new FilteredClassifier();
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(
				StringToWordVector.FILTER_NORMALIZE_ALL,
				StringToWordVector.TAGS_FILTER));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(2);
		filter.setInputFormat(trainingSet);
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		fc.buildClassifier(trainingSet);

		// Instances newTrain = Filter.useFilter(trainingSet, filter);
		// Instances newTest = Filter.useFilter(testSet, filter);

		System.out.println(fc);

		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(fc, testSet);
		System.out.println(eval.toSummaryString());

		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = fc.classifyInstance(testSet.instance(i));
			System.out.print("ID: " + testSet.instance(i).value(0));
			// System.out.print(", actual: " +
			// testSet.classAttribute().value((int)
			// testSet.instance(i).classValue()) +
			// testSet.instance(i).toString());
			System.out.println(", predicted: "
					+ testSet.classAttribute().value((int) pred));
		}

	}

}
