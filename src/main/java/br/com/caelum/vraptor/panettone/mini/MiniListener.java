package br.com.caelum.vraptor.panettone.mini;

import java.io.File;
import java.util.List;

import br.com.caelum.vraptor.panettone.CompilationListener;
import br.com.caelum.vraptor.panettone.CompiledTemplate;

public class MiniListener implements CompilationListener{

	@Override
	public String[] getInterfaces() {
		return null;
	}

	@Override
	public String overrideConstructor(String typeName) {
		return null;
	}

	@Override
	public void finished(File file, CompiledTemplate template) {
	}

	@Override
	public void finished(File f, Exception e) {
	}

	@Override
	public void clear() {
	}
	
	@Override
	public String preprocess(String content) {
		return content;
	}

	@Override
	public String useParameters(List<String> variables) {
		return "";
	}

}
