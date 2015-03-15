package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
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
							"@SuppressWarnings(\"unused\")" +
							"public class " + type.getTypeName() + " " + extension + " " + type.getInterfaces() + " {\n" +
							content +
							"\n\n\n" +
							"private void write(Object o) { if(o!=null) out.write(o.toString()); }\n" +
							"private void write(String o) { if(o!=null) out.write(o); }\n" +
							"}\n";
			try(PrintStream ps = new PrintStream(type.getFile(), "UTF-8")) {
				ps.print(sourceCode);
			}
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
	
	private String baseClassFor(List<String> imports) {
		Optional<String> existing = imports.stream().filter(p -> p.endsWith(".DefaultTemplate")).findFirst();
		return "extends " + existing.orElse("Object");
	}
	
	public CompiledType getType() {
		return type;
	}
	
}
