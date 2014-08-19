package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.util.List;

public class VRaptorCompiler {

	private final Compiler compiler;
	
	VRaptorCompiler(List<String> imports) {
		this.compiler = new Compiler(new File("src/main/view"), new File("target/view-classes"), imports);
	}
	
	public void start() {
		compiler.watch();
	}
	
	public void stop() {
		compiler.stop();
	}

	public void compileAll() {
		compiler.compileAll();
	}

}
