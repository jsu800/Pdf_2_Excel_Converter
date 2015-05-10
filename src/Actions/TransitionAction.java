package Actions;

import StateMachine.PDFKeywordState;
import Transaction.AccountObject;

public class TransitionAction extends AbstractAction<PDFKeywordState> {


	public TransitionAction(AccountObject obj) {
		super(obj);
	}

	public TransitionAction(AbstractAction<PDFKeywordState> nextAction, AccountObject obj) {
		super(nextAction, obj);
		// TODO Auto-generated constructor stub
	}

	public PDFKeywordState process(PDFKeywordState last, PDFKeywordState current) {
		
		
		
		
		return current;
	}
	
	
	@Override
	public PDFKeywordState process(String input, String selection, String fileName, PDFKeywordState state) throws Exception {
		PDFKeywordState retVal = PDFKeywordState.TRANSITION;
		
		
		
		return retVal;	
	}
	
	
	

	@Override
	public void terminate(PDFKeywordState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

}
