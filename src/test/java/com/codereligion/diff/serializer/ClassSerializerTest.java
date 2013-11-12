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
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link ClassSerializer}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class ClassSerializerTest {

    @Test
    public void appliesToAnyClass() throws Exception {
        final boolean result = ClassSerializer.INSTANCE.applies(User.class);
        assertThat(result, is(true));
    }

    @Test
    public void doesNotApplyToAnyObject() throws Exception {
        final boolean result = ClassSerializer.INSTANCE.applies(new User());
        assertThat(result, is(false));
    }

    @Test
    public void serializesIntoTheCanonicalName() throws Exception {
        final String result = ClassSerializer.INSTANCE.serialize(User.class);
        assertThat(result, is("com.codereligion.diff.util.bean.User"));

    }
}
