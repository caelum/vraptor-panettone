package br.com.caelum.vraptor.panettone.parser.ast;


public class CommentNode implements Node {

	private String comment;

	public CommentNode(String comment) {
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
