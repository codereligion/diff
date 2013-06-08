package com.codereligion.diff;

import java.util.Comparator;

/**
 * Compares objects by casting them into {@link Comparable} instances.
 * 
 * @author sgroebler
 * @since 07.06.2013
 */
class ComparableComparator implements Comparator<Object> {

	static final Comparator<Object> INSTANCE = new ComparableComparator();
	
	@Override
	public int compare(final Object first, final Object second) {
		@SuppressWarnings("unchecked")
		final Comparable<Object> comparable = (Comparable<Object>) first;
		return comparable.compareTo(second);
	}
}
