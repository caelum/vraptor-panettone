package br.com.caelum.vraptor.panettone;

import static java.nio.charset.Charset.forName;
import static java.util.Arrays.asList;

import java.io.IOException;
import java.nio.file.Files;

public class CompiledInterface {

	private final CompiledType type;
	private final String content;

	public CompiledInterface(CompiledType type, String content) {
		this.type = type;
		this.content = content;
	}
	public void write() {
		try {
			String sourceCode = "//HASH:" + content.hashCode() + "\n" + 
							"package templates" + type.getPackages() + ";\n\n" + 
							type.getImportString() +
							"public class " + type.getTypeName() + " {\n" +
							content +
							"}\n";
			Files.write(type.getFilePath(), asList(sourceCode), forName("UTF-8"));
		} catch (IOException e) {
			throw new CompilationIOException("Unable to compile", e);
		}
	}

}
