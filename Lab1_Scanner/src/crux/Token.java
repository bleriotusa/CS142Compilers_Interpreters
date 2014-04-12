package crux;

public class Token {
	
	public static enum Kind {
		AND("and"),
		OR("or"),
		NOT("not"),
		LET("let"),
		VAR("var"),
		ARRAY("array"),
		FUNC("func"),
		IF("if"),
		ELSE("else"),
		WHILE("while"),
		TRUE("true"),
		FALSE("false"),
		RETURN("return"),
		
		OPEN_PAREN("("),
		CLOSE_PAREN(")"),
		OPEN_BRACE("{"),
		CLOSE_BRACE("}"),
		OPEN_BRACKET("["),
		CLOSE_BRACKET("]"),
		ADD("+"),
		SUB("-"),
		MUL("*"),
		DIV("/"),
		GREATER_EQUAL(">="),
		LESSER_EQUAL("<="),
		NOT_EQUAL("!="),
		EQUAL("=="),
		GREATER_THAN(">"),
		LESS_THAN("<"),
		ASSIGN("="),
		COMMA(","),
		SEMICOLON(";"),
		COLON(":"),
		CALL("::"),
		
		IDENTIFIER(),
		INTEGER(),
		FLOAT(),
		ERROR(),
		EOF();
		
		// TODO: complete the list of possible tokens
		
		private String default_lexeme;
		
		Kind()
		{
			default_lexeme = "";
		}
		
		Kind(String lexeme)
		{
			default_lexeme = lexeme;
		}
		
		public boolean hasStaticLexeme()
		{
//			return default_lexeme != null; 
			return !default_lexeme.equals("");//I CHANGED FROM ORIGINAL***
		}
		
		public void setValue(String value)
		{
			default_lexeme = value;
		}
		
		public String getValue()
		{
			return default_lexeme;
		}
		
		// OPTIONAL: if you wish to also make convenience functions, feel free
		//           for example, boolean matches(String lexeme)
		//           can report whether a Token.Kind has the given lexeme
	}
	
	private int lineNum;
	private int charPos;
	Kind kind;
	private String lexeme = "";
	
	
	// OPTIONAL: implement factory functions for some tokens, as you see fit
	           
	public static Token EOF(int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.EOF;
		return tok;
	}
	
	public static Token Integer(int linePos, int charPos)
	{
		Token tok = new Token(linePos, charPos);
		tok.kind = Kind.INTEGER;
		return tok;
	}
	private Token(int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		
		// if we don't match anything, signal error
//		this.kind = Kind.ERROR;
//		this.lexeme = "No Lexeme Given";
	}
	
	public Token(String lexeme, int lineNum, int charPos)
	{
		this.lineNum = lineNum;
		this.charPos = charPos;
		this.lexeme = lexeme;
		
		// TODO: based on the given lexeme determine and set the actual kind
		switch(lexeme){
		case "and": kind = Kind.AND; break;
		case "or": kind = Kind.OR; break;
		case "not": kind = Kind.NOT; break;
		case "let": kind = Kind.LET; break;
		case "var": kind = Kind.VAR; break;
		case "array": kind = Kind.ARRAY; break;
		case "func": kind = Kind.FUNC; break;
		case "if": kind = Kind.IF; break;
		case "else": kind = Kind.ELSE; break;
		case "while": kind = Kind.WHILE; break;
		case "true": kind = Kind.TRUE; break;
		case "false": kind = Kind.FALSE; break;
		case "return": kind = Kind.RETURN; break;
		case "(": kind = Kind.OPEN_PAREN; break;
		case ")": kind = Kind.CLOSE_PAREN; break;
		case "{": kind = Kind.OPEN_BRACE; break;
		case "}": kind = Kind.CLOSE_BRACE; break;
		case "[": kind = Kind.OPEN_BRACKET; break;
		case "]": kind = Kind.CLOSE_BRACKET; break;
		case "+": kind = Kind.ADD; break;
		case "-": kind = Kind.SUB; break;
		case "*": kind = Kind.MUL; break;
		case "/": kind = Kind.DIV; break;
		case ">=": kind = Kind.GREATER_EQUAL; break;
		case "<=": kind = Kind.LESSER_EQUAL; break;
		case "!=": kind = Kind.NOT_EQUAL; break;
		case "==": kind = Kind.EQUAL; break;
		case ">": kind = Kind.GREATER_THAN; break;
		case "<": kind = Kind.LESS_THAN; break;
		case "=": kind = Kind.ASSIGN; break;
		case ",": kind = Kind.COMMA; break;
		case ";": kind = Kind.SEMICOLON; break;
		case ":": kind = Kind.COLON; break;
		case "::": kind = Kind.CALL; break;
		default: kind = Kind.ERROR; break;
		}
		
		// if we don't match anything, signal error
//		this.kind = Kind.ERROR;
//		this.lexeme = "Unrecognized lexeme: " + lexeme;
	}
	
	public int lineNumber()
	{
		return lineNum;
	}
	
	public int charPosition()
	{
		return charPos;
	}
	
	// Return the lexeme representing or held by this token
	public String lexeme()
	{
		// TODO: implement // implemented
		return lexeme;
	}
	
	// a crux string has more matches if:
	// 1. it is a valid integer
	// 2. it is a valid float
	// 3. it is a valid identifier (which includes reserved keywords that are letters)
	// 4. it is a valid reserved character that is not a full reserved character
	// 		e.g. = valid but not full. >= is valid and full, so it has no more matches.
	public static boolean hasMoreMatches(String kindPart)
	{
//		if (isInteger(kindPart))
//			return true;
		if (isValidResCharThatHasMatches(kindPart))
			return true;
//		if(isValidIdentifier(kindPart))
//			return true;	
		return false;
				
	}
	
	public String toString()
	{
		// TODO: implement this // implemented
		return String.format("%s(lineNum:%d, charPos:%d)", kind.name(), lineNum, charPos);
	}
	
	// OPTIONAL: function to query a token about its kind
	//           boolean is(Token.Kind kind)
	
	// OPTIONAL: add any additional helper or convenience methods
	//           that you find make for a clean design
	public static boolean isInteger(String tokenStr){
		try{
			Integer.parseInt(tokenStr);
		}
		catch(NumberFormatException e){
			return false;
		}
		return true;
	}
	
	public static boolean isValidIdentifier(String tokenStr){
		if(tokenStr.length() == 0)
			return true;
		boolean validIdentifier = false;
		int currChar = 0;
		int firstChar = tokenStr.charAt(0);

		if((firstChar >= 97 && firstChar <= 122)
		|| (firstChar >= 65 && firstChar <= 122)
		|| (firstChar == 95))
			validIdentifier = true;
		else
			return false;

		for(int i = 0; i < tokenStr.length(); i++)
		{
			currChar = tokenStr.charAt(i);
			if ((currChar >= 97 && currChar <= 122)
				|| (currChar >= 65 && currChar <= 132)
				|| (currChar >= 48 && currChar <= 57)
				|| (currChar == 95))
				validIdentifier = true;
			else
				return false;	
		}
		return validIdentifier;
	}
	
	private static boolean isValidResCharThatHasMatches(String tokenStr)
	{
		for(Kind kind: Kind.values())
			if(kind.getValue().contains(tokenStr) && 
					!kind.getValue().equals(tokenStr))
				return true;
		return false;
	}

}
