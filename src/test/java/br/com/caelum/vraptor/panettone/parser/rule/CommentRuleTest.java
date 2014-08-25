package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.CommentNode;

public class CommentRuleTest {

	private CommentRule rule;
	
	@Before
	public void setUp() {
		rule = new CommentRule();
	}
	
	@Test
	public void shouldAcceptComments() {
		SourceCode sc = new SourceCode("@-- comentario aqui --@");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@-- comentario aqui --@", chunks.get(0).getText());
	}

	@Test
	public void shouldCreateNode() {
		
		CommentNode node = (CommentNode) rule.getNode(new TextChunk("@-- comentario aqui --@"));
		
		Assert.assertEquals(" comentario aqui ", node.getComment());
		
	}
}
