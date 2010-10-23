/*
 * generated by Xtext
 */
package org.maschinenstuermer.clojure.scoping;

import static org.eclipse.xtext.scoping.Scopes.scopeFor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.xtend.XtendFacade;
import org.eclipse.xtend.typesystem.emf.EmfRegistryMetaModel;
import org.eclipse.xtext.scoping.IScope;
import org.eclipse.xtext.scoping.impl.AbstractDeclarativeScopeProvider;

/**
 * This class contains custom scoping description.
 * 
 * see : http://www.eclipse.org/Xtext/documentation/latest/xtext.html#scoping
 * on how and when to use it 
 *
 */
public class ClojureScopeProvider extends AbstractDeclarativeScopeProvider {
	private final XtendFacade xtendFacade;

	public ClojureScopeProvider() {
		super();
		xtendFacade = XtendFacade.create("org::maschinenstuermer::clojure::scoping::Scoping");
		xtendFacade.registerMetaModel(new EmfRegistryMetaModel());
	}
	
	public IScope scope_SymbolDef(final EObject context, 
			final EReference reference) {
		@SuppressWarnings("unchecked")
		final Iterable<EObject> result = (Iterable<EObject>) 
			xtendFacade.call("scope", context);
		return scopeFor(result);
	}	
}
