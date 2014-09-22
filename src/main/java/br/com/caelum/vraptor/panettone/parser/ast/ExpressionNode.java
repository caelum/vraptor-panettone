package br.com.caelum.vraptor.panettone.parser.ast;


public class ExpressionNode extends Node {

	private String expr;

	public ExpressionNode(String expr, int beginLine) {
		super(beginLine);
		this.expr = expr;
	}
	
	public String getExpr() {
		return expr;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitExpression(this);
	}
}
