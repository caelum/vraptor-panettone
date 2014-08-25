package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;

public class PrintVariableRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();

		String variableName = "[\\w\\[\\]\"'\\_]+";
		String parameters = "(\\([\\w'\",\\s\\.]*\\))?";
		String dot = "(\\.)";
		Pattern p = Pattern.compile("@((" + variableName + parameters + ")" + dot + "?)+");
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			chunks.add(new TextChunk(matcher.group().trim()));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new PrintVariableNode(chunk.getText().replace("@", "").trim());
	}

}
