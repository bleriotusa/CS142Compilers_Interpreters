package crux;

import java.util.ArrayList;
import types.*;
import java.util.List;
import java.util.Stack;

import ast.Command;
import ast.DeclarationList;
import ast.Expression;
import ast.ExpressionList;
import ast.Statement;
import ast.StatementList;

public class Parser {
    public static String studentName = "Michael J. Chen";
    public static String studentID = "37145431";
    public static String uciNetID = "chenmj1";

// Typing System ===================================
    
    private Type tryResolveType(String typeStr)
    {
        return Type.getBaseType(typeStr);
    }
  
// Parser ==========================================
    private Scanner scanner;
    private Token currentToken;
    
    public Parser(Scanner scanner)
    {
    	this.scanner = scanner;
    	currentToken = this.scanner.next();
    }
    
    public ast.Command parse()
    {
        initSymbolTable();
        try {
            return program();
        } catch (QuitParseException q) {
            return new ast.Error(lineNumber(), charPosition(), "Could not complete parsing.");
        }
    }
// SymbolTable Management ==========================
    private SymbolTable symbolTable;
    private Stack<SymbolTable> stack;
    
    private void initSymbolTable()
    {
    	stack = new Stack<SymbolTable>();
    	symbolTable = new SymbolTable();
    	
    	// add pre-defined functions
    	symbolTable.insertWithType("readInt", new FuncType(new TypeList(), new IntType()));
    	symbolTable.insertWithType("readFloat", new FuncType(new TypeList(), new FloatType()));
    	symbolTable.insertWithType("printBool", new FuncType(new TypeList().append(new BoolType()), new VoidType()));
    	symbolTable.insertWithType("printInt", new FuncType(new TypeList().append(new IntType()), new VoidType()));
    	symbolTable.insertWithType("printFloat", new FuncType(new TypeList().append(new FloatType()), new VoidType()));
    	
    	stack.push(symbolTable);
    }
    
    private void enterScope()
    {
    	stack.push(new SymbolTable(stack.peek()));
    	symbolTable = stack.peek();
    }
    
    private void exitScope()
    {
    	stack.pop();
    	symbolTable = stack.peek();
    }

    private Symbol tryResolveSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.lookup(name);
        } catch (SymbolNotFoundError e) {
            String message = reportResolveSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportResolveSymbolError(String name, int lineNum, int charPos)
    {
        String message = "ResolveSymbolError(" + lineNum + "," + charPos + ")[Could not find " + name + ".]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }

    private Symbol tryDeclareSymbol(Token ident)
    {
        assert(ident.is(Token.Kind.IDENTIFIER));
        String name = ident.lexeme();
        try {
            return symbolTable.insert(name);
        } catch (RedeclarationError re) {
            String message = reportDeclareSymbolError(name, ident.lineNumber(), ident.charPosition());
            return new ErrorSymbol(message);
        }
    }

    private String reportDeclareSymbolError(String name, int lineNum, int charPos)
    {
        String message = "DeclareSymbolError(" + lineNum + "," + charPos + ")[" + name + " already exists.]";
        errorBuffer.append(message + "\n");
        errorBuffer.append(symbolTable.toString() + "\n");
        return message;
    }    
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
        
	@SuppressWarnings("unused")
	private boolean expect(NonTerminal nt)
    {
        if (accept(nt))
            return true;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return false;
    }

    private Token expectRetrieve(Token.Kind kind)
    {
        Token tok = currentToken;
        if (accept(kind))
            return tok;
        String errorMessage = reportSyntaxError(kind);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    }
        
    private Token expectRetrieve(NonTerminal nt)
    {
        Token tok = currentToken;
        if (accept(nt))
            return tok;
        String errorMessage = reportSyntaxError(nt);
        throw new QuitParseException(errorMessage);
        //return ErrorToken(errorMessage);
    } 
   
// Grammar Rules =====================================================
        
    // literal := INTEGER | FLOAT | TRUE | FALSE .
    public ast.Expression literal()
    {
        ast.Expression expr;
        enterRule(NonTerminal.LITERAL);
        
        Token tok = expectRetrieve(NonTerminal.LITERAL);
        expr = Command.newLiteral(tok);
        
        exitRule(NonTerminal.LITERAL);
        return expr;
    }
    // designator := IDENTIFIER { "[" expression0 "]" } .
    public Expression designator()
    {
        enterRule(NonTerminal.DESIGNATOR);
        Token token = expectRetrieve(Token.Kind.IDENTIFIER);
        Symbol symbol = tryResolveSymbol(token);
        
		ast.Expression expr = new ast.AddressOf(currentToken.lineNumber(), currentToken.charPosition(),
				symbol);
		
        while (accept(Token.Kind.OPEN_BRACKET)) {
    		token = currentToken;
    		expr = new ast.Index(token.lineNumber(), token.charPosition(), expr, expression0());

            expect(Token.Kind.CLOSE_BRACKET);
            
        }
        
        exitRule(NonTerminal.DESIGNATOR);
        return expr;
    }

    public ast.Expression expression0()
    {
    	enterRule(NonTerminal.EXPRESSION0);
    	Token op = null;
    	
    	ast.Expression leftSide  = expression1();
    	ast.Expression rightSide = null;
    	if(have(NonTerminal.OP0))
    	{
    		op = op0();
    		rightSide = expression1();
    	}
    	    	
    	exitRule(NonTerminal.EXPRESSION0);
    	return (op != null)? ast.Command.newExpression(leftSide, op, rightSide)
    			: leftSide;
    }
    
    public ast.Expression expression1()
    {
    	enterRule(NonTerminal.EXPRESSION1);
    	Token op = null;
    	
    	ast.Expression leftSide  = expression2();
    	ast.Expression rightSide = null;
    	
    	while(have(NonTerminal.OP1)){
    		
    		if(rightSide == null)
    		{
    			op = op1();
    			rightSide = expression2();
    		}
    		else
    		{
    			leftSide = ast.Command.newExpression(leftSide, op, rightSide);
    			op = op1();
    			rightSide = expression2();    
    		}
    	}
    	
    	exitRule(NonTerminal.EXPRESSION1);
    	
    	return (op != null)? ast.Command.newExpression(leftSide, op, rightSide)
    			: leftSide;
    }
    
    public ast.Expression expression2()
    {
    	enterRule(NonTerminal.EXPRESSION2);
    	Token op = null;
    	
    	ast.Expression leftSide  = expression3();
    	ast.Expression rightSide = null;
    	while(have(NonTerminal.OP2)){
    		if(rightSide == null)
    		{
        		op = op2();
    			rightSide = expression3();
    		}
    		else
    		{
    			leftSide = ast.Command.newExpression(leftSide, op, rightSide);
        		op = op2();
    			rightSide = expression3();
    		}
		}
    	
    	exitRule(NonTerminal.EXPRESSION2);
    	return (op != null)? ast.Command.newExpression(leftSide, op, rightSide)
    			: leftSide;
    }
    
    public ast.Expression expression3()
    {
    	enterRule(NonTerminal.EXPRESSION3);
    	ast.Expression expr = null;
    	Token token = currentToken;
    	
    	if(accept(Token.Kind.NOT))
    		expr = new ast.LogicalNot(token.lineNumber(), token.charPosition(),
    				expression3());
    	else if(accept(Token.Kind.OPEN_PAREN))
    	{
    		expr = expression0();
    		expect(Token.Kind.CLOSE_PAREN);
    	}
    	else if(have(NonTerminal.CALL_EXPRESSION))
    		expr = call_expression();
    	else if(have(NonTerminal.LITERAL))
    		expr = literal();
    	else if(have(NonTerminal.DESIGNATOR))
    		expr = new ast.Dereference(currentToken.lineNumber(), currentToken.charPosition(),
    				designator());
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.EXPRESSION3);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.EXPRESSION3);
    	return expr;
    }
    
    public Token op0()
    {
    	enterRule(NonTerminal.OP0);
    	Token token = null; 
    	
    	switch(currentToken.kind().toString()){
		case "GREATER_EQUAL": 
			token = expectRetrieve(Token.Kind.GREATER_EQUAL); break;
		case "LESSER_EQUAL":
			token = expectRetrieve(Token.Kind.LESSER_EQUAL); break;
		case "NOT_EQUAL":
			token = expectRetrieve(Token.Kind.NOT_EQUAL); break;
		case "EQUAL":
			token = expectRetrieve(Token.Kind.EQUAL); break;
		case "GREATER_THAN":
			token = expectRetrieve(Token.Kind.GREATER_THAN); break;
		case "LESS_THAN":
			token = expectRetrieve(Token.Kind.LESS_THAN); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP0);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP0);
    	return token;
    }
    
    public Token op1()
    {
    	enterRule(NonTerminal.OP1);
    	Token token = null; 

    	switch(currentToken.kind().toString()){
    	case "ADD":
    		token = expectRetrieve(Token.Kind.ADD); break;
    	case "SUB":
    		token = expectRetrieve(Token.Kind.SUB); break;
    	case "OR":
    		token = expectRetrieve(Token.Kind.OR); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP1);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP1);
    	return token;
    }
    public Token op2()
    {
    	enterRule(NonTerminal.OP2);
    	Token token = null; 

    	switch(currentToken.kind().toString()){
    	case "MUL":
    		token = expectRetrieve(Token.Kind.MUL); break;
    	case "DIV":
    		token = expectRetrieve(Token.Kind.DIV); break;
    	case "AND":
    		token = expectRetrieve(Token.Kind.AND); break;
		default:String errorMessage = reportSyntaxError(NonTerminal.OP2);
				throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.OP2);
    	return token;
    }
    public ast.Call call_expression()
    {
    	enterRule(NonTerminal.CALL_EXPRESSION);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.CALL);
    	Symbol symbol = tryResolveSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.OPEN_PAREN);
    	ast.ExpressionList args = expression_list();
    	expect(Token.Kind.CLOSE_PAREN);
    	
    	exitRule(NonTerminal.CALL_EXPRESSION);
    	return new ast.Call(lineNum, charPos, symbol, args);
    }
    public ExpressionList expression_list()
    {
    	enterRule(NonTerminal.EXPRESSION_LIST);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	ast.ExpressionList expList = new ast.ExpressionList(lineNum, charPos);
    	
    	if(have(NonTerminal.EXPRESSION0))
    	{
    		do 
    		{
    			expList.add(expression0());
    		}while(accept(Token.Kind.COMMA));
    	}
    	exitRule(NonTerminal.EXPRESSION_LIST);
    	return expList;
    }
    
    public ast.Assignment assignment_statement()
    {
    	enterRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.LET);
    	ast.Expression dest = designator();
    	expect(Token.Kind.ASSIGN);
    	ast.Expression source = expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.ASSIGNMENT_STATEMENT);
    	return new ast.Assignment(lineNum, charPos, dest, source);

    }
    
    public ast.DeclarationList declaration_list()
    {
    	enterRule(NonTerminal.DECLARATION_LIST);

    	ast.DeclarationList decList = new ast.DeclarationList(currentToken.lineNumber(),
    			currentToken.charPosition());
    	
    	while(have(NonTerminal.DECLARATION))
    	{
    		decList.add(declaration());
    	}

    	exitRule(NonTerminal.DECLARATION_LIST);
    	return decList;
    }
    
    public ast.Declaration declaration()
    {
    	enterRule(NonTerminal.DECLARATION);
    	
    	ast.Declaration declaration = null;

    	if(have(NonTerminal.VARIABLE_DECLARATION))
    		declaration = variable_declaration();
    	else if(have(NonTerminal.ARRAY_DECLARATION))
    		declaration = array_declaration();
    	else if(have(NonTerminal.FUNCTION_DEFINITION))
    		declaration = function_definition();
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.DECLARATION);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.DECLARATION);
    	return declaration;
    }
    
    public ast.VariableDeclaration variable_declaration()
    {
    	enterRule(NonTerminal.VARIABLE_DECLARATION);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.VAR);
    	Symbol symbol = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	symbol.setType(type()); 
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.VARIABLE_DECLARATION);
    	return new ast.VariableDeclaration(lineNum, charPos, symbol);
    }
    
    public ast.ArrayDeclaration array_declaration()
    {
    	enterRule(NonTerminal.ARRAY_DECLARATION);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.ARRAY);
    	Symbol symbol = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
//    	symbol.setType(type()); 
    	Type type = type();
    	ArrayType arrayType = null;
    	int extent = 0;
    	int passed = 0;
    	Stack<Integer> indices = new Stack<Integer>();
    	do{
	    	expect(Token.Kind.OPEN_BRACKET);
	    	extent = Integer.parseInt(expectRetrieve(Token.Kind.INTEGER).lexeme());
	    	if(passed > 0) {
	    		arrayType.setBase(new ArrayType(extent, arrayType.base()));
	    	}
	    	else
	    		arrayType = new ArrayType(extent, type);
	    	expect(Token.Kind.CLOSE_BRACKET);
	    	passed++;
	    	System.out.println("Passed " + passed);
    	}while(have(Token.Kind.OPEN_BRACKET));
    	expect(Token.Kind.SEMICOLON);
    	
    	symbol.setType(arrayType);
    	exitRule(NonTerminal.ARRAY_DECLARATION);
    	return new ast.ArrayDeclaration(lineNum, charPos, symbol);
    }
    
    public ast.FunctionDefinition function_definition()
    {
    	enterRule(NonTerminal.FUNCTION_DEFINITION);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.FUNC);
    	Symbol symbol = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	enterScope();
    	
    	expect(Token.Kind.OPEN_PAREN);
//    	List<Symbol> args = parameter_list();
//    	TypeList typeList = new TypeList();
//    	for(Symbol parameter: args)
//    		typeList.append(parameter.type());
    	
    	TypeList args = parameter_list();

    	expect(Token.Kind.CLOSE_PAREN);
    	expect(Token.Kind.COLON);
    	Type returnType = type();
//    	symbol.setType(new FuncType(typeList, returnType));
    	symbol.setType(new FuncType(args, returnType));

    	StatementList body = statement_block();
    	    	
    	exitScope();
    	exitRule(NonTerminal.FUNCTION_DEFINITION);
    	
    	return new ast.FunctionDefinition(lineNum,
    			charPos, symbol , args, body);
    }
    
    public types.Type type()
    {
    	types.Type type = null;
    	
    	enterRule(NonTerminal.TYPE);
    	type = types.Type.getBaseType(expectRetrieve(Token.Kind.IDENTIFIER).lexeme());
    	exitRule(NonTerminal.TYPE);
    	
		return type;
    }
    
    public TypeList parameter_list()
    {
    	enterRule(NonTerminal.PARAMETER_LIST);
    	List<Symbol> parameterList = new ArrayList<Symbol>();
    	TypeList typelist = new TypeList();
    	
    	if(have(NonTerminal.PARAMETER))
    	{
//	    	parameterList.add(parameter());
    		typelist.append(parameter().type());
	    	while(accept(Token.Kind.COMMA))
	    	{
//	    		parameterList.add(parameter());
	    		typelist.append(parameter().type());
	    	}
    	}
    	
    	exitRule(NonTerminal.PARAMETER_LIST);
//    	return parameterList;
    	return typelist;
    }
    
    public Symbol parameter()
    {
    	enterRule(NonTerminal.PARAMETER);
    	Symbol parameter;
    	
    	parameter = tryDeclareSymbol(expectRetrieve(Token.Kind.IDENTIFIER));
    	expect(Token.Kind.COLON);
    	parameter.setType(type());
    	
    	exitRule(NonTerminal.PARAMETER);
    	return parameter;
    }
    
    public StatementList statement_block()
    {
    	ast.StatementList statementList;
    	enterRule(NonTerminal.STATEMENT_BLOCK);
    	
    	expect(Token.Kind.OPEN_BRACE);
    	statementList = statement_list();
    	expect(Token.Kind.CLOSE_BRACE);
    	
    	exitRule(NonTerminal.STATEMENT_BLOCK);
    	return statementList;
    }
    
    public StatementList statement_list()
    {
    	ast.StatementList statementList;

    	enterRule(NonTerminal.STATEMENT_LIST);
    	statementList = new ast.StatementList(currentToken.lineNumber(), currentToken.charPosition());

    	while(have(NonTerminal.STATEMENT))
    	{
    		statementList.add(statement());
    	}
    	
    	exitRule(NonTerminal.STATEMENT_LIST);
    	return statementList;
    }
    
    public Statement statement()
    {
    	enterRule(NonTerminal.STATEMENT);
    	ast.Statement statement = null;
    	
    	if(have(NonTerminal.VARIABLE_DECLARATION))
    		statement = variable_declaration();
    	else if(have(NonTerminal.CALL_STATEMENT))
    		statement = call_statement();
    	else if(have(NonTerminal.ASSIGNMENT_STATEMENT))
    		statement = assignment_statement();
    	else if(have(NonTerminal.IF_STATEMENT))
    		statement = if_statement();
    	else if(have(NonTerminal.WHILE_STATEMENT))
    		statement = while_statement();
    	else if(have(NonTerminal.RETURN_STATEMENT))
    		statement = return_statement();
    	else
    	{
    		String errorMessage = reportSyntaxError(NonTerminal.STATEMENT);
    		throw new QuitParseException(errorMessage);
    	}
    	
    	exitRule(NonTerminal.STATEMENT);
    	return statement;
    }
    
    public ast.Call call_statement()
    {
    	enterRule(NonTerminal.CALL_STATEMENT);
    	
    	ast.Call call = call_expression();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.CALL_STATEMENT);
    	return call;
    }
    
    public ast.IfElseBranch if_statement()
    {
    	enterRule(NonTerminal.IF_STATEMENT);
    	enterScope();
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.IF);
    	ast.Expression cond = expression0();
    	StatementList thenBlock = statement_block();
    	
    	StatementList elseBlock = new ast.StatementList(currentToken.lineNumber(), currentToken.charPosition());
    	exitScope();
    	if(accept(Token.Kind.ELSE))
    	{
    		enterScope();
    		 elseBlock = statement_block();
    		exitScope();
    	}
    	
    	exitRule(NonTerminal.IF_STATEMENT);
    	return new ast.IfElseBranch(lineNum, charPos, cond, thenBlock, elseBlock);
    }
    
    public ast.WhileLoop while_statement()
    {
    	enterRule(NonTerminal.WHILE_STATEMENT);
    	enterScope();
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.WHILE);
    	ast.Expression cond = expression0();
    	StatementList body = statement_block();
    	
    	exitScope();
    	exitRule(NonTerminal.WHILE_STATEMENT);
    	return new ast.WhileLoop(lineNum, charPos, cond, body);
    }
    
    public ast.Return return_statement()
    {
    	enterRule(NonTerminal.RETURN_STATEMENT);
    	int lineNum = currentToken.lineNumber();
    	int charPos = currentToken.charPosition();
    	
    	expect(Token.Kind.RETURN);
    	ast.Expression arg = expression0();
    	expect(Token.Kind.SEMICOLON);
    	
    	exitRule(NonTerminal.RETURN_STATEMENT);
    	return new ast.Return(lineNum, charPos, arg);
    }
    
    // program := declaration-list EOF .
    public DeclarationList program()
    {
    	ast.DeclarationList decList;
    	enterRule(NonTerminal.PROGRAM);
    	
//    	decList = new ast.DeclarationList(currentToken.lineNumber(), currentToken.charPosition());
    	
    	decList = declaration_list();
    	expect(Token.Kind.EOF);
    	
    	exitRule(NonTerminal.PROGRAM);
//        throw new RuntimeException("add code to each grammar rule, to build as ast.");
		return decList;
    }
    
}
