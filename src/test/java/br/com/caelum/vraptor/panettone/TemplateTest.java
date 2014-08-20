package br.com.caelum.vraptor.panettone;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.vraptor.panettone.Template;

public class TemplateTest {
	
	@Test
	public void shouldReturnString() {
		String expected = emptyRun("out.write(\"<html>Oi</html>\");\n");
		String result = new Template("<html>Oi</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageVariable() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(mensagem);\nout.write(\"</html>\");\n");
		String result = new Template("<html>${mensagem}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageGetterInvocation() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(message.getBytes());\nout.write(\"</html>\");\n");
		String result = new Template("<html>${message.bytes}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageDoubleGetterInvocation() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(message.getBytes().getLength());\nout.write(\"</html>\");\n");
		String result = new Template("<html>${message.bytes.length}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageMapAccess() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(message.get(15));\nout.write(\"</html>\");\n");
		String result = new Template("<html>${message[15]}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageComplexInvocation() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(message.getSize().get(bytes));\nout.write(\"</html>\");\n");
		String result = new Template("<html>${message.size[bytes]}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportLineBreak() {
		String expected = emptyRun("out.write(\"<html>\\n\");\nout.write(\"Oi</html>\");\n");
		String result = new Template("<html>\nOi</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportQuotes() {
		String expected = emptyRun("out.write(\"<html>\\\"Oi\\\"</html>\");\n");
		String result = new Template("<html>\"Oi\"</html>").renderType();
		assertEquals(expected, result);
	}

	private String emptyRun(String msg) {
		return "public void render() {\n" + msg+"}\n";
	}

	@Test
	public void shouldInterpolateObject() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(mensagem);\nout.write(\"</html>\");\n");
		String result = new Template("<html><%=mensagem%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldAddVariables() {
		String expected = "public void render( String mensagem ) {\nout.write(\"<html>\");\nout.write(mensagem);\nout.write(\"</html>\");\n}\n";
		String result = new Template("<%@ String mensagem %><html><%=mensagem%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldAddDefaultVariables() {
		String expected = "public void render( String mensagem ) {\nif(mensagem == null) mensagem =  \"hello\" ;\nout.write(\"<html>\");\nout.write(mensagem);\nout.write(\"</html>\");\n}\n";
		String result = new Template("<%@ String mensagem = \"hello\" %><html><%=mensagem%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportMethodInvocation() {
		String expected = emptyRun("out.write(\"<html>\");\nout.write(user.getName());\nout.write(\"</html>\");\n");
		String result = new Template("<html><%=user.getName()%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportLoop() {
		String expected = emptyRun("out.write(\"<html>\");\n"
				+ "for(String user : users) {\n"
				+ "out.write(user.getName());\n"
				+ "}\n"
				+ "out.write(\"</html>\");\n");
		String result = new Template("<html><%for(String user : users) {%><%=user.getName()%><%}%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportComments() {
		String expected = emptyRun("out.write(\"<html>\");\n"
				+ "out.write(\"</html>\");\n");
		String result = new Template("<html><%-- comments here %></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportMethods() {
		String getName = " String getName() { return \"Guilherme\"; } \n";
		String render = "public void render( String mensagem ) {\nout.write(\"<html>\");\nout.write(mensagem);\nout.write(\"</html>\");\n}\n";
		String expected = getName + render;
		String result = new Template("<%@ String mensagem %><html><%=mensagem%><%$ String getName() { return \"Guilherme\"; } %></html>").renderType();
		assertEquals(expected, result);
	}

}
