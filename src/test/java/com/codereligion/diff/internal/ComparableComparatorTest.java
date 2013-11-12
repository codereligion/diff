package com.codereligion.diff.internal;

import org.junit.Test;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * Tests the {@link ComparableComparator}.
 *
 * @author Sebastian Gröbler
 * @since 12.11.2013
 */
public class ComparableComparatorTest {

    @Test
    public void comparesTwoObjectsByTheirNaturalOrdering() {

        final int actual = ComparableComparator.INSTANCE.compare(1, 2);
        final int expected = Integer.valueOf(1).compareTo(2);

        assertThat(actual, is(expected));
    }
}
