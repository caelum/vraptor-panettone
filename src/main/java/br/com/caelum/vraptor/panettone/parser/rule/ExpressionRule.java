package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.ExpressionNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class ExpressionRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();
		
		// complicated? 
		// read comments on PrintVariableRule (same regex)
		Pattern p = Pattern.compile("@\\{\\s*((\\w)+((\\.)|(\\['?\"?)|('?\"?\\]\\.?)|(\\('?\"?\\)?)|(\\s*,\\s*)|('?\"?\\)\\.?))?)+\\s*\\}");
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			String matched = matcher.group();
			chunks.add(new TextChunk(matched, sc.lineBegin(matched)));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		String chunkWithNoBegin = chunk.getText().trim().substring(2);
		String finalChunk = chunkWithNoBegin.substring(0, chunkWithNoBegin.length()-1);
		return new ExpressionNode(finalChunk.trim(), chunk.getBeginLine());
	}

}
