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

import com.codereligion.diff.serializer.Serializer;

import java.util.Collections;
import java.util.List;

// TODO think of a better name
class SerializerLineWriter implements CheckableLineWriter {

    private final CheckableSerializerFinder finder;

    public SerializerLineWriter(final CheckableSerializerFinder customFinder) {
        this.finder = customFinder;
    }

    @Override
    public List<String> write(final String path, final Object value) {
        final Serializer<Object> serializer = finder.findFor(value);
        return Collections.singletonList(PathBuilder.extendPathWithValue(path, serializer.serialize(value)));
    }

    @Override
    public boolean applies(Object value) {
        return finder.findFor(value) != null;
    }
}
