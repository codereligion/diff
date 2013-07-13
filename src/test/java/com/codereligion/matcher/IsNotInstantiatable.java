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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Expects the given {@link Class} to not provide a public constructor. This
 * class is particularly useful to cover otherwise dead code of private
 * constructors in static utility methods
 * 
 * @author Sebastian Gröbler
 * @since 19.06.2013
 */
public final class IsNotInstantiatable extends TypeSafeMatcher<Class<?>> {
	
	/**
	 * @return a new instance of
	 */
	public static Matcher<Class<?>> isNotInstantiatable() {
		return new IsNotInstantiatable();
	}

	@Override
	public void describeTo(final Description description) {
		description.appendText("That the given class is not instantiatable.");
	}

	@Override
	protected void describeMismatchSafely(final Class<?> item, final Description mismatchDescription) {
		mismatchDescription.appendText("It has a non private constructor.");
	}

	@Override
	protected boolean matchesSafely(Class<?> item) {

		try {
			final Constructor<?> constructor = item.getDeclaredConstructor();
			if (constructor.isAccessible() || !Modifier.isPrivate(constructor.getModifiers())) {
				return false;
			}

			constructor.setAccessible(true);
			constructor.newInstance();
		} catch (NoSuchMethodException e) {
			return false;
		} catch (InstantiationException e) {
			return true;
		} catch (InvocationTargetException e) {
			return true;
		} catch (IllegalAccessException e) {
			return true;
		}

		return true;
	}
}
