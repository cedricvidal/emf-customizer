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

package com.proxiad.emfcustomizer.stylesheet.dsl.formatting;

import org.eclipse.xtext.formatting.impl.AbstractDeclarativeFormatter;
import org.eclipse.xtext.formatting.impl.FormattingConfig;

import com.proxiad.emfcustomizer.stylesheet.dsl.services.StylesheetGrammarAccess;

/**
 * This class contains custom formatting description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#formatting
 * on how and when to use it 
 * 
 * Also see {@link org.eclipse.xtext.xtext.XtextFormattingTokenSerializer} as an example
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class StylesheetFormatter extends AbstractDeclarativeFormatter {

	@Override
	protected void configureFormatting(FormattingConfig c) {
		StylesheetGrammarAccess f = (StylesheetGrammarAccess) getGrammarAccess();

		// Set indentation as a tabulation
		c.setIndentationSpace("\t");

		// After customize, jump 2 lines
		c.setLinewrap(2)
				.after(
						f.getCustomizeAccess()
								.getImportURISTRINGTerminalRuleCall_1_0());

		// After load, jump 2 lines
		c.setLinewrap(2).after(
				f.getModelLoadAccess().getLoadURISTRINGTerminalRuleCall_1_0());

		// Rules for Element { } (bloc) with indentation inside the bloc
		c.setIndentation(f.getElementAccess().getLeftCurlyBracketKeyword_2(), f
				.getElementAccess().getRightCurlyBracketKeyword_4());
		c.setLinewrap().after(
				f.getElementAccess().getLeftCurlyBracketKeyword_2());
		c.setLinewrap(2).after(
				f.getElementAccess().getRightCurlyBracketKeyword_4());

		// Jump a line after each definition in an Element and no space before ';'
		c.setLinewrap().after(f.getElementAccess().getSemicolonKeyword_3_1());
		c.setNoSpace().before(f.getElementAccess().getSemicolonKeyword_3_1());

		// Rules for Style { } (bloc) with indentation inside the bloc
		c.setIndentation(f.getStyleAccess().getLeftCurlyBracketKeyword_3(), f
				.getStyleAccess().getRightCurlyBracketKeyword_5());
		c.setLinewrap()
				.after(f.getStyleAccess().getLeftCurlyBracketKeyword_3());
		c.setLinewrap(2).after(
				f.getStyleAccess().getRightCurlyBracketKeyword_5());

		// Jump a line after each definition in a Style and no space before ';'
		c.setLinewrap().after(f.getStyleAccess().getSemicolonKeyword_4_1());
		c.setNoSpace().before(f.getStyleAccess().getSemicolonKeyword_4_1());

		// No space after '#' in Element
		c.setNoSpace().after(f.getElementAccess().getNumberSignKeyword_0_1());

		// No space around '::' in ModelFqn
		c.setNoSpace().around(f.getModelFqnAccess().getColonColonKeyword_1_0());

		// No space after '.' in TypeRef
		c.setNoSpace().after(f.getTypeRefAccess().getFullStopKeyword_0());

		// Rules for comments
		c.setLinewrap().after(f.getML_COMMENTRule());
		c.setNoLinewrap().after(f.getSL_COMMENTRule());
	}

}
