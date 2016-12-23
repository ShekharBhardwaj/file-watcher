package com.syncodus.filewatcher.droplistner;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.syncodus.filewatcher.dropbox.DropBoxsdk;
import com.syncodus.filewatcher.dropdelete.DeleteDrop;

@Component
public class FileListener {

	@Autowired
	DropBoxsdk dropBoxsdk;

	@Autowired
	DeleteDrop deleteDrop;

	@Autowired
	Environment env;

	private static Logger LOG = LoggerFactory.getLogger(FileListener.class);

	// private final String DIRECTORY_TO_WATCH = "/eclipse_projects/tmp";
	public void watcher() {

		try {
			WatchService watcher = FileSystems.getDefault().newWatchService();
			Path dir = Paths.get(env.getProperty("spring.listener.dir"));
			dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE/*, StandardWatchEventKinds.ENTRY_MODIFY*/);

			LOG.info("Watch Service registered for dir: " + dir.getFileName());

			while (true) {
				WatchKey key;
				try {
					key = watcher.take();
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
					WatchEvent.Kind<?> kind = event.kind();

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path fileName = ev.context();

					LOG.info(kind.name() + ": " + fileName);

					LOG.info("uploading file to dropbox");
					if (!fileName.toString().startsWith(".")) {
						dropBoxsdk.boxdk(env.getProperty("spring.listener.dir") + File.separator + fileName.toString());

					}

					deleteDrop.deleteTmpFile(
							env.getProperty("spring.listener.dir") + File.separator + fileName.toString());

				/*	if (kind == StandardWatchEventKinds.ENTRY_MODIFY
							&& fileName.toString().equals("DirectoryWatchDemo.java")) {
						LOG.info("My source file has changed!!!");
					}*/
				}

				boolean valid = key.reset();
				if (!valid) {
					break;
				}
			}

		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
