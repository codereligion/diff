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

import com.codereligion.diff.serializer.CheckableSerializer;

import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Serializes every object to string by just calling its toString method.
 * 
 * @author Sebastian Gröbler
 * @since 13.05.2013
 */
public class ExcludeSerializer implements CheckableSerializer<Object> {
	
	private Set<Class<?>> excludedTypes = Sets.newHashSet();

	public ExcludeSerializer(final Class<?>... excludedTypes) {
		this.excludedTypes = Sets.newHashSet(excludedTypes);
	}
	
	@Override
	public boolean applies(Object object) {
		for (final Class<?> excludedType : excludedTypes) {
			if (excludedType.isInstance(object)) {
				return false;
			}
		}
		
		return true;
	}

	@Override
	public String serialize(Object object) {
		return object.toString();
	}
}
