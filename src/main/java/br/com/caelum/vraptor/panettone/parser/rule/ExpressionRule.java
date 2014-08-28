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
		
		Pattern p = Pattern.compile("@\\{((\\w)+((\\.)|(\\['?\"?)|('?\"?\\]\\.?)|(\\('?\"?\\)?)|(\\s*,\\s*)|('?\"?\\)\\.?))?)+\\}");
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			chunks.add(new TextChunk(matcher.group()));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		String chunkWithNoBegin = chunk.getText().trim().substring(2);
		String finalChunk = chunkWithNoBegin.substring(0, chunkWithNoBegin.length()-1);
		return new ExpressionNode(finalChunk.trim());
	}

}
