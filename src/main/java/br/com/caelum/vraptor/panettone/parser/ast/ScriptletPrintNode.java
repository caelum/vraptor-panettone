package br.com.caelum.vraptor.panettone.parser.ast;


public class ScriptletPrintNode extends Node {

	private String expr;

	public ScriptletPrintNode(String expr, int beginLine) {
		super(beginLine);
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
