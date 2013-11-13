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

import com.codereligion.diff.internal.CheckableComparatorFinder;
import com.codereligion.diff.internal.CheckableSerializerFinder;
import com.codereligion.diff.internal.PropertyInclusionChecker;
import com.codereligion.diff.internal.linewriter.LineWriter;
import com.codereligion.diff.internal.linewriter.RootLineWriter;
import com.google.common.collect.Lists;
import difflib.DiffUtils;
import difflib.Patch;
import java.util.Collections;
import java.util.List;
import javax.annotation.Nullable;
import javax.annotation.concurrent.ThreadSafe;
import static com.google.common.base.Preconditions.checkArgument;

/**
 * Creates a unified diff between two given objects applying the given
 * {@link Configuration} and returning a list of strings representing the detected
 * difference between the two objects without any contextual lines added.
 * 
 * @author Sebastian Gröbler
 * @since 10.05.2013
 * @see Configuration
 */
@ThreadSafe
public final class Differ {

    /**
     * The contextual lines added around the output
     */
    private static final int LINES_OF_CONTEXT_AROUND_OUTPUT = 0;

    /**
     * The configuration to use.
     */
    private final Configuration configuration;

    /**
     * The root differ which starts traversing the object graph.
     */
    private final LineWriter lineWriter;
    
    /**
     * Constructs a new instance for the given {@link Configuration}.
     * 
     * @param configuration the configuration to use
     * @throws IllegalArgumentException when the given {@code configuration} is
     *             {@code null}
     */
    public Differ(final Configuration configuration) {
        checkArgument(configuration != null, "configuration must not be null.");
        this.configuration = configuration;
        this.lineWriter = createRootLineWriter();
    }

    /**
     * Creates a new instance of the root line writer.
     *
     * @return a new instance of the root line writer.
     */
    private LineWriter createRootLineWriter() {
        return new RootLineWriter(
                new PropertyInclusionChecker(configuration.getExcludedProperties()),
                new CheckableSerializerFinder(configuration.getCheckableSerializer()),
                new CheckableComparatorFinder(configuration.getCheckableComparators(), configuration.getComparables()));
    }

    /**
     * Creates a diff for the given {@code base} and {@code working} objects by
     * putting every readable and not excluded property of the given objects on
     * a single line and comparing the lines with the common diff algorithm.
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
     * @throws com.codereligion.diff.exception.MissingSerializerException when the {@link Configuration} is missing
     *             a {@link com.codereligion.diff.serializer.CheckableSerializer} to perform the diff
     * @throws com.codereligion.diff.exception.MissingComparatorException when the {@link Configuration} is
     *             missing an {@link com.codereligion.diff.comparator.CheckableComparator} to perform the diff
     * @throws com.codereligion.diff.exception.UnreadablePropertyException when a getter of a property of the given
     *             object threw an exception during invocation
     */
    public List<String> diff(@Nullable final Object base, final Object working) {

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

    /**
     * Retrieves the simple name of the given {@code object}'s class.
     *
     * <p>
     * For example the simple class name for {@link java.lang.String} would be "String".
     *
     * @param object the object to get the simple class name from
     * @return the simple name of the class of the given object
     */
    private String getBeanName(final Object object) {
        return object.getClass().getSimpleName();
    }

    /**
     * Creates the actual diff from the "documents" representing the serialized objects.
     *
     * @param baseDocument the document representing the base object
     * @param workingDocument the document representing the working object
     * @return a list of strings representing the diff between the given documents
     */
    private List<String> unifiedDiff(final List<String> baseDocument, final List<String> workingDocument) {
        final Patch<String> patch = DiffUtils.diff(baseDocument, workingDocument);
        final boolean objectsHaveNoDiff = patch.getDeltas().isEmpty();

        if (objectsHaveNoDiff) {
            return Collections.emptyList();
        }

        final String baseObjectName = configuration.getBaseObjectName();
        final String workingObjectName = configuration.getWorkingObjectName();
        return DiffUtils.generateUnifiedDiff(baseObjectName, workingObjectName, baseDocument, patch, LINES_OF_CONTEXT_AROUND_OUTPUT);
    }
}
