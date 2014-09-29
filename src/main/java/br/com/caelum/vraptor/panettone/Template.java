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
		CodeBuilder code = new CodeBuilder();
		PanettoneWalker walker = new PanettoneWalker(code);
		
		PannetoneAST ast = new PanettoneParser().parse(content);
		ast.walk(new LineNumberWalker(code, walker));
		
		return walker.getJavaCode();
	}

}
