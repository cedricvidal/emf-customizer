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
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.IHighlightingConfigurationAcceptor;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.ISemanticHighlightingConfiguration;
import org.eclipse.xtext.ui.core.editor.utils.TextStyle;

/**
 * Semantic Coloring Configuration for the editor
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 * 
 */
public class StylesheetSemanticHighlightingConfiguration implements
		ISemanticHighlightingConfiguration {

	public static final String TYPEREF = "typeRef";
	public static final String MODELFQNINMODELREF = "modelFqnInModelRef";
	public static final String ID = "id";

	public void configure(IHighlightingConfigurationAcceptor acceptor) {
		acceptor.acceptDefaultHighlighting(TYPEREF, "TypeRef",
				typeRefTextStyle());
		acceptor.acceptDefaultHighlighting(MODELFQNINMODELREF,
				"ModelFqnInModelRef", modelFqnInModelRefTextStyle());
		acceptor.acceptDefaultHighlighting(ID, "Id", idTextStyle());
	}

	/**
	 * Colors by default reuse for the other TextStyle method
	 * 
	 * @return
	 */
	private TextStyle defaultTextStyle() {
		TextStyle textStyle = new TextStyle();
		textStyle.setBackgroundColor(new RGB(255, 255, 255));
		textStyle.setColor(new RGB(0, 0, 0));
		return textStyle;
	}

	public TextStyle typeRefTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(127, 159, 191));
		textStyle.setStyle(SWT.ITALIC);
		textStyle.setStyle(SWT.BOLD);
		return textStyle;
	}

	public TextStyle modelFqnInModelRefTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(63, 63, 191));
		return textStyle;
	}

	public TextStyle idTextStyle() {
		TextStyle textStyle = defaultTextStyle().copy();
		textStyle.setColor(new RGB(0, 0, 0));
		return textStyle;
	}
}