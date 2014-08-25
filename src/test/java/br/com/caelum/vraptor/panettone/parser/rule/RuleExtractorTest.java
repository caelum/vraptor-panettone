package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.RuleChunk;
import br.com.caelum.vraptor.panettone.parser.SourceCode;

public class RuleExtractorTest {


	private RuleExtractor rule;
	
	@Before
	public void setUp() {
		rule = new RuleExtractor();
	}
	
	@Test
	public void shouldGetChunks() {
		SourceCode sc = new SourceCode(
				"(### REGRA1 1 ###)"+
				"(### REGRA2 20 ###)"+
				"(### REGRA3 456 ###)"
		);
		
		List<RuleChunk> chunks = rule.extract(sc);
		Assert.assertEquals(3, chunks.size());
		Assert.assertEquals("(### REGRA1 1 ###)", chunks.get(0).getText());
		Assert.assertEquals("(### REGRA2 20 ###)", chunks.get(1).getText());
		Assert.assertEquals("(### REGRA3 456 ###)", chunks.get(2).getText());
		
	}
}
