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

package com.codereligion.diff.util;

import com.codereligion.diff.ObjectComparator;

/**
 * Compares every object, constantly returns 0 as difference.
 * 
 * @author Sebastian Gröbler
 * @since 13.05.2013
 */
public class StubComparator implements ObjectComparator<Object>{
	
	public static final ObjectComparator<Object> INSTANCE = new StubComparator();
	
	@Override
	public int compare(Object o1, Object o2) {
		return internalCompare(o1, o2);
	}

	@Override
	public boolean compares(Object object) {
		return true;
	}

	@Override
	public int internalCompare(Object o1, Object o2) {
		return 0;
	}

}
