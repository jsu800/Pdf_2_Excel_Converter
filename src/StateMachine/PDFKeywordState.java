package StateMachine;

public enum PDFKeywordState {
	
	RUNNING("Running"),	
	TRANSITION("Trasition"),
	KEYWORD("Keyword"),
	EXCEL("Excel"),
	PARSE("Parse"),
	OFF("Off");
	
	private final String description;
	
	PDFKeywordState(String description) {
		this.description = description;
	}

	public String toString() {
		return "PDFKeywordState: " + description;
	}
	
}
