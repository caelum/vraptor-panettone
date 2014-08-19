package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.joining;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

public class Template {

	private final String content;

	public Template(Reader reader) {
		this.content = CompiledTemplate.toString(reader);
	}
	public Template(String content) {
		this.content = content;
	}

	public String renderType() {
		int position = 0;
		StringBuilder builder = new StringBuilder();
		List<String> variables = new ArrayList<>();
		List<String> methods = new ArrayList<>();
		
		while (position < content.length()) {
			int next = content.indexOf("<%", position);
			if (next == -1) {
				String part = content.substring(position);
				builder.append("out.write(\"" + part + "\");\n");
				position = content.length();
			} else {
				if (position != next) {
					String part = content.substring(position, next);
					builder.append("out.write(\"" + part + "\");\n");
				}

				// BUG: a random %> in the middle of some java code
				int finish = content.indexOf("%>", next + 1);
				if(finish==-1) {
					throw new InvalidTemplate("Template did not finish <% entry.");
				}
				
				String evaluation = content.substring(next + 2, finish);
				if(evaluation.startsWith("=")) {
					builder.append("out.write(" + evaluation.substring(1) + ");\n");
				} else if(evaluation.startsWith("--")){
					// comments
				} else if(evaluation.startsWith("@")){
					variables.add(evaluation.substring(1));
				} else if(evaluation.startsWith("$")){
					methods.add(evaluation.substring(1) + "\n");
				} else {
					builder.append(evaluation + "\n");
				}
				position = finish + 2;
			}
		}
		String parameters = variables.stream().collect(joining(","));
		String methodCode = methods.stream().collect(joining("\n\n"));
		String render = "public void render(" + parameters + ") {\n" + builder.toString() + "}\n";
		String result = methodCode + render;
		return result;
	}

	public class InvalidTemplate extends RuntimeException {

		private static final long serialVersionUID = 3508082279487674463L;

		public InvalidTemplate(String message) {
			super(message);
		}
		
	}

}
