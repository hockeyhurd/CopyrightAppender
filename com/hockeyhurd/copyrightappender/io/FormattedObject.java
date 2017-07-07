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

/**
 * Class containing code for creating a formatted object.
 * 
 * @author hockeyhurd
 * @version Dec 28, 2014
 */
public class FormattedObject {

	/** Object to store array. */
	private final Object[] OBJECT_ARRAY;
	
	/**
	 * @param objects array to format.
	 */
	public FormattedObject(Object... objects) {
		this.OBJECT_ARRAY = objects;
	}
	
	/**
	 * @return array of objects.
	 */
	public Object[] getArray() {
		return this.OBJECT_ARRAY;
	}
	
	/**
	 * @param index index to get object from array.
	 * @return object at said index if exists, else returns null.
	 */
	public Object get(int index) {
		return index >= 0 && index < this.OBJECT_ARRAY.length ? this.OBJECT_ARRAY[index] : null;
	}
	
	/**
	 * @return size of array.
	 */
	public int size() {
		return this.OBJECT_ARRAY.length;
	}
	
	/**
	 * Static function used to handle copying of arrays from one to another.
	 * @param refArray array object to copy from.
	 * @param length length of 
	 * @return copied array if successful, else returns null array.
	 */
	public static FormattedObject[] copyOf(FormattedObject[] refArray, int length) {
		FormattedObject[] retArray = null;
		
		if (length > 0 && length <= refArray.length && refArray != null && refArray.length > 0) {
			retArray = new FormattedObject[length];
			for (int i = 0; i < length; i++) {
				retArray[i] = refArray[i];
			}
		}
		
		return refArray;
	}
	
	@Override
	public String toString() {
		// String ret = "";
		StringBuilder builder = new StringBuilder();
		
		for (int i = 0; i < this.OBJECT_ARRAY.length; i++) {
			// ret += i == 0 ? String.valueOf(this.OBJECT_ARRAY[i]) : " " + String.valueOf(this.OBJECT_ARRAY[i]);
			builder.append(i == 0 ? String.valueOf(this.OBJECT_ARRAY[i]) : " " + String.valueOf(this.OBJECT_ARRAY[i]));
		}
		
		return builder.toString();
	}

}
