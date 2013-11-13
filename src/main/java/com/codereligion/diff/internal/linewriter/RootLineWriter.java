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

public class RootLineWriter implements LineWriter {

    private final List<CheckableLineWriter> lineWriters;
    private PropertyInclusionChecker propertyInclusionChecker;

    public RootLineWriter(final PropertyInclusionChecker propertyInclusionChecker,
                          final SerializerRepository serializerFinder,
                          final ComparatorRepository comparatorFinder) {

        this.propertyInclusionChecker = propertyInclusionChecker;
        this.lineWriters = Lists.newArrayList(new SerializerLineWriter(serializerFinder),
                                              new IterableLineWriter(this, comparatorFinder),
                                              new MapLineWriter(this, serializerFinder, comparatorFinder));
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

    private List<String> traverseProperties(final String path, final Object value) {

        final List<String> lines = Lists.newArrayList();
        final Class<?> beanClass = value.getClass();

        final Set<PropertyDescriptor> readableProperties = BeanIntrospections.getReadableProperties(beanClass);
        for (final PropertyDescriptor descriptor : Iterables.filter(readableProperties, propertyInclusionChecker)) {
            final String propertyName = descriptor.getName();
            final Method readMethod = descriptor.getReadMethod();
            final String extendedPath = PathBuilder.extendPathWithProperty(path, propertyName);
            final Object propertyValue = safeInvoke(extendedPath, readMethod, value);
            lines.addAll(write(extendedPath, propertyValue));
        }

        final boolean serializationFailed = lines.isEmpty();
        if (serializationFailed) {
            throw MissingSerializerException.missingPropertySerializer(path, beanClass);
        }

        return lines;
    }

    private Object safeInvoke(final String path, final Method readMethod, final Object value) {
        try {
            return readMethod.invoke(value);
        } catch (final IllegalAccessException e) {
            throw new IllegalStateException("Could not read property value at: '" + path + "' through it's getter", e);
        } catch (final InvocationTargetException e) {
            throw new UnreadablePropertyException(path, e);
        }
    }
}
