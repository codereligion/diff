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

import java.util.Comparator;

/**
 * Abstract comparator that encapsulates the functionality of type conversion from object to T.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @param <T>
 */
public interface ObjectComparator<T> extends Comparator<Object> {
	
	/**
	 * Determines whether this comparator compares the given {@link Object}.
	 * 
	 * <p>An {@code object} is considered comparable by this class,
	 * when it is an "instance of" the {@code type}.
	 * 
	 * @param object the object to check
	 * @return true if it does compare, false if not
	 */
	boolean compares(final Object object);

	/**
	 * Compares the given objects of type T.
	 * 
	 * @param o1 first object
	 * @param o2 seconds object
	 * @return either -1, 0 or 1
	 * @see Comparator
	 */
	int internalCompare(final T o1, final T o2);
}
