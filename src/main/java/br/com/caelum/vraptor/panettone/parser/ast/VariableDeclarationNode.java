package br.com.caelum.vraptor.panettone.parser.ast;


public class VariableDeclarationNode implements Node {

	private String type;
	private String name;

	public VariableDeclarationNode(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String getType() {
		return type;
	}
	
	public String getName() {
		return name;
	}

	@Override
	public void accept(ASTWalker walker) {
		walker.visitVariableDeclaration(this);
		
	}
}
