package br.com.caelum.vraptor.panettone;

import java.io.File;

public class VRaptorCompiler {

	private final Compiler compiler = new Compiler(new File("src/main/view"), new File("target/view-classes"));
	
	public void start() {
		compiler.watch();
	}
	
	public void destroy() {
		compiler.stop();
	}

}
