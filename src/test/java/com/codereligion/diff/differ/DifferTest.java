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

import com.codereligion.diff.exception.MissingObjectComparatorException;
import com.codereligion.diff.exception.MissingSerializerException;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.NaturalOrderComparator;
import com.codereligion.diff.util.StubComparator;
import com.codereligion.diff.util.bean.Address;
import com.codereligion.diff.util.bean.Credential;
import com.codereligion.diff.util.bean.User;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static com.codereligion.matcher.IterableOfStringsMatchers.hasItem;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

/**
 * Tests {@link Differ} features.
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
		expectedException.expectMessage("configuration must not be null.");
		
		new Differ(null);
	}
	
	@Test
	public void throwsMissingSerializerExceptionForUnknownPropertyType() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class))
			.excludeProperty("class")
			.useComparator(new StubComparator(Credential.class));
		
		expectedException.expect(MissingSerializerException.class);
		expectedException.expectMessage("Could not find CheckableSerializer for 'Integer' at 'User.address.zipCode'");
		
		new Differ(configuration).diff(null, createUser());
	}
	
	@Test
	public void throwsMissingSerializerExceptionWhenNoSerializerCanBeFoundForMapKey() throws Exception {
		
		final Configuration configuration = new Configuration().useComparator(NaturalOrderComparator.newInstance(Credential.class));
		final Map<Credential, String> working = Maps.newHashMap();
		working.put(new Credential().withPassword("foo"), "bar");

		expectedException.expect(MissingSerializerException.class);
		expectedException.expectMessage("Could not find CheckableSerializer for map key of type 'Credential' at 'HashMap'");
		
		new Differ(configuration).diff(null, working);
	}
	
	@Test
	public void throwsMissingObjectComparatorExceptionForUncomparableIterableType() throws Exception {
		final Configuration configuration = new Configuration()
			.excludeProperty("class")
			.useSerializer(new IncludeSerializer(Credential.class, Address.class, String.class, Integer.class));
		
		expectedException.expect(MissingObjectComparatorException.class);
		expectedException.expectMessage("Could not find CheckableComparator for iterable at 'User.credentials'");
		
		final User working = createUser().withCredential(new Credential().withPassword("password"));
		
		new Differ(configuration).diff(null, working);
	}
	
	@Test
	public void throwsMissingObjectComparatorExceptionForUncomparableMapKeyType() throws Exception {
		final Configuration configuration = new Configuration();
		final Map<Credential, String> working = Maps.newHashMap();
		working.put(new Credential().withPassword("foo"), "bar");
		
		expectedException.expect(MissingObjectComparatorException.class);
		expectedException.expectMessage("Could not find CheckableComparator for map keys of type 'Credential' at 'HashMap'");
		new Differ(configuration).diff(null, working);
	}

	@Test
	public void throwsIllegalArgumentExceptionForNullWorkingObject() throws Exception {
		
		expectedException.expect(IllegalArgumentException.class);
		expectedException.expectMessage("working object must not be null.");
		
		new Differ(new Configuration()).diff(null, null);
	}
	
	@Test
	public void serilizesNullToTheWordNull() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		working.setStreet(null);
		
		final List<String> result = new Differ(configuration).diff(null, working);
		
		assertThat(result, hasItem(containsString("+Address.street=null")));
	}
	
	@Test
	public void serializesEmptyStringToTwoQuotes() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class));

		final Address working = new Address();
		working.setStreet("");
		
		final List<String> result = new Differ(configuration).diff(null, working);
		
		assertThat(result, hasItem(containsString("+Address.street=''")));
	}
	
	@Test
	public void providesDefaultSerializationForClassProperty() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Address.class))
			.useComparator(new StubComparator(Credential.class));
		
		final Address base = createAddress();
		final User working = createUser();
		
		final List<String> result = new Differ(configuration).diff(base, working);
		
		assertThat(result, hasItem("+User.class='com.codereligion.diff.util.bean.User'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInIterablesSerialization() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(List.class))
			.useComparator(new StubComparator(Credential.class));
		
		final List<Credential> working = Lists.newArrayList(new Credential().withPassword("password"));
		final List<String> result = new Differ(configuration).diff(null, working);
		
		// uses the toString serialization
		assertThat(result, hasItem("+ArrayList='[Credential [password=password]]'"));
		
		// there are no list indices
		assertThat(result, not(hasItem("+ArrayList.password[0]='password'")));
	}
	
	@Test
	public void specifiedComparablesHavePrecedenceOverCustomComparatorsForIterables() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class))
			// does not compare, always returns 0
			.useComparator(new StubComparator(Credential.class))
			// compares and negates
			.useNaturalOrderingFor(Credential.class);
		
		final List<Credential> working = Lists.newArrayList(
				new Credential().withPassword("password1"),
				new Credential().withPassword("password2"));
		
		final List<String> result = new Differ(configuration).diff(null, working);
		
		assertThat(result, hasItem("+ArrayList[0].password='password2'"));
		assertThat(result, hasItem("+ArrayList[1].password='password1'"));
	}
	
	@Test
	public void specifiedComparablesHavePrecedenceOverCustomComparatorsForMapKeys() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Credential.class, String.class))
			// does not compare, always returns 0
			.useComparator(new StubComparator(Credential.class))
			// compares and negates
			.useNaturalOrderingFor(Credential.class);
		
		final Map<Credential, String> map = Maps.newHashMap();
		map.put(new Credential().withPassword("aaaa"), "foo");
		map.put(new Credential().withPassword("bbbb"), "bar");
		
		final List<String> result = new Differ(configuration).diff(null, map);
		
		assertThat(result, hasItem("+HashMap['Credential [password=bbbb]']='bar'"));
		assertThat(result, hasItem("+HashMap['Credential [password=aaaa]']='foo'"));
	}
	
	@Test
	public void customSerializerHavePrecendenceOverBuiltInClassSerialization() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Class.class))
			.useComparator(new StubComparator(Credential.class));
		
		final List<String> result = new Differ(configuration).diff(null, "foo");
		
		// uses the toString serialization
		assertThat(result, hasItem("+String.class='class java.lang.String'"));
		
		// does not serialize class property with default class serialization
		assertThat(result, not(hasItem("+String.class='java.lang.String'")));
	}
	
	@Test
	public void diffsMaps() throws Exception {
		final Configuration configuration = new Configuration()
			.useComparator(NaturalOrderComparator.newInstance(String.class))
			.useSerializer(new IncludeSerializer(String.class, Integer.class));
		
		final Map<String, Integer> working = Maps.newHashMap();
			working.put("aaaa", 1);
			working.put("bbbb", 2);
			working.put("cccc", 3);
	
		final List<String> result = new Differ(configuration).diff(null, working);
		assertThat(result, hasItem("+HashMap['aaaa']='1'"));
		assertThat(result, hasItem("+HashMap['bbbb']='2'"));
		assertThat(result, hasItem("+HashMap['cccc']='3'"));
	}
	
	@Test
	public void diffsMapsWithComplexKeyObjects() throws Exception {
		final Configuration configuration = new Configuration()
			.useComparator(NaturalOrderComparator.newInstance(Credential.class))
			.useSerializer(new IncludeSerializer(Credential.class, Integer.class));
		
		final Map<Credential, Integer> working = Maps.newHashMap();
		working.put(new Credential().withPassword("aaaa"), 1);
		working.put(new Credential().withPassword("bbbb"), 2);
		working.put(new Credential().withPassword("cccc"), 3);
		
		final List<String> result = new Differ(configuration).diff(null, working);
		assertThat(result, hasItem("+HashMap['Credential [password=aaaa]']='1'"));
		assertThat(result, hasItem("+HashMap['Credential [password=bbbb]']='2'"));
		assertThat(result, hasItem("+HashMap['Credential [password=cccc]']='3'"));
	}
	
	@Test
	public void diffsMapsWithComplexValueObjects() throws Exception {
		final Configuration configuration = new Configuration()
			.useComparator(NaturalOrderComparator.newInstance(Credential.class))
			.useSerializer(new IncludeSerializer(Credential.class, String.class, Integer.class));
		
		final Map<Credential, Address> working = Maps.newHashMap();
		working.put(new Credential().withPassword("aaaa"), new Address().withStreet("someStreet").withZipCode(1));
		working.put(new Credential().withPassword("bbbb"), new Address().withStreet("someOtherStreet").withZipCode(2));
		
		final List<String> result = new Differ(configuration).diff(null, working);
		assertThat(result, hasItem("+HashMap['Credential [password=aaaa]'].street='someStreet'"));
		assertThat(result, hasItem("+HashMap['Credential [password=aaaa]'].zipCode='1'"));
		assertThat(result, hasItem("+HashMap['Credential [password=bbbb]'].street='someOtherStreet'"));
		assertThat(result, hasItem("+HashMap['Credential [password=bbbb]'].zipCode='2'"));
	}
	
	@Test
	public void diffsFlatObjects() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
		final Address base = createAddress();
		final Address working = createAddress().withStreet("something new");
		
		final List<String> result = new Differ(configuration).diff(base, working);
		
		assertThat(result, hasItem("-Address.street='street'"));
		assertThat(result, hasItem("+Address.street='something new'"));
	}
	
	@Test
	public void diffsNestedObjects() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
		
		final User base = createUser();
		final User working = createUser();
		working.getAddress().setStreet("something new");
		
		final List<String> result = new Differ(configuration).diff(base, working);
		
		assertThat(result, hasItem("-User.address.street='street'"));
		assertThat(result, hasItem("+User.address.street='something new'"));
	}
	
	@Test
	public void diffsIterableObjectsWithCustomComparator() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Integer.class))
			.useComparator(NaturalOrderComparator.newInstance(Integer.class));
		
		final Set<Integer> base = Sets.newHashSet(1, 2, 3);
		final Set<Integer> working = Sets.newHashSet(2, 3, 4);
		
		final List<String> result = new Differ(configuration).diff(base, working);
		assertThat(result, hasItem("-HashSet[0]='1'"));
		assertThat(result, hasItem("-HashSet[1]='2'"));
		assertThat(result, hasItem("-HashSet[2]='3'"));
		assertThat(result, hasItem("+HashSet[0]='2'"));
		assertThat(result, hasItem("+HashSet[1]='3'"));
		assertThat(result, hasItem("+HashSet[2]='4'"));
	}
	
	@Test
	public void ordersIterablesWithSpecifiedComparator() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Integer.class))
			.useComparator(NaturalOrderComparator.newInstance(Integer.class));
		
		final List<Integer> working = Lists.newArrayList(3, 2, 1);
		final List<String> result = new Differ(configuration).diff(null, working);
		
		assertThat(result, hasItem("+ArrayList[0]='1'"));
		assertThat(result, hasItem("+ArrayList[1]='2'"));
		assertThat(result, hasItem("+ArrayList[2]='3'"));
	}
	
	@Test
	public void ordersIterablesWithSpecifiedComparable() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Integer.class))
			.useNaturalOrderingFor(Integer.class);
		
		final List<Integer> working = Lists.newArrayList(3, 2, 1);
		final List<String> result = new Differ(configuration).diff(null, working);
		
		assertThat(result, hasItem("+ArrayList[0]='1'"));
		assertThat(result, hasItem("+ArrayList[1]='2'"));
		assertThat(result, hasItem("+ArrayList[2]='3'"));
	}
	
	@Test
	public void ordersMapKeysWithSpecifiedComparable() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Integer.class, String.class))
			.useNaturalOrderingFor(Integer.class);
		
		final Map<Integer, String> map = Maps.newHashMap();
		map.put(2, "foo");
		map.put(1, "bar");
		
		final List<String> result = new Differ(configuration).diff(null, map);
		
		assertThat(result, hasItem("+HashMap['1']='bar'"));
		assertThat(result, hasItem("+HashMap['2']='foo'"));
	}
	
	@Test
	public void ordersMapKeysWithSpecifiedComparator() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(Integer.class, String.class))
			.useComparator(NaturalOrderComparator.newInstance(Integer.class));
		
		final Map<Integer, String> map = Maps.newHashMap();
		map.put(2, "foo");
		map.put(1, "bar");
		
		final List<String> result = new Differ(configuration).diff(null, map);
		
		assertThat(result, hasItem("+HashMap['1']='bar'"));
		assertThat(result, hasItem("+HashMap['2']='foo'"));
	}
	
	@Test
	public void doesNotDiffEmptyIterables() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final User user = createUser();
		user.getCredentials().clear();
		
		final List<String> result = new Differ(configuration).diff(null, user);
		
		assertThat(result, not(hasItem("+User.credentials")));
	}
	
	@Test
	public void doesNotDiffEmptyMaps() throws Exception {
		final Configuration configuration = new Configuration();
		final List<String> result = new Differ(configuration).diff(null, Maps.newHashMap());
		
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void doesNotDiffWriteOnlyProperties() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class))
			.useNaturalOrderingFor(Credential.class);
		
		final User user = createUser();
		user.setNotReadableProperty("this should not be diffed");
		
		final List<String> result = new Differ(configuration).diff(null, user);
		
		assertThat(result, not(hasItem("+User.notReadableProperty='this should not be diffed'")));
	}
	
	@Test
	public void returnsEmptyListForSameObjects() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
		
		final User user = createUser();
		final List<String> result = new Differ(configuration).diff(user, user);
		assertTrue(result.isEmpty());
	}
	
	@Test
	public void allowsBaseToBeNull() throws Exception{
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
		final List<String> result = new Differ(configuration).diff(null, createUser());
		
		assertThat(result, hasItem("+User.address.street='street'"));
		assertThat(result, hasItem("+User.address.zipCode='12345'"));
	}
	
	@Test
	public void addsBeanNameInTheBeginningOfEachProperty() throws Exception {
		final Configuration configuration = new Configuration()
			.useSerializer(new IncludeSerializer(String.class, Integer.class))
			.useComparator(new StubComparator(Credential.class));
	
		final List<String> result = new Differ(configuration).diff(null, createAddress());
		
		assertThat(result, hasItem(startsWith("+" + Address.class.getSimpleName() + ".street")));
		assertThat(result, hasItem(startsWith("+" + Address.class.getSimpleName() + ".zipCode")));
	}
	
	@Test
	public void addsBaseObjectNameAsHeader() throws Exception {
		final Configuration configuration = new Configuration().useBaseObjectName("objectName");
		final List<String> result = new Differ(configuration).diff(null, createAddress());
		
		assertThat(result, hasItem("--- objectName"));
	}
	
	@Test
	public void addsWorkingObjectNameAsHeader() throws Exception {
		final Configuration configuration = new Configuration().useWorkingObjectName("objectName");
		final List<String> result = new Differ(configuration).diff(null, createAddress());
		
		assertThat(result, hasItem("+++ objectName"));
	}
	
	@Test
	public void addHunks() throws Exception {
		final Configuration configuration = new Configuration().useSerializer(new IncludeSerializer(String.class));
		final List<String> result = new Differ(configuration).diff(null, "Hello world!");
		
		assertThat(result, hasItem("@@ -1,0 +1,1 @@"));
		assertThat(result, hasItem("+String='Hello world!'"));
	}
	
	private User createUser() {
		return new User()
			.withAddress(createAddress());
	}

	private Address createAddress() {
		return new Address()
		.withStreet("street")
		.withZipCode(12345);
	}
}
