package br.com.caelum.vraptor.panettone;

import static java.nio.file.Files.isDirectory;
import static java.nio.file.LinkOption.NOFOLLOW_LINKS;
import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.FileSystems;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class Watcher implements Runnable {

	private final WatchService service;
	private boolean running = true;
	private final Compiler compiler;
	private final Map<WatchKey,Path> keys = new HashMap<>();
	private boolean trace = false;
	private final PrintStream out = System.out;
	private final PrintStream err = System.err;

	Watcher(Path folder, Compiler compiler) {
		this.compiler = compiler;
		try {
			this.service = FileSystems.getDefault().newWatchService();
			registerAll(folder);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		trace = true;
	}
	
   private void registerAll(final Path start) {
        try {
			Files.walkFileTree(start, new SimpleFileVisitor<Path>() {
			    @Override
			    public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
			        register(dir);
			        return FileVisitResult.CONTINUE;
			    }
			});
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
    }
   
   private void register(Path dir) throws IOException {
       WatchKey key = dir.register(service, ENTRY_CREATE,
				ENTRY_MODIFY,
				ENTRY_DELETE);
       if (trace) {
           Path prev = keys.get(key);
           if (prev == null) {
               out.format("Registering new path: %s\n", dir);
           } else if (!dir.equals(prev)) {
               out.format("update: %s -> %s\n", prev, dir);
           }
       }
       keys.put(key, dir);
   }
 

	@SuppressWarnings("unchecked")
	@Override
	public void run() {
		while (running) {
			WatchKey key = getKey();
			if (key == null)
				continue;
			Path dir = keys.get(key);
			if (dir == null) {
				err.println("WatchKey not recognized: " + key);
				continue;
			}

			List<WatchEvent<?>> events = key.pollEvents();
			boolean shouldDelete = events.stream()
				.map(WatchEvent::kind)
				.anyMatch(ENTRY_DELETE::equals);
			events.stream()
				.map(e -> (WatchEvent<Path>) e)
				.peek(e -> out.println("> " + dir.resolve(e.context()).getFileName()))
				.filter(e -> e.kind()==ENTRY_CREATE)
				.map(e -> e.context())
				.map(name -> dir.resolve(name))
				.filter(path -> isDirectory(path, NOFOLLOW_LINKS))
				.forEach(this::registerAll);

			if(shouldDelete) compiler.clear();
			compiler.compileAll();
			key.reset();
		}
	}

	private WatchKey getKey() {
		try {
			return service.poll(30, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
			return null;
		}
	}

	void stop() {
		try {
			this.running = false;
			service.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			out.println("Stopped compilation service");
		}
	}

}
