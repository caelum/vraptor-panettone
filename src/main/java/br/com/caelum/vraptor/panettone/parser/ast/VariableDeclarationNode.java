package br.com.caelum.vraptor.panettone.parser.ast;


public class VariableDeclarationNode implements Node {

	private String type;
	private String name;
	private String defaultValue;

	public VariableDeclarationNode(String type, String name) {
		this(type, name, null);
	}
	
	public VariableDeclarationNode(String type, String name, String defaultValue) {
		this.type = type;
		this.name = name;
		this.defaultValue = defaultValue;
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

	public String getDefaultValue() {
		return defaultValue;
	}
	
}
