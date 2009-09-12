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

import java.util.List;

import org.eclipse.emf.common.util.WrappedException;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.xtext.linking.impl.XtextLinkingDiagnostic;
import org.eclipse.xtext.linking.lazy.LazyLinkingResource;
import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.util.Triple;

/**
 * Class StylesheetLazyLinkingResource redefining the LazyLinkingResource to
 * allow EObject references
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class StylesheetLazyLinkingResource extends LazyLinkingResource {

	@Override
	public EObject getEObject(String uriFragment) {
		try {
			if (getEncoder().isCrossLinkFragment(this, uriFragment)) {
				Triple<EObject, EReference, AbstractNode> triple = getEncoder()
						.decode(this, uriFragment);
				List<EObject> linkedObjects = getLinkingService()
						.getLinkedObjects(triple.getFirst(),
								triple.getSecond(), triple.getThird());
				if (linkedObjects.isEmpty()) {
					XtextLinkingDiagnostic diag = createDiagnostic(triple);
					if (!getErrors().contains(diag))
						getErrors().add(diag);
					return null;
				}
				if (linkedObjects.size() > 1)
					throw new IllegalStateException(
							"linkingService returned more than one object for fragment "
									+ uriFragment);
				EObject result = linkedObjects.get(0);

				// XXX EMF Customizer: Allow EObject references
				EClass eReferenceType = triple.getSecond().getEReferenceType();
				if (!EcorePackage.eINSTANCE.getEObject().equals(eReferenceType)
						&& !eReferenceType.isSuperTypeOf(result.eClass())) {
					XtextLinkingDiagnostic diag = createDiagnostic(triple);
					if (!getErrors().contains(diag))
						getErrors().add(diag);
					return null;
				}
				// remove previously added error markers, since everything
				// should be fine now
				XtextLinkingDiagnostic diag = createDiagnostic(triple);
				getErrors().remove(diag);
				return result;
			}
		} catch (RuntimeException e) {
			// wrapped because the javaDoc of this method states that
			// WrappedExceptions are thrown
			throw new WrappedException(e);
		}
		return super.getEObject(uriFragment);
	}

	private XtextLinkingDiagnostic createDiagnostic(
			Triple<EObject, EReference, AbstractNode> triple) {
		String msg = "Couldn't resolve reference to "
				+ triple.getSecond().getEType().getName() + " "
				+ triple.getThird().serialize();
		return new XtextLinkingDiagnostic(triple.getThird(), msg);
	}

}
