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

package com.proxiad.emfcustomizer.engine;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.mwe.core.WorkflowContext;
import org.eclipse.emf.mwe.core.issues.Issues;
import org.eclipse.emf.mwe.core.lib.AbstractWorkflowComponent;
import org.eclipse.emf.mwe.core.monitor.ProgressMonitor;
import org.eclipse.emf.mwe.utils.Reader;

import com.proxiad.emfcustomizer.ecss.Stylesheet;

/**
 * Class component for the engine workflow
 * 
 * @author <a href="mailto:c.vidal@proxiad.com">Cedric Vidal, ProxiAD Group</a>
 * 
 */
public class EmfCustomizerComponent extends AbstractWorkflowComponent {

	/**
	 * Source file stylesheet (*.emfcustomizer)
	 */
	private String cssSlot;

	/**
	 * Target file (the output customized model)
	 */
	private String outputSlot;

	public String getOutputSlot() {
		return outputSlot;
	}

	public void setOutputSlot(String outputSlot) {
		this.outputSlot = outputSlot;
	}

	public String getCssSlot() {
		return cssSlot;
	}

	public final void setCssSlot(String cssSlot) {
		this.cssSlot = cssSlot;
	}

	/**
	 * Method invoking the workflow engine
	 */
	@Override
	protected void invokeInternal(WorkflowContext ctx, ProgressMonitor monitor,
			Issues issues) {
		Stylesheet css = (Stylesheet) ctx.get(cssSlot);
		EmfCustomizerCopier copier = new EmfCustomizerCopier();
		copier.setCss(css);
		copier.initCss();

		String modelPath = copier.getModelPath();

		Reader reader = new Reader();
		reader.setUri(modelPath);
		reader.setModelSlot("inputModel");
		reader.invoke(ctx, monitor, issues);

		EObject inputModel = (EObject) ctx.get("inputModel");

		EObject outputModel = copier.copy(inputModel);
		copier.copyReferences();
		ctx.set(getOutputSlot(), outputModel);
	}

	/**
	 * Method to check some conditions before invoking the workflow engine
	 */
	public void checkConfiguration(Issues issues) {
		// No Check performed before invokeInternal
	}

}
