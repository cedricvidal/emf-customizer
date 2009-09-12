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

import java.util.regex.Pattern;

import org.eclipse.xtext.ui.common.editor.syntaxcoloring.DefaultLexicalHighlightingConfiguration;
import org.eclipse.xtext.ui.common.editor.syntaxcoloring.antlr.AbstractAntlrTokenToAttributeIdMapper;

/**
 * StylesheetAntlrTokenToAttributeIdMapper redefinition of the lexer based on
 * ANTLR
 * 
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 * 
 */
public class StylesheetAntlrTokenToAttributeIdMapper extends
		AbstractAntlrTokenToAttributeIdMapper {

	private static final Pattern SHARP = Pattern.compile("\'\\#\'");

	private static final Pattern DOT = Pattern.compile("\'\\.\'");

	private static final Pattern QUOTED = Pattern.compile(
			"(?:^'([^']*)'$)|(?:^\"([^\"]*)\")$", Pattern.MULTILINE);

	private static final Pattern PUNCTUATION = Pattern.compile("\\p{Punct}*");

	/**
	 * Branch on the right color in function of the tokenName
	 */
	@Override
	protected String calculateId(String tokenName, int tokenType) {
		if (SHARP.matcher(tokenName).matches()) {
			return StylesheetLexicalHighlightingConfiguration.SHARP_ID;
		}
		if (DOT.matcher(tokenName).matches()) {
			return StylesheetLexicalHighlightingConfiguration.DOT_ID;
		}
		if (PUNCTUATION.matcher(tokenName).matches()) {
			return DefaultLexicalHighlightingConfiguration.PUNCTUATION_ID;
		}
		if (QUOTED.matcher(tokenName).matches()) {
			return DefaultLexicalHighlightingConfiguration.KEYWORD_ID;
		}
		if ("RULE_STRING".equals(tokenName)) {
			return DefaultLexicalHighlightingConfiguration.STRING_ID;
		}
		if ("RULE_INT".equals(tokenName)) {
			return DefaultLexicalHighlightingConfiguration.NUMBER_ID;
		}
		if ("RULE_BOOLEAN".equals(tokenName)) {
			return StylesheetLexicalHighlightingConfiguration.BOOLEAN_ID;
		}
		if ("RULE_FLOAT".equals(tokenName)) {
			return StylesheetLexicalHighlightingConfiguration.FLOAT_ID;
		}
		if ("RULE_ML_COMMENT".equals(tokenName)
				| "RULE_SL_COMMENT".equals(tokenName)) {
			return DefaultLexicalHighlightingConfiguration.COMMENT_ID;
		}
		return DefaultLexicalHighlightingConfiguration.DEFAULT_ID;
	}
}
