import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;

import weka.core.Instances;


public class Main {

	public static void main(String[] args) throws FileNotFoundException {
		
		
		BufferedReader reader = new BufferedReader(
                new FileReader("/some/where/data.arff"));
Instances data = new Instances(reader);
reader.close();
// setting class attribute
data.setClassIndex(data.numAttributes() - 1);
		
		
		
	}

}
