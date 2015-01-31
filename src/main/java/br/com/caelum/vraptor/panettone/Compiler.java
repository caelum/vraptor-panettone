package br.com.caelum.vraptor.panettone;

import static br.com.caelum.vraptor.panettone.VRaptorCompiler.VIEW_INPUT;
import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.nio.file.Files.copy;
import static java.nio.file.Files.walk;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static java.util.Arrays.stream;
import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class Compiler {

	private final File templates, classes;
	private Watcher watcher;
	private final List<String> imports;
	private final CompilationListener[] listeners;
	private final PrintStream out = System.out;
	private final PrintStream err = System.err;
	
	private final CompiledInterfaces interfaces;

	public Compiler(File from, File to) {
		this(from, to, to, new ArrayList<String>());
	}
	
	public Compiler(File templates, File classes, File interfaces, List<String> imports, CompilationListener... listeners) {
		this.templates = templates;
		this.classes = classes;
		this.interfaces = new CompiledInterfaces(interfaces);
		this.imports = new ArrayList<>(imports);
		Config config = new Config(templates);
		this.imports.addAll(config.getImports());
		this.listeners = config.getListenersOr(listeners);
		setup();
	}

	private void setup() {
		templates.mkdirs();
		classes.mkdirs();
		File loader = new File(classes, "br/com/caelum/vraptor/panettone/PanettoneLoader.java");
		InputStream input = Compiler.class.getResourceAsStream("/PanettoneLoader.java.template");
		try {
			copy(input, loader.toPath(), REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace(err);
		}
	}

	public List<Exception> compileAll() {
		List<File> files = tonesAt(templates);
		long start = currentTimeMillis();
		out.println("Compiling " + files.size() + " files...");
		List<Exception> exceptions = files.stream()
			.map(this::compile)
			.filter(Optional::isPresent)
			.map(Optional::get)
			.collect(toList());
		long finish = currentTimeMillis();
		double delta = (finish - start) / 1000.0;
		if(!exceptions.isEmpty()) {
			err.println("Precompilation failed");
		}
		out.println(format("Finished in %.2f secs", delta));
		return exceptions;
	}

	public Optional<Exception> compile(File f) {
		try (Reader reader = openAsUtf8(f)){
			Template template = new Template(reader, this.listeners);
			String name = noExtension(nameFor(f));
			String typeName = name.replaceAll(".+/", "");
			String content = template.renderType(typeName);
			CompiledTemplate compiled = new CompiledTemplate(classes, name, imports, content, listeners);
			invokeOn(listeners, l-> l.finished(f, compiled));
			
			String method = template.renderInterface(typeName);
			interfaces.generate(name, imports, method, listeners);
			
			return empty();
		} catch (Exception e) {
			invokeOn(listeners, l -> l.finished(f, e));
			return of(e);
		}
	}

	private InputStreamReader openAsUtf8(File f) throws UnsupportedEncodingException, FileNotFoundException {
		return new InputStreamReader(new FileInputStream(f), "UTF-8");
	}

	private void invokeOn(CompilationListener[] listeners, Consumer<CompilationListener> listener) {
		stream(listeners).forEach(listener);
	}

	private String nameFor(File f) {
		String replaced = f.getAbsolutePath().replace(templates.getAbsolutePath(), "");
		if(replaced.startsWith("/")) return replaced.substring(1);
		return replaced;
	}

	private List<File> tonesAt(File currentDir) {
		try {
			List<File> tones = Files.walk(currentDir.toPath(), 10)
					.map(Path::toFile)
					.filter(this::isTone)
					.collect(toList());
			return tones.stream().filter(File::isFile).collect(toList());
		} catch (IOException e) {
			throw new CompilationIOException(e);
		}
	}
	
	private boolean isTone(File p) {
		return p.isDirectory() ||  p.getName().endsWith(".tone") || p.getName().contains(".tone.");
	}

	private String noExtension(String name) {
		return name.replaceAll("\\.tone.*", "");
	}

	/**
	 * Watches the base directory for any file changes.
	 */
	public void startWatch() {
		this.watcher = new Watcher(templates.toPath(), this);
		Thread t = new Thread(watcher);
		t.setDaemon(true);
		t.start();
	}

	/**
	 * Stops watching the base directory for file changes.
	 */
	public void stopWatch() {
		watcher.stop();
	}

	/**
	 * Compiles everything, throwing an exception if failing.
	 */
	public void compileAllOrError() {
		List<Exception> exceptions = compileAll();
		if(!exceptions.isEmpty()) throw new CompilationIOException(exceptions);
	}

	/**
	 * Removes the output folder and everything within it.
	 */
	public void clear() {
		out.println("Clearing compilation path...");
		invokeOn(listeners, l -> l.clear());
		clearChildren(classes);
	}

	private void clearChildren(File current) {
		try {
			walk(current.toPath()).map(Path::toFile).forEach(File::delete);
		} catch (IOException e) {
			err.println("Unable to clear folders: " + current.getAbsolutePath() + " due to " + e.getMessage());
		}
	}

	/**
	 * Removes the java version of a tone file. It will not remove any subclass that
	 * the tone user might have created.
	 */
	public void removeJavaVersionOf(String path) {
		int position = path.indexOf(VIEW_INPUT);
		path = path.substring(position + VIEW_INPUT.length() + 1);
		String java = "templates/" + path.replaceAll("\\.tone.*", "\\.java");
		new File(classes, java).delete();
	}

}
