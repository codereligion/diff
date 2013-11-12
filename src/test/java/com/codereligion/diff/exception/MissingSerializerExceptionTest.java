package com.codereligion.diff.exception;

import com.codereligion.diff.util.bean.User;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link MissingSerializerException} factory methods.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class MissingSerializerExceptionTest {

    @Test
    public void missingPropertySerializerCreatesPropertySpecificMessage() throws Exception {
        final Throwable throwable = MissingSerializerException.missingPropertySerializer("Foo.bar", User.class);

        assertThat(throwable.getMessage(), is("Could not find CheckableSerializer for 'User' at 'Foo.bar'"));
    }

    @Test
    public void missingMapKeySerializerCreatesMapKeySpecificMessage() throws Exception {
        final Throwable throwable = MissingSerializerException.missingMapKeySerializer("Foo.bar", User.class);

        assertThat(throwable.getMessage(), is("Could not find CheckableSerializer for map key of type 'User' at 'Foo.bar'"));
    }
}
