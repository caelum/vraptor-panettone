package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();
		
		String scriptletBegin = "(<%=)\\s*";
		String scriptletEnd = "\\s*(%>)";
		
		String parameters = "(\\([\\w'\",\\.\\s]*\\))?";
		String variableName = "[\\w\\[\\]\"']+";
		String dot = "(\\.)?";
		
		String ifTernary = "(\\s*[\\?\\:]?\\s*)";
		
		String pattern = scriptletBegin + "(((" + variableName + parameters + ")" + dot + ")+" + ifTernary + ")+" + scriptletEnd;
		
		Pattern p = Pattern.compile(pattern);
		
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			String matched = matcher.group();
			chunks.add(new TextChunk(matched, sc.lineBegin(matched)));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletPrintNode(chunk.getText().replace("<%=", "").replace("%>", "").trim(), chunk.getBeginLine());
	}

}
