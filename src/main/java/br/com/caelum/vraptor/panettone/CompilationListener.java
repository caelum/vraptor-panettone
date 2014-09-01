package br.com.caelum.vraptor.panettone;

import java.io.File;

public interface CompilationListener {
	
	String[] getInterfaces();
	
	String overrideConstructor(String typeName);

	/**
	 * Successfully finished compiling a tone
	 */
	void finished(File file, CompiledTemplate template);

	/**
	 * Finished compiling a tone but found errors.
	 */
	void finished(File f, Exception e);

	/**
	 * Clearing all compilation data.
	 */
	void clear();

}
