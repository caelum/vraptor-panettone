package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;

import java.util.List;

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

	public String toPrivateDefinition() {
		return "private final " + type + " " + name + ";";
	}

	public String toPrivateNonFinalDefinition() {
		return "private " + type + " " + name + ";";
	}

	public String toInjectDefinition() {
		return "private @javax.inject.Inject " + type + " " + name + ";";
	}

	public String toAttribution() {
		return "this." + name + " = " + name + ";";
	}

	public String getType() {
		return type;
	}

	public String getTypeClass() {
		return type + ".class";
	}

	public String getNonGenericTypeClass() {
		if (type.contains("<")) {
			return type.substring(0, type.indexOf("<")) + ".class";
		}
		return type + ".class";
	}

	public String getName() {
		return name;
	}
	
	static final List<String> WRAPPERS = asList(new String[]{"Integer", "Boolean", "Double", "Long", "Byte", "Short", "Float"});
	
	public String toSetter(String returnType) {
		String code = "public " + returnType + " " + name + "("+ type + " " + name +") { " + toAttribution() +  " return this; }\n";
		
		if (!type.equals("String") && WRAPPERS.contains(type)) {
			code += "public " + returnType + " " + name + "(String " + name +") { this."+name+" = " + type + ".valueOf(" + name + "); return this; }\n";
		}
		return code;

	}

}
