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
package com.codereligion.diff;

/**
 * Defines whether an instance of an implementation of this interface can be applied to a given object.
 */
public interface Checkable {

    /**
     * Defines whether this instance can be applied to the given {@code object}.
     *
     * @param object the object to check
     * @return true if this object can be applied to the given one
     */
    boolean applies(Object object);
}
