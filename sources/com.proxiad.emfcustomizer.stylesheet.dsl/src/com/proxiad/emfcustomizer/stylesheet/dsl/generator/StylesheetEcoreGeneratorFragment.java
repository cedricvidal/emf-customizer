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
