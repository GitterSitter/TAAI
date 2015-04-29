package Util;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Scanner;

import weka.core.Attribute;
import weka.core.FastVector;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.TextDirectoryLoader;
import weka.filters.Filter;
import weka.filters.unsupervised.attribute.StringToWordVector;
 
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
 
 public static void main(String[] args) throws Exception {
   /*
    TextDirectoryToArff tdta = new TextDirectoryToArff();
    Instances dataset = tdta.createDataset("docs");
    
   */
		TextDirectoryLoader loader = new TextDirectoryLoader();
		loader.setDirectory(new File("docs"));
		Instances dataset = loader.getDataSet();
		
		
		//Lager vektor modell
		 StringToWordVector filter = new StringToWordVector();
	     filter.setInputFormat(dataset);
		 Instances dataFiltered = Filter.useFilter(dataset, filter);
		
		String content = dataFiltered.toString();//dataset.toString();
		FileWriter wr = new FileWriter(new File("output.arff"));
		wr.write(content);
		wr.close();
		
		 
		// Dir direkte til arf
		// TextDirectoryToArff tdta = new TextDirectoryToArff();
		// Instances dataset = tdta.createDataset("docs");
	
		
	
  }
}