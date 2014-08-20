package br.com.caelum.vraptor.panettone;

import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.concurrent.TimeUnit;

public class Watcher implements Runnable {

	private final WatchService service;
	private boolean running = true;
	private final Compiler compiler;

	Watcher(Path folder, Compiler compiler) {
		this.compiler = compiler;
		try {
			this.service = FileSystems.getDefault().newWatchService();
			folder.register(service, StandardWatchEventKinds.ENTRY_CREATE,
					StandardWatchEventKinds.ENTRY_MODIFY,
					StandardWatchEventKinds.ENTRY_DELETE);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public void run() {
		while (running) {
			WatchKey key = getKey();
			if (key == null)
				continue;
			boolean shouldDelete = key.pollEvents().stream()
				.map(WatchEvent::kind)
				.anyMatch(ENTRY_DELETE::equals);
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
			System.out.println("Stopping compilation service");
			service.close();
			System.out.println("Stopped compilation service");
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
