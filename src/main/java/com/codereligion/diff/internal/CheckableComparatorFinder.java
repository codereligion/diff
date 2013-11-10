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

import java.util.Comparator;
import java.util.Set;

public class CheckableComparatorFinder {
    
    private final Set<CheckableComparator<?>> comparators;
    private final Set<Class<? extends Comparable<?>>> comparables;

    public CheckableComparatorFinder(final Set<CheckableComparator<?>> comparators, final Set<Class<? extends Comparable<?>>> comparables) {
        this.comparators = comparators;
        this.comparables = comparables;
    }
    
    public Comparator<Object> findFor(final Object value) {
        
        if (isComparable(value)) {
            return ComparableComparator.INSTANCE;
        }
        
        return findCustomComparatorFor(value);
    }

    /**
     * Finds a comparator suitable for the given {@code object}.
     * 
     * @param object the {@link Object} to find a comparator for
     * @return a {@link Comparator} or {@code null} if none was found
     * @see Comparator
     */
    @SuppressWarnings("unchecked")
    private Comparator<Object> findCustomComparatorFor(final Object object) {
        for (final CheckableComparator<?> comparator : comparators) {
            if (comparator.applies(object)) {
                return (Comparator<Object>) comparator;
            }
        }
        return null;
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
