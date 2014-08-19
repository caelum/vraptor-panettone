package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

public class SimpleJavaCompiler {

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

	private void parseErrors(DiagnosticCollector<JavaFileObject> diagnostics,
			StringWriter out) {
		StringBuilder builder = new StringBuilder();
		for (Diagnostic diagnostic : diagnostics.getDiagnostics())
			builder.append(String.format("Error on line %d in %s%n", diagnostic
					.getLineNumber(), diagnostic.getSource().toString()));
		throw new CompilationIOException("Compilation error: "
				+ out.getBuffer().toString() + " ==> " + builder.toString());
	}

	public void compile(File... files) {
		compile(Arrays.asList(files));
	}

}
