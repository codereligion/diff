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

/**
 * Serializes a given object to a string, if supported.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @param <T> The type of the object which should be serialized.
 */
public interface Serializer<T> {

    /**
     * Serializes a given {@code object} to a string.
     * 
     * @param object the {@link Object} to serialize
     * @return the serialized representation of the given object as a string
     */
    String serialize(T object);
}
