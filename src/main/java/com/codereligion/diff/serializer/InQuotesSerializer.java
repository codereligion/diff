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
package com.codereligion.diff.serializer;

public final class  InQuotesSerializer implements CheckableSerializer<Object> {
    
    private static final String VALUE_ENCLOSER = "'";
    
    private final CheckableSerializer<Object> checkableSerializer;
    
    public static CheckableSerializer<Object> wrap(final CheckableSerializer<?> checkableSerializer) {
        return new InQuotesSerializer(checkableSerializer);
    }
    
    @SuppressWarnings("unchecked")
    private InQuotesSerializer(final CheckableSerializer<?> checkableSerializer) {
        this.checkableSerializer = (CheckableSerializer<Object>) checkableSerializer;
    }

    @Override
    public boolean applies(Object object) {
        return checkableSerializer.applies(object);
    }

    @Override
    public String serialize(Object object) {
        return VALUE_ENCLOSER + checkableSerializer.serialize(object) + VALUE_ENCLOSER;
    }

}
