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
import com.codereligion.diff.internal.CheckableComparatorFinder;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Writes iterables to lines.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
class IterableLineWriter extends TypeSafeCheckableLineWriter<Iterable<Object>> {
    
    private final LineWriter lineWriter;
    private final CheckableComparatorFinder comparatorFinder;
    
    public IterableLineWriter(final LineWriter lineWriter, final CheckableComparatorFinder comparatorFinder) {
        this.lineWriter = lineWriter;
        this.comparatorFinder = comparatorFinder;
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
    
    private List<Object> transformToSortedList(final String path, final Iterable<Object> value) {
        final List<Object> list = Lists.newArrayList(value);

        if (list.isEmpty()) {
            return list;
        }
        
        final Object firstElement = list.get(0);
        final Comparator<Object> comparator = comparatorFinder.findFor(firstElement);
        
        if (comparator == null) {
            throw MissingComparatorException.missingIterableComparator(path);
        }
        
        Collections.sort(list, comparator);
        return list;
    }
}