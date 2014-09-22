package br.com.caelum.vraptor.panettone.parser.ast;


public abstract class Node {

	private int beginLine;

	public Node(int beginLine) {
		this.beginLine = beginLine;
	}
	
	public abstract void accept(ASTWalker walker);

	public int getBeginLine() {
		return beginLine;
	}
}
