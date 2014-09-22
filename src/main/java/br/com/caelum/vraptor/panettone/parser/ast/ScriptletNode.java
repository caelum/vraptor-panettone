package br.com.caelum.vraptor.panettone.parser.ast;


public class ScriptletNode extends Node {

	private String scriptlet;
	public ScriptletNode(String scriptlet, int beginLine) {
		super(beginLine);
		this.scriptlet = scriptlet;
	}
	
	@Override
	public void accept(ASTWalker walker) {
		walker.visitScriptlet(this);
	}

	public String getScriptlet() {
		return scriptlet;
	}
}
