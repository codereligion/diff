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


/**
 * TODO
 */
public class CheckableComparatorFinderTest {
	
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
}