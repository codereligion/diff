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
 * Serializes a given object to a list of strings, where a string represents a line in the to be diffed document.
 * The path determines the position of the value in the object graph and might be extended in case the implementation
 * delegates further serialization.
 *
 * @author Sebastian Gröbler
 * @since 12.08.2013
 */
public interface LineWriter {

    /**
     * Serializes the given value to a list of strings and concatenates them with the given path.
     *
     * @param path the path defining the position of the given value in the object graph
     * @param value the value to be serialized into a list of strings
     * @return a list of strings representing the document form of the given value
     * @throws com.codereligion.diff.exception.UnreadablePropertyException
     *          when a getter of a property of the given object
     *          threw an exception during invocation
     */
    List<String> write(String path, Object value);
}