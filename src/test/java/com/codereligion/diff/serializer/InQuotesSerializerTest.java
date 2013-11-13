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

import com.codereligion.diff.util.bean.User;
import com.google.common.collect.Sets;
import java.util.Set;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.core.Is.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link InQuotesSerializer}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class InQuotesSerializerTest {

    @Test
    public void wrapInQuotesDecoratesGivenObject() throws Exception {
        final CheckableSerializer<?> checkableSerializer = InQuotesSerializer.wrapInQuotes(mock(CheckableSerializer.class));

        assertThat(checkableSerializer, is(instanceOf(InQuotesSerializer.class)));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void wrapInQuotesDecoratesGivenObjects() throws Exception {
        final CheckableSerializer<?> first = mock(CheckableSerializer.class);
        final CheckableSerializer<?> second = mock(CheckableSerializer.class);
        final Set<CheckableSerializer<?>> mocks = Sets.newHashSet(first, second);
        final Set<CheckableSerializer<?>> checkableSerializers = InQuotesSerializer.wrapInQuotes(mocks);

        assertThat(checkableSerializers, hasItem(instanceOf(InQuotesSerializer.class)));
    }

    @Test
    public void appliesDelegatesToDecoratedObject() throws Exception {
        final CheckableSerializer<?> mock = mock(CheckableSerializer.class);
        final CheckableSerializer<?> checkableSerializer = InQuotesSerializer.wrapInQuotes(mock);

        final User user = new User();

        checkableSerializer.applies(user);

        verify(mock).applies(user);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void serializeEnclosesWithSingleQuotesAndDelegateActualSerializationToDecoratedObject() throws Exception {
        final CheckableSerializer<Object> mock = mock(CheckableSerializer.class);
        final CheckableSerializer<Object> checkableSerializer = (CheckableSerializer<Object>) InQuotesSerializer.wrapInQuotes(mock);

        final User user = new User();
        when(mock.serialize(user)).thenReturn("Foo");

        final String result = checkableSerializer.serialize(user);

        assertThat(result, is("'Foo'"));

        verify(mock).serialize(user);
    }

    private Matcher<Iterable<? super CheckableSerializer<?>>> hasItem(final Matcher<? super CheckableSerializer<?>> matcher) {
        return Matchers.hasItem(matcher);
    }
}
