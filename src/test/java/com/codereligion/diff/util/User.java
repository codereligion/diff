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

import com.google.common.collect.Lists;
import java.util.List;

public class User {
	
	private Address address;
	
	private List<Credential> credentials = Lists.newArrayList();

	public List<Credential> getCredentials() {
		return credentials;
	}

	public Address getAddress() {
		return address;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public User withCredential(final Credential credential) {
		credentials.add(credential);
		return this;
	}

	public User withAddress(final Address address) {
		this.address = address;
		return this;
	}
}