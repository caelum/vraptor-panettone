package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.Regexes;
import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;
import br.com.caelum.vraptor.panettone.parser.ast.ReusableVariableNode;

public class ReusableVariableRule implements Rule {

	@Override
	public List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<TextChunk>();
		
		String pattern = "@\\{\\{" + Regexes.CLASS_NAME + "\\n([^@\\}\\}])*\\n@\\}\\}\\n";
		
		Pattern p = Pattern.compile(pattern, Pattern.DOTALL);
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			String matched = matcher.group();
			chunks.add(new TextChunk(matched, sc.lineBegin(matched)));
		}
		
		return chunks;
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
