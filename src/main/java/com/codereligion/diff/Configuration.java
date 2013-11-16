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

import com.codereligion.diff.comparator.CheckableComparator;
import com.codereligion.diff.serializer.CheckableSerializer;
import com.google.common.collect.Sets;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * The configuration for the {@link Differ}.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @see Differ
 */
@ThreadSafe
public final class Configuration {

    /**
     * Simple property names that will be excluded from the diff of all objects
     * in the graph.
     */
    private final Set<String> excludedProperties = Sets.newHashSet();

    /**
     * Custom comparators to order collections.
     */
    private final Set<CheckableComparator<?>> comparators = Sets.newHashSet();

    /**
     * Comparables to be used instead of a comparator.
     */
    private final Set<Class<? extends Comparable<?>>> comparables = Sets.newHashSet();

    /**
     * Custom serializer for serialization of specific classes.
     */
    private final Set<CheckableSerializer<?>> checkableSerializers = Sets.newHashSet();

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
    public Configuration() {
    }

    /**
     * Returns a copy of this configuration with the given {@code propertyName} to be
     * excluded for the diff. The property will be excluded for all objects in
     * the graph of the to be diffed object.
     * 
     * @param propertyName the name of the property to be excluded
     * @return a copy of this instance
     * @throws IllegalArgumentException when the given {@code propertyName} is
     *             {@code null}
     */
    public Configuration excludeProperty(final String propertyName) {
        checkArgument(propertyName != null, "propertyName must not be null.");
        final Configuration copy = this.copy();
        copy.excludedProperties.add(propertyName);
        return copy;
    }

    /**
     * Returns a copy of this configuration with the given {@code comparator} added to
     * be used for sorting iterables, so that they can be ordered and compared
     * consistently.
     * 
     * @param comparator the {@link CheckableComparator} to add
     * @return a copy of this instance
     * @throws IllegalArgumentException when the given {@code comparator} is
     *             {@code null}
     * @see CheckableComparator
     */
    public Configuration useComparator(final CheckableComparator<?> comparator) {
        checkArgument(comparator != null, "comparator must not be null.");
        final Configuration copy = this.copy();
        copy.comparators.add(comparator);
        return copy;
    }

    /**
     * Returns a copy of this configuration with the given {@code comparable} defined
     * to be used to identify objects which should be sorted by their natural
     * ordering.
     * 
     * <p>
     * Comparables have a higher priority than custom comparators.
     * 
     * @param comparable the {@link Comparable} to use for natural ordering
     * @return a copy of this instance
     * @throws IllegalArgumentException when the given {@code comparable} is
     *             {@code null}
     * @see Comparable
     */
    public Configuration useNaturalOrderingFor(final Class<? extends Comparable<?>> comparable) {
        checkArgument(comparable != null, "comparable must not be null.");
        final Configuration copy = this.copy();
        copy.comparables.add(comparable);
        return copy;
    }

    /**
     * Returns a copy of this configuration with the given {@code serializer} added to
     * be used for serializing objects.
     * 
     * @param serializer the {@link CheckableSerializer} to add
     * @return a copy of this instance
     * @throws IllegalArgumentException when the given {@code serializer} is
     *             {@code null}
     * @see CheckableSerializer
     */
    public Configuration useSerializer(final CheckableSerializer<?> serializer) {
        checkArgument(serializer != null, "serializer must not be null.");
        final Configuration copy = this.copy();
        copy.checkableSerializers.add(serializer);
        return copy;
    }

    /**
     * Returns a copy of this configuration with the given {@code objectName} added to
     * be used for the working object. This property is optional.
     * 
     * @param objectName the name of the working object, may be null
     * @return a copy of this instance
     */
    public Configuration useWorkingObjectName(@Nullable final String objectName) {
        final Configuration copy = this.copy();
        copy.workingObjectName = objectName;
        return copy;
    }

    /**
     * Returns a copy of this config with the given {@code objectName} added to
     * be used for the base object. This property is optional.
     * 
     * @param objectName the name of the base object, may be null
     * @return a copy of this instance
     */
    public Configuration useBaseObjectName(@Nullable final String objectName) {
        final Configuration copy = this.copy();
        copy.baseObjectName = objectName;
        return copy;
    }

    /**
     * Creates semi-deep copy of this object. Items of the collection based members
     * will not be deep copied.
     *
     * @return a new instance in which every member variable is a copy of this object
     */
    private Configuration copy() {
        final Configuration copy = new Configuration();
        copy.baseObjectName = this.baseObjectName;
        copy.workingObjectName = this.workingObjectName;
        copy.comparables.addAll(this.comparables);
        copy.comparators.addAll(this.comparators);
        copy.checkableSerializers.addAll(this.checkableSerializers);
        copy.excludedProperties.addAll(this.excludedProperties);
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
     * Retrieves the checkable serializers.
     * 
     * @return a set of checkable serializers
     * @see CheckableSerializer
     */
    Set<CheckableSerializer<?>> getCheckableSerializer() {
        return checkableSerializers;
    }

    /**
     * Retrieves the checkable comparators.
     * 
     * @return a set of checkable comparators
     * @see CheckableComparator
     */
    Set<CheckableComparator<?>> getCheckableComparators() {
        return comparators;
    }

    /**
     * Retrieves the comparables.
     *
     * @return a set of comparables
     */
    Set<Class<? extends Comparable<?>>> getComparables() {
        return comparables;
    }

    /**
     * Retrieves the excluded properties.
     *
     * @return a set of excluded properties
     */
    Set<String> getExcludedProperties() {
        return excludedProperties;
    }
}
