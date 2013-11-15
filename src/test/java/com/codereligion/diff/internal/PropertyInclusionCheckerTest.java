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

import com.google.common.collect.Sets;
import org.junit.Test;

import java.beans.PropertyDescriptor;
import java.util.Collections;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Tests the {@link PropertyInclusionChecker}.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public class PropertyInclusionCheckerTest {

    @Test
    public void mustNotApplyForNullValue() {
        final Set<String> excludedProperties = Collections.emptySet();
        final PropertyInclusionChecker propertyInclusionChecker = new PropertyInclusionChecker(excludedProperties);
        assertThat(propertyInclusionChecker.apply(null), is(false));
    }

    @Test
    public void mustUsePropertiesNameAttribute() {
        // given
        final Set<String> excludedProperties = Sets.newHashSet("actualFieldName");
        final PropertyDescriptor propertyDescriptor = mock(PropertyDescriptor.class);
        when(propertyDescriptor.getDisplayName()).thenReturn("someLocalizedName");
        when(propertyDescriptor.getName()).thenReturn("actualFieldName");

        // when
        final PropertyInclusionChecker propertyInclusionChecker = new PropertyInclusionChecker(excludedProperties);
        final boolean result = propertyInclusionChecker.apply(propertyDescriptor);

        // then
        assertThat(result, is(false));
    }
}
