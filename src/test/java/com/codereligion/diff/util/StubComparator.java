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

import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Compares those objects which are instances of the given {@code types} and
 * constantly returns 0 as difference.
 * 
 * @author Sebastian Gröbler
 * @since 13.05.2013
 */
public class StubComparator implements CheckableComparator<Object>{
	
	private final Set<Class<?>> types;
	
	public StubComparator(final Class<?>... types) {
		this.types = Sets.newHashSet(types);
	}
	
	@Override
	public int compare(Object o1, Object o2) {
		return 0;
	}

	@Override
	public boolean applies(Object object) {
		
		for (final Class<?> type : types) {
			if (type.isInstance(object)) {
				return true;
			}
		}
		
		return false;
	}
}
