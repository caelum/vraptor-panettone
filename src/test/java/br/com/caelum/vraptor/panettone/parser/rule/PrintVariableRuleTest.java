package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;

public class PrintVariableRuleTest {

	private PrintVariableRule rule;
	
	@Before
	public void setUp() {
		rule = new PrintVariableRule();
	}
	
	@Test
	public void shouldAcceptBrackets() {
		SourceCode sc = new SourceCode("@v1[bla] @v1.v2[bla]");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@v1[bla]", chunks.get(0).getText());
		Assert.assertEquals("@v1.v2[bla]", chunks.get(1).getText());
	}

	@Test
	public void shouldAcceptMethodInvocationChaining() {
		SourceCode sc = new SourceCode("@name.bla(a, b, 10).b(1,2).c.f() @name.bla(a, b, 10).b(1,2).c.f");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@name.bla(a, b, 10).b(1,2).c.f()", chunks.get(0).getText());
		Assert.assertEquals("@name.bla(a, b, 10).b(1,2).c.f", chunks.get(1).getText());
	}

	@Test @Ignore
	public void shouldIgnoreIfComment() {
		SourceCode sc = new SourceCode("@-- comentario --@");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(0, chunks.size());
	}

	@Test
	public void shouldAcceptQuotesWhenOpeningBracketsOrParenthesis() {
		SourceCode sc = new SourceCode("@v1[\"bla\"] @v1.v2[\"bla\"] '@mensagem'");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@v1[\"bla\"]", chunks.get(0).getText());
		Assert.assertEquals("@v1.v2[\"bla\"]", chunks.get(1).getText());
		Assert.assertEquals("@mensagem", chunks.get(2).getText());
	}

	@Test
	public void shouldAcceptSimpleQuotes() {
		SourceCode sc = new SourceCode("@v1['bla'] @v1.v2['bla']");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@v1['bla']", chunks.get(0).getText());
		Assert.assertEquals("@v1.v2['bla']", chunks.get(1).getText());
	}

	@Test
	public void shouldGetChunks() {
		SourceCode sc = new SourceCode(
				"bla bla"+
				" @v1_v1 "+
				" @v2.texto "+
				" @v3.texto.texto2 " +
				" @a.b(d.e, 10) " +
				"ble ble"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(4, chunks.size());
		
		Assert.assertEquals("@v1_v1", chunks.get(0).getText());
		Assert.assertEquals("@v2.texto", chunks.get(1).getText());
		Assert.assertEquals("@v3.texto.texto2", chunks.get(2).getText());
		Assert.assertEquals("@a.b(d.e, 10)", chunks.get(3).getText());
		
	}
	
	@Test
	public void shouldCreateNode() {
		
		PrintVariableNode node = (PrintVariableNode) rule.getNode(new TextChunk("@v2.texto"));
		
		Assert.assertEquals("v2.texto", node.getExpr());
		
	}
}
