package types;

import java.util.HashMap;
import ast.*;

public class TypeChecker implements CommandVisitor {
    
    private HashMap<Command, Type> typeMap;
    private StringBuffer errorBuffer;
    private ast.FunctionDefinition currentFunctionNode;

    /* Useful error strings:
     *
     * "Function " + func.name() + " has a void argument in position " + pos + "."
     * "Function " + func.name() + " has an error in argument in position " + pos + ": " + error.getMessage()
     *
     * "Function main has invalid signature."
     *
     * "Not all paths in function " + currentFunctionName + " have a return."
     *
     * "IfElseBranch requires bool condition not " + condType + "."
     * "WhileLoop requires bool condition not " + condType + "."
     *
     * "Function " + currentFunctionName + " returns " + currentReturnType + " not " + retType + "."
     *
     * "Variable " + varName + " has invalid type " + varType + "."
     * "Array " + arrayName + " has invalid base type " + baseType + "."
     */

    public TypeChecker()
    {
        typeMap = new HashMap<Command, Type>();
        errorBuffer = new StringBuffer();
        
        // initiate pre-defined functions
        
    }

    private void reportError(int lineNum, int charPos, String message)
    {
        errorBuffer.append("TypeError(" + lineNum + "," + charPos + ")");
        errorBuffer.append("[" + message + "]" + "\n");
    }

    private void put(Command node, Type type)
    {
        if (type instanceof ErrorType) {
            reportError(node.lineNumber(), node.charPosition(), ((ErrorType)type).getMessage());
        }
        typeMap.put(node, type);
//        System.out.println(typeMap);
    }
    private boolean thisPathHasReturn(StatementList node)
    {

    	if(node == null)
    		return true;
    	// check if this depth has a return
    	for(Statement statement: node)
    	{
    		if((statement instanceof Return))
    			return true;
    	}
    	return false;
    }
    
    private boolean allPathsHaveReturn(StatementList node)
    {
    	if(thisPathHasReturn(node))
    		return true;
    	
    	// check if all other paths have return 
    	for(Statement statement: node)
    	{
    		if(statement instanceof IfElseBranch) {
    			if(!thisPathHasReturn(((IfElseBranch) statement).thenBlock()) || !thisPathHasReturn(((IfElseBranch) statement).elseBlock()))
    				return false;
    		}
    		else if(statement instanceof WhileLoop) {
    			if(!thisPathHasReturn(((WhileLoop) statement).body()));
    				return false;
    		}
    	}   
    	
    	return true;
    }
    
    public Type getType(Command node)
    {
        return typeMap.get(node);
    }
    
    public boolean check(Command ast)
    {
        ast.accept(this);
        return !hasError();
    }
    
    public boolean hasError()
    {
        return errorBuffer.length() != 0;
    }
    
    public String errorReport()
    {
        return errorBuffer.toString();
    }
    
   private TypeList expList2typeList(ExpressionList expList)
   {
	   TypeList typeList = new TypeList();
	   for(Expression expression : expList)
		   typeList.append(getType((Command) expression));
	   return typeList;
   }

    @Override
    public void visit(ExpressionList node) {
		for (Expression e : node)
			e.accept(this);
		
    }

    @Override
    public void visit(DeclarationList node) {
		for (Declaration d : node)
			d.accept(this);
    }

    @Override
    public void visit(StatementList node) {
		for (Statement s : node)
			s.accept(this);
    }

    @Override
    public void visit(AddressOf node) {
//    	if(node instanceof ast.AddressOf)
    		put(node, new AddressType(node.symbol().type()));
//    	else
//    		put(node, node.symbol().type());

    }

    @Override
    public void visit(LiteralBool node) {
    	put(node, new BoolType());

    }

    @Override
    public void visit(LiteralFloat node) {
    	put(node, new FloatType());
    }

    @Override
    public void visit(LiteralInt node) {
    	put(node, new IntType());
    }

    @Override
    public void visit(VariableDeclaration node) {
    	put(node, node.symbol().type());
    }

    @Override
    public void visit(ArrayDeclaration node) {
    	put(node, node.symbol().type());
    }

    @Override
    public void visit(FunctionDefinition node) {
    	currentFunctionNode = node;
    	put(node, node.symbol().type());

    	node.body().accept(this);
    	FuncType funcType = (FuncType) node.symbol().type();

    	// check for main return type
    	if(!(funcType.returnType() instanceof VoidType) && node.symbol().name().equals("main"))
            reportError(node.lineNumber(), node.charPosition(), "Function main has invalid signature.");
    	// check for void arguments
    	for(Type paramType: funcType.arguments())
    		if(paramType instanceof VoidType)
    			reportError(node.lineNumber(), 
    					node.charPosition(), "Function " + node.symbol().name() + " has a void argument in position " + (node.charPosition()-1) + ".");
    	for(Type paramType: funcType.arguments())
            if (paramType instanceof ErrorType) {
                reportError(node.lineNumber(), node.charPosition(), 
                		"Function " + node.symbol().name() + 
                		" has an error in argument in position " + (node.charPosition()-1) + ": " + 
                		((ErrorType)paramType).getMessage());
            }
//    	for(Statement statement: node.body()) {
//    		if(statement instanceof Return)
////    			System.out.println(statement);
//    	}
    	if(!allPathsHaveReturn(node.body()) && !(funcType.returnType() instanceof VoidType))
    		reportError(node.lineNumber(), node.charPosition(), "Not all paths in function " + node.symbol().name() + " have a return.");
    	
    }

    @Override
    public void visit(Comparison node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node, getType((Command) node.leftSide())
    		.compare(getType((Command) node.rightSide())));   
    }
    
    @Override
    public void visit(Addition node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
//    	System.out.println(node.leftSide());
//    	System.out.println(node.rightSide());
    	Type type1 = getType((Command) node.leftSide());
    	Type type2 = getType((Command) node.rightSide());
    	put(node, type1.add(type2));
    }
    
    @Override
    public void visit(Subtraction node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node,getType((Command) node.leftSide())
    		.sub(getType((Command) node.rightSide())));  
    }
    
    @Override
    public void visit(Multiplication node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node,getType((Command) node.leftSide())
    		.mul(getType((Command) node.rightSide())));  
    }
    
    @Override
    public void visit(Division node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node, getType((Command) node.leftSide())
    		.div(getType((Command) node.rightSide())));  
    }
    
    @Override
    public void visit(LogicalAnd node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node, getType((Command) node.leftSide())
    		.and(getType((Command) node.rightSide())));  
    }

    @Override
    public void visit(LogicalOr node) {
    	node.leftSide().accept(this);
    	node.rightSide().accept(this);
    	put(node, getType((Command) node.leftSide())
    		.or(getType((Command) node.rightSide())));  
    }

    @Override
    public void visit(LogicalNot node) {
    	node.expression().accept(this);
    	put(node, getType((Command) node.expression()).not());
    }
    
    @Override
    public void visit(Dereference node) {
    	node.expression().accept(this);
    	Type type = getType((Command) node.expression());
//    	Type type1 = (type instanceof )
//    	System.out.println(type);
//    	if(node.) 
//    	if (!(type instanceof AddressType))
//    		put(node, type);
//    	
//    	else
//    		put(node, ((AddressType)type).base());
    	put(node, type.deref());

    }

    @Override
    public void visit(Index node) {
        node.base().accept(this);
        node.amount().accept(this);
        put(node, getType((Command) node.base()).index(getType((Command) node.amount())));
    }

    @Override
    public void visit(Assignment node) {
    	node.destination().accept(this);
    	node.source().accept(this);
    	put(node, getType((Command) node.destination()).assign(getType((Command) node.source()))); 
    }

    @Override
    public void visit(Call node) {
    	node.arguments().accept(this);

    	TypeList givenArgs = expList2typeList(node.arguments());
    	Type callType = ((FuncType)node.function().type()).call(givenArgs);
    	
        if (callType instanceof ErrorType) 
            reportError(node.lineNumber(), node.charPosition(), ((ErrorType)callType).getMessage());
    	
    }

    // check if condition is bool
    @Override
    public void visit(IfElseBranch node) {
		node.condition().accept(this);
		node.thenBlock().accept(this);
		node.elseBlock().accept(this);
    	if(!(getType((Command) node.condition()) instanceof BoolType))
            put(node, new ErrorType("IfElseBranch requires bool condition not " + getType((Command) node.condition()) + "."));
    	
    }

    @Override
    public void visit(WhileLoop node) {
		node.condition().accept(this);
		node.body().accept(this);
    	if(!(getType((Command) node.condition()) instanceof BoolType))
            put(node, new ErrorType("WhileLoop requires bool condition not " + getType((Command) node.condition()) + "."));
    }

    @Override
    public void visit(Return node) {
		node.argument().accept(this);
		Type argType = getType((Command) node.argument());
		
		FuncType funcType = (FuncType) getType(currentFunctionNode);

		if(!funcType.returnType().equivalent(argType) && !(argType == null && funcType.returnType() instanceof VoidType))
			reportError(node.lineNumber(), node.charPosition(), 
					"Function " + currentFunctionNode.symbol().name() +
					" returns " + funcType.returnType() + " not " + argType + ".");
		
    }

    @Override
    public void visit(ast.Error node) {
        put(node, new ErrorType(node.message()));
    }
}
