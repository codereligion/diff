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
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static org.hamcrest.Matchers.sameInstance;
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
		final DiffConfig config = new DiffConfig().useComparator(new StubComparator(String.class));
		
		assertThat(config.findComparatorFor(Integer.valueOf(1)), is(nullValue()));
	}
	
	@Test
	public void useComparatorReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.useComparator(new StubComparator());
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void useComparatorLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.useComparator(new StubComparator(String.class));
		
		assertThat(original.findComparatorFor(""), is(nullValue()));
	}
	
	@Test
	public void unknownComparableCanNotBeFound() {
		
		final DiffConfig config = new DiffConfig().useNaturalOrderingFor(String.class);
		assertFalse(config.isComparable(Credential.class));
	}
	
	@Test
	public void useComparableReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.useNaturalOrderingFor(Credential.class);
		
		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useComparableLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.useNaturalOrderingFor(Credential.class);
		
		assertThat(original.isComparable(new Credential()), is(Boolean.FALSE));
	}
	
	@Test
	public void unknownSerializerCanNotBeFound() {
		final DiffConfig config = new DiffConfig().useSerializer(new IncludeSerializer(String.class));
		
		assertThat(config.findSerializerFor(Integer.valueOf(42)), is(nullValue()));
	}
	
	@Test
	public void useSerializerReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.useSerializer(new IncludeSerializer());
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void useSerializerLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.useSerializer(new IncludeSerializer(String.class));
		
		assertThat(original.findSerializerFor("foo"), is(nullValue()));
	}
	
	@Test
	public void excludePropertyReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.excludeProperty("foo");
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void excludePropertyLeavesOriginalUntouched() {
		final String propertyName = "foo";
		final DiffConfig original = new DiffConfig();
		original.excludeProperty(propertyName);
		
		assertThat(original.isPropertyExcluded(propertyName), is(Boolean.FALSE));
	}
	
	@Test
	public void useBaseObjectNameReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.useBaseObjectName("foo");
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void useBaseObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final DiffConfig original = new DiffConfig();
		original.useBaseObjectName(objectName);
		
		assertThat(original.getBaseObjectName(), is(not(objectName)));
	}
	
	@Test
	public void useWorkingObjectNameReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.useWorkingObjectName("foo");
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void useWorkingObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final DiffConfig original = new DiffConfig();
		original.useWorkingObjectName(objectName);
		
		assertThat(original.getWorkingObjectName(), is(not(objectName)));
	}

	@Test
	public void useComparatorThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparator must not be null.");
		
		new DiffConfig().useComparator(null);
	}
	
	@Test
	public void useComparableThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparable must not be null.");
		
		new DiffConfig().useNaturalOrderingFor(null);
	}
	
	@Test
	public void addExcludedPropertyThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("propertyName must not be null.");
		
		new DiffConfig().excludeProperty(null);
	}
	
	@Test
	public void addExcludedSerializerThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("serializer must not be null.");
		
		new DiffConfig().useSerializer(null);
	}
	
	@Test
	public void hasDefaultEmptyStringForBaseObjectName() {
		assertThat(new DiffConfig().getBaseObjectName(), is(""));
	}
	
	@Test
	public void hasDefaultEmptyStringForWorkingObjectName() {
		assertThat(new DiffConfig().getWorkingObjectName(), is(""));
	}
	
	@Test
	public void allowsAddingOfAndCheckingForExluddedProperties() {
		final String property = "property";
		final DiffConfig config = new DiffConfig().excludeProperty(property);
		
		assertTrue(config.isPropertyExcluded(property));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfSerializers() {
		final Serializer<Object> expectedSerializer = new IncludeSerializer(Object.class);
		final DiffConfig config = new DiffConfig().useSerializer(expectedSerializer);
		
		assertThat(config.findSerializerFor(new Object()), is(expectedSerializer));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfCompartors() {
		final StubComparator comparator = new StubComparator(Object.class);
		final DiffConfig config = new DiffConfig().useComparator(comparator);
		final Comparator<Object> expected = comparator;
		final Comparator<Object> actual = config.findComparatorFor(new Object());
		
		assertThat(actual, is(expected));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfComparables() {
		final DiffConfig config = new DiffConfig().useNaturalOrderingFor(Credential.class);

		assertTrue(config.isComparable(new Credential()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfBaseObjectName() {
		final String objectName = "foo";
		final DiffConfig diffConfig = new DiffConfig().useBaseObjectName(objectName);
		
		assertThat(objectName, is(diffConfig.getBaseObjectName()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfWorkingObjectName() {
		final String objectName = "foo";
		final DiffConfig diffConfig = new DiffConfig().useWorkingObjectName(objectName);
		
		assertThat(objectName, is(diffConfig.getWorkingObjectName()));
	}
}
