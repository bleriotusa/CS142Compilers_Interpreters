package crux;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SymbolTableTest {



	@Test
	public void test() {
		SymbolTable table1 = new SymbolTable();
		SymbolTable table2 = new SymbolTable(table1);
		SymbolTable table3 = new SymbolTable(table2);
		
		boolean exception1Thrown = false;
		boolean exception2Thrown = false;
		try{

			table1.lookup("hello");
			table3.lookup("hello");
		}
		catch(SymbolNotFoundError e)
		{
			exception1Thrown = true;
			exception2Thrown = true;
		}
		
		assertTrue(exception1Thrown && exception2Thrown);
		
		table1.insert("hello");
		table3.insert("there");
		assertTrue((table1.lookup("hello").name().equals("hello")));
		assertTrue((table3.lookup("hello").name().equals("hello")));
		assertTrue(table3.depth() == 2);
		assertTrue(table1.depth() == 0);
		
		boolean exception3Thrown = false;
		try
		{
			table1.lookup("there");
		}
		catch(SymbolNotFoundError e)
		{
			exception3Thrown = true;
		}
		assertTrue(exception3Thrown);
		
		boolean exception4Thrown = false;
		try
		{
			table1.insert("hello");
		}
		catch(RedeclarationError e)
		{
			exception4Thrown = true;
		}
		assertTrue(exception4Thrown);
		
		table2.insert("there");
		table2.insert("hello");
		assertTrue((table2.lookup("hello").name().equals("hello")));
		assertTrue((table2.lookup("there").name().equals("there")));






	}

}
