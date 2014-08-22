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
import java.util.Optional;
import java.util.stream.Collectors;

public class Compiler {

	private final File from;
	private final File to;
	private final Map<String, Class<?>> types = new HashMap<>();
	private final Watcher watcher;
	private final List<String> imports;
	private final CompilationListener[] listeners;

	public Compiler(File from, File to) {
		this(from, to, new ArrayList<String>());
	}
	
	public Compiler(File from, File to, List<String> imports, CompilationListener... listeners) {
		this.from = from;
		this.to = to;
		this.imports = new ArrayList<>(imports);
		this.listeners = listeners;
		from.mkdirs();
		to.mkdirs();
		this.watcher = new Watcher(from.toPath(), this);
		File defaults = new File(from, "tone.defaults");
		if(defaults.exists()) {
			parse(defaults);
		}
	}

	private void parse(File defaults) {
		try {
			Files.lines(defaults.toPath())
				.filter(l -> l.startsWith("import "))
				.map(l -> l.substring("import ".length()).trim())
				.forEach(l -> this.imports.add(l));
		} catch (IOException e) {
			throw new RuntimeException("Unable to read defaults "+ defaults.getAbsolutePath());
		}
	}

	public List<Exception> compileAll() {
		return precompile();
	}

	private List<Exception> precompile() {
		List<File> files = tonesAt(from);
		long start = System.currentTimeMillis();
		System.out.println("Compiling " + files.size() + " files...");
		List<Exception> exceptions = files.stream()
			.map(this::compile)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(Collectors.toList());
		long finish = System.currentTimeMillis();
		double delta = (finish - start) / 1000.0;
		if(!exceptions.isEmpty()) {
			System.err.println(String.format("Precompilation failed in %.2f secs", delta));
		}
		return exceptions;
	}

	public Optional<Exception> compile(File f) {
		try(FileReader reader = new FileReader(f)) {
			Template template = new Template(reader);
			String name = noExtension(nameFor(f));
			new CompiledTemplate(to, name, imports, template.renderType(), listeners);
			return Optional.empty();
		} catch (Exception e) {
			return Optional.of(e);
		}
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
		Thread t = new Thread(watcher);
		t.setDaemon(true);
		t.start();
	}

	public void stop() {
		watcher.stop();
	}

	public void compileAllOrError() {
		List<Exception> exceptions = compileAll();
		if(!exceptions.isEmpty()) throw new CompilationIOException(exceptions);
	}

	public void clear() {
		System.out.println("Clearing compilation path...");
		clearChildren(to);
	}

	private void clearChildren(File current) {
		try {
			Files.walk(current.toPath()).forEach(p -> p.toFile().delete());
		} catch (IOException e) {
			System.out.println("Unable to clear folders: " + current.getAbsolutePath() + " due to " + e.getMessage());
		}
	}

	public void removeJavaVersionOf(String path) {
		int position = path.indexOf(VRaptorCompiler.VIEW_INPUT);
		path = path.substring(position + VRaptorCompiler.VIEW_INPUT.length() + 1);
		String java = "templates/" + path.replace(".tone", ".java");
		new File(to, java).delete();
	}

}
