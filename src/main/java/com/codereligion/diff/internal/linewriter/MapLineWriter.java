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
import com.codereligion.diff.Serializer;
import com.google.common.base.Optional;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Serializes maps to lines after sorting them by their keys.
 *
 * @author Sebastian Gröbler
 * @since 14.11.2013
 */
class MapLineWriter extends TypeSafeCheckableLineWriter<Map<Object, Object>> {

    /**
     * Line writer to delegate the value serialization to
     */
    private final LineWriter lineWriter;

    /**
     * Repository to lookup serializers to use for serializing the keys.
     */
    private final SerializerRepository serializerRepository;

    /**
     * Repository to lookup comparators to use for sorting the keys.
     */
    private final ComparatorRepository comparatorRepository;

    /**
     * Creates a new instance for the given {@code lineWriter}, {@code serializerRepository} and
     * {@code comparatorRepository}.
     *
     * @param lineWriter the {@link LineWriter} to delegate value serialization to
     * @param serializerRepository repository to lookup key serializers
     * @param comparatorRepository repository to lookup key comparators
     */
    public MapLineWriter(
            final LineWriter lineWriter,
            final SerializerRepository serializerRepository,
            final ComparatorRepository comparatorRepository) {

        this.lineWriter = lineWriter;
        this.serializerRepository = serializerRepository;
        this.comparatorRepository = comparatorRepository;
    }

    @Override
    List<String> typeSafeWrite(final String path, final Map<Object, Object> map) {
        final SortedMap<Object, Object> mapProperty = transformToSortedMap(path, map);
        final List<String> lines = Lists.newArrayList();
        
        for (final Map.Entry<Object, Object> entry : mapProperty.entrySet()) {
            final Object key = entry.getKey();
            final Serializer<Object> serializer = findMapKeySerializerOrThrowException(path, key);
            final Object serializedKey = serializer.serialize(key);
            final String extendedPath = PathBuilder.extendPathWithMapIndex(path, serializedKey);
            lines.addAll(lineWriter.write(extendedPath, entry.getValue()));
        }
        
        return lines;
    }

    @Override
    public boolean applies(final Object value) {
        return value instanceof Map;
    }

    /**
     * Transforms the given map {@code value} into a {@link SortedMap}.
     *
     * @param path the path which describes the position of the given value in the object graph
     * @param value the map to be sorted
     * @return a new sorted map with the contents of the given map
     */
    private SortedMap<Object, Object> transformToSortedMap(final String path, final Map<Object, Object> value) {
        
        final Optional<Object> anyKey = Iterables.tryFind(value.keySet(), Predicates.notNull());

        final boolean thereIsJustOneKeyAndItIsNull = !anyKey.isPresent();
        if (thereIsJustOneKeyAndItIsNull) {
            return new TreeMap<Object, Object>();
        }

        final Comparator<Object> comparator = findComparatorOrThrowException(path, anyKey.get());
        final SortedMap<Object, Object> sortedMap = new TreeMap<Object, Object>(comparator);
        sortedMap.putAll(value);

        return sortedMap;
    }

    /**
     * Tries to find a serializer for the given key or throws an {@link MissingSerializerException}
     * if none could be found.
     *
     * @param path the path which describes the position of the given key's map in the object graph
     * @param key the key to find a serializer for
     * @return a {@link Serializer} for the given {@code key}
     * @throws MissingSerializerException when no serializer could be found for the given {@code keys}
     */
    private Serializer<Object> findMapKeySerializerOrThrowException(final String path, final Object key) {
        final Optional<Serializer<Object>> serializer = serializerRepository.findFor(key);
        if (!serializer.isPresent()) {
            throw MissingSerializerException.missingMapKeySerializer(path, key.getClass());
        }

        return serializer.get();
    }

    /**
     * Tries to find a matching comparator for the given {@code key} or throws a
     * {@link MissingComparatorException} if none was found.
     *
     * @param path the path which describes the position of the given key's map in the object graph
     * @param key the key to find a matching comparator for
     * @return a {@link Comparator} for the give {@code key}
     * @throws MissingComparatorException if no comparator could be found for the given item
     */
    private Comparator<Object> findComparatorOrThrowException(final String path, final Object key) {
        final Optional<Comparator<Object>> comparator = comparatorRepository.findFor(key);

        if (!comparator.isPresent()) {
            throw MissingComparatorException.missingMapKeyComparator(path, key.getClass());
        }

        return comparator.get();
    }
}