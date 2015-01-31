package br.com.caelum.vraptor.panettone;

import static java.lang.String.format;

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
		String instance = getInstance();

		
		String signature = walker.getMethodSignature();
		String packagedName = typeName.replaceAll("/", ".").replaceAll(".java", ".class");
		Variables variables = walker.getVariables();
		String delegate = format("\timplementation.render({%s}, {%s});",variables.getTypeList(), variables.asParametersCall());
		String method = signature + "{\n" + delegate + "\n}\n";
		String builders = getBuilders(packagedName, walker);
		String done = "public void done() { implementation.done(); };\n"; 
		return instance + method + builders + done;
	}
	private String getInstance() {
		String loader = "@Inject private PanettoneLazyLoader _lazyLoader;\n";
		String implementation = "Implementation implementation;\n";
		String construct = "@PostConstruct\n" 
				+ "public void init() {\n"
			+"\tthis.implementation = _lazyLoader.load(this.getClass());}\n";
		return loader + implementation + construct;
	}
	
	private String getBuilders(String packagedName, PanettoneWalker walker) {
		String builders = "";
		for (Variable variable : walker.getVariables().getContent()) {
			String builder = format("public i_%4$s %1$s(%2$s) {\n"
					+ "\timplementation.with(\"%1$s\", %3$s.class, %1$s);\n"
					+ "\treturn this;\n"
					+ "};\n", 
					variable.getName(), 
					variable.toDefinition(), 
					variable.getType(),
					packagedName);
			builders += builder;
		}
		return builders;
	}

}
