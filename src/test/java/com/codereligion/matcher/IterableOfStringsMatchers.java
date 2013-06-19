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
package com.codereligion.matcher;

import static org.hamcrest.Matchers.is;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;

/**
 * Convenience wrapper around hamcrest matcher which solves compiling issues
 * with generics.
 * 
 * @author sgroebler
 * @since 19.06.2013
 */
public final class IterableOfStringsMatchers {

	public static Matcher<Iterable<? super String>> hasItem(final String item) {
		return hasItem(is(item));
	}

	public static Matcher<Iterable<? super String>> hasItem(final Matcher<String> matcher) {
		return Matchers.hasItem(matcher);
	}
}
