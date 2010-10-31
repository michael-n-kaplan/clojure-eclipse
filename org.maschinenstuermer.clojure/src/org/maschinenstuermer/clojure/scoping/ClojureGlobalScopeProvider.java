package org.maschinenstuermer.clojure.scoping;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider;

import com.google.common.collect.Iterables;
import com.google.inject.Inject;

public class ClojureGlobalScopeProvider extends
		DefaultGlobalScopeProvider {

	@Inject
	private AbstractTypeScopeProvider typeScopeProvider;

	@Override
	public IScope getScope(EObject context, EReference reference) {
		final IScope clojureScope = super.getScope(context, reference);
		if (TypesPackage.Literals.JVM_TYPE.isSuperTypeOf(getEReferenceType(context, reference))) {
			final IScope javaScope = typeScopeProvider.getScope(context, reference);
			return new CombinedScope(javaScope, clojureScope);
		}
		return clojureScope;
	}
	

	protected EClass getEReferenceType(EObject context, EReference reference) {
		return reference.getEReferenceType();
	}

	private static class CombinedScope implements IScope {
		private final IScope clojureScope;
		private final IScope javaScope;
		
		
		public CombinedScope(final IScope javaScope, final IScope clojureScope) {
			this.javaScope = javaScope;
			this.clojureScope = clojureScope;
		}

		@Override
		public IScope getOuterScope() {
			// java scope has no outer scope, so it's safe to return the clojure
			// outer scope
			return clojureScope;
		}

		@Override
		public Iterable<IEObjectDescription> getContents() {
			return Iterables.concat(clojureScope.getContents(), 
					javaScope.getContents());
		}

		@Override
		public Iterable<IEObjectDescription> getAllContents() {
			return Iterables.concat(clojureScope.getAllContents(),
					javaScope.getAllContents());
		}

		@Override
		public IEObjectDescription getContentByName(String name) {
			final IEObjectDescription clojureContentByName = clojureScope.getContentByName(name);
			return clojureContentByName != null ?
					clojureContentByName : javaScope.getContentByName(name);
		}

		@Override
		public IEObjectDescription getContentByEObject(EObject object) {
			final IEObjectDescription clojureContentByEObject = clojureScope.getContentByEObject(object);
			return clojureContentByEObject != null ?
					clojureContentByEObject : javaScope.getContentByEObject(object);
		}

		@Override
		public Iterable<IEObjectDescription> getAllContentsByEObject(
				EObject object) {
			return Iterables.concat(clojureScope.getAllContentsByEObject(object),
					javaScope.getAllContentsByEObject(object));
		}
	}
}
