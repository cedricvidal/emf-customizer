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

package com.proxiad.emfcustomizer.stylesheet.dsl.contentassist;

import static com.google.common.collect.Iterables.contains;
import static com.proxiad.emfcustomizer.ecss.util.Queries.allContainedClasses;
import static com.proxiad.emfcustomizer.ecss.util.Queries.currentTypeRef;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.jdt.core.IClasspathEntry;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.xtext.Assignment;
import org.eclipse.xtext.CrossReference;
import org.eclipse.xtext.RuleCall;
import org.eclipse.xtext.scoping.IScopedElement;
import org.eclipse.xtext.ui.core.editor.contentassist.ContentAssistContext;
import org.eclipse.xtext.ui.core.editor.contentassist.ICompletionProposalAcceptor;

import com.google.common.base.Predicate;
import com.proxiad.emfcustomizer.ecss.ReferenceDefinition;
import com.proxiad.emfcustomizer.ecss.Style;
import com.proxiad.emfcustomizer.ecss.TypeRef;
import com.proxiad.emfcustomizer.ecss.util.Constraints;

/**
 * see http://www.eclipse.org/Xtext/documentation/latest/xtext.html#contentAssist on how to customize content assistant
 */
public class StylesheetProposalProvider extends AbstractStylesheetProposalProvider {

	@Override
	public void complete_BOOLEAN(EObject model, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		acceptor.accept(createCompletionProposal("true", context));
		acceptor.accept(createCompletionProposal("false", context));
	}

	@Override
	public void complete_FLOAT(EObject model, RuleCall ruleCall,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		acceptor.accept(createCompletionProposal("1.0", context));
	}

	/**
	 * The content assist is checking if the "modelRef" is compatible with the
	 * "attribute" type in the grammar rule ReferenceDefinition before proposing
	 */
	@Override
	public void completeReferenceDefinition_ModelRef(final EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		final ReferenceDefinition referenceDefinition = (ReferenceDefinition) model;

		lookupCrossReference(((CrossReference) assignment.getTerminal()),
				context, acceptor, new Predicate<IScopedElement>() {

					@Override
					public boolean apply(IScopedElement input) {
						boolean compatible = Constraints.isCompatible(
								referenceDefinition, input.element());
						return compatible;
					}
				});
	}

	/**
	 * The content assist is checking if the "modelRef" is matching the last
	 * EClass from typeRef->TypeRef->ref in the grammar rule Style before
	 * proposing
	 */
	@Override
	public void completeStyle_ModelRef(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {

		final Style style = (Style) model;
		lookupCrossReference(((CrossReference) assignment.getTerminal()),
				context, acceptor, new Predicate<IScopedElement>() {

					@Override
					public boolean apply(IScopedElement input) {

						TypeRef currentTypeRef = currentTypeRef(style);
						EClass styledClass = currentTypeRef.getRef();

						EClass candidateClass = input.element().eClass();
						Iterable<EClass> containedClasses = allContainedClasses(candidateClass);

						boolean compatible = contains(containedClasses,
								styledClass);
						return compatible;
					}
				});
	}

	/**
	 * List model files in IProject[]
	 * 
	 * @param projects
	 * @return
	 */
	private List<String> findModelsInProjects(IProject[] projects) {
		List<String> proposals = new ArrayList<String>();
		for (IProject iProject : projects) {
			try {
				IResource[] resourcesProject = iProject.members();
				for (IResource resource : resourcesProject) {
					List<String> resourceProposals = findModelsInResource(resource);
					proposals.addAll(resourceProposals);
				}
			} catch (CoreException e) {
				// ignore
			}
		}
		return proposals;
	}

	/**
	 * List model files in a IFolder
	 * 
	 * @param parentFolder
	 * @return
	 */
	private List<String> findModelsInFolder(IFolder parentFolder) {
		List<String> proposals = new ArrayList<String>();
		try {
			IResource[] foldersMembers = parentFolder.members();
			for (IResource resource : foldersMembers) {
				List<String> resourceProposals = findModelsInResource(resource);
				proposals.addAll(resourceProposals);
			}
		} catch (CoreException e) {
			// ignore
		}
		return proposals;
	}

	/**
	 * List the models in a IResource
	 * 
	 * @param resource
	 * @return
	 */
	private List<String> findModelsInResource(IResource resource) {
		List<String> resourceProposals = new ArrayList<String>();
		if (resource.getType() == IResource.FOLDER) {
			IFolder folder = (IFolder) resource;
			if (isValidFolderForFindingCustomizableFiles(folder)) {
				resourceProposals.addAll(findModelsInFolder(folder));
			}
		}
		if (resource.getType() == IResource.FILE) {
			IFile file = (IFile) resource;
			resourceProposals.addAll(sortOutCustomizableFiles(file));
		}
		return resourceProposals;
	}

	/**
	 * Return true if the folder is valid to search for customizable models
	 * 
	 * @param folder
	 *            a given IFolder in the IProject
	 * @return if the folder is valid to search for customizable models
	 */
	private boolean isValidFolderForFindingCustomizableFiles(IFolder folder) {
		boolean res = true;

		List<String> naturesProject = new ArrayList<String>();
		IProject project = folder.getProject();

		// List the project's natures available
		try {
			naturesProject = Arrays.asList(project.getDescription()
					.getNatureIds());
		} catch (CoreException e1) {
			// ignore
		}

		// if a project has a javaProject nature
		if (naturesProject.contains(JavaCore.NATURE_ID)) {
			IJavaProject javaProject = JavaCore.create(project);
			try {
				IPath location = folder.getFullPath();
				IPath outputLocation = javaProject.getOutputLocation();
				// Is "location" equal to a "outputLocation of the project" OR
				// "one
				// of the outputLocation in a folder" ?
				res = (outputLocation.equals(location) || (checkOutputLocationForEachFolder(
						javaProject, folder)));
			} catch (JavaModelException e) {
				// ignore
			}
		}
		return !res;
	}

	/**
	 * For an IJavaProject, browse all the ClasspathEntry and check if theirs
	 * OutputLocation() is corresponding to a given IFolder
	 * 
	 * @param javaProject
	 *            an IJavaProject (a project with JAVA nature)
	 * @param folder
	 *            a given IFolder
	 * @return returns true if one of the outputLocation in classpathEntry of
	 *         the javaProject is equal to a given folder.getFullPath().
	 */
	private boolean checkOutputLocationForEachFolder(IJavaProject javaProject,
			IFolder folder) {
		boolean res = false;
		try {
			IClasspathEntry[] classpathEntry = javaProject.getRawClasspath();
			for (IClasspathEntry iClasspathEntry : classpathEntry) {
				if (iClasspathEntry.getOutputLocation() != null) {
					res = iClasspathEntry.getOutputLocation().equals(
							folder.getFullPath());
				}
			}
		} catch (JavaModelException e) {
			// ignore
		}
		return res;
	}

	/**
	 * Sort out models files that can be customizable. Those models files are
	 * registered by Resource.Factory.Registry.INSTANCE in Ecore
	 * 
	 * @param file
	 *            a given IFile
	 * @return a List<String> containing all the customizable files
	 */
	private List<String> sortOutCustomizableFiles(IFile file) {
		List<String> proposals = new ArrayList<String>();
		String fileExtension = file.getFileExtension();
		// XXX: Get the global instance map from Resource.Factory.Registry.
		// From there, we can get the files extension registered in the EMF
		// plug-in
		Map<String, Object> map = Resource.Factory.Registry.INSTANCE
				.getExtensionToFactoryMap();
		if (map.containsKey(fileExtension) || ("xmi").equals(fileExtension)) {
			StringBuilder stringBuilder = new StringBuilder();
			stringBuilder.append("\'");
			// TODO: Find a nicer way to express the url for customize / load
			stringBuilder.append("platform:/resource");
			stringBuilder.append(file.getFullPath().toString());
			stringBuilder.append("\'");
			proposals.add(stringBuilder.toString());
		}
		return proposals;
	}

	/**
	 * Get the right proposals of customizable models candidate for importURI
	 */
	@Override
	public void completeCustomize_ImportURI(EObject model,
			Assignment assignment, ContentAssistContext context,
			ICompletionProposalAcceptor acceptor) {
		List<String> proposals = new ArrayList<String>();

		// List all the projects of a workspace
		// IProject[] projects = ResourcesPlugin.getWorkspace().getRoot()
		// .getProjects();

		// XXX Get the current project from the model (from the eResource model
		// precisly)
		IProject[] projects = getCurrentProjectFromModelEResource(model);

		proposals.addAll(findModelsInProjects(projects));

		for (String proposal : proposals) {
			acceptor.accept(createCompletionProposal(proposal, context));
		}
	}

	/**
	 * Get the right proposals of customizable models candidate for loadURI
	 */
	@Override
	public void completeModelLoad_LoadURI(EObject model, Assignment assignment,
			ContentAssistContext context, ICompletionProposalAcceptor acceptor) {
		List<String> proposals = new ArrayList<String>();

		IProject[] projects = getCurrentProjectFromModelEResource(model);

		proposals.addAll(findModelsInProjects(projects));

		for (String proposal : proposals) {
			acceptor.accept(createCompletionProposal(proposal, context));
		}
	}

	/**
	 * Get the current project from the model (from the eResource model //
	 * precisly)
	 * 
	 * @param model
	 *            a given EObject model (context)
	 * @return the current IProject that contains the given model
	 */
	private IProject[] getCurrentProjectFromModelEResource(EObject model) {
		// We use the textual representation of an uri to get round from the
		// difference of java.net.URI and org.eclipse.emf.common.util.URI in EMF
		// Resource sense
		String strFile = model.eResource().getURI().toPlatformString(true);
		IFile iFile = ResourcesPlugin.getWorkspace().getRoot().getFile(
				new Path(strFile));
		IProject project = iFile.getProject();

		IProject[] projects = new IProject[] { project };
		return projects;
	}
}
