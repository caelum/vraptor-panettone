package br.com.caelum.vraptor.panettone.parser.ast;


public class ScriptletPrintNode extends Node {

	private final String expr;
	private boolean shouldPrintAsRaw;

	public ScriptletPrintNode(String expr, int beginLine, boolean shouldPrintAsRaw) {
		super(beginLine);
		this.expr = expr;
		this.shouldPrintAsRaw = shouldPrintAsRaw;
	}
	
	public String getExpr() {
		return expr;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitScriptletPrint(this);
	}

	public boolean shouldPrintAsRaw() {
		return shouldPrintAsRaw;
	}

}
