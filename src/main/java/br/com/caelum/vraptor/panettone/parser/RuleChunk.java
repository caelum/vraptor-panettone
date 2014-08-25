package br.com.caelum.vraptor.panettone.parser;

import br.com.caelum.vraptor.panettone.parser.rule.Rule;
import br.com.caelum.vraptor.panettone.parser.rule.Rules;

public class RuleChunk {

	private String text;

	public RuleChunk(String text) {
		this.text = text;
	}
	
	public String getText() {
		return text;
	}

	public int number() {
		return Integer.parseInt(text.split(" ")[2]);
	}
	
	public String ruleName() {
		return text.split(" ")[1];
	}
	
	public Rule getRule() {
		return Rules.byName(ruleName());
	}
}
