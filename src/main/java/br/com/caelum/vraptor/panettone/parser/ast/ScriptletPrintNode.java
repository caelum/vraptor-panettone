package br.com.caelum.vraptor.panettone.parser.ast;


public class ScriptletPrintNode implements Node {

	private String expr;

	public ScriptletPrintNode(String expr) {
		this.expr = expr;
	}
	
	public String getExpr() {
		return expr;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitScriptletPrint(this);
	}
}
