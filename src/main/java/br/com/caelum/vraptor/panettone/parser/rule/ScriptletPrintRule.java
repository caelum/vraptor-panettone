package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ScriptletPrintNode;

public class ScriptletPrintRule extends Rule {

	protected Pattern pattern() {
		String pattern = "(<%=\\s*.*?\\s*%>)";
		
		Pattern p = Pattern.compile(pattern);
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new ScriptletPrintNode(chunk.getText().replace("<%=", "").replace("%>", "").trim(), chunk.getBeginLine());
	}

}
