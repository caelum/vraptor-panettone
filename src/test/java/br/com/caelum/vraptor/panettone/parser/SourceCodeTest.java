package br.com.caelum.vraptor.panettone.parser;

import org.junit.Assert;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.rule.Rules;

public class SourceCodeTest {

	
	@Test
	public void shouldTransformScriptletIntoRuleChunks() {
		SourceCode sc = new SourceCode(
				  "(### CODIGO1 10 ###)\n"
				+ "(### CODIGO2 20 ###)\n"
				+ "<%\n"
				+ "fazAlgo();\n"
				+ "bla.b();\n"
				+ "%>\n"
				+ "(### CODIGO3 50 ###)\n"
			);
		
		sc.transformHtmlAndScriptlet();
		
		String expected = 
				  "(### CODIGO1 10 ###)"
				+ "(### CODIGO2 20 ###)"
				+ "(### SCRIPTLET 1 ###)"
				+ "(### CODIGO3 50 ###)";
		
		Assert.assertEquals(expected, sc.getSource());

	}

	@Test
	public void shouldTransformScriptletAndHTMLMixedIntoRuleChunks() {
		SourceCode sc = new SourceCode(
						  "(### CODIGO1 10 ###)\n"
						+ "(### CODIGO2 20 ###)\n"
						+ " bla <%\n"
						+ "fazAlgo();\n"
						+ "bla.b();\n"
						+ "%> bla <% x() %> ble\n"
						+ "(### CODIGO3 50 ###)\n"
				);
		
		sc.transformHtmlAndScriptlet();
		
		String expected = 
				"(### CODIGO1 10 ###)"
						+ "(### CODIGO2 20 ###)"
						+ "(### HTML 1 ###)"
						+ "(### SCRIPTLET 2 ###)"
						+ "(### HTML 3 ###)"
						+ "(### SCRIPTLET 4 ###)"
						+ "(### HTML 5 ###)"
						+ "(### CODIGO3 50 ###)";
		
		Assert.assertEquals(expected, sc.getSource());
		
		Assert.assertEquals("\n bla ", sc.getTextChunk(1).getText());
		Assert.assertEquals("\nfazAlgo();\nbla.b();\n", sc.getTextChunk(2).getText());
		Assert.assertEquals(" bla ", sc.getTextChunk(3).getText());
		Assert.assertEquals(" x() ", sc.getTextChunk(4).getText());
		Assert.assertEquals(" ble", sc.getTextChunk(5).getText());
		
	}
	
	@Test
	public void shouldTransformHTMLIntoRuleChunks() {
		
		SourceCode sc = new SourceCode(
				  "<html><body>\n"
				+ "(### CODIGO1 10 ###)\n"
				+ "(### CODIGO2 20 ###)\n"
				+ "<p>sou peludao</p>\n"
				+ "<h2>bla bla</h2>\n"
				+ "(### CODIGO3 50 ###)\n"
				+ "</html>");
		
		sc.transformHtmlAndScriptlet();
		
		String expected = 
				  "(### HTML 1 ###)"
				+ "(### CODIGO1 10 ###)"
				+ "(### CODIGO2 20 ###)"
				+ "(### HTML 2 ###)"
				+ "(### CODIGO3 50 ###)"
				+ "(### HTML 3 ###)";
		
		Assert.assertEquals(expected, sc.getSource());
		
		
	}
	
	@Test
	public void shouldIgnoreUselessEnters() {
		
		SourceCode sc = new SourceCode(
				"<html><body>\n\n\n"
						+ "(### CODIGO1 10 ###)\n"
						+ "\n\n(### CODIGO2 20 ###)\n"
						+ "\n\n<p>sou peludao</p>\n"
						+ "\n\n<h2>bla bla</h2>\n"
						+ "(### CODIGO3 30 ###)\n"
						+ "</html>\n\n");
		
		sc.transformHtmlAndScriptlet();
		
		String expected = 
				  "(### HTML 1 ###)"
				+ "(### CODIGO1 10 ###)"
				+ "(### CODIGO2 20 ###)"
				+ "(### HTML 2 ###)"
				+ "(### CODIGO3 30 ###)"
				+ "(### HTML 3 ###)";
		
		Assert.assertEquals(expected, sc.getSource());
		
		
	}
	
	@Test
	public void shouldSaveChunks() {
		SourceCode sc = new SourceCode(
				  "<html>"
				 + "** chunk 1 **"
				 + "** chunk 2 **"
				+ "</html>"
			);
		
		sc.transform(new TextChunk("** chunk 1 **"), Rules.PRINT_VARIABLE);
		sc.transform(new TextChunk("** chunk 2 **"), Rules.VARIABLE_DECLARATION);
		
		Assert.assertEquals("** chunk 1 **", sc.getTextChunk(1).getText());
		Assert.assertEquals("** chunk 2 **", sc.getTextChunk(2).getText());
	}
}
