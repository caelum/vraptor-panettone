package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRule extends Rule {

	private static final Pattern SCRIPTLET_PRINT = Pattern.compile("(<%=\\s*.*?\\s*%>)");

	protected Pattern pattern() {
		return SCRIPTLET_PRINT;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletPrintNode(chunk.getText().replace("<%=", "").replace("%>", "").trim(), chunk.getBeginLine());
	}

}
