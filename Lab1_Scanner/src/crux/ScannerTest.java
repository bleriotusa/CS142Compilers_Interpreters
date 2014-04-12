package crux;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.junit.Before;
import org.junit.Test;

public class ScannerTest {

	Scanner input;
	@Before
	public void setUp() throws Exception {
	}


	@Test
	public void testNext0() throws FileNotFoundException {
		System.out.println("----------TESTING testNext0 ------------------");

		input = new Scanner(new FileReader("test00.crx"));
		Token token = input.next();
		System.out.println(token);
	}
	@Test
	public void testNext1() throws FileNotFoundException {
		System.out.println("----------TESTING testNext1 ------------------");

		input = new Scanner(new FileReader("test01.crx"));
		Token token = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.ASSIGN));
	}
	@Test
	public void testNext2() throws FileNotFoundException {
		System.out.println("----------TESTING testNext2 ------------------");
		input = new Scanner(new FileReader("test02.crx"));
		Token token = input.next();
		Token token2 = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.EQUAL));
		System.out.println(token2);
		assertTrue(token2.kind.equals(Token.Kind.EOF));


	}
	@Test
	public void testNext3() throws FileNotFoundException {
		System.out.println("----------TESTING testNext3 ------------------");
		input = new Scanner(new FileReader("test03.crx"));
		Token token = input.next();
		Token token2 = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.EQUAL));
		System.out.println(token2);
		assertTrue(token2.kind.equals(Token.Kind.ASSIGN));
	}
	@Test
	public void testHasMatch(){
		System.out.println("----------TESTING testHasMatch ------------------");

		assertTrue("hello".contains("he"));
		assertTrue(Token.hasMoreMatches("="));
		assertTrue(!Token.hasMoreMatches("=="));
		assertTrue(!Token.hasMoreMatches("!="));
		assertTrue(Token.hasMoreMatches("whi"));
		assertTrue(Token.hasMoreMatches("="));
		assertTrue(!Token.hasMoreMatches("while"));
		assertTrue(Token.hasMoreMatches("whi"));
		assertTrue(Token.hasMoreMatches("le"));
	}
	
	@Test
	public void testIsValidIdentifier(){		
		System.out.println("----------TESTING testIsValidIdentifier------------------");

		assertTrue(Token.isValidIdentifier("_h9ello"));
		assertTrue(!Token.isValidIdentifier("0_h9ello"));
		assertTrue(Token.isValidIdentifier("h9ello"));
		assertTrue(!Token.isValidIdentifier("9ello"));
		assertTrue(Token.isValidIdentifier("h9ello_"));
		assertTrue(Token.isValidIdentifier("and"));
		assertTrue(Token.isValidIdentifier("THERE"));
		assertTrue(Token.isValidIdentifier("_Ek"));
		assertTrue(Token.isValidIdentifier("_EE"));
		assertTrue(Token.isValidIdentifier("_ff"));
	}

}
