package PDF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

public class PDFDocument {
	 
	PDDocument pd;
	BufferedWriter wr;
	
	
	public PDFDocument() {
		
	}

	
	/* 
	 * This will load a document from a file
	 */
	public String loadFile(File input) {

		String retVal = null;
		
		try {
			
		    pd = PDDocument.load(input);				
		    retVal = getText(pd);
		    pd.close();
	        
		} catch (Exception ioe) {
			ioe.printStackTrace();
		}
		
		return retVal;
	}

	/*
	 * PDFTextStripper takes a pdf document and strips out all of the text and ignore the formatting and such
	 */
	private String getText(PDDocument pd) throws IOException {
		
		PDFTextStripper stripper = new PDFTextStripper();
		return stripper.getText(pd);        
	}
	
	
}
