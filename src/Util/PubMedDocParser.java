package Util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.util.Scanner;

public class PubMedDocParser{
	public static void parsefile(File file) throws FileNotFoundException{
		Scanner read = new Scanner(new FileReader(file));
		System.out.println(file.getAbsolutePath());
		String doc = "";
		while(read.hasNext()){
			doc += read.nextLine();
		}
		read.close();
		String[] docList = doc.split("PMID");
		int i = 0;
		PrintWriter pr;
		for (String string : docList) {
			pr	= new PrintWriter(new File("AbstractPubMedDocs/abstractmedData"+i+".txt"));
			pr.write(string);
			pr.flush();
			pr.close();
			i++;
		}
		
		System.out.println("dfONE");
		
		
		
	}
//	public static void main(String[] args) throws FileNotFoundException {
//		System.out.println("RUN");
//		parsefile(new File("abstract_pubmed_result.txt"));
//	}

}
