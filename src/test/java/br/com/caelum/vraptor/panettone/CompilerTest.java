package br.com.caelum.vraptor.panettone;

import static br.com.caelum.vraptor.panettone.ReflectionHelper.run;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompilerTest {

	private static final String SRC = "target/tmp/sources/src/main/views";
	private static final String TARGET = "target/tmp/results";
	
	private File sources = new File(SRC);
	private File classes = new File(TARGET);
	private FileIO io = new FileIO(sources, classes);
	private Compiler compiler;
	private BytecodeCompiler bytecodes;

	@Before
	public void before() {
		io.mkclear();
		SimpleJavaCompiler javaCompiler = new SimpleJavaCompiler(classes);
		this.bytecodes = new BytecodeCompiler(javaCompiler);
		this.compiler = new Compiler(sources, classes, classes, new ArrayList<>(), bytecodes);
	}

	@After
	public void after() {
		io.deleteAll();
	}

	@Test
	public void testShouldCompileFile() {
		io.copy("oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(bytecodes.get("oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldCompileFileInSubdirectory() {
		io.copy("tone/oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(bytecodes.get("tone.oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldCompileFilesInDirectory() {
		io.copy("oi.tone", "<html>Oi</html>");
		io.copy("welcome.tone", "<html>Welcome</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(bytecodes.get("oi"),new Class[]{}));
		assertEquals("<html>Welcome</html>", run(bytecodes.get("welcome"),new Class[]{}));
	}

	@Test
	public void testShouldIgnoreExceptions() {
		io.copy("oi.tone", "<html>Oi<% for a %></html>");
		io.copy("welcomeUnique.tone", "<html>Welcome</html>");
		List<Exception> exceptions = compiler.compileAll();
		assertEquals(1, exceptions.size());
	}

	@Test
	public void testShouldKeepWatchingDirectory() {
		compiler.startWatch();
		io.copy("oiWatch.tone", "<html>Oi</html>");
		try {
			// TODO should not be a thread sleep, sorry :(
			Thread.sleep(30000);
		} catch (InterruptedException e) {
			// nothing
		}		
		try {
			assertEquals("<html>Oi</html>", run(bytecodes.get("oiWatch"),new Class[]{}));
		} finally {
			compiler.stopWatch();
		}
	}

	@Test
	public void shouldRemoveFile() {
		File targetAHtml = new File(classes, "templates/A.java");
		io.copy("<html>Tone</html>", targetAHtml);
		assertTrue(targetAHtml.exists());
		compiler.removeJavaVersionOf(SRC + "/A.tone.html");
		assertFalse(targetAHtml.exists());
		
		File targetB = new File(classes, "templates/B.java");
		io.copy("<html>Tone</html>", targetB);
		assertTrue(targetB.exists());
		compiler.removeJavaVersionOf(SRC + "/B.tone");
		assertFalse(targetB.exists());
	}
}
