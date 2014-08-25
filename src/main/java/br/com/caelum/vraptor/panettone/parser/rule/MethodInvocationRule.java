package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.MethodInvocationNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class MethodInvocationRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();
		
		String dot = "\\.";
		String brackets = "\\[\\]";
		String quotes = "\\\"";
		String underscore = "\\_";
		String simpleQuotes = "'";
		String specialChars = dot + brackets + quotes + simpleQuotes + underscore;
		Pattern p = Pattern.compile("@\\S(\\w|[" + specialChars + "])*\\(\\)");
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			chunks.add(new TextChunk(matcher.group()));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new MethodInvocationNode(chunk.getText().replace("@", "").trim());
	}

}
