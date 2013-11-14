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
package com.codereligion.diff.internal;

import com.google.common.base.Predicate;

import javax.annotation.Nullable;
import java.beans.PropertyDescriptor;
import java.util.Set;

/**
 * Allows the internals to easily lookup if a property is included for the diff or not.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public final class PropertyInclusionChecker implements Predicate<PropertyDescriptor> {

    /**
     * The simple names of the properties which are excluded from the diff.
     */
    private final Set<String> excludedProperties;

    /**
     * Creates a new instance for the given {@code excludedProperties}.
     *
     * @param excludedProperties the set of excluded properties, by name
     */
    public PropertyInclusionChecker(final Set<String> excludedProperties) {
        this.excludedProperties = excludedProperties;
    }

    @Override
    public boolean apply(@Nullable final PropertyDescriptor input) {

        if (input == null) {
            return false;
        }

        return !excludedProperties.contains(input.getName());
    }
}
