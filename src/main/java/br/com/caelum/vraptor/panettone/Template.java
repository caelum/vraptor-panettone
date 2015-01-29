package br.com.caelum.vraptor.panettone;

import java.io.Reader;

import br.com.caelum.vraptor.panettone.parser.PanettoneParser;
import br.com.caelum.vraptor.panettone.parser.ast.PannetoneAST;

public class Template {

	private final String content;
	private final CompilationListener[] listeners;

	public Template(Reader reader, CompilationListener... listeners) {
		this.listeners = listeners;
		this.content = preprocessAll(CompiledType.toString(reader));
	}
	public Template(String content, CompilationListener... listeners) {
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
		PanettoneWalker walker = bake(typeName);
		return walker.getJavaCode();
	}
	
	private PanettoneWalker bake(String typeName) {
		CodeBuilder code = new CodeBuilder();
		PanettoneWalker walker = new PanettoneWalker(code, typeName, listeners);
		PannetoneAST ast = new PanettoneParser().parse(content);
		ast.walk(new LineNumberWalker(code, walker));
		return walker;
	}
	
	public String renderInterface(String typeName) {
		PanettoneWalker walker = bake("i_" + typeName);
		String instance = "@Inject private PanettoneLazyLoader _lazyLoader;\n";
		String code = walker.getMethodSignature();
		String packagedName = typeName.replaceAll("/", ".").replaceAll(".java", ".class");
		String delegate = "_lazyLoader.load(" + packagedName + ").render(" + walker.getParameterInvocation() + ");";
		return instance + code + "{\n" + delegate + "\n}\n";
	}

}
