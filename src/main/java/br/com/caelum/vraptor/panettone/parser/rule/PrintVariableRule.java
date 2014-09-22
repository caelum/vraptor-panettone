package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;

public class PrintVariableRule extends Rule {

	protected Pattern pattern() {
		// complicated, uh?!
		// basically: @(word exception)+
		// just notice the "ORs |", they match cases like (, (', (", [", and so on
		String x = "@((\\w)+((\\.)|(\\['?\"?)|('?\"?\\]\\.?)|(\\('?\"?\\)?)|(\\s*,\\s*)|('?\"?\\)\\.?))?)+";
		
		Pattern p = Pattern.compile(x);
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new PrintVariableNode(chunk.getText().replace("@", "").trim(), chunk.getBeginLine());
	}

}
