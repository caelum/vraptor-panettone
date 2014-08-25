package br.com.caelum.vraptor.panettone.parser.ast;


public interface Node {

	void accept(ASTWalker walker);

}
