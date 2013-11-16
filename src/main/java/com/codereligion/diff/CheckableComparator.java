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

import com.codereligion.diff.Checkable;
import java.util.Comparator;

/**
 * Combines the {@link Comparator} and {@link Checkable} interface to allow implementations of this interface
 * to define if they can compare a specific object.
 * 
 * @author Sebastian Gröbler
 * @since 11.05.2013
 * @param <T> The type of the object which should be compared.
 */
public interface CheckableComparator<T> extends Comparator<T>, Checkable {

}
