package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRule extends Rule {

	private static final String START_SCRIPTLET_PRINT_RAW = "<%==";
	private static final Pattern SCRIPTLET_PRINT = Pattern.compile("(<%=\\s*.*?\\s*%>)");

	protected Pattern pattern() {
		return SCRIPTLET_PRINT;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		boolean printAsRaw = chunk.getText().startsWith(START_SCRIPTLET_PRINT_RAW);
		String cleanChunk = chunk.getText().replace(printAsRaw ? START_SCRIPTLET_PRINT_RAW : "<%=", "").replace("%>", "").trim();
		return new ScriptletPrintNode(cleanChunk, chunk.getBeginLine(), printAsRaw);
	}

}
