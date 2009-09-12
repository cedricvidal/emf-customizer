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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.DefaultLexicalHighlightingConfiguration;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.core.editor.utils.TextStyle;

/**
 * 
 * Lexical Highlighting Configuration for the editor. Configuration of TextStyle
 * for the lexical coloration
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 * 
 */
public class StylesheetLexicalHighlightingConfiguration extends
		DefaultLexicalHighlightingConfiguration {

	public static final String SHARP_ID = "sharp";

	public static final String DOT_ID = "dot";

	public static final String BOOLEAN_ID = "boolean";

	public static final String FLOAT_ID = "float";

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.xtext.ui.common.editor.syntaxcoloring.
	 * ILexicalHighlightingConfiguration
	 * #configure(org.eclipse.xtext.ui.common.editor
	 * .syntaxcoloring.IHighlightingConfigurationAcceptor)
	 */
	@Override
	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		super.configure(acceptor);
		acceptor.acceptDefaultHighlighting(SHARP_ID, "Sharp", sharpTextStyle());
		acceptor.acceptDefaultHighlighting(DOT_ID, "Dot", sharpTextStyle());
		acceptor.acceptDefaultHighlighting(BOOLEAN_ID, "Boolean",
				booleanTextStyle());
		acceptor.acceptDefaultHighlighting(FLOAT_ID, "Float", floatTextStyle());
	}

	// TODO: Same color for # and modelRef in the semantic coloring
	public TextStyle sharpTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(63, 63, 191));
		return textStyle;
	}

	public TextStyle dotTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle booleanTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 0, 85));
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle floatTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(125, 125, 125));
		return textStyle;
	}
}
