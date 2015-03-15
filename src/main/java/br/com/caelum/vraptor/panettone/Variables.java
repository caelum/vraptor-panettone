package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.asList;
import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Variables {
	private final List<Variable> variables = new ArrayList<>();

	public void add(Variable... variables) {
		this.variables.addAll(asList(variables));
	}
	
	public void add(String type, String name) {
		variables.add(new Variable(type,name));
	}

	public String asParametersCall() {
		return variables.stream().map(Variable::getName).collect(joining(","));
	}

	public List<Variable> getContent() {
		return unmodifiableList(variables);
	}

	public List<String> asMethodDefinition() {
		return variables.stream().map(Variable::toDefinition).collect(toList());
	}

	public String asFullMethodDefinition() {
		return asMethodDefinition().stream().collect(joining(","));
	}

	public String getNonGenericTypeList() {
		return variables.stream().map(Variable::getNonGenericTypeClass).collect(joining(","));
	}
	
	public boolean isEmpty() {
		return variables.isEmpty();
	}

	public String asDefinitions() {
		return allAs(Variable::toPrivateDefinition);
	}

	public String asSettingThem() {
		return allAs(Variable::toAttribution);
	}

	public String asInjectDefinitions() {
		return allAs(Variable::toInjectDefinition);
	}

	private String allAs(Function<Variable, String> attribute) {
		return variables.stream().map(attribute).collect(joining("\n")) + "\n";
	}

	public String asNonFinalDefinitions() {
		return allAs(Variable::toPrivateNonFinalDefinition);
	}

	public String asSetters(String returnType) {
		return allAs(v -> v.toSetter(returnType));
	}

	public Variables except(String name) {
		Variables copy = new Variables();
		variables.stream().filter(v -> !v.getName().equals(name)).forEach(copy::add);
		return copy;
	}

}
