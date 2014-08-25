package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;

public class ReusableVariableRuleTest {

	private ReusableVariableRule rule;
	
	@Before
	public void setUp() {
		rule = new ReusableVariableRule();
	}
	
	@Test
	public void shouldExtractNameAndCode() {
		SourceCode sc = new SourceCode(
				"@{{body\nbla();ble();\n\nbli();\n@}}"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@{{body\nbla();ble();\n\nbli();\n@}}", chunks.get(0).getText());
	}

	@Test
	public void shouldCreateNode() {
		
		ReusableVariableNode node = (ReusableVariableNode) rule.getNode(new TextChunk("@{{body\nbla();@}}"));
		
		Assert.assertEquals("body", node.getName());
		Assert.assertEquals("bla();", node.getContent());
	}
}
