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
package com.codereligion.diff.internal.serializer;

import com.codereligion.diff.serializer.CheckableSerializer;

/**
 * Serializes classes into their canonical name.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public enum ClassSerializer implements CheckableSerializer<Class<?>> {

    /**
     * Singleton instance of this class.
     */
    INSTANCE; 

    @Override
    public boolean applies(final Object object) {
        return object instanceof Class;
    }

    @Override
    public String serialize(Class<?> object) {
        return object.getCanonicalName();
    }

}
