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

package com.proxiad.emfcustomizer.stylesheet.dsl.outline;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.linking.impl.SimpleAttributeResolver;
import org.eclipse.xtext.ui.common.editor.outline.ContentOutlineNode;
import org.eclipse.xtext.ui.common.editor.outline.transformer.AbstractDeclarativeSemanticModelTransformer;

import com.proxiad.emfcustomizer.ecss.AttributeDefinition;
import com.proxiad.emfcustomizer.ecss.Definition;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.TypeRef;
import com.proxiad.emfcustomizer.ecss.ValueType;
import com.proxiad.emfcustomizer.ecss.util.FqnResolver;
import com.proxiad.emfcustomizer.ecss.util.Queries;

/**
 * customization of the default outline structure
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class StylesheetTransformer extends AbstractDeclarativeSemanticModelTransformer {

	/**
	 * Getting the FqnResolver (Ex: "parentfqn::fqn")
	 */
	private static FqnResolver FQN_RESOLVER = new FqnResolver(
			SimpleAttributeResolver.NAME_RESOLVER);

	/**
	 * This method will be called by naming convention:<br>
	 * - method name must be createNode <br>
	 * - first param: subclass of EObject <br>
	 * - second param: ContentOutlineNode
	 */
	/**
	 * Rename the Style name with the name of TypeRef->ref
	 */
	public ContentOutlineNode createNode(Style semanticNode,
			ContentOutlineNode parentNode) {
		ContentOutlineNode node = super
				.newOutlineNode(semanticNode, parentNode);
		EObject modelRef = semanticNode.getModelRef();
		if (modelRef != null) {
			node.setLabel(getAllInsideTypeRef(semanticNode) + " in "
					+ FQN_RESOLVER.apply(modelRef));
		}
		return node;
	}

	/**
	 * private method providing the construction of a TypeRef and its inclusions
	 * 
	 * @param style
	 *            Style XText (0.7.2) grammar
	 * @return TypeRef -> Ref -> Next -> ...
	 */
	private String getAllInsideTypeRef(Style style) {
		TypeRef typeRef = style.getTypeRef();
		String res = typeRef.getRef().getName();
		TypeRef suivant = typeRef.getNext();
		while (suivant != null) {
			String next = suivant.getRef().getName();
			res += " > " + next;
			suivant = suivant.getNext();
		}
		return res;
	}

	/**
	 * Hide the TypeRef node
	 */
	public ContentOutlineNode createNode(TypeRef semanticNode,
			ContentOutlineNode parentNode) {
		return HIDDEN_NODE;
	}

	/**
	 * Build the node AttributeDefinition in the following manner: <br>
	 * attribute ':' value (like in the grammar)
	 */
	public ContentOutlineNode createNode(AttributeDefinition semanticNode,
			ContentOutlineNode parentNode) {
		ContentOutlineNode node = super
				.newOutlineNode(semanticNode, parentNode);
		ValueType value = semanticNode.getValue();
		if (value != null) {
			node.setLabel(semanticNode.getAttribute().getName() + " : "
					+ (Queries.value(value)).toString());
		}
		return node;
	}

	/**
	 * Build the node ReferenceDefinition in the following manner: <br>
	 * attribute 'new type=' modelRef (like in the grammar)
	 */
	public ContentOutlineNode createNode(ReferenceDefinition semanticNode,
			ContentOutlineNode parentNode) {
		ContentOutlineNode node = super
				.newOutlineNode(semanticNode, parentNode);
		EObject modelRef = semanticNode.getModelRef();
		if (modelRef != null) {
			node.setLabel(semanticNode.getAttribute().getName() + " = "
					+ FQN_RESOLVER.apply(modelRef));
		}
		return node;
	}

	/**
	 * This method will be called by naming convention: <br>
	 * - method name must be getChildren <br>
	 * - first param: subclass of EObject
	 */
	/**
	 * Hide the nodes value/modelRef in Definition
	 */
	public List<EObject> getChildren(Definition definition) {
		return NO_CHILDREN;
	}

}
