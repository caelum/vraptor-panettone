package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.TextChunkBuilder;
import br.com.caelum.vraptor.panettone.parser.ast.ExpressionNode;

public class ExpressionRuleTest {

	private ExpressionRule rule;
	
	@Before
	public void setUp() {
		rule = new ExpressionRule();
	}
	
	@Test
	public void shouldAcceptExpressions() {
		SourceCode sc = new SourceCode(
				"@{v1}\n"+
				"@{v1.v2}\n"+
				"@{v1.v2.v3}\n"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{v1}", chunks.get(0).getText());
		Assert.assertEquals("@{v1.v2}", chunks.get(1).getText());
		Assert.assertEquals("@{v1.v2.v3}", chunks.get(2).getText());
	}

	@Test
	public void shouldSupportSpaces() {
		SourceCode sc = new SourceCode(
				"@{ v1}\n"+
						"@{v2 }\n"+
						"@{ v3 }\n"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{ v1}", chunks.get(0).getText());
		Assert.assertEquals("@{v2 }", chunks.get(1).getText());
		Assert.assertEquals("@{ v3 }", chunks.get(2).getText());
	}
	
	@Test
	public void shouldCreateNode() {
		
		ExpressionNode node = (ExpressionNode) rule.getNode(TextChunkBuilder.to(" @{v1.v2} "));
		
		Assert.assertEquals("v1.v2", node.getExpr());
		
	}
}
