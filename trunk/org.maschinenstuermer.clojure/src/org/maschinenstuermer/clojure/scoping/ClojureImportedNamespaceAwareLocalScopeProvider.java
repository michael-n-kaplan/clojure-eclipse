package org.maschinenstuermer.clojure.scoping;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.maschinenstuermer.clojure.clojure.File;
import org.maschinenstuermer.clojure.clojure.Import;
import org.maschinenstuermer.clojure.clojure.Literal;
import org.maschinenstuermer.clojure.clojure.Ns;
import org.maschinenstuermer.clojure.clojure.PackageImport;
import org.maschinenstuermer.clojure.clojure.Reference;
import org.maschinenstuermer.clojure.clojure.util.ClojureSwitch;

import com.google.common.collect.Lists;

public class ClojureImportedNamespaceAwareLocalScopeProvider extends
		ImportedNamespaceAwareLocalScopeProvider {
	private Set<ImportNormalizer> defaultImports; 

	public ClojureImportedNamespaceAwareLocalScopeProvider() {
		super();
		final Set<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
		imports.add(createImportNormalizer("java.lang.*"));
		imports.add(createImportNormalizer("clojure.core.*"));
		defaultImports = Collections.unmodifiableSet(imports);
	}
	
	private final ClojureSwitch<Set<ImportNormalizer>> importNormalizer = new ClojureSwitch<Set<ImportNormalizer>>() {
		public Set<ImportNormalizer> caseFile(File _) {
			return defaultImports;			
		};
		
		public Set<ImportNormalizer> defaultCase(EObject _) {
			return Collections.emptySet();
		};
		
		public Set<ImportNormalizer> caseLiteral(final Literal object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			final EObject eContainer = EcoreUtil.getRootContainer(object);
			final List<EObject> siblings = Lists.newArrayList(eContainer.eContents());
			siblings.remove(object);
			for (EObject sibling : siblings) {
				imports.addAll(internalGetImportNormalizers(sibling));
			}
			return imports;
		};
		
		public Set<ImportNormalizer> caseImport(final Import object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			for (final PackageImport packageImport : object.getPackages()) {				
				addPackageImport(imports, packageImport);
			}
			return imports;
		};
		
		
		public Set<ImportNormalizer> caseNs(final Ns object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			for (final Reference ref : object.getRefs()) {
				for (final PackageImport packageImport : ref.getPackages()) {
					addPackageImport(imports, packageImport);
				}
			}
			return imports;
		};
	};
	
	@Override
	protected Set<ImportNormalizer> internalGetImportNormalizers(final EObject context) {
		return importNormalizer.doSwitch(context);
	}

	private void addPackageImport(final Set<ImportNormalizer> importNormalizers,
			final PackageImport packageImport) {
		String importedPackage = packageImport.getPackage();
		if (importedPackage != null) {
			importedPackage = importedPackage.concat(".");
			if (packageImport.getClasses().isEmpty()) {
				importNormalizers.add(createImportNormalizer(importedPackage.concat("*")));
			} else {
				for (final String importedClass : packageImport.getClasses()) {
					importNormalizers.add(createImportNormalizer(importedPackage.concat(importedClass)));
				}
			}
		}
	}
}
