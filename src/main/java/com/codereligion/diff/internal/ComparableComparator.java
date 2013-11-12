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

import java.util.Comparator;

/**
 * Compares objects by casting them into {@link Comparable} instances.
 * 
 * @author Sebastian Gröbler
 * @since 07.06.2013
 */
final class ComparableComparator implements Comparator<Object> {

    /**
     * Singleton instance of this class.
     */
    public static final Comparator<Object> INSTANCE = new ComparableComparator();

    @Override
    public int compare(final Object first, final Object second) {
        @SuppressWarnings("unchecked")
        final Comparable<Object> comparable = (Comparable<Object>) first;
        return comparable.compareTo(second);
    }
}
