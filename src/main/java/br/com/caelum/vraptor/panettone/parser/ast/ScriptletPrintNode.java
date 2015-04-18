package br.com.caelum.vraptor.panettone.parser.ast;


public class ScriptletPrintNode extends Node {

	private final String expr;
	private boolean rawText;

	public ScriptletPrintNode(String expr, int beginLine, boolean rawText) {
		super(beginLine);
		this.expr = expr;
		this.rawText = rawText;
	}
	
	public String getExpr() {
		return expr;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitScriptletPrint(this);
	}

	public boolean isRawText() {
		return rawText;
	}

}
