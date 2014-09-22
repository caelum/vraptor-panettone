package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.Regexes;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;

public class ReusableVariableRule extends Rule {

	protected Pattern pattern() {
		String pattern = "@\\{\\{" + Regexes.CLASS_NAME + "\\n([^@\\}\\}])*\\n@\\}\\}\\n";
		
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		int firstLine = chunk.getText().indexOf("\n");
		
		String varName = chunk.getText().substring(3, firstLine);
		String varContent = chunk.getText().substring(firstLine);
		varContent = varContent.substring(1, varContent.length()-3);
		
		return new ReusableVariableNode(varName, varContent, chunk.getBeginLine());
	}

}
