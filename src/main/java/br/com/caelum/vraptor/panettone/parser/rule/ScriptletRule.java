package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletNode;

public class ScriptletRule extends Rule {

	@Override
	public Pattern pattern() {
		throw new RuntimeException("ScriptletRule should not be executed");
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletNode(chunk.getText(), chunk.getBeginLine());
	}

}
