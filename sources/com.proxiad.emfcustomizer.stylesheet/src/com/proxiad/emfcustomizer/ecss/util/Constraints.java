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
import org.eclipse.emf.ecore.EStructuralFeature;

import com.proxiad.emfcustomizer.ecss.AttributeDefinition;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;

/**
 * Constraints are functions which return a boolean indicating whether a given
 * constraint is verified.
 * 
 * Used as common logic for both model validation and code completion.
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @see com.proxiad.emfcustomizer.validation.EmfCustomizerJavaValidator
 * @see com.proxiad.emfcustomizer.contentassist.EmfCustomizerProposalProvider
 * 
 */
public class Constraints {

	/**
	 * @param referenceDefinition
	 * @param value
	 * @return
	 */
	public static boolean isCompatible(
			final ReferenceDefinition referenceDefinition, EObject value) {
		EStructuralFeature attribute = referenceDefinition.getAttribute();
		if (attribute == null) {
			return true;
		}
		boolean compatible = attribute.getEType().isInstance(value);
		return compatible;
	}

	/**
	 * @param referenceDefinition
	 * @param value
	 * @return
	 */
	public static boolean isCompatible(
			final AttributeDefinition attributeDefinition, Object value) {
		EStructuralFeature attribute = attributeDefinition.getAttribute();
		if (attribute == null) {
			return true;
		}
		boolean compatible = attribute.getEType().isInstance(value);
		return compatible;
	}

}
