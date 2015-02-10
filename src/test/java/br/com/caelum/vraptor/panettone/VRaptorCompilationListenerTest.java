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
	public void shouldSupportXMLSyntaxWithSimpleRendered() {
		String expected = "<%if(true)%><%use(header.class).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header tone:rendered=\"@true\" />");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithSingleQuotedRendered() {
		String expected = "<%if(\"string\".isEmpty())%><%use(header.class).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header tone:rendered='@\"string\".isEmpty()' />");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldNotSupportXMLSyntaxWithSimpleRenderedWithoutAtSign() {
		String result = new VRaptorCompilationListener().preprocess("<tone:header tone:rendered=\"true\" />");
		assertFalse(result.contains("<%if"));
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithComplexRendered() {
		String expected = "<%if(list.isEmpty())%><%use(header.class).done();%>";
		String result = new VRaptorCompilationListener().preprocess("<tone:header tone:rendered=\"@list.isEmpty()\" />");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithParam() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"MyTitle\" /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithSimpleQuotedParam() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title='MyTitle' /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithCodeParamAndRendered() {
		String expected = "<html><%if(list.isEmpty())%><%use(header.class).title(title).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"@title\" tone:rendered=\"@list.isEmpty()\" /></html>");
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportXMLSyntaxWithCodeParam() {
		String expected = "<html><%use(header.class).title(title).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"@title\" /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithComplexCodeParam() {
		String expected = "<html><%use(header.class).title(obj.getTitle()).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"@obj.getTitle()\" /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithComplexCodeAndSingleQuotedParam() {
		String expected = "<html><%use(header.class).title(obj.getTitle()).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title='@obj.getTitle()' /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithComplexCodeAndStringInASingleQuotedParam() {
		String expected = "<html><%use(header.class).title(\"Title: \" + obj.getTitle()).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title='@\"Title: \" + obj.getTitle()' /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldNotSupportXMLSyntaxWithELSyntaxParam() {
		String expected = "<html><%use(header.class).title(obj.title).done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"@obj.title\" /></html>");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportXMLSyntaxWithMultipleParam() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").description(\"Desc\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title=\"MyTitle\" description=\"Desc\"/></html>");
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportXMLSyntaxWithMultipleSingleQuotedParams() {
		String expected = "<html><%use(header.class).title(\"MyTitle\").description(\"Desc\").done();%></html>";
		String result = new VRaptorCompilationListener().preprocess("<html><tone:header title='MyTitle' description='Desc'/></html>");
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
	public void shouldIncludeSpecialBuilderForBooleanVariable() {
		String expected = 
				  "private Boolean flag;\n"
				+ "public header flag(Boolean flag) { this.flag = flag; return this; }\n"
				+ "public header flag(String flag) { this.flag = Boolean.valueOf(flag); return this; }\n"
				+ "public void done() { render(flag); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("Boolean flag"), "header");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldIncludeSpecialBuilderForIntegerVariable() {
		String expected = 
				  "private Integer flag;\n"
				+ "public header flag(Integer flag) { this.flag = flag; return this; }\n"
				+ "public header flag(String flag) { this.flag = Integer.valueOf(flag); return this; }\n"
				+ "public void done() { render(flag); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("Integer flag"), "header");
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldIncludeSpecialBuilderForDoubleVariable() {
		String expected = 
				  "private Double flag;\n"
				+ "public header flag(Double flag) { this.flag = flag; return this; }\n"
				+ "public header flag(String flag) { this.flag = Double.valueOf(flag); return this; }\n"
				+ "public void done() { render(flag); }\n";
		String result = new VRaptorCompilationListener().useParameters(Arrays.asList("Double flag"), "header");
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
