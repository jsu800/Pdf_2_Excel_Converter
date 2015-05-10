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


package Actions;

import StateMachine.PDFKeywordState;
import Transaction.AccountObject;


/*
 * @author Joseph Su
 * 
 */

public class TransitionAction extends AbstractAction<PDFKeywordState> {

	public TransitionAction(AccountObject obj) {
		super(obj);
	}

	public TransitionAction(AbstractAction<PDFKeywordState> nextAction,
			AccountObject obj) {
		super(nextAction, obj);
		// TODO Auto-generated constructor stub
	}

	public PDFKeywordState process(PDFKeywordState last, PDFKeywordState current) {

		return current;
	}

	@Override
	public PDFKeywordState process(String input, String selection,
			String fileName, PDFKeywordState state) throws Exception {
		PDFKeywordState retVal = PDFKeywordState.TRANSITION;

		return retVal;
	}

	@Override
	public void terminate(PDFKeywordState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

}
