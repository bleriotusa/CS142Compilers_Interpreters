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
	public void testScanner() {
	}

	@Test
	public void testNext0() throws FileNotFoundException {
		input = new Scanner(new FileReader("test00.crx"));
		Token token = input.next();
		System.out.println(token);
	}
	@Test
	public void testNext1() throws FileNotFoundException {
		input = new Scanner(new FileReader("test01.crx"));
		Token token = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.ASSIGN));
	}
	@Test
	public void testNext2() throws FileNotFoundException {
		input = new Scanner(new FileReader("test02.crx"));
		Token token = input.next();
		Token token2 = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.EQUAL));
		System.out.println(token2);
		assertTrue(token2.kind.equals(Token.Kind.EOF));
		System.out.println("hello".contains("he"));
		System.out.println(Token.hasMatch("="));

	}
	@Test
	public void testNext3() throws FileNotFoundException {
		input = new Scanner(new FileReader("test03.crx"));
		Token token = input.next();
		Token token2 = input.next();
		System.out.println(token);
		assertTrue(token.kind.equals(Token.Kind.EQUAL));
		System.out.println(token2);
		assertTrue(token2.kind.equals(Token.Kind.ASSIGN));
	}

}
