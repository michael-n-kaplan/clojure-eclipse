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
    private final int upperBound;;

    public Arity(final int lowerBound, final boolean isBound) {
	this.lowerBound = lowerBound;
	this.upperBound = isBound ? lowerBound : Integer.MAX_VALUE;
    }

    public boolean exceedsUpperbound(final int arity) {
	return arity > upperBound;
    }

    public boolean isValid(final int arity) {
	return arity >= lowerBound && arity <= upperBound;
    }
}
