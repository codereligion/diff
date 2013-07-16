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
import java.util.Set;
import javax.annotation.Nullable;

/**
 * Configuration for the {@link Differ} using Builder pattern.
 *
 * @author Michael Perlin
 * @since 11.05.2013
 * @see Differ
 */
public final class DiffConfigBuilder {

	/**
	 * Simple property names that will be excluded from the diff of all object in the graph.
	 */
	private Set<String> excludedProperties = Sets.newHashSet();

	/**
	 * Custom comparators to order collections.
	 */
	private Set<ObjectComparator<?>> comparators = Sets.newHashSet();

	/**
	 * Comparables to be used instead of a comparator.
	 */
	private Set<Class<? extends Comparable<?>>> comparables = Sets.newHashSet();

	/**
	 * Custom serializer for serialization of specific classes.
	 */
	private Set<Serializer<?>> serializers = Sets.newHashSet();

	/**
	 * The name of the base object, which titles the diff list.
	 */
	private String baseObjectName = "";

	/**
	 * The name of the working object, which titles the diff list.
	 */
	private String workingObjectName = "";


	/**
	 * Creates a new instance.
	 */
	public DiffConfigBuilder() {
		// nothing to initialize;
	}

	/**
	 * Returns a copy of this config with the given {@code propertyName} to be excluded for the diff.
	 * The property will be excluded for all objects in the graph of the to be diffed object.
	 *
	 * @param propertyName the name of the property to be excluded
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code propertyName} is {@code null}
	 */
	public DiffConfigBuilder excludeProperty(final String propertyName) {
		checkArgument(propertyName != null, "propertyName must not be null.");
		this.excludedProperties.add(propertyName);
		return this;
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
	public DiffConfigBuilder useComparator(final ObjectComparator<?> comparator) {
		checkArgument(comparator != null, "comparator must not be null.");
		this.comparators.add(comparator);
		return this;
	}

	/**
	 * Returns a copy of this config with the given {@code comparable} defined to be used to identify objects
	 * which should be sorted by their natural ordering.
	 *
	 * <p>Comparables are prioritized over custom comparators.
	 *
	 * @param comparable the {@link Comparable} to use for natural ordering
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code comparable} is {@code null}
	 * @see Comparable
	 */
	public DiffConfigBuilder useNaturalOrderingFor(final Class<? extends Comparable<?>> comparable) {
		checkArgument(comparable != null, "comparable must not be null.");
		this.comparables.add(comparable);
		return this;
	}

	/**
	 * Returns a copy of this config with the given {@code serializer} added to be used for serializing objects.
	 *
	 * @param serializer the {@link Serializer} to add
	 * @return a copy of this instance
	 * @throws IllegalArgumentException when the given {@code serializer} is {@code null}
	 * @see Serializer
	 */
	public DiffConfigBuilder useSerializer(final Serializer<?> serializer) {
		checkArgument(serializer != null, "serializer must not be null.");
		this.serializers.add(serializer);
		return this;
	}

	/**
	 * Returns a copy of this config with the given {@code objectName} added to be used for the working object.
	 * This property is optional.
	 *
	 * @param objectName the name of the working object, may be null
	 * @return a copy of this instance
	 */
	public DiffConfigBuilder useWorkingObjectName(@Nullable final String objectName) {
		this.workingObjectName = objectName;
		return this;
	}

	/**
	 * Returns a copy of this config with the given {@code objectName} added to be used for the base object.
	 * This property is optional.
	 *
	 * @param objectName the name of the base object, may be null
	 * @return a copy of this instance
	 */
	public DiffConfigBuilder useBaseObjectName(@Nullable final String objectName) {
		this.baseObjectName = objectName;
		return this;
	}

	public DiffConfig build() {
		return new DiffConfig(excludedProperties, comparators, comparables, serializers, baseObjectName, workingObjectName);
	}
}
