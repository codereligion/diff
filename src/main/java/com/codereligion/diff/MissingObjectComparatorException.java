/*
 * Copyright 2012 The Diff Authors (www.codereligion.com)
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

package com.codereligion.diff;


/**
 * Indicates that the {@link Differ} could not create the diff, because of a missing {@link ObjectComparator}.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 * @see Differ
 * @see DiffConfig
 */
public class MissingObjectComparatorException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	/**
	 * Creates a new instance for the given {@code path} and {@code iterable}.
	 * 
	 * @param path the path of the property, that caused the exception
	 * @param iterable the iterable for which no comparator could be found
	 */
	public MissingObjectComparatorException(final String path, final Iterable<?> iterable) {
		super("Could not find object comparator for '" + path + "' with value: " + iterable);
	}
}
