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
package com.codereligion.diff.util;

import com.codereligion.diff.comparator.CheckableComparator;

/**
 * Compares objects by their natural order.
 * 
 * @author Sebastian Gröbler
 * @since 11.06.2013
 * @param <T> the type extending {@link Comparable} to which the natural ordering should be applied
 */
public class NaturalOrderComparator <T extends Comparable<T>> implements CheckableComparator<T>{
	
	private final Class<? extends Comparable<T>> type;

	public static <T extends Comparable<T>> CheckableComparator<T> newInstance(final Class<? extends Comparable<T>> type) {
		return new NaturalOrderComparator<T>(type);
	}
	
	private NaturalOrderComparator(final Class<? extends Comparable<T>> type) {
		this.type = type;
	}
	
	@Override
	public int compare(T first, T second) {

        if (first == null ^ second == null) {
            return (first == null) ? -1 : 1;
        }

        if (first == null && second == null) {
            return 0;
        }

		return first.compareTo(second);
	}
	
	@Override
	public boolean applies(Object object) {
		
		if (type.isInstance(object)) {
			return true;
		}
		
		return false;
	}
}
