package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
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
	public void shouldAcceptScriptletPrintInMethodInvocation() {
		SourceCode sc = new SourceCode(
				"bla <%= a.b() %> bla"
				);
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("<%= a.b() %>", chunks.get(0).getText());
	}

	@Test
	public void shouldCreateNode() {
		
		ScriptletPrintNode node = (ScriptletPrintNode) rule.getNode(new TextChunk("<%= v1.v2() %>"));
		
		Assert.assertEquals("v1.v2()", node.getExpr());
		
	}
}
