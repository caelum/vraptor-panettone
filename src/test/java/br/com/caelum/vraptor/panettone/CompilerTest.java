package br.com.caelum.vraptor.panettone;

import static br.com.caelum.vraptor.panettone.ReflectionHelper.run;
import static com.google.common.io.Files.write;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Optional;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.CompilationIOException;
import br.com.caelum.vraptor.panettone.Compiler;

public class CompilerTest {

	File sources = new File("target/tmp/sources");
	File targets = new File("target/tmp/results");

	@Before
	public void before() {
		sources.mkdirs();
		targets.mkdirs();
		clearAll();
	}

	private void clearAll() {
		clear(sources);
		clear(targets);
	}

	@After
	public void after() {
		clearAll();
		sources.delete();
		targets.delete();
	}

	private void clear(File dir) {
		Optional<File> internalDir = stream(dir.listFiles()).filter(File::isDirectory).filter(f->!f.getName().equals("templates")).findFirst();
		internalDir.ifPresent(
				(f) -> {
					throw new RuntimeException(
						"I will not delete if there is recurssion, is there a problem here? @"
								+ f.getAbsolutePath());
				}
		);
		stream(dir.listFiles()).forEach(File::delete);
	}

	@Test
	public void testShouldCompileFileInDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi</html>");
		compiler.compileAll();
		assertEquals("<html>Oi</html>", run(compiler.get("oi.tone"),new Class[]{}));
	}
	
	@Test
	public void testShouldCompileFilesInDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi</html>");
		copy("welcome.tone", "<html>Welcome</html>");
		compiler.compileAll();
		assertEquals("<html>Oi</html>", run(compiler.get("oi.tone"),new Class[]{}));
		assertEquals("<html>Welcome</html>", run(compiler.get("welcome.tone"),new Class[]{}));
	}

	@Test
	public void testShouldCompileIfOneHasError() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi<% for a %></html>");
		copy("welcomeUnique.tone", "<html>Welcome</html>");
		try {
			compiler.compileAll();
			fail("Should have complained about compilation error");
		} catch (CompilationIOException ex) {
			// expected
		}
		assertEquals("<html>Welcome</html>", run(compiler.get("welcomeUnique.tone"), new Class[]{}));
	}

	@Test
	public void testShouldKeepWatchingDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		compiler.watch();
		copy("oiWatch.tone", "<html>Oi</html>");
		try {
			// TODO should not be a thread sleep, sorry :(
			Thread.sleep(60000);
		} catch (InterruptedException e) {
			// nothing
		}		
		try {
			assertEquals("<html>Oi</html>", run(compiler.get("oiWatch.tone"),new Class[]{}));
		} finally {
			compiler.stop();
		}
	}

	private void copy(String fileName, String content) {
		try {
			write(content, new File(sources, fileName), Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException("Unexpected exception ", e);
		}
	}
}
