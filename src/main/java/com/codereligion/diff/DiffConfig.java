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

import com.google.common.collect.Sets;
import java.util.Comparator;
import java.util.Set;

/**
 * The configuration for the {@link Differ}.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @see Differ
 */
public class DiffConfig {

	/**
	 * Simple property names that will be excluded from the diff of all object in the graph.
	 */
	private final Set<String> excludedProperties = Sets.newHashSet();
	
	/**
	 * Custom comparators to order collections.
	 */
	private final Set<ObjectComparator<?>> comparators = Sets.newHashSet();
	
	/**
	 * Custom serializer for serialization of specific classes. 
	 */
	private final Set<Serializer> serializers = Sets.newHashSet();
	
	/**
	 * The name of the base object, which titles the diff list.
	 */
	private String baseObjectName = ""; 
	
	/**
	 * The name of the working object, which titles the diff list.
	 */
	private String workingObjectName = ""; 
	
	/**
	 * Finds a comparator suitable for the given {@code object}.
	 * 
	 * @param object the {@link Object} to find a comparator for
	 * @return a {@link Comparator} or {@code null} if none was found
	 * @see Comparator
	 */
	public Comparator<Object> findComparatorFor(final Object object) {
		for (final ObjectComparator<?> comparator : comparators) {
			if (comparator.compares(object)) {
				return comparator;
			}
		}
		return null;
	}
	
	/**
	 * Finds a serializer for the given {@code object}.
	 * 
	 * @param object the object to find the serializer for
	 * @return a {@link Serializer} or {@code null} if none was found
	 * @see Serializer
	 */
	public Serializer findSerializerFor(final Object object) {
		for(final Serializer serializer: serializers) {
			if (serializer.serializes(object)) {
				return serializer;
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
	public boolean isPropertyExcluded(final String propertyName) {
		return excludedProperties.contains(propertyName);
	}

	/**
	 * Adds a property to be excluded for the diff. The property will be excluded for
	 * all objects in the graph of the to be diffed object.
	 * 
	 * @param property the name of the property to be excluded
	 * @return a reference to this instance
	 */
	public DiffConfig addExcludedPropery(final String property) {
		excludedProperties.add(property);
		return this;
	}

	/**
	 * Adds a comparator to be used for sorting iterables, so that they can be ordered
	 * and compared consistently.
	 * 
	 * @param comparator the {@link ObjectComparator} to add
	 * @return a reference to this instance
	 * @see ObjectComparator
	 */
	public DiffConfig addComparator(final ObjectComparator<?> comparator) {
		comparators.add(comparator);
		return this;
	}

	/**
	 * Adds the given serializer to be used for serializing specific objects.
	 * 
	 * @param serializer the {@link Serializer} to add
	 * @return a reference to this instance
	 * @see Serializer
	 */
	public DiffConfig addSerializer(final Serializer serializer) {
		serializers.add(serializer);
		return this;
	}
	
	/**
	 * Sets the name of the working object.
	 * 
	 * @param objectName the name of the working object
	 * @return a reference to this instance
	 */
	public DiffConfig setWorkingObjectName(final String objectName) {
		this.workingObjectName = objectName;
		return this;
	}
	
	/**
	 * Sets the name of the base object
	 * 
	 * @param objectName the name of the base object
	 * @return a reference to this instance
	 */
	public DiffConfig setBaseObjectName(final String objectName) {
		this.baseObjectName = objectName;
		return this;
	}

	/**
	 * Retrieves the name of the base object.
	 * 
	 * @return the name of the base object
	 */
	public String getBaseObjectName() {
		return baseObjectName;
	}

	/**
	 * Retrieves the name of the working object.
	 * 
	 * @return the name of the working object
	 */
	public String getWorkingObjectName() {
		return workingObjectName;
	}
}
