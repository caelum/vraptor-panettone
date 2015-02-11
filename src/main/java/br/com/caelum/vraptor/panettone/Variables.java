package br.com.caelum.vraptor.panettone;

import static java.util.Collections.unmodifiableList;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Variables {
	private final Map<String,String> types = new HashMap<>();
	private final List<Variable> variables = new ArrayList<>();

	public void add(String type, String name) {
		types.put(name, type);
		variables.add(new Variable(type,name));
	}

	public String asParametersCall() {
		return types.keySet().stream().collect(joining(","));
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

}
