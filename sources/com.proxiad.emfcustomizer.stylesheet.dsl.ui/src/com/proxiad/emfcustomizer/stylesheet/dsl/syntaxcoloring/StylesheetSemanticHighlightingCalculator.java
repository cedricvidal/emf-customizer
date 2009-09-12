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

/**
 * 
 */
package com.proxiad.emfcustomizer.stylesheet.dsl.syntaxcoloring;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.parsetree.AbstractNode;
import org.eclipse.xtext.parsetree.NodeUtil;
import org.eclipse.xtext.resource.XtextResource;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.IHighlightedPositionAcceptor;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.ISemanticHighlightingCalculator;

import com.proxiad.emfcustomizer.ecss.TypeRef;

/**
 * 
 * Module representing the logic engine behind the semantic coloration in the
 * editor for the sstylesheet file (*.emfcustomizer)
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 * 
 */
public class StylesheetSemanticHighlightingCalculator implements
		ISemanticHighlightingCalculator {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.xtext.ui.common.editor.syntaxcoloring.
	 * ISemanticHighlightingCalculator
	 * #provideHighlightingFor(org.eclipse.xtext.resource.XtextResource,
	 * org.eclipse
	 * .xtext.ui.common.editor.syntaxcoloring.IHighlightedPositionAcceptor)
	 */
	@Override
	public void provideHighlightingFor(XtextResource resource,
			IHighlightedPositionAcceptor acceptor) {
		if (resource == null)
			return;

		Iterable<AbstractNode> allNodes = NodeUtil.getAllContents(resource
				.getParseResult().getRootNode());
		for (AbstractNode abstractNode : allNodes) {
			EObject elementNode = abstractNode.getElement();
			/**
			 * semantic coloring for the TypeRef rule in the grammar
			 */
			boolean isTypeRef = elementNode instanceof TypeRef;
			if (isTypeRef) {
				acceptor.addPosition(abstractNode.getOffset(), abstractNode
						.getLength(),
						StylesheetSemanticHighlightingConfiguration.TYPEREF);
			}

			EObject grammarElement = abstractNode.getGrammarElement();
			if (grammarElement instanceof CrossReference) {
				CrossReference cross = (CrossReference) grammarElement;
				RuleCall ruleCall = (RuleCall) cross.getTerminal();
				String ruleName = ruleCall.getRule().getName();
				/**
				 * semantic coloring for all the modelRef in the grammar
				 */
				if (ruleName.equals("ModelFqn")) {
					/**
					 * get the ModelFqn which belongs to the TypeRef->ref rule
					 */
					if (grammarElement.eContainer() instanceof Assignment) {
						Assignment assignement = (Assignment) grammarElement
								.eContainer();
						if (assignement.getFeature().equals("modelRef")) {
							acceptor
									.addPosition(
											abstractNode.getOffset(),
											abstractNode.getLength(),
											StylesheetSemanticHighlightingConfiguration.MODELFQNINMODELREF);
						}
					}
				}
				/**
				 * semantic coloring for all the EStructuralFeature including ID
				 * in the grammar
				 */
				if (ruleName.equals("ID")) {
					acceptor.addPosition(abstractNode.getOffset(), abstractNode
							.getLength(),
							StylesheetSemanticHighlightingConfiguration.ID);
				}
			}
		}
	}
}