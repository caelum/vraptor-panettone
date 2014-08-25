package br.com.caelum.vraptor.panettone.parser.rule;

import java.util.List;

import br.com.caelum.vraptor.panettone.parser.SourceCode;
import br.com.caelum.vraptor.panettone.parser.TextChunk;
import br.com.caelum.vraptor.panettone.parser.ast.Node;

public interface Rule {

	List<TextChunk> getChunks(SourceCode sc);
	Node getNode(TextChunk chunk);
	
}
