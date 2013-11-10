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
package com.codereligion.diff.differ;


/**
 * Tests {@link Configuration} features.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 */
public class ConfigurationTest {
	
//	@Rule
//	public ExpectedException expectedException = ExpectedException.none();
//	
//	@Test
//	public void unknownComparatorCanNotBeFound() {
//		final Configuration config = new Configuration().useComparator(new StubComparator(String.class));
//		
//		assertThat(config.findFor(Integer.valueOf(1)), is(nullValue()));
//	}
//	
//	@Test
//	public void useComparatorReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.useComparator(new StubComparator());
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//	
//	@Test
//	public void useComparatorLeavesOriginalUntouched() {
//		final Configuration original = new Configuration();
//		original.useComparator(new StubComparator(String.class));
//		
//		assertThat(original.findFor(""), is(nullValue()));
//	}
//	
//	@Test
//	public void unknownComparableCanNotBeFound() {
//		
//		final Configuration config = new Configuration().useNaturalOrderingFor(String.class);
//		assertFalse(config.isComparable(Credential.class));
//	}
//	
//	@Test
//	public void useComparableReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.useNaturalOrderingFor(Credential.class);
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//
//	@Test
//	public void useComparableLeavesOriginalUntouched() {
//		final Configuration original = new Configuration();
//		original.useNaturalOrderingFor(Credential.class);
//		
//		assertThat(original.isComparable(new Credential()), is(Boolean.FALSE));
//	}
//	
//	@Test
//	public void unknownSerializerCanNotBeFound() {
//		final Configuration config = new Configuration().useSerializer(new IncludeSerializer(String.class));
//		
//		assertThat(config.findFor(Integer.valueOf(42)), is(nullValue()));
//	}
//	
//	@Test
//	public void useSerializerReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.useSerializer(new IncludeSerializer());
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//	
//	@Test
//	public void useSerializerLeavesOriginalUntouched() {
//		final Configuration original = new Configuration();
//		original.useSerializer(new IncludeSerializer(String.class));
//		
//		assertThat(original.findFor("foo"), is(nullValue()));
//	}
//	
//	@Test
//	public void excludePropertyReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.excludeProperty("foo");
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//	
//	@Test
//	public void excludePropertyLeavesOriginalUntouched() {
//		final String propertyName = "foo";
//		final Configuration original = new Configuration();
//		original.excludeProperty(propertyName);
//		
//		assertThat(original.isPropertyExcluded(propertyName), is(Boolean.FALSE));
//	}
//	
//	@Test
//	public void useBaseObjectNameReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.useBaseObjectName("foo");
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//	
//	@Test
//	public void useBaseObjectNameLeavesOriginalUntouched() {
//		final String objectName = "foo";
//		final Configuration original = new Configuration();
//		original.useBaseObjectName(objectName);
//		
//		assertThat(original.getBaseObjectName(), is(not(objectName)));
//	}
//	
//	@Test
//	public void useWorkingObjectNameReturnsNewInstance() {
//		final Configuration original = new Configuration();
//		final Configuration copy = original.useWorkingObjectName("foo");
//		
//		assertThat(original, is(not(sameInstance(copy))));
//	}
//	
//	@Test
//	public void useWorkingObjectNameLeavesOriginalUntouched() {
//		final String objectName = "foo";
//		final Configuration original = new Configuration();
//		original.useWorkingObjectName(objectName);
//		
//		assertThat(original.getWorkingObjectName(), is(not(objectName)));
//	}
//
//	@Test
//	public void useComparatorThrowsIllegalArgumentExceptionOnNullValue() {
//		
//		expectedException.expect(IllegalArgumentException.class);
//		expectedException.expectMessage("comparator must not be null.");
//		
//		new Configuration().useComparator(null);
//	}
//	
//	@Test
//	public void useComparableThrowsIllegalArgumentExceptionOnNullValue() {
//		
//		expectedException.expect(IllegalArgumentException.class);
//		expectedException.expectMessage("comparable must not be null.");
//		
//		new Configuration().useNaturalOrderingFor(null);
//	}
//	
//	@Test
//	public void addExcludedPropertyThrowsIllegalArgumentExceptionOnNullValue() {
//		
//		expectedException.expect(IllegalArgumentException.class);
//		expectedException.expectMessage("propertyName must not be null.");
//		
//		new Configuration().excludeProperty(null);
//	}
//	
//	@Test
//	public void addExcludedSerializerThrowsIllegalArgumentExceptionOnNullValue() {
//		
//		expectedException.expect(IllegalArgumentException.class);
//		expectedException.expectMessage("serializer must not be null.");
//		
//		new Configuration().useSerializer(null);
//	}
//	
//	@Test
//	public void hasDefaultEmptyStringForBaseObjectName() {
//		assertThat(new Configuration().getBaseObjectName(), is(""));
//	}
//	
//	@Test
//	public void hasDefaultEmptyStringForWorkingObjectName() {
//		assertThat(new Configuration().getWorkingObjectName(), is(""));
//	}
//	
//	@Test
//	public void allowsAddingOfAndCheckingForExluddedProperties() {
//		final String property = "property";
//		final Configuration config = new Configuration().excludeProperty(property);
//		
//		assertTrue(config.isPropertyExcluded(property));
//	}
//	
//	@Test
//	public void allowsAddingAndRetrievingOfSerializers() {
//		final CheckableSerializer<Object> expectedSerializer = new IncludeSerializer(Object.class);
//		final Configuration config = new Configuration().useSerializer(expectedSerializer);
//		
//		assertThat(config.findFor(new Object()), is(expectedSerializer));
//	}
//	
//	@Test
//	public void allowsAddingAndRetrievingOfCompartors() {
//		final StubComparator comparator = new StubComparator(Object.class);
//		final Configuration config = new Configuration().useComparator(comparator);
//		final Comparator<Object> expected = comparator;
//		final Comparator<Object> actual = config.findFor(new Object());
//		
//		assertThat(actual, is(expected));
//	}
//	
//	@Test
//	public void allowsAddingAndRetrievingOfComparables() {
//		final Configuration config = new Configuration().useNaturalOrderingFor(Credential.class);
//
//		assertTrue(config.isComparable(new Credential()));
//	}
//	
//	@Test
//	public void allowsSettingAndRetrievingOfBaseObjectName() {
//		final String objectName = "foo";
//		final Configuration diffConfig = new Configuration().useBaseObjectName(objectName);
//		
//		assertThat(objectName, is(diffConfig.getBaseObjectName()));
//	}
//	
//	@Test
//	public void allowsSettingAndRetrievingOfWorkingObjectName() {
//		final String objectName = "foo";
//		final Configuration diffConfig = new Configuration().useWorkingObjectName(objectName);
//		
//		assertThat(objectName, is(diffConfig.getWorkingObjectName()));
//	}
}