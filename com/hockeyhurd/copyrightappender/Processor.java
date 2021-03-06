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
