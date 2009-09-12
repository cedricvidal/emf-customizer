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

package com.proxiad.emfcustomizer.stylesheet.dsl.generator;

import org.eclipse.xtext.Grammar;
import org.eclipse.xtext.generator.ecore.EcoreGeneratorFragment;

public class StylesheetEcoreGeneratorFragment extends EcoreGeneratorFragment {

	protected String getTemplate() {
		return EcoreGeneratorFragment.class.getName().replaceAll("\\.", "::");
	}

	@Override
	public String[] getExportedPackagesRt(Grammar grammar) {
		return new String[]{};
	}

}
