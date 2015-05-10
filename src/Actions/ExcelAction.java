/**
 * 
 */
package Actions;

import java.io.IOException;
import java.util.ArrayList;

import jxl.write.WriteException;

import Excel.WriteExcel;
import StateMachine.PDFKeywordState;
import Transaction.AccountObject;



/**
 * PDF2Excel Project
 * @author Joseph Su
 *
 * This class writes data into a single Excel spreadsheet
 *
 */
public class ExcelAction extends AbstractAction<PDFKeywordState> {

	
	private WriteExcel out;
	private ArrayList<AccountObject> list;
	private String selection;
	 
	
	
	public ExcelAction(AccountObject obj) {
		this(null, obj);
	}

	
	/**
	 * @param nextAction
	 * @param os
	 */
	public ExcelAction(AbstractAction<PDFKeywordState> nextAction, AccountObject obj) {
		super(nextAction, obj);
		
		list = new ArrayList<AccountObject>();
		out = new WriteExcel();
	}

	
	/*
	 * (non-Javadoc)
	 * @see Actions.AbstractAction#process(java.lang.String, java.lang.String, java.lang.Enum)
	 * 
	 * This method gets call multiple time iteratively until the list of files is exhausted
	 * 
	 */
	public PDFKeywordState process(String input, String selection, String fileName, PDFKeywordState state) throws Exception {
				
		PDFKeywordState retVal = PDFKeywordState.EXCEL;
		
		if (state == retVal) {
			
			this.selection = selection;
			
			// get a copy of the obj and put it into the global list
			AccountObject copy = new AccountObject(super.obj);
						
			//System.out.println("Adding: " + fileName);
			
			list.add(copy);
			
		} else {
			retVal = PDFKeywordState.OFF;
		}
		
		return retVal;	
	}

	/*
	 * @see Actions.AbstractAction#terminate(java.lang.Enum)
	 * This method is only called once at the end of adding all the lists to ArrayList<AccountObject>
	 * 
	 */
	@Override
	public void terminate(PDFKeywordState currentState) throws WriteException, IOException {
		
		System.out.println("Writing: " + currentState);

		String fileName;
		
		if (!list.isEmpty()) {

			fileName = list.get(0).getOutputFileName();
			
			out.setOutputFile("./output/" + fileName);
			
			out.setSelection(Integer.parseInt(selection));

			out.setList(list);
			
			out.write();
			
			// After done purge the list
			list.clear();
			
		}
		

		System.out.println("Done Writing: " + currentState);

	}

}
