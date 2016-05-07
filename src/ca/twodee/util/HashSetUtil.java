package ca.twodee.util;

import java.util.HashSet;

public class HashSetUtil {
	/**
	 * TODO: WHY IS THIS EVEN NECESSARY???
	 */
	public static <E> Boolean isEqual(HashSet <E> set1, HashSet<E> set2) {
		if (set1.size() != set2.size()) {
			return false;
		}
		return set1.containsAll(set2);
	}
}
