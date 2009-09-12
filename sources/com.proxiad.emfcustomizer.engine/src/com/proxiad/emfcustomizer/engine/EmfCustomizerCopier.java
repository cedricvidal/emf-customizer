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

package com.proxiad.emfcustomizer.engine;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.FeatureMapUtil;
import org.eclipse.emf.ecore.util.EcoreUtil.Copier;
import org.eclipse.xtext.linking.impl.SimpleAttributeResolver;

import com.proxiad.emfcustomizer.ecss.AttributeDefinition;
import com.proxiad.emfcustomizer.ecss.Customize;
import com.proxiad.emfcustomizer.ecss.Definition;
import com.proxiad.emfcustomizer.ecss.EcssPackage;
import com.proxiad.emfcustomizer.ecss.Element;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.Stylesheet;
import com.proxiad.emfcustomizer.ecss.util.FqnResolver;
import com.proxiad.emfcustomizer.ecss.util.Queries;

/**
 * Extension of {@see org.eclipse.emf.ecore.util.EcoreUtil.Copier}. This
 * extension allow to customize the EMF Copier which copies a model. This is
 * possible with the Template Code Pattern using from Copier.
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * 
 */
public class EmfCustomizerCopier extends Copier {

	/**
	 * Generated Serial Version
	 */
	private static final long serialVersionUID = 5410744627442975070L;

	/**
	 * Get the FqnResolver (e.g "parentfqn::fqn")
	 */
	private static FqnResolver FQN_RESOLVER = new FqnResolver(
			SimpleAttributeResolver.NAME_RESOLVER);

	/**
	 * Static Variables providing acces to (@see
	 * /com.proxiad.emfcustomizer/src/com/proxiad/emfcustomizer/Extensions.ext)
	 * this file contains the Xtend/Xpand fonctions from EMF Styling legacy
	 */
	private static final String EXTENSIONS = "com::proxiad::emfcustomizer::Extensions";
	private static final String TYPE_EXTENSION_NAME = "type";
	private static final String ID_EXTENSION_NAME = "id";

	private Stylesheet css = null;
	private Map<String, Element> elementMap;
	private String modelPath;

	private HashMap<String, Style> stylesMap;

	public String getModelPath() {
		return modelPath;
	}

	public void setModelPath(String modelPath) {
		this.modelPath = modelPath;
	}

	public Stylesheet getCss() {
		return css;
	}

	public void setCss(Stylesheet model) {
		this.css = model;
	}

	/**
	 * Customization of EAttribut copy
	 */
	@Override
	protected void copyAttribute(EAttribute attribute, EObject object,
			EObject copyEObject) {
		boolean copied = doCopy(attribute, object, copyEObject);

		if (!copied) {
			super.copyAttribute(attribute, object, copyEObject);
		}
	}

	/**
	 * Customization of EReference copy
	 */
	@Override
	protected void copyReference(EReference reference, EObject object,
			EObject copyEObject) {
		boolean copied = doCopy(reference, object, copyEObject);
		if (!copied) {
			super.copyReference(reference, object, copyEObject);
		}
	}

	/**
	 * This method does the actual {@link EStructuralFeature} feature. This
	 * method does the copy process if necessary else it delegates it to the
	 * default copy behaviour
	 * 
	 * @param feature
	 * @param object
	 * @param copyEObject
	 * @param copied
	 * @return true to block the default copy and false to initiate it
	 */
	private boolean doCopy(EStructuralFeature feature, EObject object,
			EObject copyEObject) {
		boolean copied = false;
		if (!FeatureMapUtil.isFeatureMap(feature) && !feature.isMany()) {

			if (!copied) {
				String objectId = id(object);
				Element element = elementMap.get(objectId);
				if (element != null) {
					Object newValue = getDefinitionValue(element, feature);
					if (newValue != null) {
						copyEObject.eSet(getTarget(feature), newValue);
						copied = true;
					}
				}
			}

			if (!copied) {
				String classId = id(object.eClass());
				String objectId = id(object);
				Style style = stylesMap.get(classId);
				if (style != null) {
					String modelRefId = fqn(style.getModelRef());
					if (modelRefId == null || objectId.startsWith(modelRefId)) {
						Object newValue = getDefinitionValue(style, feature);
						if (newValue != null) {
							copyEObject.eSet(getTarget(feature), newValue);
							copied = true;
						}
					}
				}
			}

		}
		return copied;
	}

	private String fqn(EObject object) {
		return FQN_RESOLVER.apply(object);
	}

	/**
	 * Get the value (of AttributeDefinition)/modelref(of ReferenceDefinition)
	 * in the XText (0.7.2) grammar
	 * 
	 * @param style
	 *            a grammar element
	 * @param feature
	 *            the EStructuralFeature corresponding to the output value
	 * @return an Object representing the value in Definition
	 */
	private Object getDefinitionValue(EObject style, EStructuralFeature feature) {
		Collection<Definition> definitions = findDefinitions(style);
		for (Definition definition : definitions) {
			if (definition.getAttribute().getName().equals(feature.getName())) {
				Object value = null;
				if (definition instanceof AttributeDefinition) {
					AttributeDefinition attributeDefinition = (AttributeDefinition) definition;
					value = Queries.value(attributeDefinition.getValue());
				}
				if (definition instanceof ReferenceDefinition) {
					ReferenceDefinition refDef = (ReferenceDefinition) definition;
					value = refDef.getModelRef();
				}
				return value;
			}
		}
		return null;
	}

	/**
	 * @param object
	 * @return un String
	 */
	protected String id(EObject object) {
		return fqn(object);
	}

	/**
	 * @param style
	 * @return une EClass
	 */
	protected EClass type(Style style) {
		return Queries.type(style.getTypeRef());
	}

	/**
	 * Construction from a stylesheet file (*.emfcustomizer) of the two
	 * HashMap<String, Element> stylesMap & elementMap. Each of the 2 maps are
	 * from the XText (0.7.2) grammar. Method which calls Xtend.
	 */
	public void initCss() {

		Customize customize = findCustomize(css);
		Collection<Style> styles = findStyles(css);
		stylesMap = new HashMap<String, Style>();
		for (Style style : styles) {
			EClass styleType = type(style);
			stylesMap.put(id(styleType), style);
		}

		Collection<Element> elements = findElements(css);
		elementMap = new HashMap<String, Element>();
		for (Element element : elements) {
			// Index the elements by the fqn of the reference model
			String fqn = FQN_RESOLVER.apply(element.getModelRef());
			elementMap.put(fqn, element);
		}

		// getModel() becomes getImportURI() on TMF XText
		modelPath = customize.getImportURI();

	}

	/**
	 * Get the Import from Model css
	 * 
	 * @param css
	 *            stylesheet file
	 * @return an Import representing the import in the stylesheet file
	 */
	private Customize findCustomize(Stylesheet css) {
		Customize importElement = (Customize) EcoreUtil.getObjectByType(css
				.eContents(), EcssPackage.eINSTANCE.getCustomize());
		return importElement;
	}

	/**
	 * Get the Style from Model css
	 * 
	 * @param css
	 *            the stylesheet file
	 * @return a Collection<Style> which gathers all the Style in the stylesheet
	 *         file
	 */
	private Collection<Style> findStyles(Stylesheet css) {
		Collection<Style> styles = EcoreUtil.getObjectsByType(css.eContents(),
				EcssPackage.eINSTANCE.getStyle());
		return styles;
	}

	/**
	 * Get the Element from Model css
	 * 
	 * @param css
	 *            the stylesheet file
	 * @return a Collection<Element> which gathers all the Element in the
	 *         stylesheet file
	 */
	private Collection<Element> findElements(Stylesheet css) {
		Collection<Element> styles = EcoreUtil.getObjectsByType(
				css.eContents(), EcssPackage.eINSTANCE.getElement());
		return styles;
	}

	/**
	 * Get the Definition (AttributeDefinition/ReferenceDefinition) from an
	 * element a given EObject.
	 * 
	 * @param element
	 *            a given EObject
	 * @return a Collection<Definition> which gathers all the Definition in the
	 *         stylesheet file
	 */
	private Collection<Definition> findDefinitions(EObject element) {
		Collection<Definition> styles = EcoreUtil.getObjectsByType(element
				.eContents(), EcssPackage.eINSTANCE.getDefinition());
		return styles;
	}

}
