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

import java.util.List;

/**
 * Encapsulates type casting for those {@link CheckableLineWriter}s which another type than object.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 * @param <T> the type to which the value should be casted
 */
abstract class TypeSafeCheckableLineWriter<T> implements CheckableLineWriter {

    /**
     * Type safe version of the {@link CheckableLineWriter#write(String, Object)} method.
     *
     * @param path the path defining the position of the given value in the object graph
     * @param value the value to be serialized into a list of strings
     * @return a list of strings representing the document form of the given value
     * @throws com.codereligion.diff.exception.UnreadablePropertyException
     *          when a getter of a property of the given object
     *          threw an exception during invocation
     */
    abstract List<String> typeSafeWrite(String path, T value);

    @Override
    @SuppressWarnings("unchecked")
    public List<String> write(final String path, final Object value) {
        return typeSafeWrite(path, (T) value);
    }
}
