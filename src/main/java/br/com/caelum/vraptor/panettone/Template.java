package br.com.caelum.vraptor.panettone;

import java.io.Reader;

import br.com.caelum.vraptor.panettone.parser.PanettoneParser;
import br.com.caelum.vraptor.panettone.parser.ast.PannetoneAST;

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
		PannetoneAST ast = new PanettoneParser().parse(content);
		ast.walk(walker);
		return walker.getJavaCode();
	}

}
