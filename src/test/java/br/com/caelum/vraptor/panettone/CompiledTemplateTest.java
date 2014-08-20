package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.CompilationIOException;
import br.com.caelum.vraptor.panettone.CompiledTemplate;
import br.com.caelum.vraptor.panettone.Template;

public class CompiledTemplateTest {
	
	private File dir;

	@Before
	public void before() {
		dir = new File("target/tmp");
		dir.mkdirs();
	}
	
	@Test
	public void shouldRunACompiledTemplate() {
		String content = "<html>Oi</html>";
		CompiledTemplate template = compile("oi", content);
		
		String expected = "<html>Oi</html>";
		assertEquals(expected, tryToRun(template));
	}
	
	private CompiledTemplate compile(String name, List<String> imports, String content) {
		return new CompiledTemplate(dir, name, imports, new Template(content).renderType()).compile();
	}
	
	private CompiledTemplate compile(String name, String content) {
		return new CompiledTemplate(dir, name, new Template(content).renderType()).compile();
	}
	
	@Test
	public void shouldInterpolateObject() {
		CompiledTemplate template = compile("interpolateObject", "<%@ String mensagem %><html><%=mensagem%></html>");
		
		String expected = "<html>Oi</html>";
		assertEquals(expected, tryToRun(template, new Class[]{String.class}, "Oi"));
	}
	
	@Test
	public void shouldSupportMethodInvocation() {
		CompiledTemplate template = compile("interpolateObject", "<%@ br.com.caelum.vraptor.panettone.User user %><html><%=user.getName()%></html>");
		
		String expected = "<html>guilherme</html>";
		assertEquals(expected, tryToRun(template, new Class[]{User.class}, new User("guilherme")));
	}

	@Test
	public void shouldSupportImports() {
		CompiledTemplate template = compile("interpolateObject", asList("br.com.caelum.vraptor.panettone.*"), "<%@ User user %><html><%=user.getName()%></html>");
		
		String expected = "<html>guilherme</html>";
		assertEquals(expected, tryToRun(template, new Class[]{User.class}, new User("guilherme")));
	}

	@Test
	public void shouldSupportInheritanceFromDefaultTemplate() {
		CompiledTemplate template = compile("interpolateObject", asList("br.com.caelum.vraptor.panettone.defaulted.DefaultTemplate"), "<html><%=environment%></html>");
		
		String expected = "<html>production</html>";
		assertEquals(expected, tryToRun(template, new Class[]{}));
	}

	@Test
	public void shouldSupportLoop() {
		CompiledTemplate template = compile("interpolateObject", "<%@ java.util.List<br.com.caelum.vraptor.panettone.User> users %><html><%for(br.com.caelum.vraptor.panettone.User user : users) {%><%=user.getName()%><%}%></html>");
		
		String expected = "<html>joaoguilherme</html>";
		assertEquals(expected, tryToRun(template, new Class[]{List.class}, Arrays.asList(new User("joao"),new User("guilherme"))));
	}

	@Test(expected=CompilationIOException.class)
	public void shouldGiveACompilationErrorOnError() {
		compile("error", "<%for a%>");
	}


	private String tryToRun(CompiledTemplate template) {
		return tryToRun(template, new Class[]{});
	}

	private String tryToRun(CompiledTemplate template, Class<?>[] types, Object ... params) {
		Class<?> type = template.getTypeFromNewClassLoader();
		return ReflectionHelper.run(type, types, params);
	}
	
}