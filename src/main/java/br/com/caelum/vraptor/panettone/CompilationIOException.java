package br.com.caelum.vraptor.panettone;

import java.io.IOException;
import java.util.List;

public class CompilationIOException extends RuntimeException {

	private static final long serialVersionUID = -290589791882262605L;

	public CompilationIOException(String message, Exception e) {
		super(message, e);
	}

	public CompilationIOException(String msg) {
		super(msg);
	}

	public CompilationIOException(IOException e) {
		super(e);
	}

	public CompilationIOException(List<Exception> exceptions) {
		super(exceptions.get(0));
	}

}
