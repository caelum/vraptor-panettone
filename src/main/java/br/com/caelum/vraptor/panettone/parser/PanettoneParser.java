package br.com.caelum.vraptor.panettone.parser;

import java.util.List;

import br.com.caelum.vraptor.panettone.parser.ast.PannetoneAST;
import br.com.caelum.vraptor.panettone.parser.rule.Rule;
import br.com.caelum.vraptor.panettone.parser.rule.RuleExtractor;
import br.com.caelum.vraptor.panettone.parser.rule.Rules;

public class PanettoneParser {

	private RuleExtractor ruleExtractor;

	public PanettoneParser() {
		ruleExtractor = new RuleExtractor();
	}
	
	public PannetoneAST parse(String content) {
		SourceCode sc = new SourceCode(content);
		
		extractAllChunksBasedOnRules(sc);
		sc.transformHtmlAndScriptlet();
		
		return createAST(sc);
	}
	
	private PannetoneAST createAST(SourceCode sc) {
		List<RuleChunk> rules = ruleExtractor.extract(sc); 

		PannetoneAST ast = new PannetoneAST();
		for(RuleChunk ruleChunk : rules) {
			ast.createNode(ruleChunk, sc.getTextChunk(ruleChunk.number()));
		}
		return ast;
	}

	private void extractAllChunksBasedOnRules(SourceCode sc) {
		for(Rules aRule : Rules.rulesToExecute()) {
			Rule rule = aRule.getRule();
			
			List<TextChunk> chunks = rule.getChunks(sc);
			for(TextChunk c : chunks) {
				sc.transform(c, aRule);
			}
		}
	}
}
