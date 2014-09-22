package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletNode;

public class ScriptletRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		throw new RuntimeException("ScriptletRule should not be executed");
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletNode(chunk.getText(), chunk.getBeginLine());
	}

}
