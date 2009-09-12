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

package com.proxiad.emfcustomizer.stylesheet.dsl;

import org.eclipse.xtext.ui.common.editor.outline.transformer.ISemanticModelTransformer;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.ILexicalHighlightingConfiguration;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.ISemanticHighlightingCalculator;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.ISemanticHighlightingConfiguration;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.antlr.AbstractAntlrTokenToAttributeIdMapper;

import com.proxiad.emfcustomizer.stylesheet.dsl.outline.StylesheetTransformer;
import com.proxiad.emfcustomizer.stylesheet.dsl.syntaxcoloring.StylesheetAntlrTokenToAttributeIdMapper;
import com.proxiad.emfcustomizer.stylesheet.dsl.syntaxcoloring.StylesheetLexicalHighlightingConfiguration;
import com.proxiad.emfcustomizer.stylesheet.dsl.syntaxcoloring.StylesheetSemanticHighlightingCalculator;
import com.proxiad.emfcustomizer.stylesheet.dsl.syntaxcoloring.StylesheetSemanticHighlightingConfiguration;

/**
 * Use this class to register components to be used within the IDE.
 */
public class StylesheetUiModule extends com.proxiad.emfcustomizer.stylesheet.dsl.AbstractStylesheetUiModule {

	/**
	 * Configuration Module for Lexical Highlighting
	 * 
	 * @return
	 */
	public Class<? extends ILexicalHighlightingConfiguration> bindILexicalHighlightingConfiguration() {
		return StylesheetLexicalHighlightingConfiguration.class;
	}

	/**
	 * Module for Lexical Highlighting
	 * 
	 * @return
	 */
	public Class<? extends AbstractAntlrTokenToAttributeIdMapper> bindAbstractAntlrTokenToAttributeIdMapper() {
		return StylesheetAntlrTokenToAttributeIdMapper.class;
	}

	/**
	 * Configuration Module for Semantic Highlighting
	 * 
	 * @return
	 */
	public Class<? extends ISemanticHighlightingConfiguration> bindISemanticHighlightingConfiguration() {
		return StylesheetSemanticHighlightingConfiguration.class;
	}

	/**
	 * Module for Semantic Highlighting
	 * 
	 * @return
	 */
	public Class<? extends ISemanticHighlightingCalculator> bindISemanticHighlightingCalculator() {
		return StylesheetSemanticHighlightingCalculator.class;
	}

	/**
	 * Module for outline customization in the editor
	 */
	@Override
	public Class<? extends ISemanticModelTransformer> bindISemanticModelTransformer() {
		return StylesheetTransformer.class;
	}

}
