package com.syncodus.filewatcher.runner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.syncodus.filewatcher.droplistner.FileListener;

@Component
public class FileWatcherRunner implements CommandLineRunner {

	private static Logger LOG = LoggerFactory.getLogger(FileWatcherRunner.class);

	@Autowired
	FileListener fileListener;

	@Override
	public void run(String... arg0) throws Exception {

		LOG.info("Circling for folder...");
		fileListener.watcher();

		Thread.sleep(5000);

	}

}
