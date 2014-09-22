package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.HTMLNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class HTMLRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		throw new RuntimeException("HTMLRule should not be executed");
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new HTMLNode(chunk.getText(), chunk.getBeginLine());
	}

}
