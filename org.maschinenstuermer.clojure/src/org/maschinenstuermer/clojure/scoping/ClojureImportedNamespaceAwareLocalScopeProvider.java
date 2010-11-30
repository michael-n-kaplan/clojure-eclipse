package org.maschinenstuermer.clojure.scoping;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.scoping.impl.ImportedNamespaceAwareLocalScopeProvider;
import org.maschinenstuermer.clojure.clojure.ImportForm;
import org.maschinenstuermer.clojure.clojure.InNs;
import org.maschinenstuermer.clojure.clojure.ModuleImport;
import org.maschinenstuermer.clojure.clojure.Ns;
import org.maschinenstuermer.clojure.clojure.PackageImport;
import org.maschinenstuermer.clojure.clojure.Reference;
import org.maschinenstuermer.clojure.clojure.util.ClojureSwitch;

public class ClojureImportedNamespaceAwareLocalScopeProvider extends
		ImportedNamespaceAwareLocalScopeProvider {
	
	private final ClojureSwitch<Set<ImportNormalizer>> importNormalizer = new ClojureSwitch<Set<ImportNormalizer>>() {		
		public Set<ImportNormalizer> defaultCase(final EObject object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			final EObject eContainer = object.eContainer();
			if (eContainer != null ) {
				final int indexOfObject = eContainer.eContents().indexOf(object);
				if (indexOfObject > 0) {
					final List<EObject> siblings = eContainer.eContents().subList(0, indexOfObject);
					for (EObject sibling : siblings) {
						imports.addAll(internalGetImportNormalizers(sibling));
					}
				} 
			}
			return imports;
		};
		
		public Set<ImportNormalizer> caseImportForm(final ImportForm object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			for (final PackageImport packageImport : object.getPackages()) {				
				addPackageImport(imports, packageImport);
			}
			return imports;
		};
		
		public Set<ImportNormalizer> caseNs(final Ns object) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			addDefaultImports(imports);
			final boolean addClojureCoreImport = object.getName() == null || 
				!"clojure.core".equals(object.getName());
			if (addClojureCoreImport)
				addPackage("clojure.core.*", imports);
			for (final Reference ref : object.getRefs()) {
				for (final PackageImport packageImport : ref.getPackages()) {
					addPackageImport(imports, packageImport);
				}
				for (final ModuleImport moduleImport : ref.getUses()) {
					addModuleImport(imports, moduleImport);
				}
			}
			return imports;
		};

		public Set<ImportNormalizer> caseInNs(InNs _) {
			final HashSet<ImportNormalizer> imports = new HashSet<ImportNormalizer>();
			addDefaultImports(imports);
			return imports;
		}

		private void addDefaultImports(final HashSet<ImportNormalizer> imports) {
			addPackage("java.lang.*", imports);
			addPackage(BigDecimal.class.getCanonicalName(), imports);
			addPackage(BigInteger.class.getCanonicalName(), imports);
		};
		
		private void addPackage(final String packageName, final Set<ImportNormalizer> imports) {
			imports.add(createImportNormalizer(packageName));
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
		
		private void addModuleImport(final Set<ImportNormalizer> importNormalizers,
				final ModuleImport moduleImport) {
//			String importedPackage = moduleImport.getPackage();
//			if (importedPackage != null) {
//				importedPackage = importedPackage.concat(".");
//				if (moduleImport.getFns().isEmpty()) {
//					importNormalizers.add(createImportNormalizer(importedPackage.concat("*")));
//				} else {
//					for (final String importedClass : moduleImport.getFns()) {
//						importNormalizers.add(createImportNormalizer(importedPackage.concat(importedClass)));
//					}
//				}
//			}
		}

	};
	
	@Override
	protected Set<ImportNormalizer> internalGetImportNormalizers(final EObject context) {
		return importNormalizer.doSwitch(context);
	}
}
