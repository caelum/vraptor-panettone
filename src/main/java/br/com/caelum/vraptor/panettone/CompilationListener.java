package br.com.caelum.vraptor.panettone;

import java.io.File;

public interface CompilationListener {
	
	/**
	 * Returns any interfaces that the view should implement
	 */
	String[] getInterfaces();
	
	/**
	 * Overrides the default constructor
	 */
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
	
	/**
	 * 
	 * @param content File content
	 * @return File content preprocessed
	 */
	String preprocess(String content);
	
	/**
	 * Returns a list of extra required injections.
	 */
	public Variable[] getExtraInjections();
	
}
