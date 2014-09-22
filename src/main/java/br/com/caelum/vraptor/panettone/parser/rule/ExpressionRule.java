package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.ExpressionNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class ExpressionRule extends Rule {

	protected Pattern pattern() {
		Pattern p = Pattern.compile("@\\{\\s*((\\w)+((\\.)|(\\['?\"?)|('?\"?\\]\\.?)|(\\('?\"?\\)?)|(\\s*,\\s*)|('?\"?\\)\\.?))?)+\\s*\\}");
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		String chunkWithNoBegin = chunk.getText().trim().substring(2);
		String finalChunk = chunkWithNoBegin.substring(0, chunkWithNoBegin.length()-1);
		return new ExpressionNode(finalChunk.trim(), chunk.getBeginLine());
	}

}
