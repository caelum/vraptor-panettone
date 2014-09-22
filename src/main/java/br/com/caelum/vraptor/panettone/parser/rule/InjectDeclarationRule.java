package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.Regexes;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.InjectDeclarationNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class InjectDeclarationRule extends Rule {

	protected Pattern pattern() {
		Pattern p = Pattern.compile(
				"\\(@inject " + 
				Regexes.SPACE + 
				Regexes.CLASS_NAME + 
				Regexes.SPACE + 
				Regexes.GENERICS +
				Regexes.SPACE + 
				"[\\w\\_]+" +
				Regexes.SPACE+
				"\\)");
		return p;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		String type = chunk.getText().split(" ")[1].trim();
		String name = chunk.getText().split(" ")[2].replace(")", "").trim();
		
		return new InjectDeclarationNode(type, name, chunk.getBeginLine());
	}

}
