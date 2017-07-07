/* MIT License
 *
 * Copyright (c) hockeyhurd 2017

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:

 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.

 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.hockeyhurd.copyrightappender;

import com.hockeyhurd.copyrightappender.io.FileUtil;
import com.hockeyhurd.copyrightappender.io.FormattedObject;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Main class for running and exectuing the program.
 *
 * @author hockeyhurd
 * @version 7/7/2017.
 */
public final class CopyrightAppender {

	private final int NUM_TRHEADS;
	private final String copyRightFile;
	private final String folderPath;
	private Thread[] threads;

	private static boolean running;

	private CopyrightAppender(int numThreads, String copyRightFile, String folderPath) {
		this.NUM_TRHEADS = numThreads;
		this.copyRightFile = copyRightFile;
		this.folderPath = folderPath;
	}

	private synchronized void start() {
		if (folderPath == null || folderPath.length() < 3)
			return;

		running = true;

		// final List<File> resultList = FileUtil.getFiles(folderPath);
		final List<File> resultList = FileUtil.getFiles(new File(folderPath));

		if (resultList != null && !resultList.isEmpty()) {
			FormattedObject[] copyrightInfo;

			try {
				copyrightInfo = FileUtil.readFromFile(copyRightFile);
			}

			catch (IOException e) {
				System.err.println("Error reading from copyright file!");
				e.printStackTrace();
				return;
			}

			final ConcurrentLinkedQueue<File> fileQueue = new ConcurrentLinkedQueue<File>(resultList);
			threads = new Thread[min(NUM_TRHEADS - 1, fileQueue.size())];

			for (int i = 0; i < threads.length; i++) {
				Processor p = new Processor(i, "file manipulator:", copyrightInfo, fileQueue);

				threads[i] = new Thread(p, p.name);
				threads[i].start();
			}

			int count;
			while (running) {
				count = 0;

				for (int i = 0; i < threads.length; i++) {
					if (threads[i] != null) {
						if (threads[i].isAlive())
							count++;
						else {
							try {
								threads[i].join();
							}
							catch (InterruptedException e) {
								e.printStackTrace();
							}

							threads[i] = null;
						}
					}
				}

				running = count > 0;
			}
		}
	}

	public static int min(final int left, final int right) {
		return left < right ? left : right;
	}

	public static void main(String[] args) {
		// System.out.printf("# cores: %d\n", Runtime.getRuntime().availableProcessors());
		// System.out.printf("# args: %d\n", args.length);

		String folder;
		String copyrightFile;

		switch (args.length) {
			/*case 1:
				copyrightFile = args[0];
				break;*/
			case 2:
				copyrightFile = args[0];
				folder = args[1];
				break;
			default:
				System.err.println("Error! Invalid arg length!");
				return;
		}

		new CopyrightAppender(Runtime.getRuntime().availableProcessors(), copyrightFile, folder).start();
	}

}
