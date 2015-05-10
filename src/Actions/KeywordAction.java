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
