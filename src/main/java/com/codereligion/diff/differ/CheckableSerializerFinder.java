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

import com.codereligion.diff.serializer.CheckableSerializer;
import com.codereligion.diff.serializer.ClassSerializer;
import com.codereligion.diff.serializer.InQuotesSerializer;
import com.codereligion.diff.serializer.NullSerializer;
import com.google.common.collect.Sets;
import java.util.Set;

final class CheckableSerializerFinder {

    private final Set<CheckableSerializer<?>> customSerializer;
    private final Set<CheckableSerializer<?>> defaultSerializer;

    public CheckableSerializerFinder(final Set<CheckableSerializer<?>> checkableSerializers) {
        this.customSerializer = wrapAll(checkableSerializers);
        this.defaultSerializer = Sets.newHashSet(wrap(ClassSerializer.INSTANCE), NullSerializer.INSTANCE);
    }
    
    private CheckableSerializer<?> wrap(final CheckableSerializer<?> checkableSerializer) {
        return InQuotesSerializer.wrap(checkableSerializer);
    }

    private Set<CheckableSerializer<?>> wrapAll(final Set<CheckableSerializer<?>> checkableSerializers) {
        
        final Set<CheckableSerializer<?>> wrapped = Sets.newHashSet();
        for (CheckableSerializer<?> checkableSerializer : checkableSerializers) {
            wrapped.add(wrap(checkableSerializer));
        }
        return wrapped;
    }
    

    @SuppressWarnings("unchecked")
    public CheckableSerializer<Object> findFor(final Object object) {
        for (final CheckableSerializer<?> serializer : customSerializer) {
            if (serializer.applies(object)) {
                return (CheckableSerializer<Object>) serializer;
            }
        }

        for (final CheckableSerializer<?> serializer : defaultSerializer) {
            if (serializer.applies(object)) {
                return (CheckableSerializer<Object>) serializer;
            }
        }
        return null;
    }
}
