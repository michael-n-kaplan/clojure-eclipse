package org.maschinenstuermer.clojure.scoping;

import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.maschinenstuermer.clojure.clojure.File;

public class ClojureImportedNamespaceAwareLocalScopeProvider extends
		ImportedNamespaceAwareLocalScopeProvider {

	@Override
	protected Set<ImportNormalizer> internalGetImportNormalizers(final EObject context) {
		final Set<ImportNormalizer> importNormalizers = super.internalGetImportNormalizers(context);
		if (context instanceof File) {
			importNormalizers.add(createImportNormalizer("java.lang.*"));
		}
		return importNormalizers;
	}
}
