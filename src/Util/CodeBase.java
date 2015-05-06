package Util;




/*
	
	
	public void kMeans() throws Exception {

		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("docs");
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
		// filter.setNormalizeDocLength(newType);¨
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(5);
		filter.setInputFormat(data);
		Instances dataFiltered = Filter.useFilter(data, filter);

		SimpleKMeans kmeans = new SimpleKMeans();
		FilteredClusterer fc = new FilteredClusterer();

		String[] options = new String[2];
		options[0] = "-R"; //Range
		options[1] = "1"; //First attribute
		Remove remove = new Remove();
		remove.setOptions(options);
		remove.setInputFormat(dataFiltered);
		fc.setFilter(remove);

		kmeans.setNumClusters(3);
		fc.setClusterer(kmeans);
		fc.buildClusterer(dataFiltered);

		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			int cluster = fc.clusterInstance(dataFiltered.instance(i));
			String test = data.instance(i).toString();
			String work = test.substring(0, test.indexOf(","));

			if (cluster == 0) {
				System.out.println("CLUSTER:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/heartFailure/" + work));
				pr.write(dataFiltered.instance(i).toString());
				pr.close();

			} else if (cluster == 1) {
				System.out.println("CLUSTER1:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/beers/"
						+ work));
				pr.write(dataFiltered.instance(i).toString());
				pr.close();
			} else if (cluster == 2) {
				System.out.println("CLUSTER2:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/heartTransplant/" + work));
				pr.write(dataFiltered.instance(i).toString());
				pr.close();
			}

		}

		// ClusterEvaluation eval = new ClusterEvaluation();
		// eval.setClusterer(kmeans);
		// eval.evaluateClusterer(data);
		// System.out.println(eval.clusterResultsToString());
		 System.out.println(kmeans);
	
	}
	
	
	public void EM() throws Exception {

		TextDirectoryToArff tdta = new TextDirectoryToArff();
		Instances dataset = tdta.createDataset("docs");
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
		// filter.setNormalizeDocLength(newType);¨
		filter.setUseStoplist(true);
		filter.setLowerCaseTokens(true);
		filter.setWordsToKeep(5);
		filter.setInputFormat(data);
		Instances dataFiltered = Filter.useFilter(data, filter);

		EM em = new EM();
		FilteredClusterer fc = new FilteredClusterer();

		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "1";
		Remove remove = new Remove();
		remove.setOptions(options);
		remove.setInputFormat(dataFiltered);
		fc.setFilter(remove);

		em.setNumClusters(3);
		fc.setClusterer(em);
		fc.buildClusterer(dataFiltered);

		for (int i = 0; i < dataFiltered.numInstances(); i++) {
			int cluster = fc.clusterInstance(dataFiltered.instance(i));
			String test = data.instance(i).toString();
			String work = test.substring(0, test.indexOf(","));

			if (cluster == 0) {
				System.out.println("CLUSTER:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/heartFailure/" + work));
				pr.write(work);
				pr.close();

			} else if (cluster == 1) {
				System.out.println("CLUSTER1:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/beer/"
						+ work));
				pr.write(work);
				pr.close();
			} else if (cluster == 2) {
				System.out.println("CLUSTER2:  " + cluster + "  "
						+ dataFiltered.instance(i));
				PrintWriter pr = new PrintWriter(new File("class/heartTransplant/" + work));
				pr.write(work);
				pr.close();
			}

		}

		 ClusterEvaluation eval = new ClusterEvaluation();
		 eval.setClusterer(em);
		 eval.evaluateClusterer(dataFiltered);
		 System.out.println(eval.clusterResultsToString());
		// System.out.println(em);
	
	}
	
	
	public class Unlabeled {

	//må ta inn trent decision tree klassifier(return fra DT metode)
	public void unlabeled() throws Exception {

		Remove rm = new Remove();
		rm.setAttributeIndices("1"); // remove 1st attribute
		
		// load unlabeled data
		Instances unlabeled = new Instances(new BufferedReader(new FileReader(
				"TestLabels/testUnlabel.arff")));
		
		Instances train = new Instances(new BufferedReader(new FileReader(
				"TestLabels/iris.arff")));

		// set class attribute
		unlabeled.setClassIndex(unlabeled.numAttributes() - 1);
		
		// create copy
		Instances labeled = new Instances(unlabeled);
		J48 j48 = new J48();//må erstattes
		// label instances
		train.setClass(train.attribute(train.numAttributes() - 1));
		
		j48.buildClassifier(train);
		for (int i = 0; i < unlabeled.numInstances(); i++) {
			double clsLabel = j48.classifyInstance(unlabeled.instance(i));
			labeled.instance(i).setClassValue(clsLabel);
		}
		// save labeled data
		BufferedWriter writer = new BufferedWriter(new FileWriter(
				"labeled.arff"));
		writer.write(labeled.toString());
		writer.newLine();
		writer.flush();
		writer.close();

		
		
// Skriver ut array med sannsynlighet for instans hører til klasse		System.out.println(clsLabel + " -> " + unlabeled.classAttribute().value((int) clsLabel));
		 
		
	}
	
	public class DecisionTree {

	public void j48(Instances trainingData,Instances testData) throws Exception{
		
		System.out.println("******* DECISION TREE J48 ************");
		
		Remove rm = new Remove();
		rm.setAttributeIndices("1"); // remove 1st attribute
		// classifier
			 J48 j48 = new J48();
			 j48.setUnpruned(true);        // using an unpruned J48
			 // meta-classifier
			 FilteredClassifier fc = new FilteredClassifier();
			 fc.setFilter(rm);
			 fc.setClassifier(j48);
			 // train and make predictions
			 fc.buildClassifier(trainingData);
			
			 
			 for (int i = 0; i < testData.numInstances(); i++) {
		
			   double pred = fc.classifyInstance(testData.instance(i));
			   System.out.print("ID: " + testData.instance(i).value(0));
			   System.out.print(", actual: " + testData.classAttribute().value((int) testData.instance(i).classValue()));
			   System.out.println(", predicted: " + testData.classAttribute().value((int) pred));
			  
			
	
	
	public class NaiveBayes {

		public void test() throws Exception {
			TextDirectoryLoader loader = new TextDirectoryLoader();
			loader.setDirectory(new File("class"));
			Instances trainingSet = loader.getDataSet();
			loader.setDirectory(new File("test"));
			Instances testSet = loader.getDataSet();
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
			
			NaiveBayes nb = new NaiveBayes();
			fc.setClassifier(nb);
			fc.buildClassifier(trainingSet);
				
			Instances newTrain = Filter.useFilter(trainingSet, filter); 
			Instances newTest = Filter.useFilter(testSet, filter);
			
			 Evaluation eval = new Evaluation(trainingSet);
			 eval.evaluateModel(fc, testSet);
			 System.out.println(eval.toSummaryString());
			 for(int i =0;i< testSet.numInstances();i++){	
				double pred = fc.classifyInstance(testSet.instance(i));
				   System.out.print("ID: " + testSet.instance(i).value(0));
				   System.out.print(", actual: " + testSet.classAttribute().value((int) testSet.instance(i).classValue()));
				   System.out.println(", predicted: " + testSet.classAttribute().value((int) pred));
			 }
			 
			
			String content = newTrain.toString();
			FileWriter wr = new FileWriter(new File("trainDataFc.arff"));
			wr.write(content);
			wr.close();

			String content1 = newTest.toString();
			FileWriter wr1 = new FileWriter(new File("testDataFc.arff"));
			wr1.write(content1);
			wr1.close();

		}

	}

}
*/