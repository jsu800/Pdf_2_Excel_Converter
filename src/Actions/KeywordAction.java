package Actions;


import StateMachine.PDFKeywordState;
import Transaction.AccountObject;

/*
* PDF2Excel Project
* @author Joseph Su
*
*/


public class KeywordAction extends AbstractAction<PDFKeywordState> {

	public KeywordAction(AccountObject obj) {
		super(obj);
	}

	
	public KeywordAction(AbstractAction<PDFKeywordState> nextAction, AccountObject obj) {
		super(nextAction, obj);
	}

	
	@Override
	public PDFKeywordState process(String input, String selection, String fileName, PDFKeywordState state) {
		
		PDFKeywordState retVal = PDFKeywordState.KEYWORD;
		
		
		return retVal;		
	}
	

	@Override
	public void terminate(PDFKeywordState currentState) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	
}
