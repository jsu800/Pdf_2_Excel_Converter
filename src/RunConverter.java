import java.io.File;
import java.util.Scanner;

import Transaction.AccountObject;
import Actions.DefaultAction;
import Actions.ExcelAction;
import Actions.KeywordAction;
import Actions.ParseAction;
import Actions.TransitionAction;
import PDF.PDFDocument;
import StateMachine.FiniteStateMachine;
import StateMachine.PDFKeywordState;



/* 
 * This is the domain-specific code. 
 */
public class RunConverter {

	public FiniteStateMachine<PDFKeywordState> machine;
	public PDFKeywordState currentState = PDFKeywordState.OFF;
	public final String INPUT_FOLDER = "./input"; 
	public String selection;
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// Let's get the command line user input
		Scanner scanner = new Scanner(System.in);
		String userSelection;
		
		while (true) {
			System.out.println("NOTE: for GP report generation please make sure you have the right set of reports to begin with.");
			System.out.println("Please select one of the following choices:");
			System.out.println(">> 1: VISTA");
			System.out.println(">> 2: GP");
			System.out.println(">> 3: PO");
			System.out.println(">> 4: Cost Report");
			userSelection = scanner.nextLine();

			if (userSelection.equalsIgnoreCase("1".trim()) || userSelection.equalsIgnoreCase("2".trim()) || userSelection.equalsIgnoreCase("3".trim()) || userSelection.equalsIgnoreCase("4".trim())) {
				break;
			} else {
				System.out.println("Please Try Again with a Valid Numeric Choice!");
			}
		}		
				
		RunConverter converter = new RunConverter(userSelection);
		converter.machine = new FiniteStateMachine<PDFKeywordState>(PDFKeywordState.OFF);
		converter.initializeMachine();
		converter.start(new File(converter.INPUT_FOLDER));

	
	}
	
	public RunConverter(String selection) {		
		this.selection = selection;
	}
	
	private void initializeMachine() {	
		
		AccountObject account = new AccountObject();
		
		// Add a list of domain specific actions
		machine.addAction(PDFKeywordState.OFF, new DefaultAction(null));
		machine.addAction(PDFKeywordState.TRANSITION, new TransitionAction(null));
		machine.addAction(PDFKeywordState.PARSE, new ParseAction(account));
		machine.addAction(PDFKeywordState.EXCEL, new ExcelAction(account));
		machine.addAction(PDFKeywordState.KEYWORD, new KeywordAction(null));
		
	}

	
	/* 
	 * This method should load a batch of files at a designated location
	 * and for each file loaded its payload in String sets the state machine
	 * in motion to be processed by its appropriate action class that handles
	 * it
	 * 
	 * 1. Load batch of files one at a time
	 * 2. Process each file to fetch data (PARSE)
	 * 3. Process data and store in destination (EXCEL)
	 */
	private void start(final File folder) {
		
		PDFDocument doc;
		boolean retVal = true;
				
		for (final File entry : folder.listFiles()) {
			
			if (entry.isDirectory()) {
				start(entry);
			} else {
				
				// Make sure we don't load dot files such as .DS_Store, .profiles, etc that are hidden in System
				if (entry.getName().startsWith(".")) {
					
					continue;
					
				} else {
					doc = new PDFDocument();
					String input = doc.loadFile(entry);
					
					String fileName = entry.getName();
					
					//System.out.println("====================");
					//System.out.println(input);
					//System.out.println(entry.getName());
					
					if (!input.isEmpty()) {
						retVal = machine.process(input, selection, fileName, PDFKeywordState.PARSE);
					}	
					
					if (retVal == false) 
						break;					
				}

			}			
		}
		
		// When done send a terminate event, writing to EXCEL
		machine.setCurrentState(PDFKeywordState.EXCEL);
		machine.terminate();
		
	}



}
