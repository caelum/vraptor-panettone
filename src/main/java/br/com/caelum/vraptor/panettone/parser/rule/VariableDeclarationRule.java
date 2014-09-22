package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.Regexes;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.VariableDeclarationNode;

public class VariableDeclarationRule extends Rule {

	protected Pattern pattern() {
		String possibleDefaultValue = "(\\s*=" + Regexes.SPACE + "\"?" + "(.*)" + "\"?)?";
		String pattern = 
				"\\(@" + 
				Regexes.SPACE + 
				Regexes.CLASS_NAME + 
				Regexes.SPACE + 
				Regexes.GENERICS + 
				Regexes.SPACE + 
				Regexes.CLASS_NAME + 
				possibleDefaultValue +
				Regexes.SPACE + 
				"\\)" + Regexes.SPACE + 
				"\\n";
		
		Pattern p = Pattern.compile(pattern, Pattern.MULTILINE);
		return p;
	}
	
	protected String parseMatched(String matched) {
		return matched.trim();
	}

	@Override
	public Node getNode(TextChunk chunk) {
		String cleanChunk = chunk.getText().replace("(@", "").replace(")", "").trim();
		String[] splittedCleanChunk = cleanChunk.split(" ");
		String type = splittedCleanChunk[0].trim();
		String name = splittedCleanChunk[1].trim();
		
		String value = null;
		if(chunk.getText().indexOf("=")>-1) {
			value = chunk.getText().substring(chunk.getText().indexOf("=")+1).trim();
			value = value.substring(0, value.length()-1);
		}
		
		return new VariableDeclarationNode(type, name, value, chunk.getBeginLine());
	}

}
