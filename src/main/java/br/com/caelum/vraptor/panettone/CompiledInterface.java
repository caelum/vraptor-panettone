package br.com.caelum.vraptor.panettone;

import static java.nio.charset.Charset.forName;
import static java.nio.file.Files.write;
import static java.util.Arrays.asList;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class CompiledInterface {

	public CompiledInterface(File classpath, String name, List<String> imports, String content, CompilationListener ... listeners) {
		CompiledType type = new CompiledType(classpath, toInterfaceName(name), imports, content, listeners);
		try {
			String sourceCode = "package templates" + type.getPackages() + ";\n\n" + 
							type.getImportString() +
							"public class " + type.getTypeName() + " {\n" +
							content +
							"}\n";
			write(type.getFilePath(), asList(sourceCode), forName("UTF-8"));
		} catch (IOException e) {
			throw new CompilationIOException("Unable to compile", e);
		}
	}

	private String toInterfaceName(String name) {
		String[] parts = name.split("/");
		String typeName = parts[parts.length-1];
		parts[parts.length-1] = "i_" + typeName;
		return stream(parts).collect(joining("."));
	}


}
