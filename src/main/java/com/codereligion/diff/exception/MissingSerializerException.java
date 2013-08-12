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
package com.codereligion.diff.exception;


/**
 * Indicates that the {@link com.codereligion.diff.differ.Differ} could not create the diff, because of a
 * missing {@link com.codereligion.diff.serializer.CheckableSerializer}.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 * @see com.codereligion.diff.differ.Differ
 * @see com.codereligion.diff.differ.DiffConfig
 * @see com.codereligion.diff.serializer.CheckableSerializer
 */
public final class MissingSerializerException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance of {@link MissingSerializerException} in case no
     * {@link com.codereligion.diff.serializer.CheckableSerializer} could be found to serialize an object.
     * 
     * @param path the path of the property which identifies the object
     * @param object the object for which no {@link com.codereligion.diff.serializer.CheckableSerializer} could be found
     * @return a new instance of {@link MissingSerializerException}
     */
    public static MissingSerializerException missingPropertySerializer(final String path, final Object object) {
        return new MissingSerializerException("Could not find CheckableSerializer for '" + object + "' at '" + path + "'");
    }

    /**
     * Creates a new instance of {@link MissingSerializerException} in case no
     * {@link com.codereligion.diff.serializer.CheckableSerializer} could be found to serialize keys of a map.
     * 
     * @param path the path of the property which identifies the map
     * @param type the type of the keys of the map
     * @return a new instance of {@link MissingSerializerException}
     */
    public static MissingSerializerException missingMapKeySerializer(final String path, final Class<?> type) {
        return new MissingSerializerException("Could not find CheckableSerializer for map key of type '" + type.getSimpleName() + "' at '" + path + "'");
    }

    private MissingSerializerException(final String message) {
        super(message);
    }
}
