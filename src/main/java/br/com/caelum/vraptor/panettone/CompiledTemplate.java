package br.com.caelum.vraptor.panettone;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.write;
import static java.util.Arrays.asList;
import static java.util.Arrays.copyOf;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class CompiledTemplate {

	private final CompiledType type;
	
	public CompiledTemplate(File classpath, String name, String content) {
		this(classpath, name, new ArrayList<>(), content);
	}
	public CompiledTemplate(File classpath, String name, List<String> imports, String content, CompilationListener ... listeners) {
		this.type = new CompiledType(classpath, implementationName(name), imports, content, listeners);
		try {
			String extension = baseClassFor(imports);
			String sourceCode = "package templates" + type.getPackages() + ";\n\n" + 
							type.getImportString() +
							"public class " + type.getTypeName() + " " + extension + " " + type.getInterfaces() + " {\n" +
							"private java.io.PrintWriter out;\n" +
							getConstructor(type.getTypeName(), listeners) +
							content +
							"\n\n\n" +
							"private void write(Object o) { if(o!=null) out.write(o.toString()); }\n" +
							"private void write(String o) { if(o!=null) out.write(o); }\n" +
							"}\n";
			write(type.getFilePath(), asList(sourceCode), forName("UTF-8"));
		} catch (IOException e) {
			throw new CompilationIOException("Unable to compile", e);
		}
	}
	
	private String implementationName(String name) {
		String[] parts = name.split("/");
		String typeName = parts[parts.length-1];
		parts[parts.length-1] = typeName + "Implementation";
		return stream(parts).collect(joining("/"));
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
	private String baseClassFor(List<String> imports) {
		Optional<String> existing = imports.stream().filter(p -> p.endsWith(".DefaultTemplate")).findFirst();
		return "extends " + existing.orElse("Object");
	}
	public CompiledType getType() {
		return type;
	}
	
}
