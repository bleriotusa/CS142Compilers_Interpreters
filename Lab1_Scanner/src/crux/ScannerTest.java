package crux;

import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
		assertTrue(Token.hasMoreMatches("while"));
		assertTrue(Token.hasMoreMatches("whi"));
		assertTrue(Token.hasMoreMatches("le"));
		assertTrue(Token.hasMoreMatches("99"));
		assertTrue(Token.hasMoreMatches("__"));
		assertTrue(!Token.hasMoreMatches("_!"));
		assertTrue(!Token.hasMoreMatches("!0"));
		assertTrue(!Token.hasMoreMatches("2j"));
		assertTrue(Token.hasMoreMatches("fdsfdf333"));
		assertTrue(!Token.hasMoreMatches("."));
		assertTrue(Token.hasMoreMatches("1.8"));
		assertTrue(!Token.hasMoreMatches("1.8."));
	}
	@Test
	public void testIsValidToken(){
		System.out.println("----------TESTING testIsValidToken ------------------");
		assertTrue(!Token.isValidToken("1.8."));
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
		assertTrue(!Token.isValidIdentifier("."));
		assertTrue(!Token.isValidIdentifier("["));
	}
	
	@Test
	public void testIsFLoat(){
		System.out.println("----------TESTING testIsFLoat------------------");

		assertTrue(Token.isFloat("9.3"));
		assertTrue(Token.isFloat("9."));
		assertTrue(!Token.isFloat("9.."));
		assertTrue(!Token.isFloat(".3"));
		assertTrue(Token.isFloat("9.33333"));
		assertTrue(Token.isFloat("9222.3"));
		assertTrue(Token.isFloat("90.22"));
		assertTrue(!Token.isFloat("90"));
		
	}
	
	@Test
	public void test04() throws IOException{
		System.out.println("----------TESTING test04------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test04.out"));
		Scanner scanner = new Scanner(new FileReader("test04.crx"));
		for(Token token: scanner)
			assertTrue(token.toString().equals(reader.readLine()));
		reader.close();
	}
	@Test
	public void test05() throws IOException{
		System.out.println("----------TESTING test05------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test05.out"));
		Scanner scanner = new Scanner(new FileReader("test05.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test06() throws IOException{
		System.out.println("----------TESTING test06------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test06.out"));
		Scanner scanner = new Scanner(new FileReader("test06.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test07() throws IOException{
		System.out.println("----------TESTING test07------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test07.out"));
		Scanner scanner = new Scanner(new FileReader("test07.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test08() throws IOException{
		System.out.println("----------TESTING test08------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test08.out"));
		Scanner scanner = new Scanner(new FileReader("test08.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test09() throws IOException{
		System.out.println("----------TESTING test09------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test09.out"));
		Scanner scanner = new Scanner(new FileReader("test09.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test10() throws IOException{
		System.out.println("----------TESTING test10------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test10.out"));
		Scanner scanner = new Scanner(new FileReader("test10.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test11() throws IOException{
		System.out.println("----------TESTING test11------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test11.out"));
		Scanner scanner = new Scanner(new FileReader("test11.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test12() throws IOException{
		System.out.println("----------TESTING test12------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test12.out"));
		Scanner scanner = new Scanner(new FileReader("test12.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test13() throws IOException{
		System.out.println("----------TESTING test13------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test13.out"));
		Scanner scanner = new Scanner(new FileReader("test13.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test14() throws IOException{
		System.out.println("----------TESTING test14------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test14.out"));
		Scanner scanner = new Scanner(new FileReader("test14.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test15() throws IOException{
		System.out.println("----------TESTING test15------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test15.out"));
		Scanner scanner = new Scanner(new FileReader("test15.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test16() throws IOException{
		System.out.println("----------TESTING test16------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test16.out"));
		Scanner scanner = new Scanner(new FileReader("test16.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test17() throws IOException{
		System.out.println("----------TESTING test17------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test17.out"));
		Scanner scanner = new Scanner(new FileReader("test17.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test18() throws IOException{
		System.out.println("----------TESTING test18------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test18.out"));
		Scanner scanner = new Scanner(new FileReader("test18.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test19() throws IOException{
		System.out.println("----------TESTING test19------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test19.out"));
		Scanner scanner = new Scanner(new FileReader("test19.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test20() throws IOException{
		System.out.println("----------TESTING test20------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test20.out"));
		Scanner scanner = new Scanner(new FileReader("test20.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test21() throws IOException{
		System.out.println("----------TESTING test21------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test21.out"));
		Scanner scanner = new Scanner(new FileReader("test21.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test22() throws IOException{
		System.out.println("----------TESTING test22------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test22.out"));
		Scanner scanner = new Scanner(new FileReader("test22.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test23() throws IOException{
		System.out.println("----------TESTING test23------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test23.out"));
		Scanner scanner = new Scanner(new FileReader("test23.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test24() throws IOException{
		System.out.println("----------TESTING test24------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test24.out"));
		Scanner scanner = new Scanner(new FileReader("test24.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test25() throws IOException{
		System.out.println("----------TESTING test25------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test25.out"));
		Scanner scanner = new Scanner(new FileReader("test25.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test26() throws IOException{
		System.out.println("----------TESTING test26------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test26.out"));
		Scanner scanner = new Scanner(new FileReader("test26.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test27() throws IOException{
		System.out.println("----------TESTING test27------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test27.out"));
		Scanner scanner = new Scanner(new FileReader("test27.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test28() throws IOException{
		System.out.println("----------TESTING test28------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test28.out"));
		Scanner scanner = new Scanner(new FileReader("test28.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test29() throws IOException{
		System.out.println("----------TESTING test29------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test29.out"));
		Scanner scanner = new Scanner(new FileReader("test29.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	@Test
	public void test30() throws IOException{
		System.out.println("----------TESTING test30------------------");
		BufferedReader reader = new BufferedReader(new FileReader("test30.out"));
		Scanner scanner = new Scanner(new FileReader("test30.crx"));
		for(Token token: scanner)
		{
			System.out.println(token);
			assertTrue(token.toString().equals(reader.readLine()));
		}
		reader.close();
	}
	

}
