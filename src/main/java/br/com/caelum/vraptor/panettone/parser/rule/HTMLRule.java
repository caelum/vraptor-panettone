package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.HTMLNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class HTMLRule extends Rule {

	protected Pattern pattern() {
		throw new RuntimeException("HTMLRule should not be executed");
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new HTMLNode(chunk.getText(), chunk.getBeginLine());
	}

}
