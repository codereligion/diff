/**
 * Copyright 2013 www.codereligion.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.codereligion.diff.internal;

/**
 * Builder to create paths or parts of paths, which represent lines of the diffed document.
 * 
 * @author Sebastian Gröbler
 * @since 19.06.2013
 */
public final class PathBuilder {

	private static final String KEY_VALUE_SEPARATOR = "=";
	private static final String PATH_SEPARATOR = ".";
	private static final String VALUE_ENCLOSER = "'";
	private static final String INDEX_ENCLOSER_START = "[";
	private static final String INDEX_ENCLOSER_END = "]";
	private static final String NULL_VALUE = "null";
	
	/**
	 * No public constructor.
	 */
	private PathBuilder() {
		throw new IllegalAccessError("This is a static utility class, which must not be instantiated");
	}

	/**
	 * Creates the full path by concatenating the given {@code path} and {@code propertyName}.
	 * 
	 * @param path the first part of the full path
	 * @param propertyName the last part of the full path
	 * @return the concatenated path
	 */
	public static String createFullPath(final String path, final String propertyName) {
		return new StringBuilder()
			.append(path)
			.append(PATH_SEPARATOR)
			.append(propertyName)
			.toString();
	}
	
	/**
	 * Creates a full iterable path by concatenating the given {@code path} with the given {@code index}.
	 * 
	 * @param path the path to be indexed
	 * @param index the actual index
	 * @return the concatenated path
	 */
	public static String createPathForIterableIndex(final String path, final int index) {
		return new StringBuilder()
			.append(path)
			.append(INDEX_ENCLOSER_START)
			.append(index)
			.append(INDEX_ENCLOSER_END)
			.toString();
	}

	/**
	 * Creates a full map path by concatenating the givne {@code path} and {@code key}.
	 * 
	 * @param path the path to be indexed
	 * @param key the actual key to be used as the index
	 * @return the concatenated path
	 */
	public static String createPathForMapIndex(final String path, final Object key) {
		return new StringBuilder()
			.append(path)
			.append(INDEX_ENCLOSER_START)
			.append(key)
			.append(INDEX_ENCLOSER_END)
			.toString();
	}
	
	/**
	 * Creates a full key/value path for the given {@code path}
	 * and the canonical name of the given {@code type}.
	 * 
	 * @param path the path to the given {@code type}
	 * @param type the actual class
	 * @return the full path
	 */
	public static String createPathWithClassValue(final String path, final Class<?> type) {
		return createPathWithValue(path, type.getCanonicalName());
	}

	/**
	 * Creates a full key/value path for the given {@code path} 
	 * setting the value to the string "null.
	 * 
	 * @param path the path to the value
	 * @return the full path
	 */
	public static String createPathWithNullValue(final String path) {
		return new StringBuilder()
			.append(path)
			.append(KEY_VALUE_SEPARATOR)
			.append(NULL_VALUE)
			.toString();
	}

	/**
	 * Creates a full key/value path for the given {@code path} and {@code value}. 
	 * 
	 * @param path the path to the value
	 * @param value the actual value
	 * @return the full path
	 */
	public static String createPathWithValue(final String path, final Object value) {
		return new StringBuilder()
			.append(path)
			.append(KEY_VALUE_SEPARATOR)
			.append(VALUE_ENCLOSER)
			.append(value)
			.append(VALUE_ENCLOSER)
			.toString();
	}
}
