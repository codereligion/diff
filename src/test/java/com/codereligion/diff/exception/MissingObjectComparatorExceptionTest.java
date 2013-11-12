package com.codereligion.diff.exception;

import com.codereligion.diff.util.bean.User;
import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link MissingObjectComparatorException} factory methods.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class MissingObjectComparatorExceptionTest {

    @Test
    public void missingIterableComparatorFactoryMethodCreatesIterableSpecificMessage() throws Exception {

        final Throwable throwable = MissingObjectComparatorException.missingIterableComparator("Foo.bar");

        assertThat(throwable.getMessage(), is("Could not find CheckableComparator for iterable at 'Foo.bar'"));
    }

    @Test
    public void missingMapKeyComparatorFactoryMethodCreatesMapKeySpecificMessage() throws Exception {

        final Throwable throwable = MissingObjectComparatorException.missingMapKeyComparator("Foo.bar", User.class);

        assertThat(throwable.getMessage(), is("Could not find CheckableComparator for map keys of type 'User' at 'Foo.bar'"));
    }
}
