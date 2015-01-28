package br.com.caelum.vraptor.panettone;

import static java.net.URLClassLoader.newInstance;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class SimpleJavaCompiler {

	private final File classPath;

	SimpleJavaCompiler(File classPath) {
		this.classPath = classPath;
	}

	public void compile(List<File> toCompile) {

		DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();

		try {
			JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
			try (StandardJavaFileManager files = compiler
					.getStandardFileManager(null, null, null)) {
				StringWriter out = new StringWriter();
				Iterable<? extends JavaFileObject> units = files
						.getJavaFileObjectsFromFiles(toCompile);
				compiler.getTask(out, files, diagnostics, null, null, units)
						.call();

				if (!diagnostics.getDiagnostics().isEmpty()) {
					parseErrors(diagnostics, out);
				}
			}
		} catch (IOException e) {
			throw new CompilationIOException(e);
		}
	}

	@SuppressWarnings("rawtypes") 
	private void parseErrors(DiagnosticCollector<JavaFileObject> diagnostics,
			StringWriter out) {
		StringBuilder builder = new StringBuilder();
		for (Diagnostic diagnostic : diagnostics.getDiagnostics()) {
			String fullMessage = String.format("Error '%s' on line %d in %s%n",
					diagnostic.getMessage(null), diagnostic.getLineNumber(),
					diagnostic.getSource().toString());
			builder.append(fullMessage);
		}
		throw new CompilationIOException("Compilation error: "
				+ out.getBuffer().toString() + " ==> " + builder.toString());
	}

	public void compile(File... files) {
		compile(asList(files));
	}

	public void compile(Stream<File> files) {
		compile(files.collect(toList()));
	}

	public void compileToBytecode(File file) {
		new SimpleJavaCompiler(classPath).compile(file);
	}

	public Class<?> getTypeFromNewClassLoader(CompiledTemplate template) {
		return loadType(this.classPath, template);
	}

	@SuppressWarnings("deprecation")
	Class<?> loadType(File classPath, CompiledTemplate template) {
		try {
			ClassLoader parent = getClass().getClassLoader();
			URL[] url = new URL[]{classPath.toURL()};
			URLClassLoader loader = newInstance(url, parent);
			return loader.loadClass(template.getType().getFullName());
		} catch (IOException e) {
			throw new CompilationLoadException("Unable to compile", e);
		} catch (ClassNotFoundException e) {
			throw new CompilationLoadException("Unable to find class " + template.getType().getFullName() + " at " + classPath, e);
		}
	}
	
}
