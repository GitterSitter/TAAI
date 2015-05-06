package Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStreamReader;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import weka.filters.unsupervised.attribute.StringToNominal;
 
/** 	
 * Builds an arff dataset from the documents in a given directory.
 * Assumes that the file names for the documents end with ".txt".
 *
 * Usage:<p/>
 *
 * TextDirectoryToArff <directory path> <p/>
 *
 * @author Richard Kirkby (rkirkby at cs.waikato.ac.nz)
 * @version 1.0
 */
public class TextDirectoryToArff {
	
	public void test() throws Exception{
		DataSource source1 = new DataSource("testData.arff");
		DataSource source2 = new DataSource("trainingData.arff");
		Instances inst1 = source1.getDataSet();
		Instances inst2 = source2.getDataSet();
		
		String[] options = new String[2];
		options[0] = "-R";
		options[1] = "first";
		StringToNominal nm = new StringToNominal();
		nm.setOptions(options);
		nm.setInputFormat(inst1);
		nm.setInputFormat(inst2);
		nm.setAttributeRange("first");
		
		String content1 = inst1.toString();// dataset.toString();
		String content2 = inst2.toString();// dataset.toString();
		FileWriter wr = new FileWriter(new File("test.arff"));
		FileWriter wr2 = new FileWriter(new File("train.arff"));
		wr.write(content1);
		wr2.write(content2);
		
		wr2.close();
		wr.close();

	System.out.println("Done");	
		
	}
	
	
  public Instances createDataset(String directoryPath) throws Exception {
 
    FastVector atts = new FastVector(2);
    atts.addElement(new Attribute("filename", (FastVector) null));
    atts.addElement(new Attribute("contents", (FastVector) null));
    Instances data = new Instances("text_files_in_" + directoryPath, atts, 0);
 
    File dir = new File(directoryPath);
    String[] files = dir.list();
    for (int i = 0; i < files.length; i++) {
      if (files[i].endsWith(".txt")) {
   
    	  try {
      double[] newInst = new double[2];
      newInst[0] = (double)data.attribute(0).addStringValue(files[i]);
      File txt = new File(directoryPath + File.separator + files[i]);
      InputStreamReader is = new InputStreamReader(new FileInputStream(txt));
      StringBuffer txtStr = new StringBuffer();
      int c;
      while ((c = is.read()) != -1) {
        txtStr.append((char)c);
      }
      newInst[1] = (double)data.attribute(1).addStringValue(txtStr.toString());
      data.add(new Instance(1.0, newInst));
      is.close();
    } catch (Exception e) {
      //System.err.println("failed to convert file: " + directoryPath + File.separator + files[i]);
    }
      }
    }
    return data;
  }
 
}
