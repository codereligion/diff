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
import com.codereligion.diff.exception.MissingSerializerException;
import com.codereligion.diff.internal.ComparatorRepository;
import com.codereligion.diff.internal.SerializerRepository;
import com.codereligion.diff.serializer.CheckableSerializer;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

class MapLineWriter extends TypeSafeCheckableLineWriter<Map<Object, Object>> {
    
    private final LineWriter lineWriter;
    private final SerializerRepository serializerFinder;
    private final ComparatorRepository comparatorFinder;
    
    public MapLineWriter(final LineWriter lineWriter, final SerializerRepository serializerFinder, final ComparatorRepository comparatorFinder) {
        this.lineWriter = lineWriter;
        this.serializerFinder = serializerFinder;
        this.comparatorFinder = comparatorFinder;
    }

    @Override
    List<String> typeSafeWrite(final String path, final Map<Object, Object> map) {
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
        
        final Optional<Object> anyKey = Iterables.tryFind(value.keySet(), Predicates.notNull());

        final boolean thereIsJustOneKeyAndItIsNull = !anyKey.isPresent();
        if (thereIsJustOneKeyAndItIsNull) {
            return value;
        }

        final Comparator<Object> comparator = comparatorFinder.findFor(anyKey.get());
        
        if (comparator == null) {
            throw MissingComparatorException.missingMapKeyComparator(path, anyKey.get().getClass());
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