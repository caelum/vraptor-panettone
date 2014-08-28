package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.PrintVariableNode;

public class PrintVariableRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();

		// complicated, uh?!
		// basically: @(word exception)+
		// just notice the "ORs |", they match cases like (, (', (", [", and so on
		String x = "@((\\w)+((\\.)|(\\['?\"?)|('?\"?\\]\\.?)|(\\('?\"?\\)?)|(\\s*,\\s*)|('?\"?\\)\\.?))?)+";
		
		Pattern p = Pattern.compile(x);
		Matcher matcher = p.matcher(sc.getSource());
		
		
		while(matcher.find()) {
			chunks.add(new TextChunk(matcher.group().trim()));
		}
		
		return chunks;
	}

	@Override
	public Node getNode(TextChunk chunk) {
		return new PrintVariableNode(chunk.getText().replace("@", "").trim());
	}

}
