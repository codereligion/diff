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
package com.codereligion.diff.internal.linewriter;


/**
 * Builder to create paths or parts of paths, which represent lines of the
 * diffed document.
 * 
 * @author Sebastian Gröbler
 * @since 19.06.2013
 */
final class PathBuilder {

    private static final String KEY_VALUE_SEPARATOR = "=";
    private static final String PATH_SEPARATOR = ".";
    private static final String INDEX_ENCLOSER_START = "[";
    private static final String INDEX_ENCLOSER_END = "]";

    /**
     * No public constructor.
     */
    private PathBuilder() {
        throw new IllegalAccessError("This is a static utility class, which must not be instantiated");
    }

    /**
     * Creates the full path by concatenating the given {@code path} and
     * {@code propertyName}.
     * 
     * @param path the first part of the full path
     * @param propertyName the last part of the full path
     * @return the concatenated path
     */
    public static String extendPathWithProperty(final String path, final String propertyName) {
        return new StringBuilder().append(path)
                                  .append(PATH_SEPARATOR)
                                  .append(propertyName)
                                  .toString();
    }

    /**
     * Creates a full iterable path by concatenating the given {@code path} with
     * the given {@code index}.
     * 
     * @param path the path to be indexed
     * @param index the actual index
     * @return the concatenated path
     */
    public static String extendPathWithIterableIndex(final String path, final int index) {
        return new StringBuilder().append(path)
                                  .append(INDEX_ENCLOSER_START)
                                  .append(index)
                                  .append(INDEX_ENCLOSER_END)
                                  .toString();
    }

    /**
     * Creates a full map path by concatenating the given {@code path} and
     * {@code key}.
     * 
     * @param path the path to be indexed
     * @param key the actual key to be used as the index
     * @return the concatenated path
     */
    public static String extendPathWithMapIndex(final String path, final Object key) {
        return new StringBuilder().append(path)
                                  .append(INDEX_ENCLOSER_START)
                                  .append(key)
                                  .append(INDEX_ENCLOSER_END)
                                  .toString();
    }

    /**
     * Creates a full key/value path for the given {@code path} and
     * {@code value}.
     * 
     * @param path the path to the value
     * @param value the actual value
     * @return the full path
     */
    public static String extendPathWithValue(final String path, final Object value) {
        return new StringBuilder().append(path)
                                  .append(KEY_VALUE_SEPARATOR)
                                  .append(value)
                                  .toString();
    }
}
