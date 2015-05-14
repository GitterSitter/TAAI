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
import weka.filters.unsupervised.attribute.ClusterMembership;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Filtering {

	public Classifier classy;
	public Clusterer clus;
	public Instances inst;
	public ClusterEvaluation evaluation;

	

	public String cluster(Clusterer cluster) throws Exception {
		delete();
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("verifiedCorpus");
		//String content = dataset.toString();
	//	FileWriter wr = new FileWriter(new File("outputCluster.arff"));wr.write(content);wr.close();
		
		dataset.deleteAttributeAt(0);
		StringToWordVector filter = new StringToWordVector();
	
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first-last");
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(50);
		filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL,StringToWordVector.TAGS_FILTER));
		filter.setInputFormat(dataset);
		Instances dataFiltered = Filter.useFilter(dataset, filter);
		FilteredClusterer fc = new FilteredClusterer();
		/*
		int[] atts = new int[2];
		int tell=0;
		for(int i =0;i < dataFiltered.numAttributes();i++){
			if("medicine".equals(dataFiltered.attribute(i).name()) || "surgery".equals(dataFiltered.attribute(i).name()) ){
				atts[tell++] = dataFiltered.attribute(i).index();
			}
		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(atts);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered  =dataFiltered1;
		
	

		
		
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "1";
		Remove remove = new Remove();
		remove.setOptions(options);
		remove.setInputFormat(dataFiltered);
		fc.setFilter(remove);
		*/
		
		if (cluster.getClass() == weka.clusterers.EM.class) {
			((EM) cluster).setNumClusters(2);
		} else if (cluster.getClass() == weka.clusterers.SimpleKMeans.class) {
			((SimpleKMeans) cluster).setNumClusters(2);
		} else if (cluster.getClass() == weka.clusterers.XMeans.class) {
			((XMeans) cluster).setMinNumClusters(2);
			((XMeans) cluster).setMaxNumClusters(2);
		}
		fc.setClusterer(cluster);
		fc.buildClusterer(dataFiltered);
		

		String output = "";
		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			int predictionClass = fc.clusterInstance(dataFiltered.instance(i));
		
			String work = "dataMed.txt";  //test.substring(0, test.indexOf(","));
			if (predictionClass == 0) {
				System.out.println("CLUSTER:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				output = output + predictionClass + "  "
						+ dataFiltered.instance(i) + "\n" ;
				 PrintWriter pr = new PrintWriter(new
				 File("labeled/medicine/" + i+work));
				
				 pr.write(dataset.instance(i).toString().replace("'", ""));
				 pr.close();
			} else if (predictionClass == 1) {
				System.out.println("CLUSTER1:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
						output = output + predictionClass + "  "
						+ dataFiltered.instance(i) + "\n";
				 PrintWriter pr = new PrintWriter(new File("labeled/surgery/"+
				 i+work));
				 pr.write(dataset.instance(i).toString().replace("'", ""));
				 pr.close();
			} else if (predictionClass == 2) {
				System.out.println("CLUSTER2:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				output = output + predictionClass + "  ";
				
			}
		}
	
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(cluster);
		eval.evaluateClusterer(dataFiltered);

		output += "\n" + eval.clusterResultsToString();
		setEvaluation(eval);
		setInst(dataFiltered);
		setClus(cluster);
		

		return output;
	}
	
public void delete() {
	// TODO Auto-generated method stub
	File fil = new File("labeled");
	for(File x: fil.listFiles()){
		for(File y : x.listFiles())
		System.out.println(y.delete());
	}
}
	
	public String classify(Classifier classifier) throws Exception {
		
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("labeled"));
		Instances trainingSet = loader.getDataSet();
		TextDirectoryToArff source = new TextDirectoryToArff();
		Instances testSet = source.createDataset("unlabeled"); 
		
		Add fil = new Add();
		testSet.deleteAttributeAt(0);
		fil.setAttributeIndex("2");
		fil.setNominalLabels("medicine,surgery");
		fil.setAttributeName("@@class@@");
		fil.setInputFormat(testSet);
		testSet.setClassIndex(0);
		testSet = Filter.useFilter(testSet, fil);
		testSet.setClassIndex(1);
		

		
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
		filter.setWordsToKeep(100);
		filter.setInputFormat(trainingSet);
		Instances dataFiltered = Filter.useFilter(trainingSet, filter);
	
		
		/*
	
		

		FilteredClassifier fc = new FilteredClassifier();
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(30);
		filter.setNormalizeDocLength(new SelectedTag(StringToWordVector.FILTER_NORMALIZE_ALL,StringToWordVector.TAGS_FILTER));
		filter.setInputFormat(trainingSet);
		Instances dataFiltered = Filter.useFilter(trainingSet, filter);
	*/
		
		/*
		int[] atts = new int[2];
		int tell=0;
		for(int i =0;i < dataFiltered.numAttributes();i++){
			if("medicine".equals(dataFiltered.attribute(i).name()) || "surgery".equals(dataFiltered.attribute(i).name()) ){
				atts[tell++] = dataFiltered.attribute(i).index();
			}
		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(atts);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered  =dataFiltered1;
		*/
	
		
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		fc.buildClassifier(trainingSet);

		String output = "";
		Evaluation eval = new Evaluation(trainingSet);
		eval.evaluateModel(fc, testSet);
		
	
	//	System.out.println(eval.toSummaryString());
		output += fc.toString() + "\n" + eval.toSummaryString();
		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = fc.classifyInstance(testSet.instance(i));
			System.out.println(pred);
			System.out.print("ID: " + testSet.instance(i).value(0));
		//	System.out.println(testSet.instance(i).toString());
			// System.out.print(", actual: " +
			// testSet.classAttribute().value((int)
			// testSet.instance(i).classValue()) +
			// testSet.instance(i).toString());
			System.out.println(", predicted: "
					+ testSet.classAttribute().value((int) pred));

			output += "ID: " + testSet.instance(i).value(0) + ", predicted: "
					+ testSet.classAttribute().value((int) pred) + "\n";
		}

		setClassy(classifier);

		return output;
	}
	
	public Classifier getClassy() {
		return classy;
	}

	public void setClassy(Classifier classy) {
		this.classy = classy;
	}

	public Clusterer getClus() {
		return clus;
	}

	public void setClus(Clusterer clus) {
		this.clus = clus;
	}
	public Instances getInst() {
		return inst;
	}

	public void setInst(Instances in) {
		this.inst = in;
	}

	public ClusterEvaluation getEvaluation() {
		return evaluation;
	}

	public void setEvaluation(ClusterEvaluation evaluation) {
		this.evaluation = evaluation;
	}

	public Filtering() {

	}
}