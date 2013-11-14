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


import com.codereligion.diff.comparator.CheckableComparator;
import com.codereligion.diff.serializer.CheckableSerializer;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.bean.Credential;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.sameInstance;
import static org.hamcrest.collection.IsIterableContainingInOrder.contains;
import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Tests {@link Configuration} features.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 */
public class ConfigurationTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();


	@Test
	public void useComparatorReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.useComparator(new StubComparator());

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useComparatorLeavesOriginalUntouched() {
		final Configuration original = new Configuration();
        final StubComparator comparator = new StubComparator(String.class);
        original.useComparator(comparator);

        assertThat(original.getCheckableComparators(), is(empty()));
	}

	@Test
	public void useComparatorAddsGivenComparator() {
		final Configuration original = new Configuration();
        final CheckableComparator<?> comparator = new StubComparator(String.class);
        final Configuration copy = original.useComparator(comparator);

        assertThat(copy.getCheckableComparators(), hasSize(1));
        assertThat(copy.getCheckableComparators().contains(comparator), is(true));
	}

	@Test
	public void useComparableReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.useNaturalOrderingFor(Credential.class);

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useComparableLeavesOriginalUntouched() {
		final Configuration original = new Configuration();
        original.useNaturalOrderingFor(Credential.class);

        assertThat(original.getComparables(), is(empty()));
	}

	@Test
	public void useComparableAddsGivenOrdering() {
		final Configuration original = new Configuration();
        final Class<? extends Comparable<?>> ordering = Credential.class;
        final Configuration copy = original.useNaturalOrderingFor(ordering);

        assertThat(copy.getComparables(), hasSize(1));
        assertThat(copy.getComparables().contains(ordering), is(true));
	}

	@Test
	public void useSerializerReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.useSerializer(new IncludeSerializer());

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useSerializerLeavesOriginalUntouched() {
		final Configuration original = new Configuration();
        final IncludeSerializer serializer = new IncludeSerializer(String.class);
        original.useSerializer(serializer);

        assertThat(original.getCheckableSerializer(), is(empty()));
	}

	@Test
	public void useSerializerAddsGivenSerialized() {
		final Configuration original = new Configuration();
        final CheckableSerializer<?> serializer = new IncludeSerializer(String.class);
        final Configuration copy = original.useSerializer(serializer);

        assertThat(copy.getCheckableSerializer(), hasSize(1));
		assertThat(copy.getCheckableSerializer().contains(serializer), is(true));
	}

	@Test
	public void excludePropertyReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.excludeProperty("foo");

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void excludePropertyLeavesOriginalUntouched() {
		final String propertyName = "foo";
		final Configuration original = new Configuration();
		final Configuration copy = original.excludeProperty(propertyName);

		assertThat(original.getExcludedProperties(), is(empty()));
		assertThat(copy.getExcludedProperties(), contains(propertyName));
	}

	@Test
	public void useBaseObjectNameReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.useBaseObjectName("foo");

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useBaseObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final Configuration original = new Configuration();
        final Configuration copy = original.useBaseObjectName(objectName);

        assertThat(original.getBaseObjectName(), is(not(objectName)));
		assertThat(copy.getBaseObjectName(), is(objectName));
	}

	@Test
	public void useWorkingObjectNameReturnsNewInstance() {
		final Configuration original = new Configuration();
		final Configuration copy = original.useWorkingObjectName("foo");

		assertThat(original, is(not(sameInstance(copy))));
	}

	@Test
	public void useWorkingObjectNameLeavesOriginalUntouched() {
		final String objectName = "foo";
		final Configuration original = new Configuration();
        final Configuration copy = original.useWorkingObjectName(objectName);

        assertThat(original.getWorkingObjectName(), is(not(objectName)));
        assertThat(copy.getWorkingObjectName(), is(objectName));
	}

	@Test
	public void useComparatorThrowsIllegalArgumentExceptionOnNullValue() {

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparator must not be null.");

		new Configuration().useComparator(null);
	}

	@Test
	public void useComparableThrowsIllegalArgumentExceptionOnNullValue() {

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparable must not be null.");

		new Configuration().useNaturalOrderingFor(null);
	}

	@Test
	public void addExcludedPropertyThrowsIllegalArgumentExceptionOnNullValue() {

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("propertyName must not be null.");

		new Configuration().excludeProperty(null);
	}

	@Test
	public void addExcludedSerializerThrowsIllegalArgumentExceptionOnNullValue() {

		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("serializer must not be null.");

		new Configuration().useSerializer(null);
	}

	@Test
	public void hasDefaultEmptyStringForBaseObjectName() {
		assertThat(new Configuration().getBaseObjectName(), is(""));
	}

	@Test
	public void hasDefaultEmptyStringForWorkingObjectName() {
		assertThat(new Configuration().getWorkingObjectName(), is(""));
	}
}