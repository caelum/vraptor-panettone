package br.com.caelum.vraptor.panettone;

import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VRaptorCompiler {

	public static final String VIEW_OUTPUT = "target/view-classes";
	public static final String VIEW_INPUT = "src/main/views";
	
	private final PrintStream err = System.err;
	private final Compiler compiler;
	
	public VRaptorCompiler(List<String> imports) {
		this(new File("."), imports);
	}
	
	public VRaptorCompiler(File baseDir, List<String> imports) {
		this.compiler = new Compiler(new File(baseDir, VIEW_INPUT), new File(baseDir, VIEW_OUTPUT), imports, new VRaptorCompilationListener());
	}
	
	public VRaptorCompiler(File baseDir) {
		this(baseDir, new ArrayList<>());
	}
	
	public void start() {
		compiler.startWatch();
	}
	
	public void stop() {
		compiler.stopWatch();
	}

	public void compileAll() {
		for(Exception ex : compileAndRetrieveErrors()) {
			err.println(ex.getMessage());
		}
	}

	public List<Exception> compileAndRetrieveErrors() {
		return compiler.compileAll();
	}
	
	public void clear() {
		compiler.clear();
	}
	
	public void removeJavaVersionOf(String path) {
		compiler.removeJavaVersionOf(path);
	}

	public Optional<Exception> compile(File file) {
		return compiler.compile(file);
	}

}
