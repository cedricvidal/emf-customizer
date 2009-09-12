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

package com.proxiad.emfcustomizer.stylesheet.dsl.conversion;

import org.eclipse.xtext.common.services.DefaultTerminalConverters;
import org.eclipse.xtext.conversion.IValueConverter;
import org.eclipse.xtext.conversion.ValueConverter;
import org.eclipse.xtext.conversion.impl.AbstractToStringConverter;
import org.eclipse.xtext.parsetree.AbstractNode;

/**
 * StylesheetConverterService is a "customization Class". This class provide
 * the conversion of terminals in the grammar used.
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * @author <a href="mailto:n.phung@proxiad.com">Nicolas Phung, ProxiAD Group</a>
 */
public class StylesheetConverterService extends DefaultTerminalConverters {

	/**
	 * ValueConverter providing conversion for parsed text in an instance of
	 * datatype Float and vice versa (rule FLOAT)
	 * 
	 * @return a float from the parsed string
	 */
	@ValueConverter(rule = "FLOAT")
	public IValueConverter<Float> FLOAT() {
		return new AbstractToStringConverter<Float>() {
			@Override
			protected Float internalToValue(String string, AbstractNode node) {
				return Float.valueOf(string);
			}
		};
	}

	/**
	 * ValueConverter providing conversion for parsed text in an instance of
	 * datatype Boolean and vice versa (rule BOOLEAN)
	 * 
	 * @return a boolean from the parsed string
	 */
	@ValueConverter(rule = "BOOLEAN")
	public IValueConverter<Boolean> BooleanValue() {
		return new AbstractToStringConverter<Boolean>() {
			@Override
			protected Boolean internalToValue(String string, AbstractNode node) {
				return Boolean.valueOf(string);
			}
		};
	}

}