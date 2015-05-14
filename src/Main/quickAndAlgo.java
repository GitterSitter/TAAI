package Main;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class quickAndAlgo {

	public static void readAndDecide() throws FileNotFoundException {
		HashMap<String,String> verifiedDoc = new HashMap<String,String>();

		File fil = new File("AbstractPubmedDocs");
		for(File x: fil.listFiles()){
			if(x.getAbsolutePath().contains("unlabeledTestSet")){
				continue;
			}
			
			Scanner read = new Scanner(new FileReader(x));
			String doc = "";
			while(read.hasNext()){
				doc += read.nextLine();
			}
			doc += doc.toLowerCase();
			
			if((doc.contains("heart transplant.") || doc.contains("heart transplant ") || doc.contains("heart transplant,"))
					&& (doc.contains("heart failure.")||doc.contains("heart failure ") || doc.contains("heart failure,"))  
					&& doc.contains("assist") && doc.contains("support")
					&& ( doc.contains("devices") || doc.contains("device"))){
				verifiedDoc.put(x.getName(),doc);
			}
		}
		PrintWriter pr = null;
		for(Entry<String,String> e: verifiedDoc.entrySet()){
			System.out.println(e.getKey());
			pr = new PrintWriter(new File("AbstractVerifiedcorpus/"+e.getKey()));
			pr.write(e.getValue());
			pr.close();
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		readAndDecide();
	}
}

