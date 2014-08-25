package br.com.caelum.vraptor.panettone.parser.ast;


public class MethodInvocationNode implements Node {

	private String expr;

	public MethodInvocationNode(String expr) {
		this.expr = expr;
	}
	
	public String getExpr() {
		return expr;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitMethodInvocation(this);
	}
}
