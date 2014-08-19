package br.com.caelum.vraptor.panettone;

import static java.net.URLClassLoader.newInstance;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Stream;

import com.google.common.io.Files;

public class CompiledTemplate {

	private final File file;
	private final String packages;
	private final String sourceCode;
	private final File classpath;

	public CompiledTemplate(File classpath, String name, String content) {
		this(classpath, name, new ArrayList<String>(), content);
	}
	public CompiledTemplate(File classpath, String name, List<String> imports, String content) {
		try {
			this.classpath = classpath;
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
			this.sourceCode = "package templates" + packages + ";\n\n" + 
							importString +
							"public class " + typeName + " " + extension + " {\n" +
							"private final java.io.PrintWriter out;\n" +
							"public " + typeName + "(java.io.PrintWriter out) { this.out = out; }\n" +
							content +
							"}\n";
			Files.write(sourceCode, file, Charset.forName("UTF-8"));
			
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

	private String getTypeName() {
		return file.getName().substring(0, file.getName().lastIndexOf("."));
	}
	
	public void compile() {
		new SimpleJavaCompiler().compile(file);
	}

	public Class<?> getType() {
		return loadType();
	}

	@SuppressWarnings("deprecation")
	private Class<?> loadType() {
		String name = "templates" + packages + "." + getTypeName();
		try {
			ClassLoader parent = getClass().getClassLoader();
			URL[] url = new URL[]{classpath.toURL()};
			URLClassLoader loader = newInstance(url, parent);
			return loader.loadClass(name);
		} catch (IOException e) {
			throw new CompilationLoadException("Unable to compile", e);
		} catch (ClassNotFoundException e) {
			throw new CompilationLoadException("Unable to find class " + name + " at " + classpath, e);
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
	
	public String getPackagedName() {
		String noExtension = file.getName().replace(".java", "");
		if(packages.isEmpty()) return noExtension;
		return packages.substring(1) + "." + noExtension;
	}
}
