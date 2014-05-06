package crux;

import java.util.LinkedHashMap;



public class SymbolTable {
	private LinkedHashMap<String, Symbol> table;
	private SymbolTable parent;
    
    public SymbolTable()
    {
    	table = new LinkedHashMap<String, Symbol>();
    	parent = null;
    }
    
    public SymbolTable(SymbolTable parent)
    {
    	table = new LinkedHashMap<String, Symbol>();
    	this.parent = parent;
    }
    
    public Symbol lookup(String name) throws SymbolNotFoundError
    {
    	Symbol symbol = table.get(name);
    	
        if(symbol == null)
        {
        	if(parent == null)
        		throw new SymbolNotFoundError(new Symbol(name));
        	else
        		symbol = parent.lookup(name);
        }
        
        return symbol;   
    }
       
    public Symbol insert(String name) throws RedeclarationError
    {
    	Symbol symbol = new Symbol(name);
    	
    	if(table.get(name) != null)
    		throw new RedeclarationError(symbol);
    	table.put(name, symbol);
    	return symbol;
    }
    
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        if (parent != null)
            sb.append(parent.toString());
        
        String indent = new String();
        for (int i = 0; i < depth(); i++) {
            indent += "  ";
        }
        
        for (Symbol s : table.values())
        {
            sb.append(indent + s.toString() + "\n");
        }
        return sb.toString();
    }
    
    public int depth()
    {
    	return (parent == null)? 0 : 1 + parent.depth();
    }
    
    public SymbolTable getParent()
    {
    	return parent;
    }
}

class SymbolNotFoundError extends Error
{
    private static final long serialVersionUID = 1L;
    private String name;
    
    SymbolNotFoundError(Symbol symbol)
    {
        this.name = symbol.name();
    }
    
    public String name()
    {
        return name;
    }
}

class RedeclarationError extends Error
{
    private static final long serialVersionUID = 1L;

    public RedeclarationError(Symbol sym)
    {
        super("Symbol " + sym + " being redeclared.");
    }
}
