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


import com.codereligion.diff.comparator.CheckableComparator;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.bean.Credential;
import com.codereligion.diff.util.bean.User;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link CheckableComparatorFinder}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class CheckableComparatorFinderTest {

    @Test
    @SuppressWarnings("unchecked")
    public void shouldPrioritizeComparablesOverComparators() {
        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(new StubComparator(Credential.class));

        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);

        final CheckableComparatorFinder finder = new CheckableComparatorFinder(checkableComparators, comparables);
        final Comparator<Object> comparator = finder.findFor(new Credential());

        assertThat(comparator, is(ComparableComparator.INSTANCE));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldFindComparatorForRegisteredComparable() {
        final Set<CheckableComparator<?>> checkableComparators = Collections.emptySet();
        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);

        final CheckableComparatorFinder finder = new CheckableComparatorFinder(checkableComparators, comparables);
        final Comparator<Object> comparator = finder.findFor(new Credential());

        assertThat(comparator, is(ComparableComparator.INSTANCE));
    }

    @Test
    public void shouldFindRegisteredComparator() {
        final CheckableComparator<Object> stubComparator = new StubComparator(Credential.class);
        final Comparator<Object> expected = stubComparator;

        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(stubComparator);
        final Set<Class<? extends Comparable<?>>> comparables = Collections.emptySet();

        final CheckableComparatorFinder finder = new CheckableComparatorFinder(checkableComparators, comparables);
        final Comparator<Object> comparator = finder.findFor(new Credential());

        assertThat(comparator, is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldReturnNullWhenNoComparatorCouldBeFound() {
        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(new StubComparator(Credential.class));
        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);

        final CheckableComparatorFinder finder = new CheckableComparatorFinder(checkableComparators, comparables);
        final Comparator<Object> comparator = finder.findFor(new User());

        assertThat(comparator, is(nullValue()));
    }
}