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

import org.eclipse.emf.ecore.EObject;

import com.google.common.base.Function;

/**
 * Class providing the construction of a FqnResolver (For our need, we need a
 * full qualified name resolver)
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class FqnResolver implements Function<EObject, String> {
	private final Function<EObject, String> nameResolver;

	/**
	 * Constructor
	 * 
	 * @param nameResolver
	 */
	public FqnResolver(Function<EObject, String> nameResolver) {
		super();
		this.nameResolver = nameResolver;
	}

	/**
	 * a method providing the construction of a fqn (full qualified name) (i.e
	 * parentFqn::fqn...)
	 */
	@Override
	public String apply(EObject from) {
		String fqn = nameResolver.apply(from);

		if (fqn != null && from.eContainer() != null) {
			String parentFqn = apply(from.eContainer());
			if (parentFqn != null) {
				fqn = parentFqn + "::" + fqn;
			}
		}

		return fqn;
	}
}
