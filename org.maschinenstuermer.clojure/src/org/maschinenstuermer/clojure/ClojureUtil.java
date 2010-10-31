package org.maschinenstuermer.clojure;

import org.eclipse.emf.ecore.EObject;
import org.maschinenstuermer.clojure.clojure.Fn;
import org.maschinenstuermer.clojure.clojure.NameBinding;
import org.maschinenstuermer.clojure.clojure.SymbolDef;

import com.google.common.base.Function;
import com.google.common.base.Predicate;

/**
 * Static helper class for accessing the parse tree.
 * 
 * @author km
 */
public class ClojureUtil {
	private ClojureUtil() {
	}

	/**
	 * Predicate is true iff. the given EObject is defining a global def.
	 */
	public static final Predicate<EObject> IS_DEF = new Predicate<EObject>() {
		@Override
		public boolean apply(EObject input) {
			return input instanceof SymbolDef 
				&& !(input instanceof Fn) 
				&& !(input instanceof NameBinding);
		}		
	};
	
	/**
	 * Returns argument as SymbolDef iff. argument is a global def.
	 */
	public static final Function<EObject, SymbolDef> AS_DEF = new Function<EObject, SymbolDef>() {
		@Override
		public SymbolDef apply(final EObject from) {
			return IS_DEF.apply(from) ? (SymbolDef) from : null;
		}
	};
}
