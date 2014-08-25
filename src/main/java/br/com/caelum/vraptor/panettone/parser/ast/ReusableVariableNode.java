package br.com.caelum.vraptor.panettone.parser.ast;


public class ReusableVariableNode implements Node {

	private String content;
	private String name;

	public ReusableVariableNode(String name, String content) {
		this.name = name;
		this.content = content;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitReusableVariable(this);
	}
	
	public String getContent() {
		return content;
	}
	
	public String getName() {
		return name;
	}

}
