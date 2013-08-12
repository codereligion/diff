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
package com.codereligion.diff.differ;

import com.codereligion.diff.exception.MissingObjectComparatorException;
import com.google.common.collect.Lists;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class IterableLineWriter implements CheckableLineWriter {
    
    private final LineWriter lineWriter;
    private final CheckableComparatorFinder comparatorFinder;
    
    public IterableLineWriter(final LineWriter lineWriter, final CheckableComparatorFinder comparatorFinder) {
        this.lineWriter = lineWriter;
        this.comparatorFinder = comparatorFinder;
    }

    @Override
    public List<String> write(final String path, final Object value) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        @SuppressWarnings("unchecked")
        final Iterable<Object> iterable = (Iterable<Object>) value;
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
    
    private List<Object> transformToSortedList(final String path, final Iterable<Object> value) {
        final List<Object> list = Lists.newArrayList(value);

        if (list.isEmpty()) {
            return list;
        }
        
        final Object firstElement = list.get(0);
        final Comparator<Object> comparator = comparatorFinder.findFor(firstElement);
        
        if (comparator == null) {
            throw MissingObjectComparatorException.missingIterableComparator(path);
        }
        
        Collections.sort(list, comparator);
        return list;
    }
}