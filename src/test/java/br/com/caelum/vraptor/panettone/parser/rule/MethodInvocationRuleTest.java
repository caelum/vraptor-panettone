package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;

public class MethodInvocationRuleTest {

	private MethodInvocationRule rule;
	
	@Before
	public void setUp() {
		rule = new MethodInvocationRule();
	}
	
	@Test
	public void shouldAcceptMethodInvocations() {
		SourceCode sc = new SourceCode("@v1() @v1.v2_x()");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@v1()", chunks.get(0).getText());
		Assert.assertEquals("@v1.v2_x()", chunks.get(1).getText());
	}

	@Test
	public void shouldAcceptMethodInvocationsWithBrackets() {
		SourceCode sc = new SourceCode("@v1['bla'].method()");
		
		List<TextChunk> chunks = rule.getChunks(sc);
		Assert.assertEquals("@v1['bla'].method()", chunks.get(0).getText());
	}
}
