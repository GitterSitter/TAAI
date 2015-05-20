package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

public class quickAndAlgo {

	public static String readAndDecide() throws FileNotFoundException {
		HashMap<String,String> verifiedDoc = new HashMap<String,String>();
		File fil = new File("AbstractPubmedDocs");
		int abPubMedSize = fil.listFiles().length; 
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
					&& (doc.contains("assist ") || doc.contains("assist.") || doc.contains("assist,"))
					&& (doc.contains("support ") || doc.contains("support.") || doc.contains("support,"))
					&& (doc.contains("devices ") || doc.contains("devices.") ||doc.contains("devices,")
					|| doc.contains("device ") || doc.contains("device.") || doc.contains("device,"))){
				verifiedDoc.put(x.getName(),doc);
			}
		}
		PrintWriter pr = null;
		String output = "";
		int teller = verifiedDoc.size();
		for(Entry<String,String> e: verifiedDoc.entrySet()){
			pr = new PrintWriter(new File("testfolder123/"+e.getKey()));
			pr.write(e.getValue());
			output += e.getKey() + "\n";
			
			pr.close();
			
		}
		System.out.println("done");
		output += "\n\nSize before filtering: " + abPubMedSize 
				+ "\nSize after filtering: " + teller;
		return output;
	}
	
}

