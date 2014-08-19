package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.util.List;

public class VRaptorCompiler {

	private static final String VIEW_OUTPUT = "target/view-classes";
	private static final String VIEW_INPUT = "src/main/view";
	
	private final Compiler compiler;
	
	VRaptorCompiler(List<String> imports) {
		this.compiler = new Compiler(new File(VIEW_INPUT), new File(VIEW_OUTPUT), imports);
	}
	
	public void start() {
		compiler.watch();
	}
	
	public void stop() {
		compiler.stop();
	}

	public void compileAll() {
		List<Exception> exceptions = compiler.compileAll();
		for(Exception ex : exceptions) {
			ex.printStackTrace();
		}
	}

}
