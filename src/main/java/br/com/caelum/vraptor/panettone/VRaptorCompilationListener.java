package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.joining;

import java.io.File;
import java.util.Arrays;
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

	private static final String TONE_TAG_REGEX = "<tone:[^>]+>";
	private static final String OPEN_TAG_REGEX = "^<tone:([^\\s>]+)\\s*";
	private static final String SELF_CLOSING_TAG_REGEX = "/>$";
	private static final String TAG_REMAINS_OPEN_REGEX = ">$";
	private static final String TAG_PARAM_REGEX = "\\s*([\\w_\\-\\d]+)\\s*=\\s*\"([^\"]*)\"\\s*";
	private static final String TAG_PARAM_WITH_CODE_REGEX = "\\s*([\\w_\\-\\d]+)\\s*=\\s*\"@([^\"]*)\"\\s*";
	private static final String CLOSING_TAG_REGEX = "</tone:[^>]+>";
	
	private static final String OPEN_INVOCATION_PART = "<%use($1.class)";
	private static final String CLOSE_INVOCATION_PART = ".done();%>";
	private static final String INVOKE_BUILDER_METHOD_WITH_STRING_PART = ".$1(\"$2\")";
	private static final String INVOKE_BUILDER_METHOD_WITH_CODE_PART = ".$1($2)";
	private static final String OPEN_BODY_PART = ".body(()->{%>\n";
	private static final String CLOSE_BODY_PART = "\n<%}).done();%>";
	
	private static final String RENDERED_PARAM_WITH_CODE_REGEX = "\\s*tone:rendered\\s*=\\s*(\"@[^\"]*\"|'@[^']*')\\s*";
	
	@Override
	public String preprocess(String content) {
		
		Pattern p = Pattern.compile(TONE_TAG_REGEX);
		Matcher m = p.matcher(content);
		StringBuffer sb = new StringBuffer();
		 
		while (m.find()) {
			String tag = m.group();
			
			String rendered = "";
			Matcher renderedMatcher = Pattern.compile(RENDERED_PARAM_WITH_CODE_REGEX).matcher(tag);
			if (renderedMatcher.find()) {
				String renderedParam = renderedMatcher.group(1);
				rendered = "<%if("+renderedParam.substring(2,renderedParam.length()-1)+")%>";
				tag = tag.replaceFirst(RENDERED_PARAM_WITH_CODE_REGEX, " ");
			}
			
			tag = tag.replaceFirst(OPEN_TAG_REGEX, OPEN_INVOCATION_PART);

			if (tag.endsWith("/>")) {
				tag = tag.replaceFirst(SELF_CLOSING_TAG_REGEX, CLOSE_INVOCATION_PART);
			} else {
				tag = tag.replaceFirst(TAG_REMAINS_OPEN_REGEX, OPEN_BODY_PART);
			}
			
			tag = tag.replaceAll(TAG_PARAM_WITH_CODE_REGEX, INVOKE_BUILDER_METHOD_WITH_CODE_PART);
			tag = tag.replaceAll(TAG_PARAM_REGEX, INVOKE_BUILDER_METHOD_WITH_STRING_PART);
			
		    m.appendReplacement(sb, rendered + tag);
		}
		m.appendTail(sb);
		content = sb.toString();
		
		content = content.replaceAll(CLOSING_TAG_REGEX, CLOSE_BODY_PART);
		
		return content;
	}
	
	@Override
	public String useParameters(List<String> variables, String typeName) {
		StringBuilder code = new StringBuilder();
		
		List<String> doneParams = new LinkedList<>();
		
		variables.forEach((variable) -> {
			String[] typeAndName = variable.split("\\s");
			String type = typeAndName[0];
			String name = typeAndName[1];
			
			code.append("private " + type + " " + name + ";\n");
			code.append("public " + typeName + " " + name + "("+ type + " " + name +") { this."+name+" = " + name + "; return this; }\n");
			
			
			final List<String> WRAPPERS = Arrays.asList(new String[]{"Integer", "Boolean", "Double", "Long", "Byte", "Short", "Float"});
			
			if (!type.equals("String") && WRAPPERS.contains(type)) {
				code.append("public " + typeName + " " + name + "(String " + name +") { this."+name+" = " + type + ".valueOf(" + name + "); return this; }\n");
			}
			
			doneParams.add(name);
		});
		
		code.append("public void done() { render(" + doneParams.stream().collect(joining(",")) + "); }\n");
		
		return code.toString();
	}

}
