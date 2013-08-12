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

import com.google.common.collect.Lists;
import difflib.DiffUtils;
import difflib.Patch;

import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import java.beans.IntrospectionException;
import java.lang.reflect.InvocationTargetException;
import java.util.Collections;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * Creates a unified diff between two given objects applying the given
 * {@link DiffConfig} and returning a list of strings representing the detected
 * difference between the two objects without any contextual lines added.
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
     * The root differ which starts traversing the object graph.
     */
    private final LineWriter lineWriter;
    
    /**
     * Constructs a new instance for the given {@link DiffConfig}.
     * 
     * @param diffConfig the config to use
     * @throws IllegalArgumentException when the given {@code diffConfig} is
     *             {@code null}
     */
    public Differ(final DiffConfig diffConfig) {
        checkArgument(diffConfig != null, "diffConfig must not be null.");
        this.diffConfig = diffConfig;
        this.lineWriter = new RootLineWriter(this.diffConfig);
    }

    /**
     * Creates a diff for the given {@code base} and {@code working} objects by
     * putting every readable property of the given objects on a single line and
     * comparing the lines with the common diff algorithm.
     * 
     * <p>
     * The returned list of strings represents the detected differences between
     * the given objects.
     * 
     * <p>
     * The {@code base} object is optional, leaving it out indicates that
     * {@code working} is newly created.
     * 
     * @param base the object which represents the state before a change
     * @param working the object which represents the state after a change
     * @throws IllegalArgumentException when the given {@code working} object is
     *             {@code null}
     * @throws com.codereligion.diff.exception.MissingSerializerException when the {@link DiffConfig} is missing
     *             a {@link com.codereligion.diff.serializer.CheckableSerializer} to perform the diff
     * @throws com.codereligion.diff.exception.MissingObjectComparatorException when the {@link DiffConfig} is
     *             missing an {@link com.codereligion.diff.comparator.CheckableComparator} to perform the diff
     * @throws InvocationTargetException when a getter of one of the given
     *             objects threw an exception
     * @throws IllegalAccessException when a getter of one of the given objects
     *             is not accessible
     * @throws IntrospectionException when one of the given objects could not be
     *             introspected
     */
    public List<String> diff(@Nullable final Object base, final Object working) throws IntrospectionException, IllegalAccessException, InvocationTargetException {

        checkArgument(working != null, "working object must not be null.");

        final List<String> serializedPropertiesOfBase = Lists.newArrayList();

        if (base != null) {
            final String simpleClassNameOfBase = getBeanName(base);
            serializedPropertiesOfBase.addAll(lineWriter.write(simpleClassNameOfBase, base));
        }

        final String simpleClassNameOfWorking = getBeanName(working);
        final List<String> serializedPropertiesOfWorking = lineWriter.write(simpleClassNameOfWorking, working);

        return unifiedDiff(serializedPropertiesOfBase, serializedPropertiesOfWorking);
    }

    private String getBeanName(final Object before) {
        return before.getClass().getSimpleName();
    }

    private List<String> unifiedDiff(final List<String> baseDocument, final List<String> workingDocument) {
        final Patch<String> patch = DiffUtils.diff(baseDocument, workingDocument);
        final boolean objectsHaveNoDiff = patch.getDeltas().isEmpty();

        if (objectsHaveNoDiff) {
            return Collections.emptyList();
        }

        final String baseObjectName = diffConfig.getBaseObjectName();
        final String workingObjectName = diffConfig.getWorkingObjectName();
        return DiffUtils.generateUnifiedDiff(baseObjectName, workingObjectName, baseDocument, patch, LINES_OF_CONTEXT_AROUND_OUTPUT);

    }
}
