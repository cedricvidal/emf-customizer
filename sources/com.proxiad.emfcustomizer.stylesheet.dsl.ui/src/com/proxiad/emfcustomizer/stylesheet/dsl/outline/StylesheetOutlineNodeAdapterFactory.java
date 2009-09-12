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

import org.eclipse.xtext.ui.common.editor.outline.actions.DefaultContentOutlineNodeAdapterFactory;

public class StylesheetOutlineNodeAdapterFactory extends DefaultContentOutlineNodeAdapterFactory {

	private static final Class[] types = { 
		// provide list of classes to adapt to, e.g.:
		// Entity.class
		// Service.class
	};

	@Override
	public Class[] getAdapterList() {
		return types;
	}

}

