package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
		from.mkdirs();
		to.mkdirs();
		this.watcher = new Watcher(from.toPath(), this);
	}

	public List<Exception> compileAll() {
		List<File> files = tonesAt(from);
		System.out.println("Compiling " + files.size() + " files: " + files);
		List<Exception> exceptions = new ArrayList<>();
		List<CompiledTemplate> toCompile = new ArrayList<>();
		for(File f : files) {
			try(FileReader reader = new FileReader(f)) {
				Template template = new Template(reader);
				String name = noExtension(nameFor(f));
				CompiledTemplate compiled = new CompiledTemplate(to, name, imports, template.renderType());
				toCompile.add(compiled);
			} catch (IOException | CompilationLoadException | CompilationIOException e) {
				exceptions.add(e);
			}
		}
		SimpleJavaCompiler compiler = new SimpleJavaCompiler(to);
		Stream<File> filesToCompile = toCompile.stream().map(CompiledTemplate::getFile);
		try {
			compiler.compile(filesToCompile);
		} catch (CompilationLoadException | CompilationIOException e) {
			exceptions.add(e);
		}
		if(exceptions.isEmpty()) {
			for(CompiledTemplate template : toCompile) {
				types.put(template.getPackagedName(), compiler.load(template));
			}
		}
		return exceptions;
	}
	
	private String nameFor(File f) {
		String replaced = f.getAbsolutePath().replace(from.getAbsolutePath(), "");
		if(replaced.startsWith("/")) return replaced.substring(1);
		return replaced;
	}

	private List<File> tonesAt(File currentDir) {
		try {
			List<File> tones = Files.walk(currentDir.toPath(), 10)
					.map(Path::toFile)
					.filter(p -> p.isDirectory() ||  p.getName().endsWith(".tone"))
					.collect(Collectors.toList());
			
			return tones.stream().filter(File::isFile).collect(toList());
		} catch (IOException e) {
			throw new CompilationIOException(e);
		}
	}

	private String noExtension(String name) {
		return name.substring(0, name.lastIndexOf("."));
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

	public void compileAllOrError() {
		List<Exception> exceptions = compileAll();
		if(!exceptions.isEmpty()) throw new CompilationIOException(exceptions);
	}

}
