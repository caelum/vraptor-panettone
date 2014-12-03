package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
		
		// extract tags
		Pattern p = Pattern.compile("<tone:[^>]+>");
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		 
		while (m.find()) {
			String tag = m.group();
			
			// open tag
			tag = tag.replaceFirst("^<tone:([^\\s>]+)\\s*", "<%use($1.class)");

			// closing tag
			if (tag.endsWith("/>")) {
				// self closing tag
				tag = tag.replaceFirst("/>$", ".done();%>");
			} else {
				// tag remains open
				tag = tag.replaceFirst(">$", ".body(()->{%>\n");
			}
			
			// params
			tag = tag.replaceAll("\\s*([\\w_\\-\\d]+)=(\"[^\"]*\")\\s*", ".$1($2)");
			
		    m.appendReplacement(sb, tag);
		}
		m.appendTail(sb);
		content = sb.toString();
		
		// closing tag
		content = content.replaceAll("</tone:[^>]+>", "\n<%}).done();%>");
		
		return content;
	}
	
	@Override
	public String useParameters(List<String> variables, String typeName) {
		StringBuilder code = new StringBuilder();
		
		List<String> doneParams = new LinkedList<String>();
		
		for (String variable : variables) {
			String[] typeAndName = variable.split("\\s");
			String type = typeAndName[0];
			String name = typeAndName[1];
			
			code.append("private " + type + " " + name + ";\n");
			code.append("public " + typeName + " " + name + "("+ type + " " + name +") { this."+name+" = " + name + "; return this; }\n");
			
			doneParams.add(name);
		}
		
		code.append("public void done() { render(" + doneParams.stream().collect(joining(",")) + "); }\n");
		
		return code.toString();
	}

}
