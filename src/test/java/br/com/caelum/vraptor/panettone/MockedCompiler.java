package br.com.caelum.vraptor.panettone;

import br.com.caelum.vraptor.panettone.api.InterfaceCompiler;

public class MockedCompiler implements InterfaceCompiler {

	private String name, method;
	@Override
	public void compile(CompiledType type, String method) {
		this.name = type.getFullName();
		this.method = method;
	}
	
	public String getMethod() {
		return method;
	}
	
	public String getName() {
		return name;
	}

	public void reset() {
		this.name = this.method = null;
	}

}
