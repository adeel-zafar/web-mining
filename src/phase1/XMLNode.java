package phase1;

public enum XMLNode {
	SUBJECT("subject"),
	CONTENT("content"),
	DATE("date"),
	TIMESTAMP("timestamp"),
	USERID("userid"),
	USERNICK("usernick"),
	ANSWER_TIMESTAMP("chosenanswertimestamp");
	
	private final String text;
	
	XMLNode(String str) {
		text = str;
	}
	
	public String toString() {
		return text;
	}
	
	public static XMLNode getNode(String s){
		for(XMLNode x : XMLNode.values()){
			if(x.text.equalsIgnoreCase(s))
				return x;
		}
		
		return null;
	}
	
}
