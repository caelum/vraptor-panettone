package br.com.caelum.vraptor.panettone;

import static java.nio.file.StandardWatchEventKinds.*;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
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
			try {
				// Obtaining watch keys every 10 seconds
				WatchKey key = service.poll(30, TimeUnit.SECONDS);
				// key value can be null if no event was triggered
				if (key == null)
					continue;
				for (WatchEvent<?> watchEvent : key.pollEvents()) {
					Kind<?> kind = watchEvent.kind();
					if (OVERFLOW == kind)
						continue;
					WatchEvent<Path> wePath = ( WatchEvent<Path>) watchEvent;
					Path path = wePath.context();
					if (ENTRY_CREATE == kind) {
						compiler.compileAll();
					}
					if(ENTRY_MODIFY == kind) {
						compiler.compileAll();
					}
					if(ENTRY_DELETE == kind) {
					}
				}
				if(key.reset()) {
					System.out.println("Stop watching due to break");
					break;
				}
			} catch (InterruptedException e) {
				// timedout
			}
		}
	}

	void stop() {
		try {
			this.running = false;
			service.close();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

}
