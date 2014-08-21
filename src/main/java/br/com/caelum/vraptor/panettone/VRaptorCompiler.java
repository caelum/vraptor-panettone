package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class VRaptorCompiler {

	private static final String VIEW_OUTPUT = "target/view-classes";
	private static final String VIEW_INPUT = "src/main/view";
	
	private final Compiler compiler;
	
	public VRaptorCompiler(List<String> imports) {
		this(new File("."), imports);
	}
	
	public VRaptorCompiler(File baseDir, List<String> imports) {
		this.compiler = new Compiler(new File(baseDir, VIEW_INPUT), new File(baseDir, VIEW_OUTPUT), imports, new VRaptorCompilationListener());
	}
	
	public void start() {
		compiler.watch();
	}
	
	public void stop() {
		compiler.stop();
	}

	public void compileAll() {
		for(Exception ex : compileAndRetrieveErrors()) {
			System.out.println(ex.getMessage());
		}
	}

	public List<Exception> compileAndRetrieveErrors() {
		List<Exception> exceptions = new ArrayList<>();
		compiler.precompile(exceptions);
		return exceptions;
	}
	
	public void clear() {
		compiler.clear();
	}

	public void compile(File file) {
		compiler.compile(file);
		// TODO should return the error
	}

}
