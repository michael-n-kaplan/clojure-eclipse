package org.maschinenstuermer.clojure.naming;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmDeclaredType;
import org.eclipse.xtext.naming.IQualifiedNameProvider;
import org.eclipse.xtext.util.IResourceScopeCache;
import org.eclipse.xtext.util.Tuples;
import org.maschinenstuermer.clojure.ClojureUtil;
import org.maschinenstuermer.clojure.clojure.Ns;
import org.maschinenstuermer.clojure.clojure.SymbolDef;

import com.google.inject.Inject;
import com.google.inject.Provider;

public class ClojureQualifiedNameProvider extends IQualifiedNameProvider.AbstractImpl {
	private static final String UNDEFINED_NAME = "";
	
	@Inject
	private IResourceScopeCache cache;
	
	@Override
	public String getQualifiedName(final EObject obj) {
		return cache.get(Tuples.pair("fqn", obj), obj.eResource(), new Provider<String>() {
			@Override
			public String get() {
				final SymbolDef def = ClojureUtil.AS_DEF.apply(obj);
				boolean hasQualifiedName = def != null 
					&& def.getName() != null 
					&& !UNDEFINED_NAME.equals(def.getName());
				if (hasQualifiedName) {
					return namespace(def) + "." + def.getName();
				} else if (obj instanceof JvmDeclaredType) {
					final JvmDeclaredType javaType = (JvmDeclaredType) obj;
					return javaType.getFullyQualifiedName();
				} else {
					return null;
				}
			}
		});
	}
	
	private String namespace(final SymbolDef symbolDef) {
		final Ns ns = getNs(symbolDef);
		final String namespace = ns != null ? 
				ns.getName() : "user";
				return namespace;
	}
	
	private Ns getNs(final EObject eObject) {
		EObject currentNs = eObject;
		while (!(currentNs instanceof Ns) && currentNs.eContainer() != null) 
			currentNs = eObject.eContainer();
		return currentNs instanceof Ns ? (Ns) currentNs : null;
	}
}
