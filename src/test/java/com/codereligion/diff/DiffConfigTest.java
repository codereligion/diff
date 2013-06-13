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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.diff.util.Credential;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.StubComparator;
import java.util.Comparator;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link DiffConfig}.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 */
public class DiffConfigTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	public void addComparatorReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.addComparator(new StubComparator());
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void addComparatorLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.addComparator(new StubComparator(String.class));
		
		assertThat(original.findComparatorFor(""), is(nullValue()));
	}
	
	@Test
	public void addComparableReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.addComparable(Credential.class);
		
		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void addComparableLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.addComparable(Credential.class);
		
		assertThat(original.isComparable(new Credential()), is(Boolean.FALSE));
	}
	
	@Test
	public void addSerializerReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.addSerializer(new IncludeSerializer());
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void addSerializerLeavesOriginalUntouched() {
		final DiffConfig original = new DiffConfig();
		original.addSerializer(new IncludeSerializer(String.class));
		
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
	public void setBaseObjectNameReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.setBaseObjectName("foo");
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void setBaseObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final DiffConfig original = new DiffConfig();
		original.setBaseObjectName(objectName);
		
		assertThat(original.getBaseObjectName(), is(not(objectName)));
	}
	
	@Test
	public void setWorkingObjectNameReturnsNewInstance() {
		final DiffConfig original = new DiffConfig();
		final DiffConfig copy = original.setWorkingObjectName("foo");
		
		assertThat(original, is(not(sameInstance(copy))));
	}
	
	@Test
	public void setWorkingObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final DiffConfig original = new DiffConfig();
		original.setWorkingObjectName(objectName);
		
		assertThat(original.getWorkingObjectName(), is(not(objectName)));
	}

	@Test
	public void addComparatorThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparator must not be null.");
		
		new DiffConfig().addComparator(null);
	}
	
	@Test
	public void addComparableThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparable must not be null.");
		
		new DiffConfig().addComparable(null);
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
		
		new DiffConfig().addSerializer(null);
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
		final Serializer expectedSerializer = new IncludeSerializer(Object.class);
		final DiffConfig config = new DiffConfig().addSerializer(expectedSerializer);
		
		assertThat(config.findSerializerFor(new Object()), is(expectedSerializer));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfCompartors() {
		final StubComparator comparator = new StubComparator(Object.class);
		final DiffConfig config = new DiffConfig().addComparator(comparator);
		final Comparator<Object> expected = comparator;
		final Comparator<Object> actual = config.findComparatorFor(new Object());
		
		assertThat(actual, is(expected));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfComparables() {
		final DiffConfig config = new DiffConfig().addComparable(Credential.class);

		assertTrue(config.isComparable(new Credential()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfBaseObjectName() {
		final String objectName = "foo";
		final DiffConfig diffConfig = new DiffConfig().setBaseObjectName(objectName);
		
		assertThat(objectName, is(diffConfig.getBaseObjectName()));
	}
	
	@Test
	public void allowsSettingAndRetrievingOfWorkingObjectName() {
		final String objectName = "foo";
		final DiffConfig diffConfig = new DiffConfig().setWorkingObjectName(objectName);
		
		assertThat(objectName, is(diffConfig.getWorkingObjectName()));
	}
}
