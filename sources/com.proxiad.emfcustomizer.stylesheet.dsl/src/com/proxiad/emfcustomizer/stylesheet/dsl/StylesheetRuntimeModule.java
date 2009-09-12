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

import org.eclipse.xtext.resource.XtextResource;

import com.proxiad.emfcustomizer.stylesheet.dsl.scoping.StylesheetLazyLinkingResource;

/**
 * Use this class to register components to be used within the IDE.
 */
public class StylesheetRuntimeModule extends com.proxiad.emfcustomizer.stylesheet.dsl.AbstractStylesheetRuntimeModule {

	@Override
	public Class<? extends XtextResource> bindXtextResource() {
		return StylesheetLazyLinkingResource.class;
	}

}
