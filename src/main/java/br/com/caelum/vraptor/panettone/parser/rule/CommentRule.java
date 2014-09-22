package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.CommentNode;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public class CommentRule extends Rule {

	protected Pattern pattern() {
		return Pattern.compile("@--(.*)--@");
	}
	
	@Override
	public Node getNode(TextChunk chunk) {
		String comment = chunk.getText().substring(3);
		comment = comment.substring(0, comment.length()-3);
		
		return new CommentNode(comment, chunk.getBeginLine());
	}

}
