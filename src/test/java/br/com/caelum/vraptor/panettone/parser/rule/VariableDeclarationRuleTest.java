package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
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
				"bla bla"+
				" (@ pac1.pac2.Class end) "+
				"ble ble"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ pac1.pac2.Class end)", chunks.get(0).getText());
	}

	@Test
	public void shouldUnderstandVariablesWithGenerics() {
		SourceCode sc = new SourceCode(
						" (@ java.util.List<br.com.Entity> var1) "+
						" (@ java.util.List < br.com.Entity > var2)"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("(@ java.util.List<br.com.Entity> var1)", chunks.get(0).getText());
		Assert.assertEquals("(@ java.util.List < br.com.Entity > var2)", chunks.get(1).getText());
	}
	
	
	@Test
	public void shouldUnderstandVariablesWithSimpleTypes() {
		SourceCode sc = new SourceCode(
				"bla bla"+
				" (@ String nome) "+
				" (@ String end) "+
				"ble ble"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("(@ String nome)", chunks.get(0).getText());
		Assert.assertEquals("(@ String end)", chunks.get(1).getText());
	}
	
	@Test
	public void shouldIgnoreWhitespaces() {
		SourceCode sc = new SourceCode(
				"bla bla"+
						" (@String   nome) "+
						" (@   String   end   ) "+
						"ble ble"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals(2, chunks.size());
		Assert.assertEquals("(@String   nome)", chunks.get(0).getText());
		Assert.assertEquals("(@   String   end   )", chunks.get(1).getText());
	}
	
	@Test
	public void shouldCreateNode() {
		
		VariableDeclarationNode node = (VariableDeclarationNode) rule.getNode(new TextChunk("(@ String nome)"));
		
		Assert.assertEquals("String", node.getType());
		Assert.assertEquals("nome", node.getName());
		
	}
}
