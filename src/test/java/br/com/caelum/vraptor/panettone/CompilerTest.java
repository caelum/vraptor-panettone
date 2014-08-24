package br.com.caelum.vraptor.panettone;

import static br.com.caelum.vraptor.panettone.ReflectionHelper.run;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompilerTest {

	private File sources = new File("target/tmp/sources");
	private File targets = new File("target/tmp/results");
	private FileIO io = new FileIO(sources, targets);

	@Before
	public void before() {
		io.mkclear();
	}

	@After
	public void after() {
		io.deleteAll();
	}

	@Test
	public void testShouldCompileFile() {
		Compiler compiler = new Compiler(sources, targets);
		io.copy("oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldCompileFileInSubdirectory() {
		Compiler compiler = new Compiler(sources, targets);
		io.copy("tone/oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("tone.oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldAutoImportFileInDirectory() {
		String importExpression = "br.com.caelum.vraptor.panettone.*";
		Compiler compiler = new Compiler(sources, targets, Arrays.asList(importExpression));
		io.copy("oi.tone", "<%@ User user %><html>Guilherme</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Guilherme</html>", run(compiler.get("oi"),new Class[]{User.class}, new User("guilherme")));
	}
	
	@Test
	public void testShouldCompileFilesInDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		io.copy("oi.tone", "<html>Oi</html>");
		io.copy("welcome.tone", "<html>Welcome</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("oi"),new Class[]{}));
		assertEquals("<html>Welcome</html>", run(compiler.get("welcome"),new Class[]{}));
	}

	@Test
	public void testAllowCrossReference() {
		Compiler compiler = new Compiler(sources, targets);
		io.copy("oi.tone", "<body>Oi</body>");
		io.copy("welcome.tone", "<html><% new templates.oi(out).render(); %></html>");
		compiler.compileAllOrError();
		assertEquals("<body>Oi</body>", run(compiler.get("oi"),new Class[]{}));
		assertEquals("<html><body>Oi</body></html>", run(compiler.get("welcome"),new Class[]{}));
	}

	@Test
	public void testShouldIgnoreExceptions() {
		Compiler compiler = new Compiler(sources, targets);
		io.copy("oi.tone", "<html>Oi<% for a %></html>");
		io.copy("welcomeUnique.tone", "<html>Welcome</html>");
		List<Exception> exceptions = compiler.compileAll();
		assertEquals(1, exceptions.size());
	}

	@Test
	public void testShouldKeepWatchingDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		compiler.watch();
		io.copy("oiWatch.tone", "<html>Oi</html>");
		try {
			// TODO should not be a thread sleep, sorry :(
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// nothing
		}		
		try {
			assertEquals("<html>Oi</html>", run(compiler.get("oiWatch"),new Class[]{}));
		} finally {
			compiler.stop();
		}
	}

	@Test
	public void shouldRemoveFile() {
		File targetAHtml = new File(targets, "A.java");
		io.copy("<html>Tone</html>", targetAHtml);
		assertTrue(targetAHtml.exists());
		new Compiler(sources, targets).removeJavaVersionOf("A.tone.html");
		assertFalse(targetAHtml.exists());
		
		File targetB = new File(targets, "B.java");
		io.copy("<html>Tone</html>", targetB);
		assertTrue(targetB.exists());
		new Compiler(sources, targets).removeJavaVersionOf("B.tone");
		assertFalse(targetB.exists());
	}
}
