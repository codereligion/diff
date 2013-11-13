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

import com.codereligion.diff.exception.MissingObjectComparatorException;
import com.codereligion.diff.exception.MissingSerializerException;
import com.codereligion.diff.internal.CheckableComparatorFinder;
import com.codereligion.diff.internal.CheckableSerializerFinder;
import com.codereligion.diff.serializer.CheckableSerializer;
import com.google.common.collect.Lists;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MapLineWriter implements CheckableLineWriter {
    
    private final LineWriter lineWriter;
    private final CheckableSerializerFinder serializerFinder;
    private final CheckableComparatorFinder comparatorFinder;
    
    public MapLineWriter(final LineWriter lineWriter, final CheckableSerializerFinder serializerFinder, final CheckableComparatorFinder comparatorFinder) {
        this.lineWriter = lineWriter;
        this.serializerFinder = serializerFinder;
        this.comparatorFinder = comparatorFinder;
    }

    @Override
    public List<String> write(final String path, final Object value) throws IllegalAccessException, InvocationTargetException, IntrospectionException {
        @SuppressWarnings("unchecked")
        final Map<Object, Object> map = (Map<Object, Object>) value;
        final Map<Object, Object> mapProperty = transformToSortedMap(path, map);
        final List<String> lines = Lists.newArrayList();
        
        for (final Map.Entry<Object, Object> entry : mapProperty.entrySet()) {
            final Object key = entry.getKey();
            final CheckableSerializer<Object> serializer = findMapKeySerializerOrThrowException(path, key);
            final Object serializedKey = serializer.serialize(key);
            final String extendedPath = PathBuilder.extendPathWithMapIndex(path, serializedKey);
            lines.addAll(lineWriter.write(extendedPath, entry.getValue()));
        }
        
        return lines;
    }

    @Override
    public boolean applies(Object value) {
        return value instanceof Map;
    }
    
    private Map<Object, Object> transformToSortedMap(final String path, final Map<Object, Object> value) {
        
        if (value.isEmpty()) {
            return Collections.emptyMap();
        }
        
        final Object anyKey = value.keySet().iterator().next();
        final Comparator<Object> comparator = comparatorFinder.findFor(anyKey);
        
        if (comparator == null) {
            throw MissingObjectComparatorException.missingMapKeyComparator(path, anyKey.getClass());
        }
        
        final Map<Object, Object> sortedMap = new TreeMap<Object, Object>(comparator);
        sortedMap.putAll(value);
        return sortedMap;
    }
    
    private CheckableSerializer<Object> findMapKeySerializerOrThrowException(final String path, final Object key) {
        final CheckableSerializer<Object> serializer = serializerFinder.findFor(key);
        if (serializer == null) {
            throw MissingSerializerException.missingMapKeySerializer(path, key.getClass());
        }
        
        return serializer;
    }
}