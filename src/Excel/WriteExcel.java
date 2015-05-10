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

package Excel;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Map;

import Transaction.AccountObject;

import jxl.CellView;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.format.UnderlineStyle;
import jxl.write.Label;
import jxl.write.Number;
import jxl.write.WritableCellFormat;
import jxl.write.WritableFont;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import jxl.write.WriteException;
import jxl.write.biff.RowsExceededException;

/*
 * @author Joseph Su
 * 
 * Specific Excel I/O
 * 
 */

public class WriteExcel {


	private WritableCellFormat cellFormat;
	private WritableCellFormat times;
	private CellView cv;
	
	private String outputFile;	
	private ArrayList<AccountObject> list = null;
	private int numEntries;
	private int selection = -1;
	
	
	public WriteExcel() {
	}
	
	public void setSelection(int selection) {
		this.selection = selection;
	}
	
	public void setOutputFile(String outputFile) {
		this.outputFile = outputFile;
	}

	public void setList(ArrayList<AccountObject> list) {
		this.list = list;
	}
	
	public void write() throws IOException, WriteException {
				
		if (!list.isEmpty())
			numEntries = list.size();
		
		File file = new File(outputFile);
		WorkbookSettings wbSettings = new WorkbookSettings();

		wbSettings.setLocale(new Locale("en", "EN"));

		WritableWorkbook workbook = Workbook.createWorkbook(file, wbSettings);
		workbook.createSheet("Account Information", 0);
		WritableSheet excelSheet = workbook.getSheet(0);
		
		setFormat(excelSheet);
		
		createHeaders(excelSheet);
		
		createContent(excelSheet);		

		workbook.write();
		
		workbook.close();
	}

	private void setFormat(WritableSheet sheet) throws WriteException {
		// Lets create a times font
		WritableFont timesFont = new WritableFont(WritableFont.TIMES, 14);
		
		// Define the cell format
		times = new WritableCellFormat(timesFont);
		
		// Lets automatically wrap the cells
		times.setWrap(true);

		
		// Create create a bold font with underlines
		WritableFont times10ptBoldUnderline = new WritableFont(
				WritableFont.TIMES, 14, WritableFont.BOLD, false,
				UnderlineStyle.SINGLE);
		
		cellFormat = new WritableCellFormat(times10ptBoldUnderline);
		
		// Lets automatically wrap the cells
		cellFormat.setWrap(true);		
		
		cv = new CellView();
		cv.setFormat(cellFormat);
		
	}
	
	private void addCellView(WritableSheet sheet, String header, int index) throws WriteException {

		int len = header.length();		
		cv.setSize((len+3)*256);
		
		sheet.setColumnView(index, cv);
		
	}
	
	private void createHeaders(WritableSheet sheet) throws WriteException {


		// Write out column header titles using the first entry in list
		if (numEntries > 0) {
			
			int xIndex = 0;
			AccountObject obj = list.get(0);
			Map<String, Double> accountMap = obj.getMap();	
			
			switch (selection) {
			
			case 1:
				addCellView(sheet, "Account Number", xIndex);
				addCaption(sheet, xIndex++, 0, "Account Number");	
				
				addCellView(sheet, "Show Name             ", xIndex);
				addCaption(sheet, xIndex++, 0, "Show Name");	
				
				for (String key : accountMap.keySet()) {
					
					addCellView(sheet, key, xIndex);
					addCaption(sheet, xIndex++, 0, key);
				}
				break;
			case 2: 
				addCellView(sheet, "Checkbook ID           ", xIndex);
				addCaption(sheet, xIndex++, 0, "Checkbook ID");	
				
				addCellView(sheet, "Show Name             ", xIndex);
				addCaption(sheet, xIndex++, 0, "Show Name");	
				
				for (String key : accountMap.keySet()) {
					
					addCellView(sheet, key, xIndex);
					addCaption(sheet, xIndex++, 0, key);
				}
				break;
				
			case 3: 
				addCellView(sheet, "Input File Name           ", xIndex);
				addCaption(sheet, xIndex++, 0, "Input File Name");	
				break;
				
			case 4: 
				addCellView(sheet, "Show Name             ", xIndex);
				addCaption(sheet, xIndex++, 0, "Show Name");
				
				addCellView(sheet, "Bank             ", xIndex);
				addCaption(sheet, xIndex++, 0, "Bank");	

				addCellView(sheet, "Beginning Balance             ", xIndex);
				addCaption(sheet, xIndex++, 0, "Beginning Balance");	

				addCellView(sheet, "Outstanding Debits    ", xIndex);
				addCaption(sheet, xIndex++, 0, "Outstanding Debits");	

				addCellView(sheet, "Outstanding Credits    ", xIndex);
				addCaption(sheet, xIndex++, 0, "Outstanding Credits");	

				addCellView(sheet, "Adjusted Bank Balance    ", xIndex);
				addCaption(sheet, xIndex++, 0, "Adjusted Bank Balance");	

				addCellView(sheet, "GL Balance    ", xIndex);
				addCaption(sheet, xIndex++, 0, "GL Balance");
				break;
				
			default:
				break;
			}
			
			
			
		}
		

	}

	private void createContent(WritableSheet sheet) throws WriteException, RowsExceededException {
		
		if (numEntries > 0) {
			
			int yIndex = 1;
			int xIndex = 0;
			
			for (AccountObject obj : list) {

				Map<String, Double> accountMap;
				
				switch (selection) {
				
				case 1:
					addData(sheet, xIndex++, yIndex, obj.getAccountNumber());
					addData(sheet, xIndex++, yIndex, obj.getShowName());
					
					accountMap = obj.getMap();
					for (Double value : accountMap.values()) {
						addNumber(sheet, xIndex++, yIndex, value.doubleValue());					
					}
					break;
					
				case 2: 
					addData(sheet, xIndex++, yIndex, obj.getCheckbookID());
					addData(sheet, xIndex++, yIndex, obj.getShowName());
					
					accountMap = obj.getMap();
					for (Double value : accountMap.values()) {
						addNumber(sheet, xIndex++, yIndex, value.doubleValue());					
					}
					break;
				
				case 3: 									
						String inputFileName = obj.getInputFileName();
						addData(sheet, xIndex++, yIndex, inputFileName);
						
						ArrayList<String> dataList = obj.getPOList();
						for (String data : dataList) {							
							if (data == "=") {
								xIndex = 1;
								yIndex++;
								addData(sheet, 0, yIndex, inputFileName);	
								continue;
							} else {
								addData(sheet, xIndex++, yIndex, data);	
							}
						}
						yIndex--;
							
					break;
					
				case 4:
					addData(sheet, xIndex++, yIndex, obj.getShowName());
					addData(sheet, xIndex++, yIndex, obj.getBankName());
					accountMap = obj.getMap();
					for (Double value : accountMap.values()) {
						addNumber(sheet, xIndex++, yIndex, value.doubleValue());					
					}
					break;
					
				default: 
					break;
				}
												
				xIndex=0;
				yIndex++;
			} // end for loop
			
			// TODO - FIX THIS BAD CODING PRACTICE HERE
			// rewriting the last entry which would have been a file name with empty string
			if (selection == 3)
				addData(sheet, 0, yIndex, "");
		}
				
	}

	private void addCaption(WritableSheet sheet, int column, int row, String s)
			throws RowsExceededException, WriteException {
		Label label;
		label = new Label(column, row, s, cellFormat);
		sheet.addCell(label);
	}

	private void addNumber(WritableSheet sheet, int column, int row,
			double d) throws WriteException, RowsExceededException {
		Number number;
		number = new Number(column, row, d, times);
		sheet.addCell(number);
	}


	private void addData(WritableSheet sheet, int column, int row, String s)
			throws WriteException, RowsExceededException {
		Label label;
		label = new Label(column, row, s, times);
		sheet.addCell(label);
	}
}