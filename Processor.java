package com.hockeyhurd.copyrightappender;

import com.hockeyhurd.copyrightappender.io.FileUtil;
import com.hockeyhurd.copyrightappender.io.FormattedObject;

import java.io.File;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Processor.
 *
 * @author hockeyhurd
 * @version 7/7/2017.
 */
public final class Processor implements Runnable {

	public final int threadID;
	public final String name;

	private final FormattedObject[] copyrightInfo;
	private final ConcurrentLinkedQueue<File> q;

	public Processor(int threadID, String name, FormattedObject[] copyrightInfo, ConcurrentLinkedQueue<File> q) {
		this.threadID = threadID;
		this.name = name + threadID;
		this.copyrightInfo = copyrightInfo;
		this.q = q;
	}

	@Override
	public void run() {
		while (!q.isEmpty()) {
			final File file = q.remove();

			if (file != null)
				FileUtil.appendToFile(file.getAbsolutePath(), copyrightInfo);
		}

	}
}
