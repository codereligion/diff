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
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link ComparatorRepository}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class ComparatorRepositoryTest {

    @Test
    @SuppressWarnings("unchecked")
    public void prioritizesComparablesOverComparators() {
        // given
        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(new StubComparator(Credential.class));
        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);
        final ComparatorRepository finder = new ComparatorRepository(checkableComparators, comparables);

        // when
        final Optional<Comparator<Object>> comparator = finder.findFor(new Credential());
        final Comparator<Object> expected = ComparableComparator.INSTANCE;

        //then
        assertThat(comparator.get(), is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void findsComparatorForRegisteredComparable() {
        // given
        final Set<CheckableComparator<?>> checkableComparators = Collections.emptySet();
        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);
        final ComparatorRepository finder = new ComparatorRepository(checkableComparators, comparables);

        // when
        final Optional<Comparator<Object>> comparator = finder.findFor(new Credential());
        final Comparator<Object> expected = ComparableComparator.INSTANCE;

        // then
        assertThat(comparator.get(), is(expected));
    }

    @Test
    public void findsRegisteredComparator() {
        // given
        final CheckableComparator<Object> stubComparator = new StubComparator(Credential.class);
        final Comparator<Object> expected = stubComparator;
        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(stubComparator);
        final Set<Class<? extends Comparable<?>>> comparables = Collections.emptySet();

        // when
        final ComparatorRepository finder = new ComparatorRepository(checkableComparators, comparables);
        final Optional<Comparator<Object>> comparator = finder.findFor(new Credential());

        // when
        assertThat(comparator.get(), is(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void returnsNullWhenNoComparatorCouldBeFound() {
        // given
        final Set<CheckableComparator<?>> checkableComparators = Sets.<CheckableComparator<?>>newHashSet(new StubComparator(Credential.class));
        final Set<Class<? extends Comparable<?>>> comparables = Sets.<Class<? extends Comparable<?>>>newHashSet(Credential.class);

        // when
        final ComparatorRepository finder = new ComparatorRepository(checkableComparators, comparables);
        final Optional<Comparator<Object>> comparator = finder.findFor(new User());

        // then
        assertThat(comparator.isPresent(), is(false));
    }
}