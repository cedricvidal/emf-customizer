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

import static com.google.common.collect.Iterables.concat;
import static com.google.common.collect.Iterables.filter;
import static com.google.common.collect.Iterables.transform;
import static com.proxiad.emfcustomizer.ecss.util.Iterables2.unique;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.scoping.impl.ImportUriResolver;
import org.eclipse.xtext.scoping.impl.ImportUriUtil;
import org.eclipse.xtext.util.Pair;
import org.eclipse.xtext.util.Tuples;

import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.proxiad.emfcustomizer.ecss.BooleanValue;
import com.proxiad.emfcustomizer.ecss.FloatValue;
import com.proxiad.emfcustomizer.ecss.IntValue;
import com.proxiad.emfcustomizer.ecss.StringValue;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.TypeRef;
import com.proxiad.emfcustomizer.ecss.ValueType;

/**
 * Contains model queries. They strive to use the Iterable streaming API as much
 * as possible for efficiency.
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class Queries {

	/**
	 * static method providing EClass from a Iterable<EObject>
	 * 
	 * @param eobjects
	 *            an Iterable<EObject>
	 * @return the EClass of eobjects
	 */
	public static Iterable<EClass> metaClasses(Iterable<EObject> eobjects) {
		Iterable<EClass> metaClasses = transform(eobjects,
				new Function<EObject, EClass>() {
					@Override
					public EClass apply(EObject from) {
						return from.eClass();
					}
				});

		return metaClasses;
	}

	// a more optimized version with "streaming" algo is used
	@Deprecated
	public static Iterable<String> importUrisSimple(Iterable<EObject> eobjects,
			final ImportUriResolver importUriResolver) {
		Iterator<EObject> iter = eobjects.iterator();
		final Set<String> uniqueImportURIs = new HashSet<String>(10);
		final List<String> orderedImportURIs = new ArrayList<String>(10);
		while (iter.hasNext()) {
			EObject object = iter.next();
			String uri = importUriResolver.apply(object);
			if (uri != null && uniqueImportURIs.add(uri)
					&& ImportUriUtil.isValid(object, uri)) {
				orderedImportURIs.add(uri);
			}
		}
		return uniqueImportURIs;
	}

	/**
	 * Works well but the added complexity is not worth the effort since it adds
	 * no performance gain in this specific situation.
	 * 
	 * @param eobjects
	 *            un Iterable<EObject>
	 * @return
	 */
	public static Iterable<String> importUris(Iterable<EObject> eobjects,
			final ImportUriResolver importUriResolver) {

		/**
		 * Creation of allUris (Iterable<Pair<EObject, String>>) which
		 * represents all the imported uris from importUriResolver
		 */
		Iterable<Pair<EObject, String>> allUris = transform(eobjects,
				new Function<EObject, Pair<EObject, String>>() {

					@Override
					public Pair<EObject, String> apply(EObject object) {
						String uri = importUriResolver.apply(object);
						return Tuples.create(object, uri);
					}

				});
		/**
		 * From these uris, filter the one which the Second is not null
		 */
		Iterable<Pair<EObject, String>> uris = filter(allUris,
				new Predicate<Pair<EObject, String>>() {

					@Override
					public boolean apply(Pair<EObject, String> input) {
						return input.getSecond() != null;
					}
				});
		/**
		 * Filter duplicates in uris (Iterable<Pair<EObject, String>>)
		 */
		Iterable<Pair<EObject, String>> uniqueUris = unique(uris);

		/**
		 * Check if those uniqueUris (Iterable<Pair<EObject, String>>) are valid
		 */
		Iterable<Pair<EObject, String>> validUris = filter(uniqueUris,
				new Predicate<Pair<EObject, String>>() {

					@Override
					public boolean apply(Pair<EObject, String> input) {
						boolean isValid = ImportUriUtil.isValid(input
								.getFirst(), input.getSecond());
						return isValid;
					}

				});

		/**
		 * Among uniqueUris (Iterable<Pair<EObject, String>>) get their Second
		 * (String)
		 */
		Iterable<String> onlyUris = transform(validUris,
				new Function<Pair<EObject, String>, String>() {

					@Override
					public String apply(Pair<EObject, String> input) {
						return input.getSecond();
					}
				});

		return onlyUris;
	}

	/**
	 * For each uris (Iterable<String>), get theirs corresponding Resources
	 * 
	 * @param uris
	 *            un Iterable<String>
	 * @param context
	 *            a Resource
	 * @return an Iterable<Resource>
	 */
	public static Iterable<Resource> resources(Iterable<String> uris,
			final Resource context) {
		return transform(uris, new Function<String, Resource>() {
			@Override
			public Resource apply(String uri) {
				return ImportUriUtil.getResource(context, uri);
			}

		});
	}

	/**
	 * Extract the content (EObject) of each element of an Iterable<Resource>
	 * and stock it in an Iterable<Object>
	 * 
	 * @param resources
	 *            an Iterable<Resource>
	 * @return an Iterable<EObject> of the content of each Resource of resources
	 */
	public static Iterable<EObject> content(Iterable<Resource> resources) {
		return concat(transform(resources,
				new Function<Resource, Iterable<EObject>>() {
					@Override
					public Iterable<EObject> apply(Resource resource) {
						return content(resource);
					}
				}));
	}

	/**
	 * 
	 * @param resource
	 * @return un Iterable<EObject>
	 */
	public static Iterable<EObject> content(final Resource resource) {
		return new Iterable<EObject>() {

			@Override
			public Iterator<EObject> iterator() {
				return EcoreUtil.getAllContents(resource, true);
			}

		};
	}

	/**
	 * get the Object issu from the value of a ValueType
	 * 
	 * @param newValue
	 *            represents one of the ValueType
	 * @return an Object which is the value of one ValueType in the grammar
	 */
	public static Object value(ValueType newValue) {
		if (newValue == null) {
			return null;
		}
		if (newValue instanceof StringValue) {
			return ((StringValue) newValue).getValue();
		}
		if (newValue instanceof BooleanValue) {
			return ((BooleanValue) newValue).isValue();
		}
		if (newValue instanceof FloatValue) {
			return ((FloatValue) newValue).getValue();
		}
		if (newValue instanceof IntValue) {
			return ((IntValue) newValue).getValue();
		}
		return null;
	}

	/**
	 * For each EReference, return the EReferenceType
	 * 
	 * @param refs
	 *            an Iterable<EReference>
	 * @return in an Iterable<EClass>, get the EReferenceType corresponding of
	 *         each EReference in refs
	 */
	public static Iterable<EClass> referenceType(Iterable<EReference> refs) {
		return transform(refs, new Function<EReference, EClass>() {
			@Override
			public EClass apply(EReference ref) {
				return ref.getEReferenceType();
			}

		});
	}

	/**
	 * Return the last not null typeRef->ref. Provides a list of correct
	 * Definition in the Style scope.
	 * 
	 * @param style
	 *            the grammar in Style rule
	 * @return the last not null typeRef->ref
	 */
	public static TypeRef currentTypeRef(Style style) {
		TypeRef typeRef = style.getTypeRef();
		if (typeRef == null) {
			return null;
		}
		while (typeRef.getNext() != null) {
			typeRef = typeRef.getNext();
		}
		return typeRef;
	}

	/**
	 * Returns the closure of an EClass eclass: go inside recursively and list
	 * the content of a given EClass
	 * 
	 * @param eclass
	 *            a given EClass
	 * @return the list of EClass from eclass in a Iterable<EClass>
	 */
	// XXX Requires cache
	public static Iterable<EClass> allContainedClasses(EClass eclass) {
		Iterable<EClass> classes = referenceType(eclass.getEAllContainments());
		Iterable<EClass> transform = concat(transform(classes,
				new Function<EClass, Iterable<EClass>>() {

					@Override
					public Iterable<EClass> apply(EClass from) {
						return allContainedClasses(from);
					}

				}));
		return concat(classes, transform);
	}

}
