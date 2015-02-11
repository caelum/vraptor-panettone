package br.com.caelum.vraptor.panettone;

public class CodeBuilder {

	private final StringBuilder code;
	
	public CodeBuilder() {
		code = new StringBuilder();
	}
	
	public void append(String c) {
		code.append(c);
	}
	
	public String getCode() {
		return code.toString();
	}
	
	@Override
	public String toString() {
		return getCode();
	}
}
