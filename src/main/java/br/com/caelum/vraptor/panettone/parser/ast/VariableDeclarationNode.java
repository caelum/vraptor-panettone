package br.com.caelum.vraptor.panettone.parser.ast;


public class VariableDeclarationNode extends Node {

	private String type;
	private String name;
	private String defaultValue;

	public VariableDeclarationNode(String type, String name, int beginLine) {
		this(type, name, null, beginLine);
	}
	
	public VariableDeclarationNode(String type, String name, String defaultValue, int beginLine) {
		super(beginLine);
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
