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


import com.codereligion.diff.serializer.CheckableSerializer;
import com.codereligion.diff.serializer.Serializer;
import com.codereligion.diff.util.IncludeSerializer;
import com.codereligion.diff.util.bean.Credential;
import com.codereligion.diff.util.bean.User;
import com.google.common.base.Optional;
import com.google.common.collect.Sets;
import java.util.Set;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link SerializerRepository}.
 *
 * @author Sebastian Gröbler
 * @since 13.11.2013
 */
public class SerializerRepositoryTest {

    @Test
    public void findsExistingSerializer() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);
            final User user = new User();

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(user);
                final String expectedSerializedValue = serializer.serialize(user);
                final String actualSerializedValue = actual.get().serialize(user);

                then: {
                    assertThat(actualSerializedValue, containsString(expectedSerializedValue));
                }
            }
        }
    }

    @Test
    public void doesNotFindNonExistingSerializer() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(new Credential());

                then: {
                    assertThat(actual.isPresent(), is(false));
                }
            }
        }
    }

    @Test
    public void wrapsGivenSerializerResultsInQuotes() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(new Credential());

                then: {
                    assertThat(actual.isPresent(), is(false));
                }
            }
        }
    }

    @Test
    public void providesDefaultSerializerForClass() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(Credential.class);

                then: {
                    assertThat(actual.get().serialize(Credential.class), containsString("com.codereligion.diff.util.bean.Credential"));
                }
            }
        }
    }

    @Test
    public void providesDefaultSerializerForNullValue() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(null);

                then: {
                    assertThat(actual.get().serialize(null), is("null"));
                }
            }
        }
    }

    @Test
    public void prioritizesCustomSerializersOverDefaultSerializers() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(Class.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(User.class);

                then: {
                    assertThat(actual.get().serialize(User.class), containsString("class com.codereligion.diff.util.bean.User"));
                }
            }
        }
    }

    @Test
    public void returnNullInCaseNoMatchWasFound() {
        given: {
            final CheckableSerializer<Object> serializer = new IncludeSerializer(User.class);
            final Set<CheckableSerializer<?>> checkableSerializers = Sets.<CheckableSerializer<?>>newHashSet(serializer);
            final SerializerRepository finder = new SerializerRepository(checkableSerializers);

            when: {
                final Optional<Serializer<Object>> actual = finder.findFor(new Credential());

                then: {
                    assertThat(actual.isPresent(), is(false));
                }
            }
        }
    }
}