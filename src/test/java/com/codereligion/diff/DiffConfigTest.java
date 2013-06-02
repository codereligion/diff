/*
 * Copyright 2012 The Diff Authors (www.codereligion.com)
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
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
	public void addComparatorThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("comparator must not be null.");
		
		new DiffConfig().addComparator(null);
	}
	
	@Test
	public void addExcludedPropertyThrowsIllegalArgumentExceptionOnNullValue() {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("propertyName must not be null.");
		
		new DiffConfig().excludePropery(null);
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
	public void allowsAddingAndRetrievingOfExcludedProperties() {
		final DiffConfig config = new DiffConfig().addComparator(StubComparator.INSTANCE);
		final Comparator<Object> expected = StubComparator.INSTANCE;
		
		assertThat(config.findComparatorFor(new Object()), is(expected));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfSerializers() {
		final Serializer expectedSerializer = new IncludeSerializer(Object.class);
		final DiffConfig config = new DiffConfig().addSerializer(expectedSerializer);
		
		assertThat(config.findSerializerFor(new Object()), is(expectedSerializer));
	}
	
	@Test
	public void allowsAddingAndRetrievingOfCompartors() {
		final String property = "property";
		final DiffConfig config = new DiffConfig().excludePropery(property);
		
		assertTrue(config.isPropertyExcluded(property));
	}
}
