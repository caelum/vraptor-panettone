package br.com.caelum.vraptor.panettone;

import static com.google.common.io.Files.write;
import static java.util.Arrays.stream;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

class FileIO {

	private final File sources;
	private final File targets;

	FileIO(File sources, File targets) {
		this.sources = sources;
		this.targets = targets;
	}

	void copy(String fileName, String content) {
		File file = new File(sources, fileName);
		copy(content, file);
	}

	void copy(String content, File file) {
		try {
			file.getParentFile().mkdirs();
			write(content, file, Charset.forName("UTF-8"));
		} catch (IOException e) {
			throw new RuntimeException("Unexpected exception ", e);
		}
	}

	public void mkclear() {
		sources.mkdirs();
		targets.mkdirs();
		clearAll();
	}

	private void clearAll() {
		clear(sources);
		clear(targets);
	}

	public void deleteAll() {
		clearAll();
		sources.delete();
		targets.delete();
	}

	private void clear(File dir) {
		try {
			List<String> internalDirs = Files.list(dir.toPath())
					.map(Path::toFile).filter(File::isDirectory)
					.map(File::getName).collect(Collectors.toList());
			internalDirs.remove("templates");
			internalDirs.remove("tone");
			if (!internalDirs.isEmpty()) {
				throw new RuntimeException(
						"I will not delete if there is recurssion, is there a problem here? @"
								+ internalDirs);
			}
			stream(dir.listFiles()).filter(File::isDirectory).forEach(
					this::clear);
			stream(dir.listFiles()).forEach(File::delete);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
