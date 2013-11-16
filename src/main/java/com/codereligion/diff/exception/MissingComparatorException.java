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
package com.codereligion.diff.exception;

/**
 * Indicates that the {@link com.codereligion.diff.Differ} could not create the diff, because of a
 * missing {@link com.codereligion.diff.comparator.CheckableComparator}.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 * @see com.codereligion.diff.Differ
 * @see com.codereligion.diff.Configuration
 * @see com.codereligion.diff.comparator.CheckableComparator
 */
public final class MissingComparatorException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new {@link MissingComparatorException} to indicate that an
     * iterable could not be ordered, due to a missing {@link com.codereligion.diff.comparator.CheckableComparator}.
     * 
     * @param path the path of the property which identifies the iterable
     * @return a new instance of {@link MissingComparatorException}
     */
    public static MissingComparatorException missingIterableComparator(final String path) {
        return new MissingComparatorException("Could not find CheckableComparator for iterable at '" + path + "'");
    }

    /**
     * Creates a new {@link MissingComparatorException} to indicate that map
     * keys of a specific {@code type} could not be ordered, due to a missing
     * {@link com.codereligion.diff.comparator.CheckableComparator}.
     * 
     * @param path the path of the property which identifies the map
     * @param type the type of the map key
     * @return a new instance of {@link MissingComparatorException}
     */
    public static MissingComparatorException missingMapKeyComparator(final String path, final Class<?> type) {
        return new MissingComparatorException("Could not find CheckableComparator for map keys of type '" + type.getSimpleName() + "' at '" + path + "'");
    }

    /**
     * Disallows public instantiation.
     *
     * @param message the message to use
     */
    private MissingComparatorException(final String message) {
        super(message);
    }
}
