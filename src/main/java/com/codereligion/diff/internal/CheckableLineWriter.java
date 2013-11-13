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
package com.codereligion.diff.internal;

import com.codereligion.diff.Checkable;


/**
 * Combines the {@link LineWriter} and {@link Checkable} to provide an interface
 * for implementations which can write lines and determine if the can write specific objects
 * to lines.
 *
 * @author Sebastian Gröbler
 * @since 10.11.2013
 */
interface CheckableLineWriter extends LineWriter, Checkable {
    
}