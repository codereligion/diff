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
package com.codereligion.diff.internal.linewriter;

import com.codereligion.diff.internal.linewriter.PathBuilder;
import org.junit.Test;

import static com.codereligion.matcher.IsNotInstantiatable.isNotInstantiatable;
import static org.junit.Assert.assertThat;

/**
 * This is a kind of esoteric test to bring coverage into a utility class with a
 * private constructor.
 * 
 * @author Sebastian Gröbler
 * @since 19.06.2013
 */
public class PathBuilderTest {
	
	@Test
	public void isNotPublicInstantiatable() throws Exception {
		assertThat(PathBuilder.class, isNotInstantiatable());
	}
}
