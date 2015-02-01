package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.List;

import br.com.caelum.vraptor.panettone.api.InterfaceCompiler;

public class CompiledInterfaces {
	
	private final File interfaces;
	private final InterfaceCompiler compiler;

	public CompiledInterfaces(File interfaces, InterfaceCompiler compiler) {
		this.interfaces = interfaces;
		this.compiler = compiler;
	}

	public void generate(String name, List<String> imports, String method, CompilationListener... listeners) {
		CompiledType type = new CompiledType(interfaces, toInterfaceName(name), imports, method, listeners);
        String hash = "//HASH:" +method.hashCode() + "";
		if(type.getHash().equals(hash))
			return;
		compiler.compile(type, method);
	}

	private String toInterfaceName(String name) {
		String[] parts = name.split("/");
		String typeName = parts[parts.length-1];
		parts[parts.length-1] = "i_" + typeName;
		return stream(parts).collect(joining("/"));
	}

}
