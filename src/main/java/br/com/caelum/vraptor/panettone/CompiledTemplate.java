package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class CompiledTemplate {

	private final File file;
	private final String packages;
	private final String sourceCode;
	public CompiledTemplate(File classpath, String name, String content) {
		this(classpath, name, new ArrayList<String>(), content);
	}
	public CompiledTemplate(File classpath, String name, List<String> imports, String content, CompilationListener ... listeners) {
		try {
			File templates = new File(classpath, "templates");
			String javaName = name + ".java";
			this.file = new File(templates, javaName);
			this.packages = stream(javaName.split("/"))
				.filter(f -> !f.endsWith(".java"))
				.map(packageName -> "." + packageName)
				.collect(joining());
			file.getParentFile().mkdirs();
			String typeName = getTypeName();
			String extension = baseClassFor(imports);
			String importString = importStatementsFor(imports);
			String interfaces = interfacesFor(listeners);
			this.sourceCode = "package templates" + packages + ";\n\n" + 
							importString +
							"public class " + typeName + " " + extension + " " + interfaces + " {\n" +
							"private java.io.PrintWriter out;\n" +
							getConstructor(typeName, listeners) +
							content +
							"\n\n\n" +
							"private void write(Object o) { if(o!=null) out.write(o.toString()); }\n" +
							"private void write(String o) { if(o!=null) out.write(o); }\n" +
							"}\n";
			Files.write(file.toPath(), Arrays.asList(sourceCode), Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new CompilationIOException("Unable to compile", e);
		}
	}
	
	private String getConstructor(String typeName,
			CompilationListener[] listeners) {
		for (CompilationListener cl : listeners) {
			if (cl.overrideConstructor(typeName) != null)
				return cl.overrideConstructor(typeName);
		}
		String standard = "public " + typeName + "(java.io.PrintWriter out) {\n" +
				" this.out = out; "+
				"}\n";
		return standard;
	}
	private String interfacesFor(CompilationListener[] listeners) {
		String interfaces = stream(listeners)
				.map(l -> l.getInterfaces())
				.filter(i -> i != null)
				.flatMap(Arrays::stream)
				.collect(joining(","));
		if(interfaces.isEmpty()) return "";
		return "implements " + interfaces;
	}
	private String baseClassFor(List<String> imports) {
		Optional<String> existing = imports.stream().filter(p -> p.endsWith(".DefaultTemplate")).findFirst();
		return "extends " + existing.orElse("Object");
	}
	
	private String importStatementsFor(List<String> imports) {
		String base = "import java.util.*;\n" +
					  "import templates.*;\n";
		String customImports = imports.stream().map(s -> "import " + s + ";\n").collect(joining());
		return base + customImports + "\n";
	}

	private String getTypeName() {
		return file.getName().replaceAll("\\.java.*", "");
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
	
	public String getPackagedName() {
		String noExtension = file.getName().replace(".java", "");
		if(packages.isEmpty()) return noExtension;
		return packages.substring(1) + "." + noExtension;
	}
	
	public File getFile() {
		return file;
	}

	String getFullName() {
		return "templates" + packages + "." + getTypeName();
	}
}
