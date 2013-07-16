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
package com.codereligion.diff;

import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

/**
 * The configuration for the {@link com.codereligion.diff.Differ}.
 * 
 * @author Sebastian Gröbler
 * @author Michael Perlin
 * @since 11.05.2013
 * @see com.codereligion.diff.Differ
 */
public final class DiffConfig {

	/**
	 * Simple property names that will be excluded from the diff of all object in the graph.
	 */
	private final Set<String> excludedProperties;
	/**
	 * Custom comparators to order collections.
	 */
	private final Set<ObjectComparator<?>> comparators;
	/**
	 * Comparables to be used instead of a comparator.
	 */
	private final Set<Class<? extends Comparable<?>>> comparables;
	/**
	 * Custom serializer for serialization of specific classes.
	 */
	private final Set<Serializer<?>> serializers;
	/**
	 * The name of the base object, which titles the diff list.
	 */
	private final String baseObjectName;
	/**
	 * The name of the working object, which titles the diff list.
	 */
	private final String workingObjectName;

	public DiffConfig(Set<String> excludedProperties, Set<ObjectComparator<?>> comparators, Set<Class<? extends Comparable<?>>> comparables,
			Set<Serializer<?>> serializers, String baseObjectName, String workingObjectName) {
		this.excludedProperties = Collections.unmodifiableSet(excludedProperties);
		this.comparators = Collections.unmodifiableSet(comparators);
		this.comparables = Collections.unmodifiableSet(comparables);
		this.serializers = Collections.unmodifiableSet(serializers);
		this.baseObjectName = baseObjectName;
		this.workingObjectName = workingObjectName;
	}

	/**
	 * Retrieves the name of the base object.
	 *
	 * @return the name of the base object
	 */
	String getBaseObjectName() {
		return baseObjectName;
	}

	/**
	 * Retrieves the name of the working object.
	 *
	 * @return the name of the working object
	 */
	String getWorkingObjectName() {
		return workingObjectName;
	}

	/**
	 * Finds a comparator suitable for the given {@code object}.
	 *
	 * @param object the {@link Object} to find a comparator for
	 * @return a {@link java.util.Comparator} or {@code null} if none was found
	 * @see java.util.Comparator
	 */
	@SuppressWarnings("unchecked")
	Comparator<Object> findComparatorFor(final Object object) {
		for (final ObjectComparator<?> comparator : comparators) {
			if (comparator.compares(object)) {
				return (Comparator<Object>) comparator;
			}
		}
		return null;
	}

	/**
	 * Checks whether the given {@code object} implements the {@link Comparable} and if
	 * it has been defined to be used in comparisons for sorting iterables and map keys.
	 *
	 * @param object the {@link Object} to check
	 * @return true when the object implements the {@link Comparable} interface and should be used for comparison
	 * @see Comparable
	 */
	boolean isComparable(final Object object) {
		for (final Class<? extends Comparable<?>> comparable : comparables) {
			if (comparable.isInstance(object)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Finds a serializer for the given {@code object}.
	 *
	 * @param object the object to find the serializer for
	 * @return a {@link Serializer} or {@code null} if none was found
	 * @see Serializer
	 */
	@SuppressWarnings("unchecked")
	Serializer<Object> findSerializerFor(final Object object) {
		for(final Serializer<?> serializer: serializers) {
			if (serializer.serializes(object)) {
				return (Serializer<Object>) serializer;
			}
		}
		return null;
	}

	/**
	 * Determines whether the given {@code propertyName} is excluded from the diff
	 * of all objects in the graph.
	 *
	 * @param propertyName the name of the property
	 * @return true if it is excluded, false otherwise
	 */
	boolean isPropertyExcluded(final String propertyName) {
		return excludedProperties.contains(propertyName);
	}
}
