package crux;

import static org.junit.Assert.*;

import org.junit.Test;

import crux.Token.Kind;

public class EnumTest {
	@Test
	public void test() {
		
		Kind token = Kind.ADD;
		System.out.println("Token2 Lexeme:" + token.getValue() + ".");
		assertTrue(token.name().equals("ADD"));
		assertTrue(token.hasStaticLexeme());
		
		
		Kind token2 = Kind.IDENTIFIER;
		System.out.println("Token2 Lexeme:" + token2.getValue() + ".");
		assertTrue(!token2.hasStaticLexeme());
		token2.setValue("x");
		System.out.println(token2.getValue());
		assertTrue(token2.hasStaticLexeme());
		
		for(Kind kind: Kind.values())
			System.out.println(kind.getValue());
		
		String test = "";
		test += -1;
		System.out.println("EOF STRING = " + test);
		
		Token a = new Token("and", 3, 4);
		System.out.println(a.kind().toString());
	}

}
