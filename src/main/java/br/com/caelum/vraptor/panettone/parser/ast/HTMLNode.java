package br.com.caelum.vraptor.panettone.parser.ast;


public class HTMLNode extends Node {

	private String html;
	public HTMLNode(String html, int beginLine) {
		super(beginLine);
		this.html = html;
	}
	
	@Override
	public void accept(ASTWalker walker) {
		walker.visitHTML(this);
	}

	public String getHtml() {
		return html;
	}
}
