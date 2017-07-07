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

package com.hockeyhurd.copyrightappender.io;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.MissingFormatArgumentException;

/**
 * Class containing io code for writing and reading text based files.
 *
 * @author Nick Hurd
 * @version Dec 28, 2014
 */
public class FileUtil {

	private FileUtil() {
	}

	/**
	 * Creates new file object from path.
	 * 
	 * @param path path to create object from.
	 * @return file object.
	 */
	public static File createFile(String path) {
		return new File(path);
	}

	/**
	 * Function used to check if a file exists from specified path.
	 * 
	 * @param file file object to test.
	 * @return true if file exists, else returns false.
	 */
	public static boolean exists(File file) {
		return file.exists();
	}

	/**
	 * Function used to 'rename' file should it exist.
	 * 
	 * @param file file object as reference.
	 * @return new file object with alternate name.
	 */
	public static File getAltFile(File file) {
		int index = 0;
		while (exists(file)) {
			file = createFile(++index + file.getName());
		}

		return file;
	}

	/**
	 * Method used to write an array of FormattedObject(s) to a text file.
	 * 
	 * @param path path to write to.
	 * @param format format to use (Set to null if not applicable).
	 * @param overwrite whether we can overwrite the file should it exist.
	 * @param objects array to print.
	 * @throws IOException
	 */
	public static void writeToFile(String path, String format, boolean overwrite, FormattedObject... objects) throws IOException {

		// Get starting file object.
		File file = createFile(path);

		// If exists, rename file.
		if (!overwrite && exists(file)) file = getAltFile(file);

		PrintWriter pw = new PrintWriter(file);

		// Loop through array printing out objects.
		int counter = 0;
		for (FormattedObject o : objects) {
			try {
				if (o != null && o.getArray().length > 0) {
					if (format != null && format.length() > 1 && o.getArray().length > 0) {
						if (counter++ > 0) pw.println();
						pw.printf(format, o.getArray());
					}
					else pw.println(String.valueOf(o));
				}
			}

			catch (MissingFormatArgumentException e) {
				continue;
			}
		}

		// Flush and close output stream.
		pw.flush();
		pw.close();
	}

	public static void appendToFile(String path, FormattedObject... objects) {
		File file = createFile(path);

		if (!exists(file)) {
			System.err.println("File at path: " + path + " doesn't exist!");
			return;
		}

		FormattedObject[] readArray;
		try {
			readArray = readFromFile(path);
		}
		catch (IOException e) {
			e.printStackTrace();
			return;
		}

		FormattedObject[] newArray = new FormattedObject[objects.length + readArray.length];

		int i = 0;
		for (; i < objects.length; i++) {
			newArray[i] = objects[i];
			// System.out.println("Added:\t" + objects[i].toString());
		}

		for (int j = 0; j < readArray.length; j++) {
			newArray[i + j] = readArray[j];
			// System.out.println("Added:\t" + readArray[j].toString());
		}

		System.out.printf("Read and organizing data complete!  Re-writing file: %s now!%s", file.getName(), System.lineSeparator());

		try {
			writeToFile(path, null, true, newArray);
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * Function used to read a file from given path.
	 * 
	 * @param path path to read from.
	 * @return FormattedObject array containing all read objects.
	 * @throws IOException
	 */
	public static FormattedObject[] readFromFile(String path) throws IOException {

		// Create and store file object.
		File file = createFile(path);

		// Make sure file exists and if not, warn user!
		if (!exists(file)) {
			System.err.println("Error! File doesn't exist!");
			return null;
		}

		// Tracking lists.
		List<FormattedObject> list = new ArrayList<FormattedObject>();

		BufferedReader reader = new BufferedReader(new FileReader(file));
		String line;
		int maxSpace = 5;
		int counter = 0;
		
		while (counter <= maxSpace) {
			if ((line = reader.readLine()) != null) {
				if (counter > 0) counter = 0;
				list.add(new FormattedObject(line));
			}
			else counter++;
		}
		
		reader.close();
		
		// Return list --> FormattedObject array.
		return list.toArray(new FormattedObject[list.size()]);
	}

	/**
	 * A recursive function allows getting all files in a given directory.
	 *
	 * @deprecated as of 7/7/17 use overloaded method with File object.
	 *
	 * @param path path to check
	 * @return list of files if successful else returns null.
	 */
	@Deprecated
	public static List<File> getFiles(String path) {
		
		// get file from path.
		File file = createFile(path);
		
		// check if valid file.
		if (file == null || !exists(file))
			return null;

		// instantiate resulting list.
		List<File> files = new ArrayList<File>(0x40);

		// if current file path is a file, add to list.
		if (file.isFile())
			files.add(file);
		
		// else if it is a directory:
		else {
			
			// get files in directory.
			File[] fileArray = file.listFiles();

			// check if directory is not empty (contains something).
			if (fileArray != null && fileArray.length > 0) {
				
				// subfile list.
				List<File> subFiles;
				for (File f : fileArray) {
					
					// iterate over each file and call recursive method.
					subFiles = getFiles(f.getPath());

					// if subfiles has something in list, add it to main list.
					if (subFiles != null && !subFiles.isEmpty()) {
						for (File f2 : subFiles) {
							files.add(f2);
						}
					}
				}
			}
		}

		return files;
	}

	public static List<File> getFiles(File file) {
		if (file == null || (file.isFile() && !exists(file)))
			return null;

		final List<File> files = new ArrayList<File>(0x40);

		recursivelyGetFiles(file, files);

		return files;
	}

	private static void recursivelyGetFiles(File file, List<File> fileList) {
		if (file.isFile())
			fileList.add(file);

		else {
			System.out.println(file.listFiles() == null);
			for (File f : file.listFiles()) {
				recursivelyGetFiles(f, fileList);
			}
		}
	}

}
