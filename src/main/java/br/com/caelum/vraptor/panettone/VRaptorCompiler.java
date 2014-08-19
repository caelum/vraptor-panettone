package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.List;

public class VRaptorCompiler {

	private final Compiler compiler;
	
	VRaptorCompiler(List<String> imports) {
		String importer = imports.stream().map(s -> "import " + s).collect(joining(";\n"));
		this.compiler = new Compiler(new File("src/main/view"), new File("target/view-classes"), importer);
	}
	
	public void start() {
		compiler.watch();
	}
	
	public void stop() {
		compiler.stop();
	}

}
