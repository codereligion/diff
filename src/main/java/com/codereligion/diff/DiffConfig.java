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

import static com.google.common.base.Preconditions.checkArgument;

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Set;
import javax.annotation.concurrent.ThreadSafe;

/**
 * The configuration for the {@link Differ}.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @see Differ
 */
@ThreadSafe
public final class DiffConfig {

	/**
	 * Simple property names that will be excluded from the diff of all object in the graph.
	 */
	private final Set<String> excludedProperties = Sets.newHashSet();
	
	/**
	 * Custom comparators to order collections.
	 */
	private final Set<ObjectComparator<?>> comparators = Sets.newHashSet();
	
	/**
	 * Comparables to be used instead of a comparator.
	 */
	private final Set<Class<? extends Comparable<?>>> comparables = Sets.newHashSet();
	
	/**
	 * Custom serializer for serialization of specific classes. 
	 */
	private final Set<Serializer<?>> serializers = Sets.newHashSet();
	
	/**
	 * The name of the base object, which titles the diff list.
	 */
	private String baseObjectName = ""; 
	
	/**
	 * The name of the working object, which titles the diff list.
	 */
	private String workingObjectName = ""; 

	/**
	 * Returns a copy of this config with the given {@code propertyName} added to be excluded for the diff.
	 * The property will be excluded for all objects in the graph of the to be diffed object.
	 * 
	 * @param propertyName the name of the property to be excluded
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code propertyName} is {@code null} 
	 */
	public DiffConfig excludeProperty(final String propertyName) {
		checkArgument(propertyName != null, "propertyName must not be null.");
		final DiffConfig copy = this.copy();
		copy.excludedProperties.add(propertyName);
		return copy;
	}

	/**
	 * Returns a copy of this config with the given {@code comparator} added to be used for sorting iterables,
	 * so that they can be ordered and compared consistently.
	 * 
	 * @param comparator the {@link ObjectComparator} to add
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code comparator} is {@code null}
	 * @see ObjectComparator
	 */
	public DiffConfig useComparator(final ObjectComparator<?> comparator) {
		checkArgument(comparator != null, "comparator must not be null.");
		final DiffConfig copy = this.copy();
		copy.comparators.add(comparator);
		return copy;
	}
	
	/**
	 * Returns a copy of this config with the given {@code comparable} to be used to identify objects
	 * which should be sorted by their natural ordering.
	 * 
	 * <p>Comparables are prioritized over custom comparators.
	 * 
	 * @param comparable the {@link Comparable} to use for natural ordering
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code comparable} is {@code null}
	 * @see Comparable
	 */
	public DiffConfig useNaturalOrderingFor(final Class<? extends Comparable<?>> comparable) {
		checkArgument(comparable != null, "comparable must not be null.");
		final DiffConfig copy = this.copy();
		copy.comparables.add(comparable);
		return copy;
	}
	
	/**
	 * Returns a copy of this config with the given {@code serializer} added to be used for serializing objects.
	 * 
	 * @param serializer the {@link Serializer} to add
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code serializer} is {@code null}
	 * @see Serializer
	 */
	public DiffConfig useSerializer(final Serializer<?> serializer) {
		checkArgument(serializer != null, "serializer must not be null.");
		final DiffConfig copy = this.copy();
		copy.serializers.add(serializer);
		return copy;
	}
	
	/**
	 * Returns a copy of this config with the given {@code objectName} added to be used for the working object.
	 * This property is optional.
	 * 
	 * @param objectName the name of the working object, may be null
	 * @return a copy of this instance
	 */
	public DiffConfig useWorkingObjectName(final String objectName) {
		final DiffConfig copy = this.copy();
		copy.workingObjectName = objectName;
		return copy;
	}
	
	/**
	 * Returns a copy of this config with the given {@code objectName} added to be used for the base object.
	 * This property is optional.
	 * 
	 * @param objectName the name of the base object, may be null
	 * @return a copy of this instance
	 */
	public DiffConfig useBaseObjectName(final String objectName) {
		final DiffConfig copy = this.copy();
		copy.baseObjectName = objectName;
		return copy;
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
	 * @return a {@link Comparator} or {@code null} if none was found
	 * @see Comparator
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
	
	private DiffConfig copy() {
		final DiffConfig copy = new DiffConfig();
		copy.baseObjectName = this.baseObjectName;
		copy.workingObjectName = this.workingObjectName;
		copy.comparables.addAll(this.comparables);
		copy.comparators.addAll(this.comparators);
		copy.serializers.addAll(this.serializers);
		copy.excludedProperties.addAll(this.excludedProperties);
		return copy;
	}
}
