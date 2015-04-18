package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.TextChunkBuilder;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRuleTest {

	private ScriptletPrintRule rule;
	
	@Before
	public void setUp() {
		rule = new ScriptletPrintRule();
	}
	
	@Test
	public void shouldIgnoreSpaces() {
		SourceCode sc = new SourceCode(
				"<%=var%> <%= var%> <%=var %> <%= var %>"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("<%=var%>", chunks.get(0).getText());
		Assert.assertEquals("<%= var%>", chunks.get(1).getText());
		Assert.assertEquals("<%=var %>", chunks.get(2).getText());
		Assert.assertEquals("<%= var %>", chunks.get(3).getText());
	}
	
	@Test
	public void shouldAcceptScriptletPrint() {
		SourceCode sc = new SourceCode(
				"bla <%= a_b %> bla"
		);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("<%= a_b %>", chunks.get(0).getText());
	}

	@Test
	public void shouldAcceptTernaryIf() {
		SourceCode sc = new SourceCode(
				"<%= x.y() ? \"abc\" : \"\" %>"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("<%= x.y() ? \"abc\" : \"\" %>", chunks.get(0).getText());
	}
	
	@Test
	public void shouldAcceptScriptletPrintInMethodInvocation() {
		SourceCode sc = new SourceCode(
				"<%= a.b() %>" +
				"<%= a.b(d.e, 10) %>"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("<%= a.b() %>", chunks.get(0).getText());
		Assert.assertEquals("<%= a.b(d.e, 10) %>", chunks.get(1).getText());
	}

	@Test
	public void shouldCreateNode() {
		
		ScriptletPrintNode node = (ScriptletPrintNode) rule.getNode(TextChunkBuilder.to("<%= v1.v2() %>"));
		
		Assert.assertEquals("v1.v2()", node.getExpr());
		
	}
	
	@Test
	public void shouldCreateNodeEscaped() {
		
		ScriptletPrintNode node = (ScriptletPrintNode) rule.getNode(TextChunkBuilder.to("<%= v1.v2() %>"));
		
		Assert.assertEquals("v1.v2()", node.getExpr());
		Assert.assertFalse(node.isRawText());
		
	}
	
	@Test
	public void shouldCreateNodeWithoutEscape() {
		
		ScriptletPrintNode node = (ScriptletPrintNode) rule.getNode(TextChunkBuilder.to("<%== v1.v2() %>"));
		
		Assert.assertEquals("v1.v2()", node.getExpr());
		Assert.assertTrue(node.isRawText());
		
	}
}
