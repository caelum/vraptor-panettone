package br.com.caelum.vraptor.panettone;

import static java.util.stream.Collectors.toList;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class DefaultImportFile {

	private final File defaults;

	public DefaultImportFile(File from) {
		defaults = new File(from, "tone.defaults");
	}

	public List<String> getImports() {
		if (!defaults.exists())
			return new ArrayList<>();
		return parse(defaults);
	}

	private List<String> parse(File defaults) {
		try {
			return Files.lines(defaults.toPath())
				.filter(this::isImport)
				.map(this::extractPackageFromImport)
				.collect(toList());
		} catch (IOException e) {
			throw new RuntimeException("Unable to read defaults " + defaults.getAbsolutePath());
		}
	}

	private boolean isImport(String l) {
		return l.startsWith("import ");
	}

	private String extractPackageFromImport(String l) {
		return l.substring("import ".length()).trim();
	}

}
