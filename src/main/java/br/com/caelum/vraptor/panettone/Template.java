package br.com.caelum.vraptor.panettone;

import java.io.Reader;

import br.com.caelum.vraptor.panettone.parser.PanettoneParser;

public class Template {

	private final String content;

	public Template(Reader reader) {
		this.content = CompiledTemplate.toString(reader);
	}
	public Template(String content) {
		this.content = content;
	}
	
	public String renderType() {
		PanettoneWalker walker = new PanettoneWalker();
		new PanettoneParser().parse(content).walk(walker);
		return walker.getJavaCode();
	}

}
