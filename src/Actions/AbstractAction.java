package Actions;

import Transaction.AccountObject;

/* 
 * PDF2Excel Project
 * @author Joseph Su
 *
 * Write this using Chain of Responsibility design pattern
 * 
 * (1) set action: AbstractAction(nextAction)
 * (2) execute next state: setNext(state)
 * 
 */

public abstract class AbstractAction <T extends Enum<?>> {

	protected final AbstractAction<T> nextAction;
	protected AccountObject obj;
	
	public AbstractAction(AccountObject obj) {
		this(null, obj);
	}
	
	public AbstractAction(AbstractAction<T> nextAction, AccountObject obj) {
		this.nextAction = nextAction;
		this.obj = obj;
	}
	
	public AccountObject getAccount() {
		return obj;
	}
	
	/*
	 * This is Chain of Responsibility Design Pattern
	 * 
	 */	
	public void setNext(T state) throws Exception { 
		
		if (nextAction != null) {
			nextAction.process(null, null, null, state);
		}
	}
	
	
	/* 
	 * This will be handled by each respective action class 
	 */
	public abstract T process(String input, String selection, String fileName, T state) throws Exception;
	
	/* 
	 * This method is called when InputStream terminates
	 * 
	 */
	public abstract void terminate(T currentState) throws Exception;



	
	
	
	
}
