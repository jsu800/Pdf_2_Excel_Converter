/*
 * Copyright (c) 2014 Joseph Su
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package PDF;

import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.util.PDFTextStripper;

/*
 * @author Joseph Su
 * 
 * PDF document reader I/O
 * 
 */

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
