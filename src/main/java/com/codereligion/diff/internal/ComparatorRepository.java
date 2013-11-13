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
package com.codereligion.diff.internal;

import com.codereligion.diff.comparator.CheckableComparator;
import com.google.common.base.Optional;
import java.util.Comparator;
import java.util.Set;

/**
 * Allows the internals to easily lookup a matching comparator for a given object.
 *
 * @author Sebastian Gröbler
 * @since 10.11.2013
 */
public class ComparatorRepository {

    /**
     * The set of possible checkableComparators.
     */
    private final Set<CheckableComparator<?>> checkableComparators;

    /**
     * The set of possible comparables.
     */
    private final Set<Class<? extends Comparable<?>>> comparables;

    /**
     * Creates a new instance for the given {@code checkableComparators} and
     * {@code comparable}.
     *
     * @param checkableComparators the checkable comparators to use
     * @param comparables the comparables to use
     */
    public ComparatorRepository(
            final Set<CheckableComparator<?>> checkableComparators,
            final Set<Class<? extends Comparable<?>>> comparables) {

        this.checkableComparators = checkableComparators;
        this.comparables = comparables;
    }

    /**
     * Checks whether this object's class has been configured to be a comparable and if so
     * returns a comparator which applies the natural ordering according to the object {@link Comparable}
     * implementation. In case the no comparable has been registered for the given object's type the logic will
     * try to find a configured comparator for the given object.
     *
     * @param object the object to find a comparator for
     * @return an optional of a {@link Comparator}
     */
    public Optional<Comparator<Object>> findFor(final Object object) {
        
        if (isComparable(object)) {
            return Optional.<Comparator<Object>>of(ComparableComparator.INSTANCE);
        }
        
        return findComparatorFor(object);
    }

    /**
     * Finds an optional comparator suitable for the given {@code object}.
     * 
     * @param object the {@link Object} to find a comparator for
     * @return an optional of a {@link Comparator}
     * @see Comparator
     */
    @SuppressWarnings("unchecked")
    private Optional<Comparator<Object>> findComparatorFor(final Object object) {
        for (final CheckableComparator<?> comparator : checkableComparators) {
            if (comparator.applies(object)) {
                return Optional.of((Comparator<Object>) comparator);
            }
        }
        return Optional.absent();
    }

    /**
     * Checks whether the given {@code object} implements the {@link Comparable}
     * and if it has been defined to be used in comparisons for sorting
     * iterables and map keys.
     * 
     * @param object the {@link Object} to check
     * @return true when the object implements the {@link Comparable} interface
     *         and should be used for comparison
     * @see Comparable
     */
    private boolean isComparable(final Object object) {
        for (final Class<? extends Comparable<?>> comparable : comparables) {
            if (comparable.isInstance(object)) {
                return true;
            }
        }
        return false;
    }
}
