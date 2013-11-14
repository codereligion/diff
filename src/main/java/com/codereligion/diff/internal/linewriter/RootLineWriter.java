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

import com.codereligion.diff.exception.MissingSerializerException;
import com.codereligion.diff.exception.UnreadablePropertyException;
import com.codereligion.diff.internal.ComparatorRepository;
import com.codereligion.diff.internal.SerializerRepository;
import com.codereligion.diff.internal.PropertyInclusionChecker;
import com.codereligion.reflect.BeanIntrospections;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

/**
 * The root line writer which aggregates other line writers and provides recursive traversing of the object graph.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public class RootLineWriter implements LineWriter {

    /**
     * The line writers which are applied to the given object graph.
     */
    private final List<CheckableLineWriter> lineWriters;

    /**
     * Checker which allows to look up if properties are supposed to be included in the serialization of the object graph.
     */
    private final PropertyInclusionChecker propertyInclusionChecker;

    /**
     * Creates a new instance for the given {@code propertyInclusionChecker}, {@code serializerRepository} and
     * {@code comparatorRepository}.
     *
     * @param propertyInclusionChecker allows to look up which properties to included in the serialization
     * @param serializerRepository repository to find serializers
     * @param comparatorRepository repository to find comparators
     */
    public RootLineWriter(final PropertyInclusionChecker propertyInclusionChecker,
                          final SerializerRepository serializerRepository,
                          final ComparatorRepository comparatorRepository) {

        this.propertyInclusionChecker = propertyInclusionChecker;
        this.lineWriters = Lists.newArrayList(new SerializerLineWriter(serializerRepository),
                                              new IterableLineWriter(this, comparatorRepository),
                                              new MapLineWriter(this, serializerRepository, comparatorRepository));
    }

    @Override
    public List<String> write(final String path, final Object value) {
        for (final CheckableLineWriter lineWriter : lineWriters) {
            if (lineWriter.applies(value)) {
                return lineWriter.write(path, value);
            }
        }
        return traverseProperties(path, value);
    }

    /**
     * Builds a recursion to traverse the object graph together with the {@link RootLineWriter#write(String, Object)}
     * method. It traverses all public readable properties of the given {@code value} and delegates line writing
     * the write method.
     *
     * @param path the path representing the position of the given {@code value} in the object graph
     * @param value the object to traverse the properties of
     * @return a list of strings representing the serialized properties of the given {@code value}
     * @throws MissingSerializerException when a branch of the graph could not be serialized
     */
    private List<String> traverseProperties(final String path, final Object value) {

        final List<String> lines = Lists.newArrayList();
        final Class<?> beanClass = value.getClass();

        final Set<PropertyDescriptor> readableProperties = BeanIntrospections.getReadableProperties(beanClass);
        for (final PropertyDescriptor descriptor : Iterables.filter(readableProperties, propertyInclusionChecker)) {
            final String propertyName = descriptor.getName();
            final Method readMethod = descriptor.getReadMethod();
            final String extendedPath = PathBuilder.extendPathWithProperty(path, propertyName);
            final Object propertyValue = safeInvoke(extendedPath, value, readMethod);
            lines.addAll(write(extendedPath, propertyValue));
        }

        final boolean serializationFailed = lines.isEmpty();
        if (serializationFailed) {
            throw MissingSerializerException.missingPropertySerializer(path, beanClass);
        }

        return lines;
    }

    /**
     * Safely invokes the given {@code readMethod} on the given {@code object} and returns the result.
     *
     * @param path the path representing the position of the read methods property in the object graph
     * @param object the object to call the method on
     * @param readMethod the {@link Method} to call
     * @return the return value of the given {@code readMethod}
     * @throws UnreadablePropertyException in case the given {@code readMethod} threw an exception during invocation
     */
    private Object safeInvoke(final String path, final Object object, final Method readMethod) {
        try {
            return readMethod.invoke(object);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Could not read property value at: '" + path + "' through it's getter.", e);
        } catch (final InvocationTargetException e) {
            throw new UnreadablePropertyException(path, e);
        }
    }
}
