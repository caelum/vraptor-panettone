package br.com.caelum.vraptor.panettone;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;

public class Compiler {

	private final File from;
	private final File to;
	private final Map<String, Class<?>> types = new HashMap<>();
	private final Watcher watcher;
	private final List<String> imports;

	public Compiler(File from, File to) {
		this(from, to, new ArrayList<String>());
	}
	public Compiler(File from, File to, List<String> imports) {
		this.from = from;
		this.to = to;
		this.imports = imports;
		this.watcher = new Watcher(from.toPath(), this);
	}

	public void compileAll() {
		List<File> files = stream(from.listFiles())
			.filter(matchesExtension()).collect(toList());
		List<Exception> exceptions = new ArrayList<>();
		for(File f : files) {
			try(FileReader reader = new FileReader(f)) {
				Template template = new Template(reader);
				String name = noExtension(f.getName());
				CompiledTemplate compiled = new CompiledTemplate(to, name, imports, template.renderType());
				types.put(f.getName(), compiled.getType());
			} catch (IOException | CompilationLoadException | CompilationIOException e) {
				exceptions.add(e);
			}
		}
		if(!exceptions.isEmpty()) {
			throw new CompilationIOException(exceptions);
		}
	}

	private String noExtension(String name) {
		return name.substring(0, name.lastIndexOf("."));
	}

	private Predicate<? super File> matchesExtension() {
		return f -> f.getName().endsWith(".tone");
	}

	public Class<?> get(String type) {
		if(!types.containsKey(type)) {
			throw new NullPointerException("Unable to find compiled " + type);
		}
		return types.get(type);
	}

	public void watch() {
		new Thread(watcher).start();
	}

	public void stop() {
		watcher.stop();
	}

}
