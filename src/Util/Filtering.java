package Util;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import Main.Gui;
import weka.attributeSelection.AttributeSelection;
import weka.attributeSelection.InfoGainAttributeEval;
import weka.attributeSelection.Ranker;
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
import weka.core.Utils;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;

public class Filtering {

	public Classifier classy;
	public Clusterer clus;
	public Instances inst;
	public ClusterEvaluation evaluation;

	public Instances prepareAttributes(Instances dataFiltered) throws Exception {

		File fil = new File("att.txt");
		Scanner sc = new Scanner(new FileReader(fil));
		int[] atts = new int[15];
		ArrayList<String> input = new ArrayList<String>();

		while (sc.hasNextLine()) {
			input.add(sc.nextLine());
		}
		int telll = 0;
		for (int i = 0; i < dataFiltered.numAttributes(); i++) {
			for (int j = 0; j < atts.length; j++) {
				if (input.get(j).equals(dataFiltered.attribute(i).name())) {
					atts[telll++] = i;
					System.out.println(dataFiltered.attribute(i).name() + " "
							+ dataFiltered.attribute(i).index());

				}

			}

		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(atts);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered = dataFiltered1;

		return dataFiltered;
	}

	public String cluster(Clusterer cluster) throws Exception {
		delete();
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("AbstractVerifiedcorpus");
	

		dataset.deleteAttributeAt(0);
		StringToWordVector filter = new StringToWordVector();

		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first-last");
		filter.setStopwords(new File("stopwordlist.txt"));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(1000);
		filter.setNormalizeDocLength(new SelectedTag(
				StringToWordVector.FILTER_NORMALIZE_ALL,
				StringToWordVector.TAGS_FILTER));
		filter.setInputFormat(dataset);
		Instances dataFiltered = Filter.useFilter(dataset, filter);
		FilteredClusterer fc = new FilteredClusterer();

		for (int i = 0; i < dataFiltered.numAttributes(); i++) {
			System.out.println(dataFiltered.attribute(i).name());
		}

		File fil = new File("att.txt");
		Scanner sc = new Scanner(new FileReader(fil));

		ArrayList<String> input = new ArrayList<String>();
		while (sc.hasNextLine()) {
			input.add(sc.nextLine());
		}

		int[] atts = new int[input.size()];
		int telll = 0;
		for (int i = 0; i < dataFiltered.numAttributes(); i++) {
			for (int j = 0; j < atts.length; j++) {
				if (input.get(j).equals(dataFiltered.attribute(i).name())) {
					atts[telll++] = i;
			

				}

			}

		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(atts);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered = dataFiltered1;

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
		PrintWriter pr=null;
		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			int predictionClass = fc.clusterInstance(dataFiltered.instance(i));
			String work = "dataMed.txt"; 
			if (predictionClass == 0) {
				System.out.println("CLUSTER:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				output = output + predictionClass + "  "
						+ dataFiltered.instance(i) + "\n";
				 pr = new PrintWriter(new File("labeled/heartDevices/" + i + work));

				pr.write(dataset.instance(i).toString().replace("'", ""));
				pr.flush();
				pr.close();
			} else if (predictionClass == 1) {
				System.out.println("CLUSTER1:  " + predictionClass + "  "
						+ dataFiltered.instance(i));
				output = output + predictionClass + "  "+ dataFiltered.instance(i) + "\n";
				 pr = new PrintWriter(new File("labeled/heartSurgery/"  + i + work));
				pr.write(dataset.instance(i).toString().replace("'", ""));
				pr.flush();
				pr.close();
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
		File fil = new File("labeled");
		for (File x : fil.listFiles()) {
			for (File y : x.listFiles())
				y.delete();
		}
	}

	public String classify(Classifier classifier, String filePath, boolean att)
			throws Exception {
		
	
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("labeled"));
		//Instances trainingSet = loader.getDataSet();
	//	TextDirectoryToArff source = new TextDirectoryToArff();
	//	Instances testSet = source.createDataset(filePath);

		
		double percent = 66.0; 
		Instances inst = loader.getDataSet();
		
		int trainSize = (int) Math.round(inst.numInstances() * percent / 100); 
		int testSize = inst.numInstances() - trainSize; 
		Instances train = new Instances(inst, 0, trainSize); 
		Instances test = new Instances(inst, trainSize, testSize); 
		
		
		/*
		Add fil = new Add();
		testSet.deleteAttributeAt(0);
		fil.setAttributeIndex("2");
		fil.setNominalLabels("heartDevices,heartSurgery");
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


*/
		FilteredClassifier fc = new FilteredClassifier();
		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(
				StringToWordVector.FILTER_NORMALIZE_ALL,
				StringToWordVector.TAGS_FILTER));
		filter.setStopwords(new File("stopwordlist.txt"));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);

		if (att) {
			filter.setWordsToKeep(1000);
		} else {
			filter.setWordsToKeep(10);
		}
		filter.setInputFormat(train);
		Instances dataFiltered = Filter.useFilter(train, filter);

		
		
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		fc.buildClassifier(train); 
		setClassy(classifier);
		
		String output ="";
		Evaluation eval = new Evaluation(train);
		
		output += evaluationPredictions(eval,test, fc) + "\n\n";
	
		if(Gui.isValidating){
		output += crossValidate(dataFiltered,fc,eval);
		}else{
		eval.evaluateModel(fc, test);
		output+=eval.toSummaryString()+"\n";
		output+= eval.toClassDetailsString();
		
	
		}
			
	
	
		return output;
	}

	
	
	public String crossValidate(Instances dataFiltered, FilteredClassifier fc, Evaluation eval) throws Exception{
	    
		String output ="";
		Random rand = new Random(1);
		int folds = 10;
		eval.crossValidateModel(fc, dataFiltered, folds, rand);
		output += eval.toSummaryString();
		output += "\n" + eval.toClassDetailsString() + "\n";
		output +=  "Confusion Matrix: \n";
		output += "\n  0    1  Classes";
		int teller = 0;
		double[][] matrix = eval.confusionMatrix();
		for (double[] ds : matrix) {
			if (teller == 0) {
				output += "\n" + Arrays.toString(ds).replace(".0", "")+ " " + teller + " "
						+ "heartDevices \n";
			} else {
				output += Arrays.toString(ds).replace(".0", "") + " " + teller + " "
						+ "heartSurgery" + "\n\n";
			}
			teller++;
		}
		
		
		return output;
	}
	
	
	
	public String evaluationPredictions(Evaluation eval,Instances testSet,FilteredClassifier fc) throws Exception{
		
		String output ="";
		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = fc.classifyInstance(testSet.instance(i));
			System.out.print("ID: " + testSet.instance(i).value(0));
			// System.out.println(testSet.instance(i).toString());
			// System.out.print(", actual: " +
			// testSet.classAttribute().value((int)
			// testSet.instance(i).classValue()) +
			// testSet.instance(i).toString());
			System.out.println(", predicted: "
					+ testSet.classAttribute().value((int) pred));
			output += "ID: " + testSet.instance(i).value(0) + ", predicted: "
					+ testSet.classAttribute().value((int) pred) + "\n";
		}
		
		return output;
	}
	
	
	
	
	public String findBestAtt() throws Exception {

		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("labeled"));
		Instances trainingSet = loader.getDataSet();
		TextDirectoryToArff source = new TextDirectoryToArff();
		Instances testSet = source.createDataset("AbstractVerifiedcorpus");

		Add fil = new Add();
		testSet.deleteAttributeAt(0);
		fil.setAttributeIndex("2");
		fil.setNominalLabels("heartDevices,heartSurgery");
		fil.setAttributeName("@@class@@");
		fil.setInputFormat(testSet);
		testSet.setClassIndex(0);
		testSet = Filter.useFilter(testSet, fil);
		testSet.setClassIndex(1);

		StringToWordVector filter = new StringToWordVector();
		filter.setIDFTransform(true);
		filter.setTFTransform(true);
		filter.setAttributeIndices("first");
		filter.setNormalizeDocLength(new SelectedTag(
				StringToWordVector.FILTER_NORMALIZE_ALL,
				StringToWordVector.TAGS_FILTER));
		filter.setStopwords(new File("stopwordlist.txt"));
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(1000);
		filter.setInputFormat(trainingSet);
		Instances dataFiltered = Filter.useFilter(trainingSet, filter);

		AttributeSelection attsel = new AttributeSelection();
		InfoGainAttributeEval gain = new InfoGainAttributeEval();
		attsel.setRanking(true);
		attsel.setEvaluator(gain);
		Ranker rank = new Ranker();
		rank.setThreshold(0.20);
		rank.setGenerateRanking(true);
		rank.setNumToSelect(-1);

		attsel.setSearch(rank);
		attsel.SelectAttributes(dataFiltered);

		int[] indices = attsel.selectedAttributes();
		System.out.println(Utils.arrayToString(indices));

		String att = "";
		for (int i : indices) {
			System.out.println(i);
			att += dataFiltered.attribute(i).name() + "\n";
		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(indices);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered = dataFiltered1;
		System.out.println(dataFiltered.toString());

		return att;
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