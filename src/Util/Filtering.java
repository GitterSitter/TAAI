package Util;

import java.io.File;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import javax.swing.JOptionPane;

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
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.Add;
import weka.filters.unsupervised.attribute.Remove;
import weka.filters.unsupervised.attribute.StringToWordVector;
import Main.Gui;

public class Filtering {

	public Classifier classy;
	public Clusterer clus;
	public Instances inst;
	public ClusterEvaluation evaluation;


	public String cluster(Clusterer cluster) throws Exception {
	int nrOfClusters=2;
	int test =	JOptionPane.showConfirmDialog(Gui.contentPane, "Devide into 2 clusters as default?");
	boolean  multiCluster = false;
	if(test == 1){
		String nrClusters = "2";
		nrClusters =JOptionPane.showInputDialog("Choose number of clusters", nrClusters);
		nrOfClusters = Integer.parseInt(nrClusters);
		multiCluster = true;
	}else if(test == 2){
		return null;
	}
	
		TextDirectoryToArff tdta = new TextDirectoryToArff();
		delete();
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

		File fil = new File("att.txt");
		Scanner sc = new Scanner(new FileReader(fil));

		ArrayList<String> input = new ArrayList<String>();
		while (sc.hasNextLine()) {
			input.add(sc.nextLine());
		}
		sc.close();
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
			((EM) cluster).setNumClusters(nrOfClusters);
		} else if (cluster.getClass() == weka.clusterers.SimpleKMeans.class) {
			((SimpleKMeans) cluster).setNumClusters(nrOfClusters);
		} else if (cluster.getClass() == weka.clusterers.XMeans.class) {
			((XMeans) cluster).setMinNumClusters(2);
			((XMeans) cluster).setMaxNumClusters(nrOfClusters);
		}
		

		fc.setClusterer(cluster);
		double startTime = System.currentTimeMillis();
		fc.buildClusterer(dataFiltered);
		double endTime = System.currentTimeMillis();
		double timeUsed = endTime - startTime;
		
		String output = "";
		output += "The Vector Model: \n\n";
		PrintWriter pr = null;
		int predictionClass=0;
		if(multiCluster){
			
		}else {
		setClus(cluster);
		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			 predictionClass = fc.clusterInstance(dataFiltered.instance(i));
			String work = "dataMed.txt"; 
			if (predictionClass == 0) {
				//System.out.println("CLUSTER:  " + predictionClass + "  "+ dataFiltered.instance(i));
				output = output + predictionClass + "  "
						+ dataFiltered.instance(i) + "\n";
				 pr = new PrintWriter(new File("labeled/heartDevices/" + i + work));

				pr.write(dataset.instance(i).toString().replace("'", ""));
				pr.flush();
				pr.close();
			} else if (predictionClass == 1) {
			//	System.out.println("CLUSTER1:  " + predictionClass + "  "+ dataFiltered.instance(i));
				output = output + predictionClass + "  "+ dataFiltered.instance(i) + "\n";
				 pr = new PrintWriter(new File("labeled/heartSurgery/"  + i + work));
				pr.write(dataset.instance(i).toString().replace("'", ""));
				pr.flush();
				pr.close();
			} 
		}

		}
		
		ClusterEvaluation eval = new ClusterEvaluation();
		eval.setClusterer(cluster);
		eval.evaluateClusterer(dataFiltered);

		output += "\n" + eval.clusterResultsToString();
		setEvaluation(eval);
		setInst(dataFiltered);
		
		output += "\n Time used by the algorithm: " + timeUsed +" ms";
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
		boolean predictNewDocs = false;
		
		double percent = 66.0; 
		Instances inst = loader.getDataSet();
		int trainSize = (int) Math.round(inst.numInstances() * percent / 100); 
		int testSize = inst.numInstances() - trainSize; 
		Instances train = inst; // new Instances(inst, 0, trainSize); 
		Instances test = new Instances(inst, 30, 63); 
	
		for (int i = 30; i < 64; i++) {
		train.delete(i);
		
		}
		
		
		//JOptionPane.showConfirmDialog(Gui.contentPane, "Randomize test instances?");
		/*
		Random rd = new Random();
		int rand =0;
		for (int i = 0; i < testSize; i++) {
			rand = rd.nextInt(inst.numInstances()-1);
			test.add(inst.instance(rand));
			train.delete(rand);
			
		}
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

		if(att) {
			filter.setWordsToKeep(1000);
		} else {
			filter.setWordsToKeep(10);
		}
		filter.setInputFormat(train);
		Instances dataFiltered = Filter.useFilter(train, filter);

		
		fc.setFilter(filter);
		fc.setClassifier(classifier);
		double startTime = System.currentTimeMillis();
		fc.buildClassifier(train); 
		double endTime = System.currentTimeMillis();
		double timeUsed = endTime - startTime;
		setClassy(classifier);
		
		
		
		if(filePath != ""){
			TextDirectoryToArff source = new TextDirectoryToArff();
			Instances docsToClassify = source.createDataset(filePath);
			train = docsToClassify;
		
			Add fil = new Add();
			train.deleteAttributeAt(0);
			fil.setAttributeIndex("2");
			fil.setNominalLabels("heartDevices,heartSurgery");
			fil.setAttributeName("@@class@@");
			fil.setInputFormat(train);
			train.setClassIndex(0);
			train = Filter.useFilter(train, fil);
			train.setClassIndex(1);
			
			PrintWriter pr = new PrintWriter(new File("docsToClassify.arff"));
			pr.write(train.toString());
			pr.flush();
			pr.close();
			predictNewDocs = true;
	
	}
		
		if(train.numInstances() <= 0){
			JOptionPane.showMessageDialog(Gui.contentPane, "Please run with 2 clusters for this to work!");
			return null;
		}
		PrintWriter prr = new PrintWriter(new File("test.arff"));
		prr.write(train.toString());
		prr.close();
	
		String output ="";
		Evaluation eval = new Evaluation(train);
		if(predictNewDocs){
			for (int i = 0; i < train.numInstances(); i++) {
				double pred = fc.classifyInstance(train.instance(i));
				output += "ID: "
						+(int) train.instance(i).value(0)
						+ ". Predicted: "
						+ train.classAttribute().value((int) pred) + "\n";
			}
			
		}else{
		output += evaluationPredictions(eval,test, fc) + "\n\n";
		if(Gui.isValidating){
		output += crossValidate(dataFiltered,fc,eval);
		}else{
			output +=	evalModel( fc,  test, eval);
		}
		
		}
		output = output.replace("=","");
		output += "\n Time used by the algorithm: " + timeUsed +" ms";
		return output;
	}

	
	public String evalModel(FilteredClassifier fc, Instances test,Evaluation eval) throws Exception{
		String output ="";
		eval.evaluateModel(fc, test);
		output+=eval.toSummaryString()+"\n";
		output+= eval.toClassDetailsString()+"\n\n";
		output += confusionMatrix(eval);
		return output;
	}
	
	
	public String confusionMatrix(Evaluation eval){
		String output ="";
		output +=  "Confusion Matrix: \n";
		output += "\n0    1  Classes";
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
	public String crossValidate(Instances dataFiltered, FilteredClassifier fc, Evaluation eval) throws Exception{
	    
		String output ="";
		Random rand = new Random(1);
		int folds = 10;
		eval.crossValidateModel(fc, dataFiltered, folds, rand);
		output += eval.toSummaryString();
		output += "\n" + eval.toClassDetailsString() + "\n";
		output += confusionMatrix(eval);
		
		return output;
	}
	
	
	
	public String evaluationPredictions(Evaluation eval, Instances testSet, FilteredClassifier fc) throws Exception {
		String output = "";
		for (int i = 0; i < testSet.numInstances(); i++) {
			double pred = fc.classifyInstance(testSet.instance(i));
			output += "ID: "
					+(int) testSet.instance(i).value(0)
					+ ". Predicted: "
					+ testSet.classAttribute().value((int) pred)
					+ ". Actual: "
					+ testSet.classAttribute().value(
							(int) testSet.instance(i).classValue()) + "\n";
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

		String att = "";
		for (int i : indices) {
			att += dataFiltered.attribute(i).name() + "\n";
		}

		Remove remove = new Remove();
		remove.setAttributeIndicesArray(indices);
		remove.setInvertSelection(true);
		remove.setInputFormat(dataFiltered);
		Instances dataFiltered1 = Filter.useFilter(dataFiltered, remove);
		dataFiltered = dataFiltered1;

		return att;
	}
	
	public Instances prepareAttributes(Instances dataFiltered) throws Exception {

		File fil = new File("att.txt");
		Scanner sc = new Scanner(new FileReader(fil));
		int[] atts = new int[15];
		ArrayList<String> input = new ArrayList<String>();

		while (sc.hasNextLine()) {
			input.add(sc.nextLine());
		}
		sc.close();
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

		return dataFiltered;
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