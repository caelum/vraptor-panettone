package br.com.caelum.vraptor.panettone;

public interface CompilationListener {
	
	String[] getInterfaces();
	
	String overrideConstructor(String typeName);

}
