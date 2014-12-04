package br.com.caelum.vraptor.panettone;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

public class VRaptorCompilationListenerTest {
	
	@Test
	public void shouldSupportXMLSyntax() {
		String expected = "<%use(header.class).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header />");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithParam() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"MyTitle\" /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithMultipleParam() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").description(\"Desc\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"MyTitle\" description=\"Desc\"/></html>");
		assertEquals(expected, result);
	}


	@Test
	public void shouldSupportXMLSyntaxWithEmptyBody() {
		String expected = "<%use(header.class).body(()->{%>\n\n<%}).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header></tone:header>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithTextBody() {
		String expected = "<%use(header.class).body(()->{%>\nBody\n<%}).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header>Body</tone:header>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithParamAndBody() {
		String expected = "<%use(header.class).title(\"MyTitle\").body(()->{%>\nBody\n<%}).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header title=\"MyTitle\">Body</tone:header>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithMultipleTags() {
		String expected = "<%use(header.class).body(()->{%>\nBody\n<%}).done();%><%use(footer.class).body(()->{%>\nmore\n<%}).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header>Body</tone:header><tone:footer>more</tone:footer>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithNestedTags() {
		String expected = "<%use(header.class).body(()->{%>\nBody<%use(content.class).body(()->{%>\nmore\n<%}).done();%>\n<%}).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header>Body<tone:content>more</tone:content></tone:header>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldIncludeBuilderForVariable() {
		String expected = 
				  "private String message;\n"
				+ "public header message(String message) { this.message = message; return this; }\n"
				+ "public void done() { render(message); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("String message"), "header");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldIncludeBuilderForMultipleVariables() {
		String expected = 
				  "private String message;\n"
				+ "public header message(String message) { this.message = message; return this; }\n"
				+ "private String title;\n"
				+ "public header title(String title) { this.title = title; return this; }\n"
				+ "public void done() { render(message,title); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("String message", "String title"), "header");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldIncludeBuilderForMultipleVariablesAndBody() {
		String expected = 
				  "private String message;\n"
				+ "public header message(String message) { this.message = message; return this; }\n"
				+ "private String title;\n"
				+ "public header title(String title) { this.title = title; return this; }\n"
				+ "private Runnable body;\n"
				+ "public header body(Runnable body) { this.body = body; return this; }\n"
				+ "public void done() { render(message,title,body); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("String message", "String title", "Runnable body"), "header");
		assertEquals(expected, result);
	}
}
