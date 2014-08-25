package br.com.caelum.vraptor.panettone.parser;

import java.util.HashMap;
import java.util.Map;

import br.com.caelum.vraptor.panettone.parser.rule.Rules;
import static br.com.caelum.vraptor.panettone.parser.Tokens.RULECHUNK_END;
import static br.com.caelum.vraptor.panettone.parser.Tokens.RULECHUNK_START;
import static br.com.caelum.vraptor.panettone.parser.Tokens.RULECHUNK_START_REGEX;
import static br.com.caelum.vraptor.panettone.parser.Tokens.SCRIPTLET_END;
import static br.com.caelum.vraptor.panettone.parser.Tokens.SCRIPTLET_START;

public class SourceCode {

	private String source;
	private Map<Integer, TextChunk> extractedChunks;
	private int counter = 0;

	public SourceCode(String source) {
		this.source = source;
		extractedChunks = new HashMap<Integer, TextChunk>();
	}
	
	public String getSource() {
		return source;
	}

	public void transform(TextChunk chunk, Rules aRule) {
		addChunk(chunk);
		source = source.replace(chunk.getText(), 
				RULECHUNK_START + " " + aRule.name() + " " + counter + " " + RULECHUNK_END);
	}
	
	public TextChunk getTextChunk(int number) {
		return extractedChunks.get(number);
	}

	
	public void transformHtmlAndScriptlet() {
		StringBuilder newSourceCode = new StringBuilder();
		
		String[] lines = source.split(RULECHUNK_START_REGEX);
		
		for(String line : lines) {
			String trimmedLine = line.trim();
			if(trimmedLine.isEmpty()) continue;
			
			if(trimmedLine.endsWith(RULECHUNK_END)) {
				newSourceCode.append(RULECHUNK_START + " " + trimmedLine);
			} 
			else if(trimmedLine.contains(RULECHUNK_END)) {
				String firstPart = trimmedLine.substring(0, trimmedLine.indexOf(RULECHUNK_END)).trim();
				newSourceCode.append(RULECHUNK_START + " " + firstPart + " " + RULECHUNK_END);

				String secondPart = trimmedLine.substring(trimmedLine.indexOf(RULECHUNK_END)+4);
				htmlOrScriptlet(newSourceCode, secondPart);

			}
			else {
				htmlOrScriptlet(newSourceCode, trimmedLine);
			}
		}

		source = newSourceCode.toString();
	}

	private void htmlOrScriptlet(StringBuilder newSourceCode, String chunk) {
		
		String trimmedChunk = chunk;
		if(chunk.trim().startsWith(SCRIPTLET_START)) {
			
			String justScriptlet = trimmedChunk.substring(2);
			int endOfTheScriptlet = justScriptlet.indexOf(SCRIPTLET_END); // be careful, an "%>" would break it
			justScriptlet = justScriptlet.substring(0, endOfTheScriptlet);

			addChunk(new TextChunk(justScriptlet));
			newSourceCode.append(RULECHUNK_START + " " + Rules.scriptletRuleName() + " " + counter + " " + RULECHUNK_END);
			
			if(trimmedChunk.length() > trimmedChunk.indexOf(SCRIPTLET_END) +2) {
				String theRestOfTheChunk = trimmedChunk.substring(trimmedChunk.indexOf(SCRIPTLET_END)+2);
				htmlOrScriptlet(newSourceCode, theRestOfTheChunk);
			}
		} else {

			String justHTML = trimmedChunk;
			int startOfScriptlet = justHTML.indexOf(SCRIPTLET_START);
			justHTML = justHTML.substring(0, startOfScriptlet == -1 ? justHTML.length() : startOfScriptlet);
			
			addChunk(new TextChunk(justHTML));
			newSourceCode.append(RULECHUNK_START + " " + Rules.htmlRuleName() + " " + counter + " " + RULECHUNK_END);
			
			if(startOfScriptlet > -1) {
				String theRestOfTheChunk = trimmedChunk.substring(trimmedChunk.indexOf(SCRIPTLET_START));
				htmlOrScriptlet(newSourceCode, theRestOfTheChunk);
			}
			
		}
	}
	
	private void addChunk(TextChunk chunk) {
		counter++;
		extractedChunks.put(counter, chunk);
	}
}
