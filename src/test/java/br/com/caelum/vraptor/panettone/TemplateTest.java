package br.com.caelum.vraptor.panettone;

import static org.junit.Assert.*;

import org.junit.Test;

import br.com.caelum.vraptor.panettone.Template;

public class TemplateTest {
	
	@Test
	public void shouldReturnString() {
		String expected = emptyRun("write(\"<html>Oi</html>\");\n");
		String result = new Template("<html>Oi</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageVariable() {
		String expected = emptyRun("write(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n");
		String result = new Template("<html>@mensagem</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageWithinAnotherCode() {
		String expected = emptyRun("write(\"<script>var x = '\");\nwrite(mensagem);\nwrite(\"'; var y = '\");\nwrite(mensagem);\nwrite(\"';</script>\");\n");
		String result = new Template("<script>var x = '@mensagem'; var y = '@mensagem';</script>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageGetterInvocation() {
		String expected = emptyRun("write(\"<html>\");\nwrite(message.getBytes());\nwrite(\"</html>\");\n");
		String result = new Template("<html>@message.bytes</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageGetWithString() {
		String expected = emptyRun("write(\"<html>\");\nwrite(message.get(\"bytes\"));\nwrite(\"</html>\");\n");
		String result = new Template("<html>@message['bytes']</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageMethodInvocation() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(message.bytes(a.getB()).c(d).getE().f());\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@message.bytes(a.b).c(d).e.f()</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageDoubleGetterInvocation() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(message.getBytes().getLength());\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@message.bytes.length</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageMapAccess() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(message.get(15));\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@message[15]</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageString() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(a.getB());\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@{a.b}</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageMapAccessWithString() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(message.get(\"a.b\"));\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@message['a.b']</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageComplexInvocation() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(message.getSize().get(bytes));\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@message.size[bytes]</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportExpressionLanguageComplexInvocation2() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "write(l.get(nip.getDate()).custom(\"date_hour\"));\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@l[nip.date].custom('date_hour')</html>").renderType();
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportLineBreak() {
		String expected = emptyRun("write(\"<html>\\n\");\nwrite(\"Oi</html>\");\n");
		String result = new Template("<html>\nOi</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportQuotes() {
		String expected = emptyRun("write(\"<html>\\\"Oi\\\"</html>\");\n");
		String result = new Template("<html>\"Oi\"</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportBackslashes() {
		String expected = emptyRun("write(\"<html>\\\\s</html>\");\n");
		String result = new Template("<html>\\s</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportBackslashesInQuotes() {
		String expected = emptyRun("write(\"var words = p.split(\\\"\\\\s+\\\");\");\n");
		String result = new Template("var words = p.split(\"\\s+\");").renderType();
		assertEquals(expected, result);
	}

	private String emptyRun(String msg) {
		return "public void render() {\n" + msg+"}\n";
	}

	private String emptyRun(String prefix, String msg) {
		return prefix + "public void render() {\n" + msg+"}\n";
	}

	@Test
	public void shouldInterpolateObject() {
		String expected = emptyRun("write(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n");
		String result = new Template("<html><%=mensagem%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldAddVariables() {
		String expected = "public void render(String mensagem) {\nwrite(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n}\n";
		String result = new Template("(@ String mensagem )\n<html><%= mensagem %></html>").renderType();
		assertEquals(expected, result);
	}
	@Test
	public void shouldAddVariablesEvenWithLaterAts() {
		String expected = "public void render(String mensagem) {\nwrite(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n}\n";
		String result = new Template("(@ String mensagem )\n<html>@mensagem</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldAddVariablesWithoutSpace() {
		String expected = "public void render(String mensagem) {\nwrite(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n}\n";
		String result = new Template("(@String mensagem)\n<html><%= mensagem %></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldAddDefaultVariables() {
		String expected = "public void render(String mensagem) {\nif(mensagem == null) mensagem = \"hello\" ;\nwrite(\"<html>\");\nwrite(mensagem);\nwrite(\"</html>\");\n}\n";
		String result = new Template("(@ String mensagem = \"hello\" )\n<html><%=mensagem%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportMethodInvocation() {
		String expected = emptyRun("write(\"<html>\");\nwrite(user.getName());\nwrite(\"</html>\");\n");
		String result = new Template("<html><%= user.getName() %></html>").renderType();
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportMethodInvocationWithPackage() {
		String expected = emptyRun("@javax.inject.Inject private br.com.caelum.vraptor.panettone.User user;\n",
				"write(\"<html>\");\nwrite(user.getName());\nwrite(\"</html>\");\n");
		String result = new Template("(@inject br.com.caelum.vraptor.panettone.User user)\n"
				+ "<html><%=user.getName()%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportMethodInvocationWithMember() {
		String expected = emptyRun("@javax.inject.Inject private User user;\n", 
				"write(\"<html>\");\nwrite(user.getName());\nwrite(\"</html>\");\n");
		String result = new Template("(@inject User user)\n"
				+ "<html><%=user.getName()%></html>").renderType();
		assertEquals(expected, result);
	}


	@Test
	public void shouldSupportLoop() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "for(String user : users) {\n"
				+ "write(user.getName());\n"
				+ "}\n"
				+ "write(\"</html>\");\n");
		String result = new Template("<html><%for(String user : users) {%><%= user.getName() %><%}%></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportComments() {
		String expected = emptyRun("write(\"<html>\");\n"
				+ "/* comments here */"
				+ "write(\"</html>\");\n");
		String result = new Template("<html>@-- comments here --@</html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportSingleMemberWithNoSpace() {
		String expected = "@javax.inject.Inject private String mensagem;\n"
				+ "public void render() {\n"
				+ "write(\"<html>\");\n"
				+ "write(mensagem);\n"
				+ "write(\"</html>\");\n"
				+ "}\n";
		String result = new Template("(@inject String mensagem)\n<html><%= mensagem %></html>").renderType();
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportSingleMember() {
		String expected = "@javax.inject.Inject private String mensagem;\n"
				+ "public void render() {\n"
				+ "write(\"<html>\");\n"
				+ "write(mensagem);\n"
				+ "write(\"</html>\");\n"
				+ "}\n";
		String result = new Template("(@inject String mensagem )\n<html><%= mensagem %></html>").renderType();
		assertEquals(expected, result);
	}
	
	@Test
	public void shouldSupportSingleMemberWithExtraCodeNearby() {
		String expected = "@javax.inject.Inject private i18n.Messages m;\n"
				+ "public void render(User user,List<News> newses) {\n"
				+ " use(header.class).render(\"Welcome\"); \n"
				+ "}\n";
		String result = new Template(
				"(@ User user)\n" +
				"(@ List<News> newses)\n" +
				"(@inject i18n.Messages m )\n" +
				"<% use(header.class).render(\"Welcome\"); %>"
				).renderType();
		assertEquals(expected, result);
	}
	@Test
	public void shouldSupportMembers() {
		String variable = "@javax.inject.Inject private User user;\n";
		String render = "public void render(String mensagem) {\n"
				+ "write(\"<html>\");\n"
				+ "write(mensagem);\n"
				+ "write(\"</html>\");\n"
				+ "}\n";
		String expected = variable + render;
		String result = new Template("(@inject User user)\n(@ String mensagem)\n<html><%= mensagem %></html>").renderType();
		assertEquals(expected, result);
	}

	@Test
	public void shouldSupportBodies() {
		String expected = "public void render() {\n"
				+ "Runnable body = () -> {\n"
				+ "write(\"Guilherme \");\n"
				+ "write(mensagem);\n"
				+ "};\n"
				+ "write(\"<html>\");\n"
				+ "body.run();\n"
				+ "write(\"</html>\");\n}\n";
		String result = new Template("@{{body\nGuilherme @mensagem @}}<html><%body.run();%></html>").renderType();
		assertEquals(expected, result);
	}
	
}
