package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import br.com.caelum.vraptor.panettone.parser.RuleChunk;
import br.com.caelum.vraptor.panettone.parser.SourceCode;

public class RuleExtractor {

	public List<RuleChunk> extract(SourceCode sc) {
		List<RuleChunk> chunks = new ArrayList<RuleChunk>();
		
		Pattern p = Pattern.compile("\\(### \\w* \\d* ###\\)");
		Matcher matcher = p.matcher(sc.getSource());
		
		while(matcher.find()) {
			chunks.add(new RuleChunk(matcher.group()));
		}
		
		return chunks;
	}

}
