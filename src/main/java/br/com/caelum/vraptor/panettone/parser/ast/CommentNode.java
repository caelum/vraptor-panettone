package br.com.caelum.vraptor.panettone.parser.ast;


public class CommentNode extends Node {

	private String comment;

	public CommentNode(String comment, int beginLine) {
		super(beginLine);
		this.comment = comment;
	}
	
	@Override
	public void accept(ASTWalker walker) {
		walker.visitComment(this);
	}

	public String getComment() {
		return comment;
	}
}
