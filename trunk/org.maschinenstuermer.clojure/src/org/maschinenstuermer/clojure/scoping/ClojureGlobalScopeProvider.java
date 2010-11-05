package org.maschinenstuermer.clojure.scoping;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtext.common.types.TypesPackage;
import org.eclipse.xtext.common.types.xtext.AbstractTypeScopeProvider;
import org.eclipse.xtext.resource.IEObjectDescription;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.DefaultGlobalScopeProvider;
import org.maschinenstuermer.clojure.ClojureUtil;

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
		public IEObjectDescription getContentByName(final String name) {
			final IEObjectDescription javaContentByName = isJavaIdentifier(name) ? 
					javaScope.getContentByName(name) : null;
			final IEObjectDescription contentByName = javaContentByName == null ? 
				clojureScope.getContentByName(name) : javaContentByName;
			return contentByName;
		}

		private boolean isJavaIdentifier(final String name) {
			int i = 0;
			boolean isJavaName = true; 
			while (i < name.length()) {
				isJavaName &= Character.isJavaIdentifierStart(name.charAt(i++));
				while (isJavaName && i < name.length()) {
					isJavaName = Character.isJavaIdentifierPart(name.charAt(i));
					if (!isJavaName && name.charAt(i) == '.' && i < name.length() - 1) {
						isJavaName = true;
						i++;
						break;
					}
					i++;
				}
			}
			return isJavaName;
		}

		@Override
		public IEObjectDescription getContentByEObject(final EObject object) {
			final IEObjectDescription javaContentByEObject = ClojureUtil.IS_JVM_MEMBER.apply(object) ?
					javaScope.getContentByEObject(object) : null;
			final IEObjectDescription contentByEObject = javaContentByEObject == null ?
					clojureScope.getContentByEObject(object) : javaContentByEObject;
			return contentByEObject;
		}

		@Override
		public Iterable<IEObjectDescription> getAllContentsByEObject(
				EObject object) {
			return Iterables.concat(clojureScope.getAllContentsByEObject(object),
					javaScope.getAllContentsByEObject(object));
		}
	}
}
