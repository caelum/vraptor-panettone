package br.com.caelum.vraptor.panettone;

public class Variable {

	private final String type;
	private final String name;

	public Variable(String type, String name) {
		this.type = type;
		this.name = name;
	}
	
	public String toDefinition() {
		return type + " " + name;
	}

	public String getType() {
		return type;
	}

	public String getTypeClass() {
		return type + ".class";
	}

	public String getNonGenericTypeClass() {
		if(type.contains("<")) {
			return type.substring(0, type.indexOf("<")) + ".class"; 
		}
		return type + ".class";
	}

	public String getName() {
		return name;
	}



}
