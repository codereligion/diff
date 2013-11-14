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
package com.codereligion.diff.serializer;

import com.google.common.collect.Sets;
import java.util.Set;

/**
 * Decorates a given {@link CheckableSerializer} so that each serialized value is enclosed by single quotes.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public final class InQuotesSerializer implements CheckableSerializer<Object> {

    /**
     * The enclosing quotes.
     */
    private static final String VALUE_ENCLOSER = "'";

    /**
     * The decorated checkable serializer.
     */
    private final CheckableSerializer<Object> checkableSerializer;

    /**
     * Decorates the given serializer so that it's serialized value will be enclosed in single quotes.
     *
     * @param checkableSerializer the instance to decorate
     * @return a decorated instance of the given one
     */
    public static CheckableSerializer<?> wrapInQuotes(final CheckableSerializer<?> checkableSerializer) {
        return new InQuotesSerializer(checkableSerializer);
    }

    /**
     * Decorates the given serializers so that it's serialized values will be enclosed in single quotes.
     *
     * @param checkableSerializers the instances to decorate
     * @return decorated instances of the given ones
     */
    public static Set<CheckableSerializer<?>> wrapInQuotes(final Set<CheckableSerializer<?>> checkableSerializers) {

        final Set<CheckableSerializer<?>> wrapped = Sets.newHashSet();
        for (CheckableSerializer<?> checkableSerializer : checkableSerializers) {
            wrapped.add(wrapInQuotes(checkableSerializer));
        }
        return wrapped;
    }

    /**
     * Hides public instantiation.
     *
     * @param checkableSerializer the {@link CheckableSerializer} to decorate
     */
    @SuppressWarnings("unchecked")
    private InQuotesSerializer(final CheckableSerializer<?> checkableSerializer) {
        this.checkableSerializer = (CheckableSerializer<Object>) checkableSerializer;
    }

    @Override
    public boolean applies(Object object) {
        return checkableSerializer.applies(object);
    }

    @Override
    public String serialize(Object object) {
        return VALUE_ENCLOSER + checkableSerializer.serialize(object) + VALUE_ENCLOSER;
    }

}
