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
