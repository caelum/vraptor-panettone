package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.Stack;
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
		String text = chunk.getText()
					.replace("@inject", "")
					.replace("(", "")
					.replace(")", "")
					.trim();
		
		String type = "";
		String name = "";
		
		boolean readingType = true;
		StringBuilder word = new StringBuilder();
		Stack<String> generics = new Stack<String>();
		
		for(int i = 0; i < text.length(); i++) {
			
			char current = text.charAt(i);
			
			boolean isPartOfTheWord = !(current == ' ' && generics.isEmpty());
			if(isPartOfTheWord) {
				if(current == '<') generics.push("<");
				if(current == '>') generics.pop();
				word.append(current);
			}
			else {
				if(readingType) type = word.toString();
				readingType = false;
				word = new StringBuilder();
			}
		}
		
		name = word.toString();
		
		return new InjectDeclarationNode(type, name, chunk.getBeginLine());
	}


}
