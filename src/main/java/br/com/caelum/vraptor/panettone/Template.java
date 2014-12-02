package br.com.caelum.vraptor.panettone;

import java.io.Reader;
import java.util.List;

import br.com.caelum.vraptor.panettone.parser.PanettoneParser;
import br.com.caelum.vraptor.panettone.parser.ast.PannetoneAST;

public class Template {

	private final String content;
	private final CompilationListener[] listeners;

	public Template(CompilationListener[] listeners, Reader reader) {
		this.listeners = listeners;
		this.content = preprocessAll(CompiledTemplate.toString(reader));
	}
	public Template(CompilationListener[] listeners, String content) {
		this.listeners = listeners;
		this.content = preprocessAll(content);
	}
	
	private String preprocessAll(String content) {
		for (CompilationListener cl : listeners) {
			content = cl.preprocess(content);
		}
		return content;
	}
	
	public String renderType(String typeName) {
		CodeBuilder code = new CodeBuilder();
		PanettoneWalker walker = new PanettoneWalker(code, listeners, typeName);
		
		PannetoneAST ast = new PanettoneParser().parse(content);
		ast.walk(new LineNumberWalker(code, walker));
		
		return walker.getJavaCode();
	}

}
