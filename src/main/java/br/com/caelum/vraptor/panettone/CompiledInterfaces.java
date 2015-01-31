package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.List;

public class CompiledInterfaces {
	
	private final File interfaces;

	public CompiledInterfaces(File interfaces) {
		this.interfaces = interfaces;
	}

	public void generate(String name, List<String> imports, String method, CompilationListener[] listeners) {
		CompiledType type = new CompiledType(interfaces, toInterfaceName(name), imports, method, listeners);
        String hash = method.hashCode() + "";
		if(type.getHash().equals(hash))
			return;

		new CompiledInterface(type, method).write();
	}

	private String toInterfaceName(String name) {
		String[] parts = name.split("/");
		String typeName = parts[parts.length-1];
		parts[parts.length-1] = "i_" + typeName;
		return stream(parts).collect(joining("."));
	}

}
