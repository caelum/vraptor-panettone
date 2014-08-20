package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VRaptorCompiler {

	private static final String VIEW_OUTPUT = "target/view-classes";
	private static final String VIEW_INPUT = "src/main/view";
	
	private final Compiler compiler;
	
	VRaptorCompiler(List<String> imports) {
		this.compiler = new Compiler(new File(VIEW_INPUT), new File(VIEW_OUTPUT), imports, new VRaptorCompilationListener());
	}
	
	public void start() {
		compiler.watch();
	}
	
	public void stop() {
		compiler.stop();
	}

	public void compileAll() {
		List<Exception> exceptions = new ArrayList<>();
		compiler.precompile(exceptions);
		for(Exception ex : exceptions) {
			System.out.println(ex.getMessage());
		}
	}

}
