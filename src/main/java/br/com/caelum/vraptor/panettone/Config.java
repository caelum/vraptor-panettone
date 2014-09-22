package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

public class Config {

	private final File defaults;

	public Config(File from) {
		defaults = new File(from, "tone.defaults");
	}

	public List<String> getImports() {
		if (defaults.exists())
			return parse();
		return new ArrayList<>();
	}

	private List<String> parse() {
		return lines("import").collect(toList());
	}

	private Stream<String> lines(String prefix) {
		int len = prefix.length() + 1;
		try {
			return Files.lines(defaults.toPath())
					.filter(l -> l.startsWith(prefix + " "))
					.map(l -> l.substring(len).trim());
		} catch (IOException e) {
			throw new RuntimeException("Unable to read defaults " + defaults.getAbsolutePath());
		}
	}

	public CompilationListener[] getListenersOr(CompilationListener[] original) {
		CompilationListener[] overriden = lines("listener").map(this::instantiate).toArray(i -> new CompilationListener[i]);
		if(overriden.length==0) return original;
		return overriden;
	}

	private CompilationListener instantiate(String type) {
		try {
			return (CompilationListener) Class.forName(type).newInstance();
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
			throw new RuntimeException(e);
		}
	}

}
