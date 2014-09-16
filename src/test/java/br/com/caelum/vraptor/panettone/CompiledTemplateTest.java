package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import templates.AutoImported;

public class CompiledTemplateTest {
	
	private File dir;
	private SimpleJavaCompiler typeCompiler;

	@Before
	public void before() {
		dir = new File("target/tmp");
		dir.mkdirs();
		this.typeCompiler = new SimpleJavaCompiler(dir);
	}
	
	@Test
	public void shouldRunACompiledTemplate() {
		String content = "<html>Oi</html>";
		CompiledTemplate template = compile("oi", content);
		
		String expected = "<html>Oi</html>";
		assertEquals(expected, tryToRun(template));
	}
	
	private CompiledTemplate compile(String name, List<String> imports, String content) {
		CompiledTemplate ct = new CompiledTemplate(dir, name, imports, new Template(content).renderType());
		typeCompiler.compileToBytecode(ct.getFile());
		return ct;
	}
	
	private CompiledTemplate compile(String name, String content) {
		String template = new Template(content).renderType();
		CompiledTemplate ct = new CompiledTemplate(dir, name, template);
		typeCompiler.compileToBytecode(ct.getFile());
		return ct;
	}
	
	@Test
	public void shouldInterpolateObject() {
		CompiledTemplate template = compile("interpolateObject", "(@ String mensagem )\n<html><%= mensagem %></html>");
		
		String expected = "<html>Oi</html>";
		assertEquals(expected, tryToRun(template, new Class[]{String.class}, "Oi"));
	}
	
	@Test
	public void shouldSupportMethodInvocation() {
		CompiledTemplate template = compile("interpolateObject",
				"(@inject br.com.caelum.vraptor.panettone.User user)\n"
				+ "<html>@user</html>");
		
		String expected = "<html></html>";
		assertEquals(expected, tryToRun(template, new Class[]{}));
	}

	@Test
	public void shouldSupportImports() {
		CompiledTemplate template = compile("interpolateObject", asList("br.com.caelum.vraptor.panettone.*"),
				"(@inject User user)\n"
				+ "<html>@user</html>");
		
		String expected = "<html></html>";
		assertEquals(expected, tryToRun(template, new Class[]{}));
	}

	@Test
	public void shouldSupportDefaultImportsJavaUtilAndTemplates() {
		CompiledTemplate template = compile("interpolateObject", 	"(@List<br.com.caelum.vraptor.panettone.User> users)\n(@AutoImported auto)\n"
				+ "<html><%= auto.render(users) %></html>");
		
		String expected = "<html>empty</html>";
		assertEquals(expected, tryToRun(template, new Class[]{List.class, AutoImported.class}, null, new AutoImported()));
	}

	@Test
	public void shouldSupportInheritanceFromDefaultTemplate() {
		CompiledTemplate template = compile("interpolateObject", asList("br.com.caelum.vraptor.panettone.defaulted.DefaultTemplate"), "<html><%=environment%></html>");
		
		String expected = "<html>production</html>";
		assertEquals(expected, tryToRun(template, new Class[]{}));
	}

	@Test
	public void shouldSupportLoop() {
		CompiledTemplate template = compile("interpolateObject", "(@ java.util.List<br.com.caelum.vraptor.panettone.User> users )\n<html><%for(br.com.caelum.vraptor.panettone.User user : users) {%><%=user.getName()%><%}%></html>");
		
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
		typeCompiler.compileToBytecode(template.getFile());
		Class<?> type = typeCompiler.getTypeFromNewClassLoader(template);
		return ReflectionHelper.run(type, types, params);
	}
	
}