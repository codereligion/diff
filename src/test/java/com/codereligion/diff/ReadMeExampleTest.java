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

import static com.codereligion.matcher.IterableOfStringsMatchers.hasItem;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.assertThat;

import com.google.common.collect.Lists;
import difflib.DiffUtils;
import difflib.Patch;
import java.util.List;
import org.junit.Test;

/**
 * Proves that the code example from the read me is working.
 * 
 * @author sgroebler
 * @since 19.06.2013
 */
public class ReadMeExampleTest {
	
	@Test
	public void readMeExample() {
		
		final List<String> original = Lists.newArrayList("SomeDomainObject.someIntegerProperty='21'");
		final List<String> revised = Lists.newArrayList(
				"SomeDomainObject.someIntegerProperty='42'",
				"SomeDomainObject.someIterableProperty[0]='foo'",
				"SomeDomainObject.someIterableProperty[1]='bar'",
				"SomeDomainObject.someMapProperty[someMapKey]='someMapValue'");
		
		final Patch patch = DiffUtils.diff(original, revised);
		final List<String> diff = DiffUtils.generateUnifiedDiff("BaseObject", "WorkingObject", original, patch, 0);
		
		assertThat(diff, hasItem(containsString("--- BaseObject")));
		assertThat(diff, hasItem(containsString("+++ WorkingObject")));
		assertThat(diff, hasItem(containsString("@@ -1,1 +1,4 @@")));
		assertThat(diff, hasItem(containsString("-SomeDomainObject.someIntegerProperty='21'")));
		assertThat(diff, hasItem(containsString("+SomeDomainObject.someIntegerProperty='42'")));
		assertThat(diff, hasItem(containsString("+SomeDomainObject.someIterableProperty[0]='foo'")));
		assertThat(diff, hasItem(containsString("+SomeDomainObject.someIterableProperty[1]='bar'")));
		assertThat(diff, hasItem(containsString("+SomeDomainObject.someMapProperty[someMapKey]='someMapValue'")));
	}

}
