package org.anachronos.clojure.core.parser.antlr;

/**
 * Represents the arity of a function. An arity has a lower isBound of
 * parameters and can either be isBound or unbound. An unbound arity means that
 * the function at least requires the upper isBound of parameters.
 * 
 * @author km
 * 
 */
public class Arity {
    private final int lowerBound;
    private final boolean isBound;

    public Arity(final int lowerBound, final boolean isBound) {
	this.lowerBound = lowerBound;
	this.isBound = isBound;
    }

    public int getLowerBound() {
	return lowerBound;
    }

    public boolean isBound() {
	return isBound;
    }

    public boolean isValid(final int arity) {
	return arity == lowerBound || !isBound && arity >= lowerBound;
    }
}
