package br.com.caelum.vraptor.panettone;

import static br.com.caelum.vraptor.panettone.ReflectionHelper.run;
import static com.google.common.io.Files.write;
import static java.util.Arrays.stream;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class CompilerTest {

	private File sources = new File("target/tmp/sources");
	private File targets = new File("target/tmp/results");

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
		try {
			List<String> internalDirs = Files.list(dir.toPath())
						.map(Path::toFile)
						.filter(File::isDirectory)
						.map(File::getName)
						.collect(Collectors.toList());
			internalDirs.remove("templates");
			internalDirs.remove("tone");
			if(!internalDirs.isEmpty()) {
				throw new RuntimeException(
						"I will not delete if there is recurssion, is there a problem here? @"
									+ internalDirs);
			}
			stream(dir.listFiles()).filter(File::isDirectory).forEach(this::clear);
			stream(dir.listFiles()).forEach(File::delete);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Test
	public void testShouldCompileFile() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldCompileFileInSubdirectory() {
		Compiler compiler = new Compiler(sources, targets);
		copy("tone/oi.tone", "<html>Oi</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("tone.oi"),new Class[]{}));
	}
	
	@Test
	public void testShouldAutoImportFileInDirectory() {
		String importExpression = "br.com.caelum.vraptor.panettone.*";
		Compiler compiler = new Compiler(sources, targets, Arrays.asList(importExpression));
		copy("oi.tone", "<%@ User user %><html>Guilherme</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Guilherme</html>", run(compiler.get("oi"),new Class[]{User.class}, new User("guilherme")));
	}
	
	@Test
	public void testShouldCompileFilesInDirectory() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi</html>");
		copy("welcome.tone", "<html>Welcome</html>");
		compiler.compileAllOrError();
		assertEquals("<html>Oi</html>", run(compiler.get("oi"),new Class[]{}));
		assertEquals("<html>Welcome</html>", run(compiler.get("welcome"),new Class[]{}));
	}

	@Test
	public void testShouldIgnoreExceptions() {
		Compiler compiler = new Compiler(sources, targets);
		copy("oi.tone", "<html>Oi<% for a %></html>");
		copy("welcomeUnique.tone", "<html>Welcome</html>");
		List<Exception> exceptions = compiler.compileAll();
		assertEquals(1, exceptions.size());
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
			assertEquals("<html>Oi</html>", run(compiler.get("oiWatch"),new Class[]{}));
		} finally {
			compiler.stop();
		}
	}

	private void copy(String fileName, String content) {
		try {
			File file = new File(sources, fileName);
			file.getParentFile().mkdirs();
			write(content, file, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException("Unexpected exception ", e);
		}
	}
}
