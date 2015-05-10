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

package StateMachine;

import java.util.HashMap;
import java.util.Map;

import Actions.AbstractAction;

/* 
 * @author Joseph Su
 * 
 * Declaring and defining the state machine, which comes with
 * a number of states (T) as defined in the PDFKeywordState enum class. 
 * 
 * This machine has-a Abstraction Action of many actions. These actions are stored
 * inside a HashMap (not synchronized) of <key, value> = <state, action> pairs
 * 
 * 
 * State Machine processes file streams (PDF)
 * 
 */
public class FiniteStateMachine<T extends Enum<?>> {

	private T currentState;
	private final Map<T, AbstractAction<T>> actionMap = new HashMap<T, AbstractAction<T>>();

	public FiniteStateMachine(T state) {
		this.currentState = state;
	}

	/*
	 * 
	 * This loops until state != currentState It's the behavior of each state of
	 * remember or know to terminate the state (by setting it to OFF) when it
	 * thinks delegation is done
	 */

	public boolean process(String input, String selection, String fileName,
			T state) {

		boolean retVal = true;

		currentState = state;

		try {

			AbstractAction<T> action = getAction(currentState);
			state = action.process(input, selection, fileName, currentState);

			if (isStateChanged(currentState, state) == true) {

				currentState = state;
				process(null, selection, fileName, state);

			} else if (state == PDFKeywordState.OFF) {

				retVal = false;

			} else {

			}

		} catch (Exception ioe) {
			ioe.printStackTrace();
			System.out.println("FiniteStateMachine:process(): Exception");
		}

		return retVal;
	}

	public T getCurrentState() {
		return currentState;
	}

	public void setCurrentState(T currentState) {
		this.currentState = currentState;
	}

	private boolean isStateChanged(T current, T state) {

		return (state != currentState && state != PDFKeywordState.OFF);
	}

	private AbstractAction<T> getAction(T state) {
		return actionMap.get(state);
	}

	/*
	 * Add action to the existing action map
	 */
	public void addAction(T state, AbstractAction<T> action) {
		actionMap.put(state, action);
	}

	/*
	 * Remove action from the existing action map
	 */
	public void removeAction(T state) {
		actionMap.remove(state);
	}

	/*
	 * Done with InputStream and there's nothing more to do. We call
	 * terminate().
	 */
	public void terminate() {

		AbstractAction<T> action = actionMap.get(currentState);

		try {
			if (action != null)
				action.terminate(currentState);

		} catch (Exception ioe) {

			ioe.printStackTrace();
			System.out.println("FiniteStateMachine:terminate(): Exception");
		}
	}

}
