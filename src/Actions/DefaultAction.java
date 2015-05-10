package Actions;

import StateMachine.PDFKeywordState;
import Transaction.AccountObject;

/* 
 * This is the default action that does nothing when State is set to OFF
 */
public class DefaultAction extends AbstractAction<PDFKeywordState> {


	public DefaultAction(AccountObject obj) {
		super(obj);
	}
	
	public DefaultAction(AbstractAction<PDFKeywordState> nextAction, AccountObject obj) {
		super(nextAction, obj);
		// TODO Auto-generated constructor stub
	}

	@Override
	public PDFKeywordState process(String input, String selection, String fileName, PDFKeywordState state) throws Exception {
		
		PDFKeywordState retVal = state;
		
		return retVal;	

	}

	

	@Override
	public void terminate(PDFKeywordState currentState) throws Exception {
		// TODO Auto-generated method stub
		
	}

}
