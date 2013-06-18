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

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import com.codereligion.diff.util.Address;
import com.codereligion.diff.util.Credential;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.NaturalOrderComparator;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.User;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
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
			.useSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		working.setStreet(null);
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		
		assertThat(result, hasItem(containsString("+Address.street=null")));
	}
	
	@Test
	public void serializesEmptyStringToTwoQuotes() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		working.setStreet("");
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		
		assertThat(result, hasItem(containsString("+Address.street=''")));
	}
	
	@Test
	public void providesDefaultSerializationForClassProperty() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(Address.class))
			.useComparator(new StubComparator(Credential.class));
		
		final Address base = createAddress();
		final User working = createUser();
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, hasItem("+User.class='com.codereligion.diff.util.User'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInIterablesSerialization() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(User.class, Address.class, Set.class))
			.useComparator(new StubComparator(Credential.class));
		
		final User base = createUser();
		final User working = createUser();
		working.getAddress().setStreet("something new");
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, not(hasItem(containsString("[0]"))));
	}
	
	@Test
	public void specifiedComparablesHavePrecedenceOverCustomComparatorsForIterables() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class))
			// does not compare, always returns 0
			.useComparator(new StubComparator(Credential.class))
			// compares and negates
			.useNaturalOrderingFor(Credential.class);
		
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.credentials[0].password='password2'"));
		assertThat(result, hasItem("+User.credentials[1].password='password1'"));
	}
	
	@Test
	public void specifiedComparablesHavePrecedenceOverCustomComparatorsForMapKeys() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(Credential.class, String.class))
			// does not compare, always returns 0
			.useComparator(new StubComparator(Credential.class))
			// compares and negates
			.useNaturalOrderingFor(Credential.class);
		
		final Map<Credential, String> map = Maps.newHashMap();
		map.put(new Credential().withPassword("aaaa"), "foo");
		map.put(new Credential().withPassword("bbbb"), "bar");
		
		final List<String> result = new Differ(diffConfig).diff(null, map);
		
		assertThat(result, hasItem("+HashMap[Credential [password=bbbb]]='bar'"));
		assertThat(result, hasItem("+HashMap[Credential [password=aaaa]]='foo'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInClassSerialization() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(Address.class, Class.class))
			.useComparator(new StubComparator(Credential.class));
		
		final Address base = createAddress();
		final User working = createUser();
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		
		assertThat(result, not(hasItem("+User.class='com.codereligion.diff.util.User'")));
	}
	
	@Test
	public void throwsMissingSerializerExceptionForUnknownPropertyType() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class))
			.excludeProperty("class")
			.useComparator(new StubComparator(Credential.class));
		
		expectedException.expect(MissingSerializerException.class);
		expectedException.expectMessage("Could not find Serializer for '12345' at 'User.address.zipCode'");
		
		new Differ(diffConfig).diff(null, createUser());
	}
	
	@Test
	public void throwsMissingSerializerExceptionWhenNoSerializerCanBeFoundForMapKey() throws Exception {
		
		final DiffConfig diffConfig = new DiffConfig().useComparator(NaturalOrderComparator.newInstance(Credential.class));
		final Map<Credential, String> working = Maps.newHashMap();
		working.put(new Credential().withPassword("foo"), "bar");

		expectedException.expect(MissingSerializerException.class);
		expectedException.expectMessage("Could not find Serializer for map key of type 'Credential' at 'HashMap'");
		
		new Differ(diffConfig).diff(null, working);
	}
	
	@Test
	public void throwsMissingObjectComparatorExceptionForUncomparableIterableType() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.excludeProperty("class")
			.useSerializer(new IncludeSerializer(Credential.class, Address.class, String.class, Integer.class));
		
		expectedException.expect(MissingObjectComparatorException.class);
		expectedException.expectMessage("Could not find ObjectComparator for iterable at 'User.credentials'");
		
		new Differ(diffConfig).diff(null, createUser());
	}
	
	@Test
	public void throwsMissingObjectComparatorExceptionForUncomparableMapKeyType() throws Exception {
		final DiffConfig diffConfig = new DiffConfig();
		final Map<Credential, String> working = Maps.newHashMap();
		working.put(new Credential().withPassword("foo"), "bar");
		
		expectedException.expect(MissingObjectComparatorException.class);
		expectedException.expectMessage("Could not find ObjectComparator for map keys of type 'Credential' at 'HashMap'");
		new Differ(diffConfig).diff(null, working);
	}
	
	@Test
	public void diffsMaps() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useComparator(NaturalOrderComparator.newInstance(String.class))
			.useSerializer(new IncludeSerializer(String.class, Integer.class));
		
		final Map<String, Integer> working = Maps.newHashMap();
			working.put("aaaa", 1);
			working.put("bbbb", 2);
			working.put("cccc", 3);
	
		final List<String> result = new Differ(diffConfig).diff(null, working);
		assertThat(result, hasItem("+HashMap[aaaa]='1'"));
		assertThat(result, hasItem("+HashMap[bbbb]='2'"));
		assertThat(result, hasItem("+HashMap[cccc]='3'"));
	}
	
	@Test
	public void diffsMapsWithComplexKeyObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useComparator(NaturalOrderComparator.newInstance(Credential.class))
			.useSerializer(new IncludeSerializer(Credential.class, Integer.class));
		
		final Map<Credential, Integer> working = Maps.newHashMap();
		working.put(new Credential().withPassword("aaaa"), 1);
		working.put(new Credential().withPassword("bbbb"), 2);
		working.put(new Credential().withPassword("cccc"), 3);
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		assertThat(result, hasItem("+HashMap[Credential [password=aaaa]]='1'"));
		assertThat(result, hasItem("+HashMap[Credential [password=bbbb]]='2'"));
		assertThat(result, hasItem("+HashMap[Credential [password=cccc]]='3'"));
	}
	
	@Test
	public void diffsMapsWithComplexValueObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useComparator(NaturalOrderComparator.newInstance(Credential.class))
			.useSerializer(new IncludeSerializer(Credential.class, String.class, Integer.class));
		
		final Map<Credential, Address> working = Maps.newHashMap();
		working.put(new Credential().withPassword("aaaa"), new Address().withStreet("someStreet").withZipCode(1));
		working.put(new Credential().withPassword("bbbb"), new Address().withStreet("someOtherStreet").withZipCode(2));
		
		final List<String> result = new Differ(diffConfig).diff(null, working);
		assertThat(result, hasItem("+HashMap[Credential [password=aaaa]].street='someStreet'"));
		assertThat(result, hasItem("+HashMap[Credential [password=aaaa]].zipCode='1'"));
		assertThat(result, hasItem("+HashMap[Credential [password=bbbb]].street='someOtherStreet'"));
		assertThat(result, hasItem("+HashMap[Credential [password=bbbb]].zipCode='2'"));
	}
	
	@Test
	public void diffsFlatObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
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
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
		
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
			.useSerializer(new IncludeSerializer(Integer.class))
			.useComparator(new StubComparator(Integer.class));
		
		final Set<Integer> base = Sets.newHashSet(1, 2, 3);
		final Set<Integer> working = Sets.newHashSet(2, 3, 4);
		
		final List<String> result = new Differ(diffConfig).diff(base, working);
		assertThat(result, hasItem("-HashSet[0]='1'"));
		assertThat(result, hasItem("+HashSet[0]='4'"));
	}
	
	@Test
	public void ordersIterablesWithSpecifiedComparable() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.credentials[0].password='password2'"));
		assertThat(result, hasItem("+User.credentials[1].password='password1'"));
	}
	
	@Test
	public void ordersMapKeysWithSpecifiedComparable() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(Credential.class, String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final Map<Credential, String> map = Maps.newHashMap();
		map.put(new Credential().withPassword("aaaa"), "foo");
		map.put(new Credential().withPassword("bbbb"), "bar");
		
		final List<String> result = new Differ(diffConfig).diff(null, map);
		
		assertThat(result, hasItem("+HashMap[Credential [password=bbbb]]='bar'"));
		assertThat(result, hasItem("+HashMap[Credential [password=aaaa]]='foo'"));
	}
	
	@Test
	public void doesNotDiffEmptyIterables() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final User user = createUser();
		user.getCredentials().clear();
		
		final List<String> result = new Differ(diffConfig).diff(null, user);
		
		assertThat(result, not(hasItem("+User.credentials")));
	}
	
	@Test
	public void doesNotDiffEmptyMaps() throws Exception {
		final DiffConfig diffConfig = new DiffConfig();
		final List<String> result = new Differ(diffConfig).diff(null, Maps.newHashMap());
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void doesNotDiffWriteOnlyProperties() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final User user = createUser();
		user.setNotReadableProperty("this should not be diffed");
		
		final List<String> result = new Differ(diffConfig).diff(null, user);
		
		assertThat(result, not(hasItem("+User.notReadableProperty='this should not be diffed'")));
	}
	
	@Test
	public void returnsEmptyListForSameObjects() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
		
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
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
		final List<String> result = new Differ(diffConfig).diff(null, createUser());
		
		assertThat(result, hasItem("+User.address.street='street'"));
		assertThat(result, hasItem("+User.address.zipCode='12345'"));
		assertThat(result, hasItem("+User.credentials[0].password='password1'"));
	}
	
	@Test
	public void addsBeanNameInTheBeginningOfEachProperty() throws Exception {
		final DiffConfig diffConfig = new DiffConfig()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
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
