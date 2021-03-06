package crux;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import crux.Token.Kind;

@SuppressWarnings("unused")
public class Parser {
    public static String studentName = "Michael J. Chen";
    public static String studentID = "37145431";
    public static String uciNetID = "chenmj1";
    
    
// Grammar Rule Reporting ==========================================
    private int parseTreeRecursionDepth = 0;
    private StringBuffer parseTreeBuffer = new StringBuffer();

    public void enterRule(NonTerminal nonTerminal) {
        String lineData = new String();
        for(int i = 0; i < parseTreeRecursionDepth; i++)
        {
            lineData += "  ";
        }
        lineData += nonTerminal.name();
        //System.out.println("descending " + lineData);
        parseTreeBuffer.append(lineData + "\n");
        parseTreeRecursionDepth++;
    }
    
    private void exitRule(NonTerminal nonTerminal)
    {
        parseTreeRecursionDepth--;
    }
     
    public String parseTreeReport()
    {
        return parseTreeBuffer.toString();
    }

// Error Reporting ==========================================
    private StringBuffer errorBuffer = new StringBuffer();
    
    private String reportSyntaxError(NonTerminal nt)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected a token from " + nt.name() + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
     
    private String reportSyntaxError(Token.Kind kind)
    {
        String message = "SyntaxError(" + lineNumber() + "," + charPosition() + ")[Expected " + kind + " but got " + currentToken.kind() + ".]";
        errorBuffer.append(message + "\n");
        return message;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    private class QuitParseException extends RuntimeException
    {
        private static final long serialVersionUID = 1L;
        public QuitParseException(String errorMessage) {
            super(errorMessage);
        }
    }
    
    private int lineNumber()
    {
        return currentToken.lineNumber();
    }
    
    private int charPosition()
    {
        return currentToken.charPosition();
    }
          
// Parser ==========================================
    private Scanner scanner;
    private Token currentToken;
    
    public Parser(Scanner scanner)
    {
    	this.scanner = scanner;
    	currentToken = this.scanner.next();
    }
    
    public void parse()
    {
        try {
            program();
        } catch (QuitParseException q) {
            errorBuffer.append("SyntaxError(" + lineNumber() + "," + charPosition() + ")");
            errorBuffer.append("[Could not complete parsing.]");
        }
    }
    
// Helper Methods ==========================================
    private boolean have(Token.Kind kind)
    {
        return currentToken.is(kind);
    }
    
    private boolean have(NonTerminal nt)
    {
        return nt.firstSet().contains(currentToken.kind());
    }

    private boolean accept(Token.Kind kind)
    {
        if (have(kind)) {
        	
            currentToken = scanner.next();
            return true;
        }
        return false;
    }    
    
    private boolean accept(NonTerminal nt)
    {
        if (have(nt)) {
            currentToken = scanner.next();
            return true;
        }
        return false;
    }
   
    private boolean expect(Token.Kind kind)
    {
        if (accept(kind))
            return true;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return false;
    }
        
	private boolean expect(NonTerminal nt)
    {
        if (accept(nt))
            return true;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return false;
    }
   
// Grammar Rules =====================================================
    
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public void literal()
    {
    	enterRule(NonTerminal.LITERAL);
    	
    	switch(currentToken.kind().toString()){
    		case "INTEGER": 
    			accept(Token.Kind.INTEGER); break;
    		case "FLOAT":
    			accept(Token.Kind.FLOAT); break;
    		case "TRUE":
    			accept(Token.Kind.TRUE); break;
    		case "FALSE":
    			accept(Token.Kind.FALSE); break;
    		default:String errorMessage = reportSyntaxError(NonTerminal.LITERAL);
    				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.LITERAL);
    }
    
    // designator := IDENTIFIER { "[" expression0 "]" } .
    public void designator()
    {
        enterRule(NonTerminal.DESIGNATOR);

        expect(Token.Kind.IDENTIFIER);
        while (accept(Token.Kind.OPEN_BRACKET)) {
            expression0();
            expect(Token.Kind.CLOSE_BRACKET);
        }
        
        exitRule(NonTerminal.DESIGNATOR);
    }

    public void expression0()
    {
    	enterRule(NonTerminal.EXPRESSION0);
    	
    	expression1();
    	if(have(NonTerminal.OP0))
    	{
    		op0();
    		expression1();
    	}
    	    	
    	exitRule(NonTerminal.EXPRESSION0);
    }
    
    public void expression1()
    {
    	enterRule(NonTerminal.EXPRESSION1);
    	
    	expression2();
    	while(have(NonTerminal.OP1)){
    		op1();
    		expression2();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION1);
    }
    
    public void expression2()
    {
    	enterRule(NonTerminal.EXPRESSION2);
    	
    	expression3();
    	while(have(NonTerminal.OP2)){
    		op2();
    		expression3();
    	}
    	
    	exitRule(NonTerminal.EXPRESSION2);
    }
    
    public void expression3()
    {
    	enterRule(NonTerminal.EXPRESSION3);
    	
    	if(accept(Token.Kind.NOT))
    		expression3();
    	else if(accept(Token.Kind.OPEN_PAREN))
    	{
    		expression0();
    		expect(Token.Kind.CLOSE_PAREN);
    	}
    	else if(have(NonTerminal.DESIGNATOR))
    		designator();
    	else if(have(NonTerminal.CALL_EXPRESSION))
    		call_expression();
    	else if(have(NonTerminal.LITERAL))
    		literal();
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.EXPRESSION3);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.EXPRESSION3);
    }
    
    public void op0()
    {
    	enterRule(NonTerminal.OP0);
    	
    	switch(currentToken.kind().toString()){
		case "GREATER_EQUAL": 
			expect(Token.Kind.GREATER_EQUAL); break;
		case "LESSER_EQUAL":
			expect(Token.Kind.LESSER_EQUAL); break;
		case "NOT_EQUAL":
			expect(Token.Kind.NOT_EQUAL); break;
		case "EQUAL":
			expect(Token.Kind.EQUAL); break;
		case "GREATER_THAN":
			expect(Token.Kind.GREATER_THAN); break;
		case "LESS_THAN":
			expect(Token.Kind.LESS_THAN); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP0);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP0);
    }
    
    public void op1()
    {
    	enterRule(NonTerminal.OP1);
    	
    	switch(currentToken.kind().toString()){
    	case "ADD":
    		expect(Token.Kind.ADD); break;
    	case "SUB":
    		expect(Token.Kind.SUB); break;
    	case "OR":
    		expect(Token.Kind.OR); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP1);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP1);
    }
    public void op2()
    {
    	enterRule(NonTerminal.OP2);
    	
    	switch(currentToken.kind().toString()){
    	case "MUL":
    		expect(Token.Kind.MUL); break;
    	case "DIV":
    		expect(Token.Kind.DIV); break;
    	case "AND":
    		expect(Token.Kind.AND); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP2);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP2);
    }
    public void call_expression()
    {
    	enterRule(NonTerminal.CALL_EXPRESSION);
    	
    	expect(Token.Kind.CALL);
    	expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.OPEN_PAREN);
    	expression_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	
    	exitRule(NonTerminal.CALL_EXPRESSION);
    }
    public void expression_list()
    {
    	enterRule(NonTerminal.EXPRESSION_LIST);
    	
    	if(have(NonTerminal.EXPRESSION0))
    	{
    		do 
    		{
    			expression0();
    		}while(accept(Token.Kind.COMMA));
    	}
    	exitRule(NonTerminal.EXPRESSION_LIST);
    }
    
    public void assignment_statement()
    {
    	enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	
    	expect(Token.Kind.LET);
    	designator();
    	expect(Token.Kind.ASSIGN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
    }
    
    public void declaration_list()
    {
    	enterRule(NonTerminal.DECLARATION_LIST);

    	while(have(NonTerminal.DECLARATION))
    	{
    		declaration();
    	}

    	exitRule(NonTerminal.DECLARATION_LIST);
    }
    
    public void declaration()
    {
    	enterRule(NonTerminal.DECLARATION);

    	if(have(NonTerminal.VARIABLE_DECLARATION))
    		variable_declaration();
    	else if(have(NonTerminal.ARRAY_DECLARATION))
    		array_declaration();
    	else if(have(NonTerminal.FUNCTION_DEFINITION))
    		function_definition();
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.DECLARATION);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.DECLARATION);
    }
    
    public void variable_declaration()
    {
    	enterRule(NonTerminal.VARIABLE_DECLARATION);
    	
    	expect(Token.Kind.VAR);
    	expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type(); 
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.VARIABLE_DECLARATION);
    }
    
    public void array_declaration()
    {
    	enterRule(NonTerminal.ARRAY_DECLARATION);
    	
    	expect(Token.Kind.ARRAY);
    	expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type(); 
    	do{
    	expect(Token.Kind.OPEN_BRACKET);
    	expect(Token.Kind.INTEGER);
    	expect(Token.Kind.CLOSE_BRACKET);
    	}while(have(Token.Kind.OPEN_BRACKET));
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.ARRAY_DECLARATION);
    }
    
    public void function_definition()
    {
    	enterRule(NonTerminal.FUNCTION_DEFINITION);

    	expect(Token.Kind.FUNC);
    	expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.OPEN_PAREN);
    	parameter_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	expect(Token.Kind.COLON);
    	type();
    	statement_block();
    	
    	exitRule(NonTerminal.FUNCTION_DEFINITION);
    }
    
    public void type()
    {
    	enterRule(NonTerminal.TYPE);
    	expect(Token.Kind.IDENTIFIER);
    	exitRule(NonTerminal.TYPE);
    }
    
    public void parameter_list()
    {
    	enterRule(NonTerminal.PARAMETER_LIST);
    	if(have(NonTerminal.PARAMETER))
    	{
	    	parameter();
	    	while(accept(Token.Kind.COMMA))
	    	{
	    		parameter();
	    	}
    	}
    	
    	exitRule(NonTerminal.PARAMETER_LIST);
    }
    
    public void parameter()
    {
    	enterRule(NonTerminal.PARAMETER);
    	
    	expect(Token.Kind.IDENTIFIER);
    	expect(Token.Kind.COLON);
    	type();
    	
    	exitRule(NonTerminal.PARAMETER);
    }
    
    public void statement_block()
    {
    	enterRule(NonTerminal.STATEMENT_BLOCK);
    	
    	expect(Token.Kind.OPEN_BRACE);
    	statement_list();
    	expect(Token.Kind.CLOSE_BRACE);
    	
    	exitRule(NonTerminal.STATEMENT_BLOCK);
    }
    
    public void statement_list()
    {
    	enterRule(NonTerminal.STATEMENT_LIST);
    	
    	while(have(NonTerminal.STATEMENT))
    	{
    		statement();
    	}
    	
    	exitRule(NonTerminal.STATEMENT_LIST);
    }
    
    public void statement()
    {
    	enterRule(NonTerminal.STATEMENT);
    	
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    		variable_declaration();
    	else if(have(NonTerminal.CALL_STATEMENT))
    		call_statement();
    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT))
    		assignment_statement();
    	else if(have(NonTerminal.IF_STATEMENT))
    		if_statement();
    	else if(have(NonTerminal.WHILE_STATEMENT))
    		while_statement();
    	else if(have(NonTerminal.RETURN_STATEMENT))
    		return_statement();
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.STATEMENT);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.STATEMENT);	
    }
    
    public void call_statement()
    {
    	enterRule(NonTerminal.CALL_STATEMENT);
    	
    	call_expression();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.CALL_STATEMENT);
    }
    
    public void if_statement()
    {
    	enterRule(NonTerminal.IF_STATEMENT);
    	
    	expect(Token.Kind.IF);
    	expression0();
    	statement_block();
    	if(accept(Token.Kind.ELSE))
    		statement_block();
    	
    	exitRule(NonTerminal.IF_STATEMENT);
    }
    
    public void while_statement()
    {
    	enterRule(NonTerminal.WHILE_STATEMENT);
    	
    	expect(Token.Kind.WHILE);
    	expression0();
    	statement_block();
    	
    	exitRule(NonTerminal.WHILE_STATEMENT);
    }
    
    public void return_statement()
    {
    	enterRule(NonTerminal.RETURN_STATEMENT);
    	
    	expect(Token.Kind.RETURN);
    	expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.RETURN_STATEMENT);
    }
    
    
    // program := declaration-list EOF .
    public void program()
    {
    	enterRule(NonTerminal.PROGRAM);
    	
    	declaration_list();
    	expect(Token.Kind.EOF);
    	
    	exitRule(NonTerminal.PROGRAM);
    }
    
}
