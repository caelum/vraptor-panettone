package br.com.caelum.vraptor.panettone.api;

import br.com.caelum.vraptor.panettone.CompiledType;

public interface InterfaceCompiler {

	public void compile(CompiledType type, String method);
}
