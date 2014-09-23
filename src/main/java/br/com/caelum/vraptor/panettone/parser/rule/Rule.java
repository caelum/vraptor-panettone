package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public abstract class Rule {

	public final List<TextChunk> getChunks(SourceCode sc) {
		List<TextChunk> chunks = new ArrayList<>();

		Pattern p = pattern();
		Matcher matcher = p.matcher(sc.getSource());

		while (matcher.find()) {
			String matched = parseMatched(matcher.group());
			chunks.add(new TextChunk(matched, sc.lineBegin(matched)));
		}

		return chunks;
	}

	protected String parseMatched(String matched) {
		return matched;
	}

	protected abstract Pattern pattern();

	public abstract Node getNode(TextChunk chunk);

}
