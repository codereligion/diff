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

import java.lang.reflect.InvocationTargetException;

/**
 * Indicates that a property could not be read through its getter.
 * This happens when the getter throws an exception on invocation.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public class UnreadablePropertyException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * Creates a new instance for the given {@code path} and {@code exception}.
     *
     * @param path the path of the property which could not be read
     * @param exception the {@link InvocationTargetException} of which the cause will be chained with this instance
     */
    public UnreadablePropertyException(final String path, final InvocationTargetException exception) {
        super("Could not read property at '" + path + "' due to an exception during invocation.", exception.getCause());
    }
}
