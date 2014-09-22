package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.TextChunkBuilder;
import br.com.caelum.vraptor.panettone.parser.ast.VariableDeclarationNode;

public class VariableDeclarationRuleTest {

	private VariableDeclarationRule rule;
	@Before
	public void setUp() {
		rule = new VariableDeclarationRule();
	}
	
	@Test
	public void shouldUnderstandVariablesWithFullTypeName() {
		SourceCode sc = new SourceCode(
				"bla bla\n"+
				" (@ pac1.pac2.Class end)\n"+
				"ble ble\n"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ pac1.pac2.Class end)", chunks.get(0).getText());
	}

	@Test
	public void shouldUnderstandVariablesWithGenerics() {
		SourceCode sc = new SourceCode(
						" (@ java.util.List<br.com.Entity> var1)\n"+
						" (@ java.util.List < br.com.Entity > var2)\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ java.util.List<br.com.Entity> var1)", chunks.get(0).getText());
		Assert.assertEquals("(@ java.util.List < br.com.Entity > var2)", chunks.get(1).getText());
	}

	@Test
	public void shouldUnderstandVariablesWithNestedGenerics() {
		SourceCode sc = new SourceCode(
				"(@ java.util.List<br.com.Entity<OtherType, X>> var1)\n"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ java.util.List<br.com.Entity<OtherType, X>> var1)", chunks.get(0).getText());
	}
	
	@Test
	public void shouldUnderstandVariablesWithSimpleNestedGenerics() {
		SourceCode sc = new SourceCode(
				"(@ List<CountingSummary<Course>> mostFinalizedThisMonth )\n"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ List<CountingSummary<Course>> mostFinalizedThisMonth )", chunks.get(0).getText());
	}
	
	
	@Test
	public void shouldUnderstandVariablesWithSimpleTypes() {
		SourceCode sc = new SourceCode(
				"bla bla\n"+
				" (@ String nome) \n"+
				" (@ String end) \n"+
				"ble ble\n"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("(@ String nome)", chunks.get(0).getText());
		Assert.assertEquals("(@ String end)", chunks.get(1).getText());
	}

	@Test
	public void shouldUnderstandDefaultValues() {
		SourceCode sc = new SourceCode(
						" (@ String nome = \"Mau\")\n"+
						" (@ int number = \"10\")\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("(@ String nome = \"Mau\")", chunks.get(0).getText());
		Assert.assertEquals("(@ int number = \"10\")", chunks.get(1).getText());
	}
	
	@Test
	public void shouldIgnoreWhitespaces() {
		SourceCode sc = new SourceCode(
				"bla bla\n"+
						" (@String   nome) \n"+
						" (@   String   end   ) \n"+
						"ble ble\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("(@String   nome)", chunks.get(0).getText());
		Assert.assertEquals("(@   String   end   )", chunks.get(1).getText());
	}
	
	@Test
	public void shouldCreateNode() {
		
		VariableDeclarationNode node = (VariableDeclarationNode) rule.getNode(TextChunkBuilder.to("(@String nome)"));
		VariableDeclarationNode node2 = (VariableDeclarationNode) rule.getNode(TextChunkBuilder.to("(@ int x = 10)"));
		VariableDeclarationNode node3 = (VariableDeclarationNode) rule.getNode(TextChunkBuilder.to("(@ String email = \"bla@bla.com\")"));
		
		Assert.assertEquals("String", node.getType());
		Assert.assertEquals("nome", node.getName());
		Assert.assertNull(node.getDefaultValue());

		Assert.assertEquals("int", node2.getType());
		Assert.assertEquals("x", node2.getName());
		Assert.assertEquals("10", node2.getDefaultValue());

		Assert.assertEquals("String", node3.getType());
		Assert.assertEquals("email", node3.getName());
		Assert.assertEquals("\"bla@bla.com\"", node3.getDefaultValue());
		
	}
}
