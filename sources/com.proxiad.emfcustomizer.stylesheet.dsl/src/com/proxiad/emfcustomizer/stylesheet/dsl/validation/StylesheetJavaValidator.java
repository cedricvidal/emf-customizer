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

package com.proxiad.emfcustomizer.stylesheet.dsl.validation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.xtext.validation.Check;

import com.proxiad.emfcustomizer.ecss.AttributeDefinition;
import com.proxiad.emfcustomizer.ecss.EcssPackage;
import com.proxiad.emfcustomizer.ecss.Element;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.util.Constraints;
import com.proxiad.emfcustomizer.ecss.util.Queries;

public class StylesheetJavaValidator extends AbstractStylesheetJavaValidator {

	/**
	 * 
	 * @param attributeDefinition
	 */
	@Check
	public void checkAttributeDefinitionValueCompatibility(
			AttributeDefinition attributeDefinition) {
		Object value = Queries.value(attributeDefinition.getValue());
		if (value != null
				&& !Constraints.isCompatible(attributeDefinition, value)) {
			EStructuralFeature attribute = attributeDefinition.getAttribute();
			error(value + " is incompatible with attribute "
					+ attribute.getName(),
					EcssPackage.ATTRIBUTE_DEFINITION__VALUE);
		}
	}

	/**
	 * 
	 * @param referenceDefinition
	 */
	@Check
	public void checkReferenceDefinitionValueCompatibility(
			ReferenceDefinition referenceDefinition) {
		EObject value = referenceDefinition.getModelRef();
		if (value != null
				&& !Constraints.isCompatible(referenceDefinition, value)) {
			EStructuralFeature attribute = referenceDefinition.getAttribute();
			error("This " + value.eClass().getName()
					+ " is incompatible with attribute " + attribute.getName(),
					EcssPackage.ATTRIBUTE_DEFINITION__VALUE);
		}
	}

	@Check
	// TODO: Change the grammar ?
	public void checkNotNullDefinitionInStyle(Style style) {
		if (style.getDefinitions().size() == 0) {
			error("At least a Definition in Style must be defined",
					EcssPackage.STYLE__DEFINITIONS);
		}
	}

	@Check
	// TODO: Change the grammar ?
	public void checkNotNullDefinitionInElement(Element element) {
		if (element.getDefinitions().size() == 0) {
			error("At least a Definition in Element must be defined",
					EcssPackage.ELEMENT__DEFINITIONS);
		}
	}

}
