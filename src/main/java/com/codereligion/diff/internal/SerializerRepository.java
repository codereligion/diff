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

import com.codereligion.diff.serializer.CheckableSerializer;
import com.codereligion.diff.internal.serializer.ClassSerializer;
import com.codereligion.diff.internal.serializer.NullSerializer;
import com.codereligion.diff.serializer.Serializer;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Set;
import static com.codereligion.diff.internal.serializer.InQuotesSerializer.wrapInQuotes;

/**
 * Allows the internals to easily lookup a matching serializer for a given object.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public final class SerializerRepository {

    /**
     * Custom checkable serializers.
     */
    private final Set<CheckableSerializer<?>> customSerializer;

    /**
     * Default serializers provided by the framework.
     */
    @SuppressWarnings("unchecked")
    private final Set<CheckableSerializer<?>> defaultSerializer = Sets.newHashSet(wrapInQuotes(ClassSerializer.INSTANCE), NullSerializer.INSTANCE);

    /**
     * Creates a new instance for the given {@code checkableSerializers}, decorating each one of them
     * in a quote wrapping serializer, so that the serialized values will be enclosed in single quotes.
     *
     * @param checkableSerializers the set of {@link CheckableSerializer} to store
     */
    public SerializerRepository(final Set<CheckableSerializer<?>> checkableSerializers) {
        this.customSerializer = wrapInQuotes(checkableSerializers);
    }

    /**
     * Tries to find a serializer for the given {@code object} by first searching through
     * custom serializers and second through the default serializers.
     *
     * @param object the object to find the serializer for
     * @return an optional of a {@link Serializer}
     */
    @SuppressWarnings("unchecked")
    public Optional<Serializer<Object>> findFor(final Object object) {
        for (final CheckableSerializer<?> serializer : customSerializer) {
            if (serializer.applies(object)) {
                return Optional.of((Serializer<Object>) serializer);
            }
        }

        for (final CheckableSerializer<?> serializer : defaultSerializer) {
            if (serializer.applies(object)) {
                return Optional.of((Serializer<Object>) serializer);
            }
        }

        return Optional.absent();
    }
}
