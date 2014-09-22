package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRule extends Rule {

	protected Pattern pattern() {
		String scriptletBegin = "(<%=)\\s*";
		String scriptletEnd = "\\s*(%>)";
		
		String parameters = "(\\([\\w'\",\\.\\s]*\\))?";
		String variableName = "[\\w\\[\\]\"']+";
		String dot = "(\\.)?";
		
		String ifTernary = "(\\s*[\\?\\:]?\\s*)";
		
		String pattern = scriptletBegin + "(((" + variableName + parameters + ")" + dot + ")+" + ifTernary + ")+" + scriptletEnd;
		
		Pattern p = Pattern.compile(pattern);
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletPrintNode(chunk.getText().replace("<%=", "").replace("%>", "").trim(), chunk.getBeginLine());
	}

}
