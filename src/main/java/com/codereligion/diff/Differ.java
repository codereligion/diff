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
package com.codereligion.diff;

import static com.google.common.base.Preconditions.checkArgument;

import com.codereligion.diff.internal.ComparableComparator;
import com.codereligion.diff.internal.PathBuilder;
import com.codereligion.reflect.Reflector;
import com.google.common.collect.Lists;
import difflib.DiffUtils;
import difflib.Patch;
import java.beans.IntrospectionException;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.concurrent.ThreadSafe;

/**
 * Creates a unified diff between two given objects applying the given {@link DiffConfig} and returning
 * a list of strings representing the detected difference between the two objects without any contextual
 * lines added.
 * 
 * @author Sebastian Gröbler
 * @since 10.05.2013
 * @see DiffConfig
 */
@ThreadSafe
public final class Differ {
	
	private static final int LINES_OF_CONTEXT_AROUND_OUTPUT = 0;
	
	/**
	 * The configuration to use.
	 */
	private final DiffConfig diffConfig;
	
	/**
	 * Constructs a new instance for the given {@link DiffConfig}.
	 * 
	 * @param diffConfig the config to use
	 * @throws IllegalArgumentException when the given {@code diffConfig} is {@code null}
	 */
	public Differ(final DiffConfig diffConfig) {
		checkArgument(diffConfig != null, "diffConfig must not be null.");
		this.diffConfig = diffConfig;
	}
	
	/**
	 * Creates a diff for the given {@code base} and {@code working} objects by putting every readable
	 * property of the given objects on a single line and comparing the lines with the common diff algorithm.
	 * 
	 * <p>The returned list of strings represents the detected differences between the given objects.
	 * 
	 * <p>The {@code base} object is optional, leaving it out indicates that {@code working} is newly created.
	 * 
	 * @param base the object which represents the state before a change
	 * @param working the object which represents the state after a change
	 * @throws IllegalArgumentException when the given {@code working} object is {@code null}
	 * @throws MissingSerializerException when the {@link DiffConfig} is missing a {@link Serializer} to perform the diff
	 * @throws MissingObjectComparatorException when the {@link DiffConfig} is missing an {@link ObjectComparator} to perform the diff
	 * @throws InvocationTargetException when a getter of one of the given objects threw an exception
	 * @throws IllegalAccessException when a getter of one of the given objects is not accessible
	 * @throws IntrospectionException when one of the given objects could not be introspected
	 */
	public List<String> diff(
			final Object base,
			final Object working) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		checkArgument(working != null, "working object must not be null.");
		
		final List<String> serializedPropertiesOfBase = Lists.newArrayList();
		final List<String> serializedPropertiesOfWorking = Lists.newArrayList();
		
		if (base != null) {
			final String simpleClassNameOfBase = getBeanName(base);
			diffObject(serializedPropertiesOfBase, simpleClassNameOfBase, base);
		}
		
		final String simpleClassNameOfWorking = getBeanName(working);
		diffObject(serializedPropertiesOfWorking, simpleClassNameOfWorking, working);

		return unifiedDiff(serializedPropertiesOfBase, serializedPropertiesOfWorking);
	}

	private String getBeanName(final Object before) {
		return before.getClass().getSimpleName();
	}
	
	private List<String> unifiedDiff(final List<String> baseDocument, final List<String> workingDocument) {
		
		if (workingDocument.isEmpty()) {
			return Collections.emptyList();
		}

		final Patch patch = DiffUtils.diff(baseDocument, workingDocument);
		final boolean objectsHaveNoDiff = patch.getDeltas().isEmpty();

		if (objectsHaveNoDiff) {
			return Collections.emptyList();
		}

		final String baseObjectName = diffConfig.getBaseObjectName();
		final String workingObjectName = diffConfig.getWorkingObjectName();
		return DiffUtils.generateUnifiedDiff(baseObjectName, workingObjectName, baseDocument, patch, LINES_OF_CONTEXT_AROUND_OUTPUT);

	}

	private void diffObject(
			final List<String> lines,
			final String path,
			final Object value) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		if (value == null) {
			lines.add(PathBuilder.createPathWithNullValue(path));
			return;
		}
		
		final Serializer<Object> serializer = diffConfig.findSerializerFor(value);
		if (serializer != null) {
			lines.add(PathBuilder.createPathWithValue(path, serializer.serialize(value)));
			return;
		}
		
		if (value instanceof Iterable) {
			@SuppressWarnings("unchecked")
			final Iterable<Object> iterable = (Iterable<Object>) value;
			diffIterable(lines, path, iterable);
			return;
		}
		
		if (value instanceof Map) {
			@SuppressWarnings("unchecked")
			final Map<Object, Object> map = (Map<Object, Object>) value;
			diffMap(lines, path, map);
			return;
		}
		
		if (value instanceof Class) {
			lines.add(PathBuilder.createPathWithClassValue(path, (Class<?>) value));
			return;
		}
		
		diffProperties(lines, path, value);
	}
	
	private void diffIterable(
			final List<String> lines,
			final String path,
			final Iterable<Object> value) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final List<Object> iterableProperty = transformToSortedList(path, value);
		int i = 0;
		for (final Object nestedProperty : iterableProperty) {
			diffObject(lines, PathBuilder.createPathForIterableIndex(path, i++), nestedProperty);
		}
	}
	
	private void diffMap(
			final List<String> lines,
			final String path,
			final Map<Object, Object> value) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final Map<Object, Object> mapProperty = transformToSortedMap(path, value);
		
		for (final Map.Entry<Object, Object> entry : mapProperty.entrySet()) {
			final Object key = entry.getKey();
			final Serializer<Object> serializer = findMapKeySerializerOrThrowException(path, key);
			final Object serializedKey = serializer.serialize(key);
			diffObject(lines, PathBuilder.createPathForMapIndex(path, serializedKey), entry.getValue());
		}
	}

	private Serializer<Object> findMapKeySerializerOrThrowException(final String path, final Object key) {
		final Serializer<Object> serializer = diffConfig.findSerializerFor(key);
		if (serializer == null) {
			throw MissingSerializerException.missingMapKeySerializer(path, key.getClass());
		}
		
		return serializer;
	}

	private List<Object> transformToSortedList(final String path, final Iterable<Object> value) {
		final List<Object> list = Lists.newArrayList(value);

		if (list.isEmpty()) {
			return list;
		}
		
		final Object firstElement = list.get(0);
		final Comparator<Object> comparator = findComparatorFor(firstElement);
		
		if (comparator == null) {
			throw MissingObjectComparatorException.missingIterableComparator(path);
		}
		
		Collections.sort(list, comparator);
		return list;
	}
	
	private Comparator<Object> findComparatorFor(final Object value) {
		
		if (diffConfig.isComparable(value)) {
			return ComparableComparator.INSTANCE;
		}
		
		return diffConfig.findComparatorFor(value);
	}

	private Map<Object, Object> transformToSortedMap(final String path, final Map<Object, Object> value) {
		
		if (value.isEmpty()) {
			return Collections.emptyMap();
		}
		
		final Object anyKey = value.keySet().iterator().next();
		final Comparator<Object> comparator = findComparatorFor(anyKey);
		
		if (comparator == null) {
			throw MissingObjectComparatorException.missingMapKeyComparator(path, anyKey.getClass());
		}
		
		final Map<Object, Object> sortedMap = new TreeMap<Object, Object>(comparator);
		sortedMap.putAll(value);
		return sortedMap;
	}

	private void diffProperties(
			final List<String> lines,
			final String path,
			final Object value) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final int linesBefore = lines.size();
		final Class<?> beanClass = value.getClass();
	
		for (final PropertyDescriptor descriptor : Reflector.getReadableProperties(beanClass)) {
			final String propertyName = descriptor.getName();
	
			if (diffConfig.isPropertyExcluded(propertyName)) {
				continue;
			}
			final Method readMethod = descriptor.getReadMethod();
			final Object propertyValue = readMethod.invoke(value);
			diffObject(lines, PathBuilder.createFullPath(path, propertyName), propertyValue);
		}
		
		final boolean serializationFailed = linesBefore == lines.size();
		if (serializationFailed) {
			throw MissingSerializerException.missingPropertySerializer(path, value);
		}
	}
}
