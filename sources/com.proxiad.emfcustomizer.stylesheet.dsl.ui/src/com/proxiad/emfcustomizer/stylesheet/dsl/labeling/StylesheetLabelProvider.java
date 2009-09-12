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

package com.proxiad.emfcustomizer.stylesheet.dsl.labeling;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.Keyword;
import org.eclipse.xtext.ui.core.DefaultLabelProvider;

import com.proxiad.emfcustomizer.ecss.AttributeDefinition;
import com.proxiad.emfcustomizer.ecss.Customize;
import com.proxiad.emfcustomizer.ecss.Element;
import com.proxiad.emfcustomizer.ecss.ModelLoad;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.Stylesheet;
import com.proxiad.emfcustomizer.ecss.TypeRef;
import com.proxiad.emfcustomizer.ecss.ValueType;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#labelProvider
 */
public class StylesheetLabelProvider extends DefaultLabelProvider {

	String text(Stylesheet model) {
		return model.eResource().getURI().lastSegment();
	}

	String text(Customize imp) {
		return imp.getImportURI();
	}

	String text(Style style) {
		String res = "<unnamed>";
		TypeRef typeRef = style.getTypeRef();
		if (typeRef != null) {
			EClass ref = typeRef.getRef();
			if (ref != null) {
				res = ref.getName();
			}
		}
		return res;
	}

	String text(TypeRef typeRef) {
		String name = typeRef.getRef().getName();
		String res = "<unnamed>";
		if (name != null) {
			res = name;
		}
		return res;
	}

	String text(Element element) {
		EObject modelRef = element.getModelRef();
		String name = null;
		if (modelRef != null) {
			name = text(modelRef);
		}
		String res = "<unnamed>";
		if (name != null) {
			res = name;
		}
		return res;
	}

	String text(AttributeDefinition attributeDefinition) {
		String name = (attributeDefinition.getAttribute()).getName();
		String res = "<unnamed>";
		if (name != null) {
			res = name;
		}
		return res;
	}

	String text(ReferenceDefinition referenceDefinition) {
		EObject modelRef = referenceDefinition.getModelRef();
		String name = null;
		if (modelRef != null) {
			name = text(modelRef);
		}
		String res = "<unnamed>";
		if (name != null) {
			res = name;
		}
		return res;
	}

	String image(Customize imp) {
		return "Import.gif";
	}

	String image(Stylesheet model) {
		return "customize.gif";
	}

	String image(Style style) {
		return "EClass.gif";
	}

	String image(TypeRef typeRef) {
		return "EClass.gif";
	}

	String image(Element element) {
		return "Element.gif";
	}

	String image(AttributeDefinition definition) {
		return "EAttribute.gif";
	}

	String image(ReferenceDefinition definition) {
		return "EReference.gif";
	}

	String image(EAttribute attribute) {
		return "EAttribute.gif";
	}

	String image(EReference reference) {
		return "EReference.gif";
	}

	String image(EClass eClass) {
		return "EClass.gif";
	}

	String image(EObject eObject) {
		return "Element.gif";
	}

	String image(ValueType value) {
		return "default.gif";
	}

	String image(Keyword kw) {
		return "keyword.gif";
	}

	String image(ModelLoad modelLoad) {
		return "modelLoad.gif";
	}

}
