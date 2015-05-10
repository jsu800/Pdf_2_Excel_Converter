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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import StateMachine.PDFKeywordState;
import Transaction.AccountObject;

/* 
 * @author Joseph Su
 *
 * Parser action: specific parse logic may be extended to other 
 * subclasses of AbstractAction
 *
 */

public class ParseAction extends AbstractAction<PDFKeywordState> {

	public ParseAction(AccountObject obj) {
		this(null, obj);
	}

	public ParseAction(AbstractAction<PDFKeywordState> nextAction,
			AccountObject obj) {
		super(nextAction, obj);

	}

	public AccountObject getAccount() {
		return super.obj;
	}

	@Override
	public void terminate(PDFKeywordState currentState) throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public PDFKeywordState process(String input, String selection,
			String fileName, PDFKeywordState state) throws Exception {

		PDFKeywordState retVal = PDFKeywordState.PARSE;

		// Parse, and populate account class
		boolean isParsed = parse(input, fileName, Integer.parseInt(selection));

		// Done with parsing, set next action
		if (isParsed)
			retVal = PDFKeywordState.EXCEL;

		return retVal;
	}

	private boolean parse(String in, String fileName, int selection) {

		String endingRegEx;
		boolean isGoodToGo = false;
		CharSequence cs;

		switch (selection) {

		case 1:
			endingRegEx = "\\d\\d\\s\\d\\d\\d\\s\\d\\d\\d\\d";
			isGoodToGo = processString(in, null, endingRegEx, false);

			// Just to be absolutely sure we should check to see if the
			// statement contains
			// more keywords before proceeding!
			cs = "Bank Statement Balance :";
			isGoodToGo = in.contains(cs);

			if (isGoodToGo) {
				endingRegEx = "[:\\s]*[,.0-9-]*";
				processShowName(in);
				processString(in, "Bank Statement Balance", endingRegEx, true);
				processString(in, "Outstanding Checks", endingRegEx, true);
				processString(in, "Outstanding J.E. Deposits", endingRegEx,
						true);
				processString(in, "Adjusted Bank Balance", endingRegEx, true);
				processString(in, "Expected Ledger Balance", endingRegEx, true);
				processString(in, "Actual Ledger Balance", endingRegEx, true);
				processString(in, "Unreconciled Amt", endingRegEx, true);
				super.obj.setOutputFileName("VISTA.xls");
			}
			break;
		case 2:
			endingRegEx = "[:\\s]*[a-zA-Z0-9]*[\\s]{0,1}[a-zA-Z0-9]*";
			isGoodToGo = processCheckID(in, "Checkbook ID", endingRegEx);
			if (isGoodToGo) {
				processGPShowName(in);
				endingRegEx = "[\\s]*[$,.0-9-]*";
				processString(in, "Statement Ending Balance ", endingRegEx);
				processString(in, "Outstanding Checks \\(-\\)", endingRegEx);
				processString(in, "Deposits in Transit \\(\\+\\)", endingRegEx);
				processString(in, "Adjusted Bank Balance", endingRegEx);
				processString(in, "Checkbook Balance as of Cutoff", endingRegEx);
				processString(in, "Adjustments", endingRegEx);
				processString(in, "Adjusted Book Balance", endingRegEx);
				processString(in, "Difference", endingRegEx);
				super.obj.setOutputFileName("GP.xls");
			}
			break;
		case 3:
			cs = "PO.pdf";
			if (fileName.contains(cs)) {
				cs = "MOBILITY PRODUCTIONS";
				if (in.contains(cs)) {

					// before we start let's make sure we clear the previous
					// super.obj list
					super.obj.po_clear();

					endingRegEx = ".*" + cs + ".*";
					processMobilityProductions(in, endingRegEx);
					super.obj.setOutputFileName("PO.xls");
					super.obj.setInputFileName(fileName);
					isGoodToGo = true;
				}
			}
			break;
		case 4:
			processCPASShowName(in);
			processBankName(in);
			processAccountingNumbers(in);
			super.obj.setOutputFileName("CPAS.xls");
			isGoodToGo = true;
			break;
		default:
			break;

		}

		return isGoodToGo;

	}

	private void processMobilityProductions(String in, String regex) {

		String value;
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(in);

		while (m.find()) {

			value = m.group();

			String results[] = value.split("MOBILITY PRODUCTIONS");

			if (results.length > 1) {

				String pre = results[0];
				String post = results[1];

				String[] preResults = pre.split("[\\s]{2,}");
				String[] postResults = post.split("[\\s]{2,}");

				for (String r : preResults) {
					super.obj.po_insert(r.trim());
				}

				super.obj.po_insert("MOBILITY PRODUCTIONS");

				for (String r : postResults) {
					super.obj.po_insert(r.trim());
				}

				// adding an artificial delimiter
				super.obj.po_insert("=");

			}

		}

	}

	private void processGPShowName(String in) {
		String regex = "[\\d]*-[\\w\\s]*";

		String value = null;
		Pattern p;
		Matcher m;

		p = Pattern.compile(regex);
		m = p.matcher(in);
		if (m.find()) {
			value = m.group(0);

			super.obj.setShowName(value);
		}

	}

	private boolean processCheckID(String in, String key, String regex) {

		String value = null;
		Pattern p;
		Matcher m;
		boolean retVal = true;

		if (key != null)
			p = Pattern.compile(key + regex);
		else
			return false;

		m = p.matcher(in);

		if (m.find()) {
			value = m.group(0);
			if (value == null) {
				System.out
						.println("Sorry but there's nothing to parse! Please check your input files again.");
				retVal = false;
			}
		} else {
			retVal = false;
		}

		if (value != null) {

			String[] results = value.split(":");

			if (results.length == 2) {
				value = results[1].trim();
			}

		} else
			retVal = false;

		super.obj.setCheckbookID(value);

		return retVal;

	}

	private void processShowName(String in) {

		String regex = "C=CLEARED" + "[\\d\\w\\s\\n\\r\\t-.]*\\d{2}";
		String showName = null;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(in);

		if (m.find()) {

			String line = m.group(0);

			String[] results = line.split("\n");

			if (results.length > 1) {

				String name = results[2];
				name = name.trim();

				if (name.length() == 2) {

					if (isNumeric(name)) {
						showName = "N/A";
					} else {
						showName = results[2];
					}
				} else {
					showName = results[2];
				}
			}

			super.obj.setShowName(showName);

		}

	}

	private void processAccountingNumbers(String in) {

		String regex = "Difference :";
		for (int i = 0; i < 9; i++)
			regex += "[\\n\\r]*[$,.0-9-]*";

		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(in);

		if (m.find()) {

			String line = m.group(0);
			line = line.replace("Difference :", "");
			String[] numbers = line.split("\n");

			if (numbers.length > 1)
				insertNumber("Beginning Balance", getDouble(numbers[1]));
			if (numbers.length > 6)
				insertNumber("Outstanding Debits", getDouble(numbers[6]));
			if (numbers.length > 7)
				insertNumber("Outstanding Credits", getDouble(numbers[7]));
			if (numbers.length > 3)
				insertNumber("Adjusted Bank Balance", getDouble(numbers[3]));
			if (numbers.length > 8)
				insertNumber("GL Balance", getDouble(numbers[8]));

		}
	}

	private void insertNumber(String key, double d) {
		super.obj.insert(key, d);
	}

	private void processBankName(String in) {

		String regex = ".*(Bank).*:";
		String bankName = null;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(in);

		if (m.find()) {

			String line = m.group(0);

			// if the current line doesn't contain ( then we have to go back two
			// lines up in regex
			if (line.contains("(") == false) {
				regex = "(.*)[\\n\\r]*(.*)[\\n\\r]*.*(Bank).*:";
				p = Pattern.compile(regex);
				m = p.matcher(in);
				if (m.find())
					line = m.group(0);
			}

			bankName = line.replace("\n", "").replace("\r", "")
					.replace("Bank :", "");

			super.obj.setBankName(bankName);
		}
	}

	private void processCPASShowName(String in) {

		String regex = "(BankRec.*)[\\n\\r]*.*";
		String showName = null;
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(in);

		// System.out.println(in);

		if (m.find()) {

			String line = m.group(0);
			showName = line.replace("BankRec", "").replace("\n", "")
					.replace("\r", "");
			System.out.println("Show Name: " + showName);

			super.obj.setShowName(showName);
		}
	}

	private boolean isNumeric(String str) {
		boolean retVal = true;

		try {
			Double.parseDouble(str);
		} catch (NumberFormatException nfe) {
			retVal = false;
		}

		return retVal;
	}

	/*
	 * For processing GP reports
	 */
	private boolean processString(String in, String key, String regex) {
		String value = null;
		Pattern p;
		Matcher m;

		p = Pattern.compile(key + regex);
		m = p.matcher(in);

		if (m.find()) {
			value = m.group(0);
			if (value == null) {
				System.out
						.println("Sorry but there's nothing to parse! Please check your input files again.");
				return false;
			} else
				value = value.trim();
		} else {
			return false;
		}

		// System.out.println("value = " + value);

		// Check to see if value is negative
		CharSequence negativeSign = "-$";
		boolean isNegative = value.contains(negativeSign);

		String[] results = value.split("\\$");

		if (results.length == 2) {
			Double d = getDouble(results[1]);
			key = key.replace("\\", "");
			if (isNegative)
				super.obj.insert(key, -d);
			else
				super.obj.insert(key, d);
		}

		return true;
	}

	/*
	 * For processing VISTA reports
	 */
	private boolean processString(String in, String key, String regex,
			boolean toSplit) {

		String value = null;
		Pattern p;
		Matcher m;

		if (key != null)
			p = Pattern.compile(key + regex);
		else
			p = Pattern.compile(regex);

		m = p.matcher(in);

		if (m.find()) {
			value = m.group(0);
			if (value == null) {
				System.out
						.println("Sorry but there's nothing to parse! Please check your input files again.");
				return false;
			}
		} else {
			return false;
		}

		if (toSplit) {

			String[] results = value.split(":");

			if (results.length == 2) {
				Double d = getDouble(results[1]);
				super.obj.insert(key, d);
			}

		} else {

			super.obj.setAccountNumber(value);

		}

		return true;
	}

	private Double getDouble(String str) {

		boolean isNegative = false;

		if (str.contains("-"))
			isNegative = true;

		str = str.replaceAll("[,\\s-]", "");

		if (isNegative)
			str = "-" + str;

		return Double.parseDouble(str);
	}

}