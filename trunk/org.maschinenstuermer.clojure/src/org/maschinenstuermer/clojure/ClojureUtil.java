package org.maschinenstuermer.clojure;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.xtext.common.types.JvmMember;
import org.eclipse.xtext.common.types.TypesPackage;
import org.maschinenstuermer.clojure.clojure.Def;
import org.maschinenstuermer.clojure.clojure.Defn;
import org.maschinenstuermer.clojure.clojure.DefnPriv;
import org.maschinenstuermer.clojure.clojure.Fn;
import org.maschinenstuermer.clojure.clojure.Form;
import org.maschinenstuermer.clojure.clojure.NameBinding;
import org.maschinenstuermer.clojure.clojure.SymbolDef;
import org.maschinenstuermer.clojure.clojure.util.ClojureSwitch;

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

	private static ClojureSwitch<Boolean> IS_FN_SWITCH = new ClojureSwitch<Boolean>() {
		@Override
		public Boolean defaultCase(EObject object) {
			return false;
		}
		
		public Boolean caseFn(Fn object) {
			return true;
		};
		
		@Override
		public Boolean caseDefn(Defn object) {
			return true;
		}
		
		@Override
		public Boolean caseDefnPriv(DefnPriv object) {
			return true;
		}
		
		@Override
		public Boolean caseDef(Def object) {
			final Form init = object.getInit();
			return init == null ? false : doSwitch(init);
		}
	};
	
	/**
	 * Predicate is true iff. the given EObject is defining a global def.
	 */
	public static final Predicate<EObject> IS_DEF = new Predicate<EObject>() {
		@Override
		public boolean apply(EObject input) {
			return input instanceof SymbolDef 
				&& !(input instanceof Fn) 
				&& !(input instanceof NameBinding)
				&& !(input instanceof DefnPriv);
		}		
	};
	
	public static boolean isFn(final SymbolDef symbolDef) {
		return IS_FN_SWITCH.doSwitch(symbolDef);
	}
	/**
	 * Returns argument as SymbolDef iff. argument is a global def.
	 */
	public static final Function<EObject, SymbolDef> AS_DEF = new Function<EObject, SymbolDef>() {
		@Override
		public SymbolDef apply(final EObject from) {
			return IS_DEF.apply(from) ? (SymbolDef) from : null;
		}
	};
	
	/**
	 * Predicate is true iff. the given EObject is a JvmMember.
	 */
	public static final Predicate<EObject> IS_JVM_MEMBER = new Predicate<EObject>() {
		@Override
		public boolean apply(EObject input) {
			return input instanceof JvmMember; 
		}		
	};

	/**
	 * Predicate is true iff. the given EObject is a JvmMember.
	 */
	public static final Predicate<EClass> IS_JVM_FEATURE_CLAZZ = new Predicate<EClass>() {
		@Override
		public boolean apply(EClass input) {
			return TypesPackage.eINSTANCE.getJvmFeature().isSuperTypeOf(input); 
		}		
	};

	/**
	 * Returns argument as SymbolDef iff. argument is a JvmMember.
	 */
	public static final Function<EObject, JvmMember> AS_JVM_MEMBER = new Function<EObject, JvmMember>() {
		@Override
		public JvmMember apply(final EObject from) {
			return IS_JVM_MEMBER.apply(from) ? (JvmMember) from : null;
		}
	};
}
