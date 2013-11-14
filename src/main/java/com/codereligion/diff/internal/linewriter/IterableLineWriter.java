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
package com.codereligion.diff.internal.linewriter;

import com.codereligion.diff.exception.MissingComparatorException;
import com.codereligion.diff.internal.ComparatorRepository;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Serializes {@link Iterable}s to lines after sorting them.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
class IterableLineWriter extends TypeSafeCheckableLineWriter<Iterable<Object>> {

    /**
     * Line writer to delegate actual line writing to.
     */
    private final LineWriter lineWriter;

    /**
     * Repository to lookup comparators to use for sorting the given iterables.
     */
    private final ComparatorRepository comparatorRepository;

    /**
     * Creates a new instance for the given {@code lineWriter} and {@code comparatorRepository}.
     *
     * @param lineWriter the {@link LineWriter} to delegate actual line writing to
     * @param comparatorRepository the repository to look up comparators
     */
    public IterableLineWriter(final LineWriter lineWriter, final ComparatorRepository comparatorRepository) {
        this.lineWriter = lineWriter;
        this.comparatorRepository = comparatorRepository;
    }

    @Override
    List<String> typeSafeWrite(final String path, final Iterable<Object> iterable) {
        final List<Object> iterableProperty = transformToSortedList(path, iterable);
        final List<String> lines = Lists.newArrayList();
        
        int i = 0;
        for (final Object nestedProperty : iterableProperty) {
            final String extendedPath = PathBuilder.extendPathWithIterableIndex(path, i++);
            lines.addAll(lineWriter.write(extendedPath, nestedProperty));
        }
        
        return lines;
    }

    @Override
    public boolean applies(final Object value) {
        return value instanceof Iterable;
    }

    /**
     * Transforms the given iterable value into a sorted list or throws an {@link MissingComparatorException}
     * if none was found.
     *
     * @param path the path which describes the position of the given item's iterable in the object graph
     * @param value the property value to sort into a list
     * @return a new sorted list of the given iterable
     * @throws MissingComparatorException if no comparator could be found for the given iterable
     */
    private List<Object> transformToSortedList(final String path, final Iterable<Object> value) {
        final List<Object> list = Lists.newArrayList(value);
        final Optional<Object> firstElement = Iterables.tryFind(list, Predicates.notNull());

        if (!firstElement.isPresent()) {
            return list;
        }

        final Comparator<Object> comparator = findComparatorOrThrowException(path, firstElement.get());
        Collections.sort(list, comparator);

        return list;
    }

    /**
     * Tries to find a matching comparator for the given {@code item} or throws a
     * {@link MissingComparatorException} if none was found.
     *
     * @param path the path which describes the position of the given item's iterable in the object graph
     * @param item the item to find a matching comparator for
     * @return a {@link Comparator} for the given {@code item}
     * @throws MissingComparatorException if no comparator could be found for the given item
     */
    private Comparator<Object> findComparatorOrThrowException(final String path, final Object item) {

        final Optional<Comparator<Object>> optional = comparatorRepository.findFor(item);

        if (!optional.isPresent()) {
            throw MissingComparatorException.missingIterableComparator(path);
        }

        return optional.get();
    }
}