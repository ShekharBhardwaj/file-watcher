package com.syncodus.filewatcher.dropdelete;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class DeleteDrop {

	private static Logger LOG = LoggerFactory.getLogger(DeleteDrop.class);

	public void deleteTmpFile(String droppedfile) {
		File file = new File(droppedfile);

		try {
			if (file.exists()) {
				LOG.debug("deleting {} ...", droppedfile);
				file.delete();
			}
		} catch (Exception e) {
			LOG.error("Cannot delete {} due to {}", droppedfile, e.getMessage());

		}

	}
}
