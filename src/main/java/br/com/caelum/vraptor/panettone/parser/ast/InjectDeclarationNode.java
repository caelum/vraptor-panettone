package br.com.caelum.vraptor.panettone.parser.ast;


public class InjectDeclarationNode extends Node {

	private String type;
	private String name;

	public InjectDeclarationNode(String type, String name, int beginLine) {
		super(beginLine);
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
		walker.visitInjectDeclaration(this);
		
	}
}
