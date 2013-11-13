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

import com.codereligion.diff.util.bean.User;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link MissingObjectComparatorException} factory methods.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class MissingObjectComparatorExceptionTest {

    @Test
    public void missingIterableComparatorFactoryMethodCreatesIterableSpecificMessage() throws Exception {

        final Throwable throwable = MissingObjectComparatorException.missingIterableComparator("Foo.bar");

        assertThat(throwable.getMessage(), is("Could not find CheckableComparator for iterable at 'Foo.bar'"));
    }

    @Test
    public void missingMapKeyComparatorFactoryMethodCreatesMapKeySpecificMessage() throws Exception {

        final Throwable throwable = MissingObjectComparatorException.missingMapKeyComparator("Foo.bar", User.class);

        assertThat(throwable.getMessage(), is("Could not find CheckableComparator for map keys of type 'User' at 'Foo.bar'"));
    }
}
