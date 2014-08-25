package br.com.caelum.vraptor.panettone.parser.ast;


public class HTMLNode implements Node {

	private String html;
	public HTMLNode(String html) {
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
