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

import com.codereligion.diff.exception.MissingSerializerException;
import com.codereligion.reflect.BeanIntrospections;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Set;

public class RootLineWriter implements LineWriter {

    private final List<CheckableLineWriter> lineWriters;
    private PropertyInclusionChecker propertyInclusionChecker;

    public RootLineWriter(final PropertyInclusionChecker propertyInclusionChecker,
                          final CheckableSerializerFinder serializerFinder,
                          final CheckableComparatorFinder comparatorFinder) {

        this.propertyInclusionChecker = propertyInclusionChecker;
        this.lineWriters = Lists.newArrayList(new SerializerLineWriter(serializerFinder),
                                              new IterableLineWriter(this, comparatorFinder),
                                              new MapLineWriter(this, serializerFinder, comparatorFinder));
    }

    @Override
    public List<String> write(final String path, final Object value) throws IllegalAccessException,
                                                                    InvocationTargetException,
                                                                    IntrospectionException {
        for (final CheckableLineWriter lineWriter : lineWriters) {
            if (lineWriter.applies(value)) {
                return lineWriter.write(path, value);
            }
        }
        return traverseProperties(path, value);
    }

    private List<String> traverseProperties(final String path, final Object value) throws IllegalAccessException,
                                                                                 InvocationTargetException,
                                                                                 IntrospectionException {

        final List<String> lines = Lists.newArrayList();
        final Class<?> beanClass = value.getClass();

        final Set<PropertyDescriptor> readableProperties = BeanIntrospections.getReadableProperties(beanClass);
        for (final PropertyDescriptor descriptor : Iterables.filter(readableProperties, propertyInclusionChecker)) {
            final String propertyName = descriptor.getName();
            final Method readMethod = descriptor.getReadMethod();
            final Object propertyValue = readMethod.invoke(value);
            final String extendedPath = PathBuilder.extendPathWithProperty(path, propertyName);
            lines.addAll(write(extendedPath, propertyValue));
        }

        final boolean serializationFailed = lines.isEmpty();
        if (serializationFailed) {
            throw MissingSerializerException.missingPropertySerializer(path, beanClass);
        }

        return lines;
    }
}
