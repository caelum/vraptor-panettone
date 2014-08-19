package br.com.caelum.vraptor.panettone;

import static java.net.URLClassLoader.newInstance;
import static java.util.Arrays.asList;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import com.google.common.io.Files;

public class CompiledTemplate {

	private final File file;
	private final Class<?> type;

	public CompiledTemplate(File dir, String name, String content) {
		this(dir, name, new ArrayList<String>(), content);
	}
	public CompiledTemplate(File dir, String name, List<String> imports, String content) {
		try {
			File templates = new File(dir, "templates");
			templates.mkdirs();
			this.file = new File(templates, name + ".java");
			String typeName = getTypeName();
			String extension = baseClassFor(imports);
			String importString = importStatementsFor(imports);
			String main = "package templates;\n\n" + 
							importString +
							"public class " + typeName + " " + extension + " {\n" +
							"private final java.io.PrintWriter out;\n" +
							"public " + typeName + "(java.io.PrintWriter out) { this.out = out; }\n" +
							content +
							"}\n";
			Files.write(main, file, Charset.forName("UTF-8"));
			
			DiagnosticCollector<JavaFileObject> diagnostics = new DiagnosticCollector<>();
			
	       JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
	       try(StandardJavaFileManager files = compiler.getStandardFileManager(null, null, null)) {
		       StringWriter out = new StringWriter();
		       compiler.getTask(out, files, diagnostics, null, null, unitsFor(files)	).call();
	
		       if(!diagnostics.getDiagnostics().isEmpty()) {
			    	   StringBuilder builder = new StringBuilder();
			    	   	for (Diagnostic diagnostic : diagnostics.getDiagnostics())
			    	   		builder.append(String.format("Error on line %d in %s%n",
			    				   diagnostic.getLineNumber(),
			    				   diagnostic.getSource().toString()));
			    	   	throw new CompilationIOException("Compilation error: "+ out.getBuffer().toString() + " ==> " + builder.toString() + " // " + main);
		       }
	       }
	       this.type = loadType();
		} catch (IOException e) {
			throw new CompilationIOException("Unable to compile", e);
		}
	}
	private String baseClassFor(List<String> imports) {
		Stream<String> nonStatics = imports.stream().filter(p -> !p.startsWith("static"));
		Stream<String> defaultsOrWholePackages = nonStatics
						.filter(p -> p.endsWith(".DefaultTemplate") || p.endsWith(".*"));
		Stream<String> defaultTemplates = defaultsOrWholePackages
						.map(p -> p.endsWith(".*") ? p.replace(".*", ".DefaultTemplate") : p);
		
		Stream<String> existing = defaultTemplates
						.filter(this::existsType);
		Optional<String> found = existing
						.map(p -> " extends " + p + " ")
						.findFirst();
		return found.orElse("");
	}
	private boolean existsType(String type) {
		try {
			Class.forName(type);
			return true;
		} catch (ClassNotFoundException e) {
			return false;
		}
	}
	private String importStatementsFor(List<String> imports) {
		if (imports.isEmpty())
			return "";
		return imports.stream().map(s -> "import " + s + ";\n").collect(joining()) + "\n";
	}
	private Iterable<? extends JavaFileObject> unitsFor(
			StandardJavaFileManager files) {
		return files.getJavaFileObjectsFromFiles(asList(file));
	}

	private String getTypeName() {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}

	public Class<?> getType() {
		return this.type;
	}

	private Class<?> loadType() {
		try {
			ClassLoader parent = getClass().getClassLoader();
			URL[] url = new URL[]{file.getParentFile().getParentFile().toURL()};
			URLClassLoader loader = newInstance(url, parent);
			return loader.loadClass("templates." + getTypeName());
		} catch (IOException | ClassNotFoundException e) {
			throw new CompilationLoadException("Unable to compile", e);
		}
	}
	public static String toString(Reader reader) {
		try(Scanner scanner = new Scanner(reader)) {
			return readAll(scanner);
		}
	}

	static String toString(InputStream stream) {
		try(Scanner scanner = new Scanner(stream, "UTF-8")) {
			return readAll(scanner);
		}
	}
	private static String readAll(Scanner scanner) {
		scanner.useDelimiter("\\A");
		String input = scanner.hasNext() ? scanner.next() : "";
		scanner.close();
		return input;
	}
}
