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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.diff.util.Address;
import com.codereligion.diff.util.Credential;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.User;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Tests {@link Differ}.
 * 
 * @author Sebastian Gröbler
 * @since 12.05.2013
 */
public class DifferTest {
	
	@Rule
	public ExpectedException expectedException = ExpectedException.none();
	
	@Test
	@SuppressWarnings("unused")
	public void throwsIllegalArgumentExceptionForNullDiffConfig() throws Exception {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("diffConfig must not be null.");
		
		new Differ(null);
	}
	
	@Test
	public void serilizesNullToTheWordNull() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.excludePropery("class")
			.addSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		
		assertThat(result, hasItem(containsString("null")));
	}
	
	@Test
	public void serializesEmptyStringToTwoQuotes() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.excludePropery("class")
			.addSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		working.setStreet("");
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		
		assertThat(result, hasItem(containsString("''")));
	}
	
	@Test
	public void providesDefaultSerializationForClassProperty() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(Address.class))
			.addComparator(new StubComparator(Credential.class));
		
		final Address base = createAddress();
		final User working = createUser();
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, hasItem("+User.class='com.codereligion.diff.util.User'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInIterablesSerialization() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(User.class, Address.class, Set.class))
			.addComparator(new StubComparator(Credential.class));
		
		final User base = createUser();
		final User working = createUser();
		working.getAddress().setStreet("something new");
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, not(hasItem(containsString("[0]"))));
	}
	
	@Test
	public void customComparablesHavePrecedenceOverCustomComparators() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class))
			// does not compare, always returns 0
			.addComparator(new StubComparator(Credential.class))
			// compares and negates
			.addComparable(Credential.class);
		
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.credentials[0].password='password2'"));
		assertThat(result, hasItem("+User.credentials[1].password='password1'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInClassSerialization() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(Address.class, Class.class))
			.addComparator(new StubComparator(Credential.class));
		
		final Address base = createAddress();
		final User working = createUser();
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, not(hasItem("+User.class='com.codereligion.diff.util.User'")));
	}
	
	
	@Test
	public void throwsMissingSerializerExceptionForUnknownPropertyType() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
		
		expectedException.expectMessage("Could not find serializer for 'User.address.zipCode' with value: 12345");
		
		new Differ(diffConfig).diff(null, createUser());
	}
	
	@Test
	public void throwsMissingComparatorExceptionForUncomparableIterableType() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.excludePropery("class")
			.addSerializer(new IncludeSerializer(Credential.class, Address.class, String.class, Integer.class));
		
		expectedException.expect(MissingObjectComparatorException.class);
		expectedException.expectMessage("Could not find object comparator for 'User.credentials' with value: [com.codereligion.diff.util.Credential");
		
		new Differ(diffConfig).diff(null, createUser());
	}
	
	@Test
	public void diffsFlatObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class, Integer.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
	
		final Address base = createAddress();
		final Address working = createAddress();
		working.setStreet("something new");
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, hasItem("-Address.street='street'"));
		assertThat(result, hasItem("+Address.street='something new'"));
	}
	
	@Test
	public void diffsNestedObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class, Integer.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
		
		final User base = createUser();
		final User working = createUser();
		working.getAddress().setStreet("something new");
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, hasItem("-User.address.street='street'"));
		assertThat(result, hasItem("+User.address.street='something new'"));
	}
	
	@Test
	public void diffsIterableObjectsWithCustomComparator() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(Integer.class))
			.addComparator(new StubComparator(Integer.class));
		
		final Set<Integer> base = Sets.newHashSet(1, 2, 3);
		final Set<Integer> working = Sets.newHashSet(2, 3, 4);
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		assertThat(result, hasItem("-HashSet[0]='1'"));
		assertThat(result, hasItem("+HashSet[0]='4'"));
	}
	
	@Test
	public void diffsIterableObjectsWithSpecifiedComparable() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class))
			.addComparable(Credential.class);
		
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.credentials[0].password='password2'"));
		assertThat(result, hasItem("+User.credentials[1].password='password1'"));
	}
	
	@Test
	public void returnsEmptyListForSameObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class, Integer.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
		
		final User user = createUser();
		final List<String> result = new Differ(diffConfig).diff(user, user);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void throwsIllegalArgumentExceptionForNullWorkingObject() throws Exception {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("working object must not be null.");
		
		new Differ(new DiffConfig()).diff(null, null);
	}
	
	@Test
	public void allowsBaseToBeNull() throws Exception{
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class, Integer.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
	
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.address.street='street'"));
		assertThat(result, hasItem("+User.address.zipCode='12345'"));
		assertThat(result, hasItem("+User.credentials[0].password='password1'"));
	}
	
	@Test
	public void addsBeanNameInTheBeginningOfEachProperty() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.addSerializer(new IncludeSerializer(String.class, Integer.class))
			.excludePropery("class")
			.addComparator(new StubComparator(Credential.class));
	
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem(containsString(User.class.getSimpleName())));
	}

	private User createUser() {
		return new User()
			.withAddress(createAddress())
			.withCredential(new Credential().withPassword("password1"))
			.withCredential(new Credential().withPassword("password2"));
	}

	private Address createAddress() {
		return new Address()
		.withStreet("street")
		.withZipCode(12345);
	}

	private Matcher<Iterable<? super String>> hasItem(final String item) {
		return hasItem(is(item));
	}

	private Matcher<Iterable<? super String>> hasItem(final Matcher<String> matcher) {
		return Matchers.hasItem(matcher);
	}
}
