package br.com.caelum.vraptor.panettone.api;

import br.com.caelum.vraptor.panettone.CompiledInterface;
import br.com.caelum.vraptor.panettone.CompiledType;

public class RealInterfaceCompiler implements InterfaceCompiler {

	public void compile(CompiledType type, String method) {
		new CompiledInterface(type, method).write();
	}

}
