package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.InputStream;
import java.io.Reader;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CompiledType {

	private final File file;
	private final String packages;
	private String importString;
	private String interfaces;

	public CompiledType(File classpath, String name, List<String> imports, String content, CompilationListener ... listeners) {
		File templates = new File(classpath, "templates");
		String javaName = name + ".java";
		this.file = new File(templates, javaName);
		this.packages = stream(javaName.split("/"))
			.filter(this::isNotTheFileName)
			.map(packageName -> "." + packageName)
			.collect(joining());
		file.getParentFile().mkdirs();
		this.importString = importStatementsFor(imports);
		this.interfaces = interfacesFor(listeners);
	}

	private boolean isNotTheFileName(String f) {
		return !f.endsWith(".java");
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
	
	private String importStatementsFor(List<String> imports) {
		String base = "import java.util.*;\n" +
					  "import templates.*;\n";
		String customImports = imports.stream().map(s -> "import " + s + ";\n").collect(joining());
		return base + customImports + "\n";
	}

	String getTypeName() {
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

	public String getPackages() {
		return packages;
	}

	public String getImportString() {
		return importString;
	}

	public Path getFilePath() {
		return file.toPath();
	}
	
	public String getInterfaces() {
		return interfaces;
	}
}
