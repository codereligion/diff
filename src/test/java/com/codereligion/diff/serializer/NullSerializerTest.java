package com.codereligion.diff.serializer;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

/**
 * Tests the {@link NullSerializer}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class NullSerializerTest {

    @Test
    public void appliesToNullValue() throws Exception {
        final boolean result = NullSerializer.INSTANCE.applies(null);

        assertThat(result, is(true));
    }

    @Test
    public void doesNotApplyToNonNullValue() throws Exception {
        final boolean result = NullSerializer.INSTANCE.applies("foo");

        assertThat(result, is(false));
    }

    @Test
    public void serializesNullAsString() throws Exception {
        final String result = NullSerializer.INSTANCE.serialize(null);

        assertThat(result, is("null"));
    }
}
