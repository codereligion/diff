/*
 * Copyright 2012 The Diff Authors (www.codereligion.com)
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

import com.google.common.collect.Lists;
import difflib.DiffUtils;
import difflib.Patch;
import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import javax.annotation.Nullable;

/**
 * Creates a diff between two given objects applying the given {@link DiffConfig} and returning
 * a list of strings representing the detected difference between the two objects.
 * 
 * @author Sebastian Gröbler
 * @since 10.05.2013
 * @see DiffConfig
 */
public class Differ {
	
	/**
	 * The configuration to use.
	 */
	private final DiffConfig diffConfig;
	
	/**
	 * Constructs a new instance for the given {@link DiffConfig}.
	 * 
	 * @param diffConfig the config to use
	 */
	public Differ(final DiffConfig diffConfig) {
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
	 * @throws InvocationTargetException when a getter of one of the given objects threw an exception
	 * @throws IllegalAccessException when a getter of one of the given objects is not accessible
	 * @throws IntrospectionException when one of the given objects could not be introspected
	 * @throws MissingConfigException when the {@link DiffConfig} is missing essential data to diff the given objects
	 */
	public List<String> diff(
			@Nullable final Object base,
			final Object working) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final List<String> serializedPropertiesOfBase = Lists.newArrayList();
		final List<String> serializedPropertiesOfWorking = Lists.newArrayList();
		
		if (base != null) {
			final String simpleClassNameOfBase = getBeanName(base);
			diffObject(serializedPropertiesOfBase, simpleClassNameOfBase, base);
		}
		
		final String simpleClassNameOfWorking = getBeanName(working);
		diffObject(serializedPropertiesOfWorking, simpleClassNameOfWorking, working);

		final Patch patch = DiffUtils.diff(serializedPropertiesOfBase, serializedPropertiesOfWorking);
		final boolean objectsHaveNoDiff = patch.getDeltas().isEmpty();

		if (objectsHaveNoDiff) {
			return Collections.emptyList();
		}

		return DiffUtils.generateUnifiedDiff(diffConfig.getBaseObjectName(), diffConfig.getWorkingObjectName(), serializedPropertiesOfBase, patch, 0);
	}
	
	private String getBeanName(final Object before) {
		return before.getClass().getSimpleName();
	}

	private void diffObject(
			final List<String> lines,
			final String path,
			@Nullable final Object object) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		if (object == null) {
			lines.add(path + "=null");
			return;
		}
		
		final Serializer serializer = diffConfig.findSerializerFor(object);
		if (serializer != null) {
			lines.add(path + "='" + serializer.serialize(object) + "'");
			return;
		}
		
		if (object instanceof Iterable) {
			diffIterable(lines, path, object);
			return;
		}
		
		if (object instanceof Class) {
			lines.add(path + "='" + ((Class<?>) object).getCanonicalName() + "'");
			return;
		}
		
		diffObjectProperties(lines, path, object);
	}
	
	private void diffIterable(
			final List<String> lines,
			final String propertyName,
			final Object property) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final List<Object> iterableProperty = transformToSortedList((Iterable<?>) property);
		int i = 0;
		for (final Object nestedProperty : iterableProperty) {
			diffObject(lines, propertyName + "[" + i++ + "]", nestedProperty);
		}
	}

	private List<Object> transformToSortedList(final Iterable<?> iterable) {
		final List<Object> list = Lists.newArrayList(iterable);
		
		if (list.isEmpty()) {
			return list;
		}
		
		final Comparator<Object> comparator = diffConfig.findComparatorFor(list.get(0));
		
		if (comparator == null) {
			throw new MissingConfigException("Could not find comparator to sort: " + iterable);
		}
		
		Collections.sort(list, comparator);
		return list;
	}
	
	private void diffObjectProperties(
			final List<String> lines,
			final String path,
			final Object object) throws IntrospectionException, IllegalAccessException, InvocationTargetException {
		
		final int linesBefore = lines.size();
		final Class<?> beanClass = object.getClass();
		final BeanInfo beanInfo = Introspector.getBeanInfo(beanClass);
	
		for (final PropertyDescriptor descriptor : beanInfo.getPropertyDescriptors()) {
			final String propertyName = descriptor.getName();
	
			if (diffConfig.isPropertyExcluded(propertyName)) {
				continue;
			}
			
			final Method readMethod = descriptor.getReadMethod();
			
			if (readMethod == null) {
				continue;
			}
			
			final Object property = readMethod.invoke(object);
			final String fullPath = path + "." + propertyName;
			diffObject(lines, fullPath, property);
		}
		
		final boolean serializationFailed = linesBefore == lines.size();
		if (serializationFailed) {
			throw new MissingConfigException("Could not serialize property: " + path + " with value: " + object);
		}
	}
}
