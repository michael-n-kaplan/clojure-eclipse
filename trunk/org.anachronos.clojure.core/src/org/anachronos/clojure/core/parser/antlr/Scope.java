package org.anachronos.clojure.core.parser.antlr;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Scope of variables.
 * 
 * @author km
 */
public class Scope {
    private final Scope parentScope;
    private final Set<String> names = new HashSet<String>();
    private final Map<String, Arity> functionNameToArity = new HashMap<String, Arity>();

    public Scope() {
	this(null);
    }

    private Scope(final Scope parentScope) {
	this.parentScope = parentScope;
    }

    /**
     * Creates a new scope with this object as parent scope.
     * 
     * @return new scope
     */
    public Scope newScope() {
	return new Scope(this);
    }

    /**
     * Ends this scope and returns parent scope.
     */
    public Scope endScope() {
	return parentScope;
    }

    /**
     * @param name
     * @return true iff. name is defined in this scopes hierachy
     */
    public boolean isDefined(final String name) {
	final boolean contained = names.contains(name);
	final boolean containedInHierachy = parentScope != null
		&& parentScope.isDefined(name);
	return contained || containedInHierachy;
    }

    public boolean isFunction(final String name) {
	return functionNameToArity.containsKey(name);
    }

    public boolean isDefinedArity(final String name, final int arity) {
	return getArity(name).isValid(arity);
    }

    private Arity getArity(final String name) {
	return functionNameToArity.get(name);
    }

    public boolean exceedsArityUpperBound(final String name, final int arity) {
	return getArity(name).exceedsUpperbound(arity);
    }

    /**
     * Adds the given declaration to this scope.
     * 
     * @param name
     */
    public void addVariableDef(String name) {
	names.add(name);
    }

    public void addFunctionDef(String name, int arity, boolean bound) {
	names.add(name);
	functionNameToArity.put(name, new Arity(arity, bound));
    }
}
