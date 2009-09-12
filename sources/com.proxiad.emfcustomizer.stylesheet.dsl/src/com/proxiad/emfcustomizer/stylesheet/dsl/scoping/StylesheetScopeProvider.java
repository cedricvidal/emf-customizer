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

package com.proxiad.emfcustomizer.stylesheet.dsl.scoping;

import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.proxiad.emfcustomizer.ecss.util.Iterables2.unique;
import static com.proxiad.emfcustomizer.ecss.util.Queries.content;
import static com.proxiad.emfcustomizer.ecss.util.Queries.importUris;
import static com.proxiad.emfcustomizer.ecss.util.Queries.metaClasses;
import static com.proxiad.emfcustomizer.ecss.util.Queries.resources;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.xtext.linking.impl.SimpleAttributeResolver;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.IScopedElement;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;
import org.eclipse.xtext.scoping.impl.DefaultScope;
import org.eclipse.xtext.scoping.impl.ImportUriResolver;
import org.eclipse.xtext.scoping.impl.ScopedElement;
import org.eclipse.xtext.scoping.impl.SimpleScope;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.Iterables;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import com.proxiad.emfcustomizer.ecss.Definition;
import com.proxiad.emfcustomizer.ecss.Element;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.Stylesheet;
import com.proxiad.emfcustomizer.ecss.TypeRef;
import com.proxiad.emfcustomizer.ecss.util.FqnResolver;
import com.proxiad.emfcustomizer.ecss.util.Queries;

/**
 * This class contains custom scoping description.
 * 
 * see : http://wiki.eclipse.org/Xtext/Documentation#Scoping on how and when to
 * use it
 * 
 * This class provides customization of the scope provider (scope provide &
 * scope creation)
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class StylesheetScopeProvider extends
		AbstractDeclarativeScopeProvider {

	@Inject
	private ImportUriResolver importUriResolver;

	/**
	 * Injection of an other instance ImportUriResolver to separate modelRef
	 * from the customized model (e.g *.xmi customization) and modelRef from
	 * loaded model (e.g in case for DDL reverse use case)
	 */
	@Inject
	@Named("loadImportUriResolver")
	private ImportUriResolver loadImportUriResolver;

	private static FqnResolver FQN_RESOLVER = new FqnResolver(
			SimpleAttributeResolver.NAME_RESOLVER);

	/**
	 * local getter for importUriResolver
	 * 
	 * @return the importUriResolver
	 */
	private ImportUriResolver getImportUriResolver() {
		return importUriResolver;
	}

	// Customization for scopes

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_TypeRef_ref(Style context, EReference ref) {
		IScope scope = createScope(importedMetaClasses(context.eResource()),
				FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_TypeRef_ref(Stylesheet context, EReference ref) {
		IScope scope = createScope(importedMetaClasses(context.eResource()),
				FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_TypeRef_next(EObject context, EReference ref) {
		IScope scope = createScope(importedMetaClasses(context.eResource()),
				FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_Element_modelRef(EObject context, EReference ref) {
		IScope scope = new DefaultScope(context.eResource(), ref
				.getEReferenceType(), getImportUriResolver(), FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_Style_modelRef(EObject context, EReference ref) {
		IScope scope = new DefaultScope(context.eResource(), ref
				.getEReferenceType(), getImportUriResolver(), FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param context
	 * @param ref
	 * @return a scope with fqn use
	 */
	IScope scope_Style_modelRef(Stylesheet context, EReference ref) {
		IScope scope = new DefaultScope(context.eResource(), ref
				.getEReferenceType(), getImportUriResolver(), FQN_RESOLVER);
		return scope;
	}

	/**
	 * Scope using the FQN_RESOLVER to provide full qualified name
	 * 
	 * @param referenceDefinition
	 * @param ref
	 * @return a scope with fqn use
	 */
	// TODO: EMFCustomizer External Editor 1.1 / Ticket #93 / Listing
	// transitives resources from a model
	IScope scope_ReferenceDefinition_modelRef(
			ReferenceDefinition referenceDefinition, EReference ref) {
		// scopeFils "son scope" from ModelLoad -> loadURI
		IScope scopeFils = new DefaultScope(referenceDefinition.eResource(),
				ref.getEReferenceType(), loadImportUriResolver, FQN_RESOLVER);
		// scopeParent "parent scope" from Customize -> importURI
		IScope scopeParent = new DefaultScope(referenceDefinition.eResource(),
				ref.getEReferenceType(), getImportUriResolver(), FQN_RESOLVER);
		// construction of a scope using the 2 others scopes provide
		IScope scope = new SimpleScope(scopeParent, scopeFils.getAllContents());
		return scope;
	}

	/**
	 * Create a scope which returns metaclasses of the target metamodel and
	 * restrict inclusion for metaclasses (e.g scope Table > Column > Datatype
	 * but no Column > Table)
	 * 
	 * @param context
	 * @param ref
	 * @return a scope of metaclasses from target metamodel
	 */
	@SuppressWarnings("unchecked")
	IScope scope_TypeRef_ref(TypeRef context, EReference ref) {
		Iterable<EClass> metaClasses = null;
		EObject contextEContainer = context.eContainer();
		if (contextEContainer instanceof Style) {
			metaClasses = importedMetaClasses(context.eResource());
		} else {
			// XXX Filter in the scope the metaclasses which are not used yet in
			// the parent TypeRef
			TypeRef parent = (TypeRef) contextEContainer;
			EClass metaClass = parent.getRef();
			List<? extends EObject> listeRes = getInScopeEClassFromEStructuralFeatures(metaClass);
			metaClasses = (Iterable<EClass>) listeRes;
		}
		IScope scope = createScope(metaClasses, FQN_RESOLVER);
		return scope;
	}

	/**
	 * Create a scope with the name of EStructuralFeatures from a Définition
	 * 
	 * @param attributeDefinition
	 * @param ref
	 * @return
	 */
	IScope scope_Definition_attribute(Definition attributeDefinition,
			EReference ref) {
		EObject styleOrElement = attributeDefinition.eContainer();
		List<? extends EObject> features = Collections.emptyList();
		if (styleOrElement instanceof Style) {
			features = getInScopeStructuralFeatures((Style) styleOrElement);
		} else if (styleOrElement instanceof Element) {
			features = getInScopeStructuralFeatures((Element) styleOrElement);
		}
		return createScope(features, SimpleAttributeResolver.NAME_RESOLVER);
	}

	/**
	 * Create a scope with the nom of EStructuralFeatures from an Element
	 * 
	 * @param element
	 * @param ref
	 * @return
	 */
	IScope scope_Definition_attribute(Element element, EReference ref) {
		List<? extends EObject> features = getInScopeStructuralFeatures(element);
		return createScope(features, SimpleAttributeResolver.NAME_RESOLVER);
	}

	/**
	 * Create a scope with the name of EStructuralFeatures from a Style
	 * 
	 * @param style
	 * @param ref
	 * @return
	 */
	IScope scope_Definition_attribute(Style style, EReference ref) {
		List<? extends EObject> features = getInScopeStructuralFeatures(style);
		return createScope(features, SimpleAttributeResolver.NAME_RESOLVER);
	}

	// privates methods tools to customize the scopes

	/**
	 * Take an EClass element and return the EClass of each EStructuralFeatures
	 * in a List
	 * 
	 * @param element
	 *            an EClass
	 * @return a list of EClass from EStructuralFeatures element
	 */
	private List<? extends EObject> getInScopeEClassFromEStructuralFeatures(
			EClass element) {
		List<EStructuralFeature> features = element.getEStructuralFeatures();

		// Filter EReference from EStructuralFeatures
		Iterable<EReference> eReferenceOnly = filter(features, EReference.class);

		// Get EClass from EReference
		Iterable<EClass> eClassOnly = transform(eReferenceOnly,
				new Function<EReference, EClass>() {

					public EClass apply(EReference from) {
						return from.getEReferenceType();
					}

				});

		List<EClass> listEClass = new ArrayList<EClass>();
		Iterables.addAll(listEClass, eClassOnly);
		return listEClass;
	}

	/**
	 * Returns structure features of the EClass of the customized model element
	 * 
	 * @param element
	 * @return a list which the EStructuralFeature doesn't have more than 1 in
	 *         cardinality
	 */
	private List<? extends EObject> getInScopeStructuralFeatures(Element element) {
		List<EStructuralFeature> features = element.getModelRef().eClass()
				.getEAllStructuralFeatures();
		Iterable<EStructuralFeature> notManyFeatures = filter(features,
				new Predicate<EStructuralFeature>() {

					public boolean apply(EStructuralFeature input) {
						return !input.isMany();
					}
				});
		List<EStructuralFeature> notManyFeaturesList = new ArrayList<EStructuralFeature>();
		Iterables.addAll(notManyFeaturesList, notManyFeatures);
		return notManyFeaturesList;
	}

	/**
	 * Returns structure features of the customized EClass
	 * 
	 * @param style
	 * @return a list of all the EStructuralFeature from style->typref->ref
	 */
	private List<? extends EObject> getInScopeStructuralFeatures(Style style) {
		List<? extends EObject> features = Queries.currentTypeRef(style)
				.getRef().getEAllStructuralFeatures();
		return features;
	}

	/**
	 * Create a scope from an iterable eobjects and a function providing the
	 * good name spelling in a Scope
	 * 
	 * @param eobjects
	 *            an iterable of EObject
	 * @param nameFunc
	 *            a function which takes EObject and returns a String id
	 * @return a scope in which each eobject in parameters become a
	 *         ScopedElement of "a well-from scope" (i.e for our custom needed)
	 */
	private IScope createScope(Iterable<? extends EObject> eobjects,
			final Function<EObject, String> nameFunc) {
		Iterable<IScopedElement> transformed = transform(eobjects,
				new Function<EObject, IScopedElement>() {

					public IScopedElement apply(EObject from) {
						return ScopedElement.create(nameFunc.apply(from), from);
					}

				});
		return new SimpleScope(transformed);
	}

	/**
	 * Import all metaclasses from a given Resource
	 * 
	 * @param resource
	 * @return an iterable of EClass where are all the metaclasses extracted
	 *         from the given resource
	 */
	private Iterable<EClass> importedMetaClasses(final Resource resource) {
		if (resource != null) {
			Iterable<Resource> importedResources = resources(importUris(
					content(resource), importUriResolver), resource);
			Iterable<EObject> importedElements = content(importedResources);
			Iterable<EClass> metaClasses = unique(metaClasses(importedElements));

			return metaClasses;
		}
		return Iterables.emptyIterable();
	}

}
