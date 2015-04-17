package br.com.caelum.vraptor.panettone;


public class CompilationException extends RuntimeException {

	private static final long serialVersionUID = -290589791882262605L;

	public CompilationException(String message, Exception e) {
		super(message, e);
	}

	public CompilationException(String msg) {
		super(msg);
	}

	public CompilationException(Exception e) {
		super(e);
	}

}
