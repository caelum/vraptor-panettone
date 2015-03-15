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
		PanettoneWalker walker = bake();
		String constructor = getConstructor(typeName, walker);
		String injectors = getExtraInjectors(walker);
		String use = "private <T extends br.com.caelum.vraptor.View> T use(Class<T> type) { return result.use(type); }\n";
		return injectors + constructor + walker.getJavaCode() + use;
	}
	
	public String getDefaultConstructor(String typeName, PanettoneWalker walker) {
		Variables inject = walker.getInjectVariables();
		String standard = "public " + typeName + "Implementation(" + inject.asFullMethodDefinition() + ") {\n" +
				inject.asSettingThem() +
				"}\n";
		return standard;
	}

	private String getConstructor(String typeName, PanettoneWalker walker) {
		for (CompilationListener cl : listeners) {
			if (cl.overrideConstructor(typeName) != null)
				return cl.overrideConstructor(typeName);
		}
		return getDefaultConstructor(typeName, walker);
	}
	
	private String getExtraInjectors(PanettoneWalker walker) {
		Variables variables = walker.getInjectVariables();
		return variables.asDefinitions();
	}
	
	private PanettoneWalker bake() {
		CodeBuilder code = new CodeBuilder();
		PanettoneWalker walker = new PanettoneWalker(code, listeners);
		PannetoneAST ast = new PanettoneParser().parse(content);
		ast.walk(new LineNumberWalker(code, walker));
		return walker;
	}

	public String renderInterface(String typeName) {
		PanettoneWalker walker = bake();
		String instance = getGenericInstance(walker.getInjectVariables());

		String signature = walker.getMethodSignature();
		String packagedName = typeName.replaceAll("/", ".").replaceAll(".java", ".class");
		Variables variables = walker.getVariables();
		String delegate = invokeRender(variables);
		String method = signature + "{\n" + delegate + "\n}\n";
		String builders = getBuilders(packagedName, walker.getVariables());
		String injects = walker.getInjectVariables().except("out").asInjectDefinitions();
		return instance + injects + method + builders;
	}
	private String invokeRender(Variables variables) {
		return format("\timplementation.render(new Class[]{%s} %s);",variables.getNonGenericTypeList(), parameterInArray(variables));
	}
	private String parameterInArray(Variables variables) {
		if(variables.isEmpty()) {
			return "";
		}
		return ", " + variables.asParametersCall();
	}
	private String getGenericInstance(Variables injects) {
		String loader = "private @javax.inject.Inject br.com.caelum.vraptor.panettone.PanettoneLoader _lazyLoader;\n";
		String implementation = "private br.com.caelum.vraptor.panettone.Implementation implementation;\n";
		String out = "private java.io.PrintWriter out;\n";
		String construct = "@javax.annotation.PostConstruct\n" 
				+ "public void init() {\n"
				+ "try {\n"
				+ "	res.setContentType(\"text/html; charset=UTF-8\");\n"
				+ "	this.out = res.getWriter();\n"
				+ "} catch (java.io.IOException e) {\n"
				+ "	throw new RuntimeException(e);\n"
				+ "}\n"
				+" this.implementation = _lazyLoader.load(this.getClass(), " + injects.asParametersCall() + ");\n"
			+ "}\n";
		return loader + implementation + out + construct;
	}
	
	private String getBuilders(String packagedName, Variables variables) {
		String code = variables.asNonFinalDefinitions();
		code += variables.asSetters(packagedName);
		code += "public void done() { render(" + variables.asParametersCall() + "); }\n";
		return code;
	}

}
