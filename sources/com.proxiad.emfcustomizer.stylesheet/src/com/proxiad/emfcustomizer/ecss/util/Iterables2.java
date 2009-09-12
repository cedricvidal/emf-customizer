/*******************************************************************************
 * Copyright (c) 2008-2009 Cedric Vidal and ProxiAD Group
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Cedric Vidal - original idea, initial contribution and API
 *     ProxiAD Group - 1.0 release
 *******************************************************************************/

package com.proxiad.emfcustomizer.ecss.util;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.google.common.base.Predicate;
import com.google.common.collect.AbstractIterable;
import com.google.common.collect.Iterators;

/**
 * Class Iterables2 provide a static method which apply unicity in a given
 * Iterable<T>
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class Iterables2 {

	/**
	 * Take a generic Iterable<T> and sort out all duplicates from this
	 * Iterable<T>
	 * 
	 * @param <T>
	 * @param input
	 * @return un Iterable<T> générique en retirant les doublons
	 */
	public static <T> Iterable<T> unique(final Iterable<T> input) {

		Iterable<T> unique = new AbstractIterable<T>() {
			public Iterator<T> iterator() {
				final Set<T> set = new HashSet<T>();

				return Iterators.filter(input.iterator(), new Predicate<T>() {
					public boolean apply(T input) {
						return set.add(input);
					}
				});
			}
		};
		return unique;
	}
}
