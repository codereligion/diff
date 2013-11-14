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

import com.codereligion.diff.internal.SerializerRepository;
import com.codereligion.diff.serializer.Serializer;
import com.google.common.base.Optional;
import java.util.Collections;
import java.util.List;

/**
 * Serializes objects to lines by using the {@link Serializer}s provided by the user.
 *
 * @author Sebastian Gröbler
 * @since 14.11.2013
 */
class SerializerLineWriter implements CheckableLineWriter {

    /**
     * Repository to look up serializers for the given objects.
     */
    private final SerializerRepository serializerRepository;

    /**
     * Creates a new instance for the given {@code serializerRepository}.
     *
     * @param serializerRepository the repository to look up serializers for the given objects
     */
    public SerializerLineWriter(final SerializerRepository serializerRepository) {
        this.serializerRepository = serializerRepository;
    }

    @Override
    public boolean applies(final Object value) {
        return serializerRepository.findFor(value).isPresent();
    }

    @Override
    public List<String> write(final String path, final Object value) {
        final Optional<Serializer<Object>> serializer = serializerRepository.findFor(value);
        final String serializedValue = PathBuilder.extendPathWithValue(path, serializer.get().serialize(value));
        return Collections.singletonList(serializedValue);
    }
}
