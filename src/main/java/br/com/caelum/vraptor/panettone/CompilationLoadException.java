package br.com.caelum.vraptor.panettone;

public class CompilationLoadException extends RuntimeException {

	private static final long serialVersionUID = -8880766268327922407L;

	public CompilationLoadException(String msg, Exception e) {
		super(msg, e);
	}

}
