package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class VRaptorCompilationListener implements CompilationListener {

	@Override
	public String[] getInterfaces() {
		return new String[] { "br.com.caelum.vraptor.View" };
	}

	@Override
	public String overrideConstructor(String typeName) {
		return "@javax.inject.Inject\n"
				+ "private javax.servlet.http.HttpServletResponse res;\n"
				+ "@javax.inject.Inject\n"
				+ "private br.com.caelum.vraptor.Result result;\n"
				+ "@javax.annotation.PostConstruct\n"
				+ "public void fillout() {\n"
				+ "try {\n"
				+ "	res.setContentType(\"text/html; charset=UTF-8\");\n"
				+ "	this.out = res.getWriter();\n"
				+ "} catch (java.io.IOException e) {\n"
				+ "	throw new RuntimeException(e);\n"
				+ "}\n"
				+ "}\n"
				+ "private <T extends br.com.caelum.vraptor.View> T use(Class<T> type) { return result.use(type); }\n";
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
		// TODO regex pra invocacao de tag
		return content;
	}
	
	@Override
	public String useParameters(List<String> variables, String typeName) {
		// TODO gera builder e render() final
		
		StringBuilder code = new StringBuilder();
		
		List<String> doneParams = new LinkedList<String>();
		
		for (String variable : variables) {
			String[] typeAndName = variable.split("\\s");
			String type = typeAndName[0];
			String name = typeAndName[1];
			
			code.append("private " + type + " " + name + ";");
			code.append("public " + typeName + " " + name + "("+ type + " " + name +") { this."+name+" = " + name + "; return this; }");
			
			doneParams.add(name);
		}
		
		code.append("public void done() { render(" + doneParams.stream().collect(joining(",")) + "); }");
		
		return code.toString();
	}

}
