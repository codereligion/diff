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

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.bean.Credential;
import java.util.Comparator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link DiffConfig} features.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 */
public class DiffConfigTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();

	@Test
	public void unknownComparatorCanNotBeFound() {
		final DiffConfig config = new DiffConfigBuilder().useComparator(new StubComparator(String.class)).build();
		
		assertThat(config.findComparatorFor(Integer.valueOf(1)), is(nullValue()));
	}

	@Test
	public void unknownComparableCanNotBeFound() {
		final DiffConfig config = new DiffConfigBuilder().useNaturalOrderingFor(String.class).build();
		assertFalse(config.isComparable(Credential.class));
	}

	@Test
	public void unknownSerializerCanNotBeFound() {
		final DiffConfig config = new DiffConfigBuilder().useSerializer(new IncludeSerializer(String.class)).build();
		assertThat(config.findSerializerFor(Integer.valueOf(42)), is(nullValue()));
	}

	@Test
	public void useComparatorThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparator must not be null.");
		
		new DiffConfigBuilder().useComparator(null).build();
	}
	
	@Test
	public void useComparableThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparable must not be null.");
		
		new DiffConfigBuilder().useNaturalOrderingFor(null).build();
	}
	
	@Test
	public void addExcludedPropertyThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("propertyName must not be null.");
		
		new DiffConfigBuilder().excludeProperty(null).build();
	}
	
	@Test
	public void addExcludedSerializerThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("serializer must not be null.");
		
		new DiffConfigBuilder().useSerializer(null).build();
	}
	
	@Test
	public void hasDefaultEmptyStringForBaseObjectName() {
		assertThat(new DiffConfigBuilder().build().getBaseObjectName(), is(""));
	}
	
	@Test
	public void hasDefaultEmptyStringForWorkingObjectName() {
		assertThat(new DiffConfigBuilder().build().getWorkingObjectName(), is(""));
	}
	
	@Test
	public void allowsAddingOfAndCheckingForExluddedProperties() {
		final String property = "property";
		final DiffConfig config = new DiffConfigBuilder().excludeProperty(property).build();
		
		assertTrue(config.isPropertyExcluded(property));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfSerializers() {
		final Serializer<Object> expectedSerializer = new IncludeSerializer(Object.class);
		final DiffConfig config = new DiffConfigBuilder().useSerializer(expectedSerializer).build();
		
		assertThat(config.findSerializerFor(new Object()), is(expectedSerializer));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfCompartors() {
		final StubComparator comparator = new StubComparator(Object.class);
		final DiffConfig config = new DiffConfigBuilder().useComparator(comparator).build();
		final Comparator<Object> expected = comparator;
		final Comparator<Object> actual = config.findComparatorFor(new Object());
		
		assertThat(actual, is(expected));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfComparables() {
		final DiffConfig config = new DiffConfigBuilder().useNaturalOrderingFor(Credential.class).build();

		assertTrue(config.isComparable(new Credential()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfBaseObjectName() {
		final String objectName = "foo";
		final DiffConfig DiffConfig2 = new DiffConfigBuilder().useBaseObjectName(objectName).build();
		
		assertThat(objectName, is(DiffConfig2.getBaseObjectName()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfWorkingObjectName() {
		final String objectName = "foo";
		final DiffConfig DiffConfig2 = new DiffConfigBuilder().useWorkingObjectName(objectName).build();
		
		assertThat(objectName, is(DiffConfig2.getWorkingObjectName()));
	}
}
