package crux;
import java.io.IOException;
import java.io.Reader;
import java.util.Iterator;


public class Scanner implements Iterable<Token> {
	public static String studentName = "Michael J. Chen";
	public static String studentID = "37145431";
	public static String uciNetID = "chenmj1";
	
	private int lineNum;  // current line count
	private int charPos;  // character offset for current line
	private int nextChar; // contains the next char (-1 == EOF)
	private Reader input;
	
	private static final int EOF = -1;
	
	Scanner(Reader reader)
	{
		// TODO: initialize the Scanner
		input = reader;
		lineNum +=1;
	}	
	
	// OPTIONAL: helper function for reading a single char from input
	//           can be used to catch and handle any IOExceptions,
	//           advance the charPos or lineNum, etc.
	
	// usage: -1 is EOF, otherwise, cast return to char
	private int readChar() {
		int character = '\0';
		try{
			character = input.read();
			if((char)character == '\n')
			{
				lineNum += 1;
				charPos = 0;
			}
			else
				charPos += 1;
		}
		catch(IOException e){
			e.printStackTrace();
			System.err.println("Error reading next character from file");
			System.exit(-2);
		}

		return character;
	}
	
		

	/* Invariants:
	 *  1. call assumes that nextChar is already holding an unread character
	 *  2. return leaves nextChar containing an untokenized character
	 */
	public Token next()
	{
		// TODO: implement this
		// SPECIAL CASES
		// if no char has been read yet, read a first character
		if(nextChar == '\0')
			nextChar = readChar();
		
		// make sure starting character isn't whitespace
		while(Character.isWhitespace(nextChar))
			nextChar = readChar();
		
		// if end of File has already been read, keep returning EOF
		if(nextChar == EOF)
			return Token.EOF(lineNum, charPos);
		
		
		String tokenStr = "";
		Token token = null;
		int startCharPos = charPos;
		boolean tokenSet = false;

		// while another token can still be made and character isn't whitespace,
		// try to map the string lexical to a token
		while(!Character.isWhitespace(nextChar) && 
				Token.hasMoreMatches(tokenStr))
		{
			tokenStr += (char)nextChar;
//			System.out.println("Scanner Loop Passed");
			
			for(Token.Kind kind : Token.Kind.values())
			{
				if(kind.getValue().equals(tokenStr))
				{
					token = new Token(tokenStr, lineNum, startCharPos);
					tokenSet = true;
				}
				else if(Token.isInteger(tokenStr))
				{
					token = Token.Integer(lineNum, startCharPos);
					tokenSet = true;
				}
				else if(tokenStr.equals(Integer.toString(EOF)))
				{
					token = Token.EOF(lineNum, startCharPos);
					tokenSet = true;
				}
			}
			
			// only read on if file is not at the end,
			// and a token has been set, if token exists but hasn't been set this time... 
//			if(nextChar != EOF && Token.hasMoreMatches(tokenStr))
			if(nextChar != EOF)
				nextChar = readChar();
		}
		return (token == null)? new Token("Error", lineNum, startCharPos) : token;
	}

	@Override
	public Iterator<Token> iterator() {
		// TODO Auto-generated method stub
		return null;
	}
	private boolean hasPossibleMatches(String tokenStr){
		if(Token.hasMoreMatches(tokenStr))
			;
		return true;
	}

	// OPTIONAL: any other methods that you find convenient for implementation or testing

}
