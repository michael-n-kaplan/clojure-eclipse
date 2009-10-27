package org.anachronos.clojure.core.parser.antlr;

import static org.junit.Assert.assertTrue;

import org.junit.Test;


public class ArityTest {

    @Test
    public void isValid() {
	assertTrue(new Arity(1, true).isValid(1));
	assertTrue(new Arity(1, true).isValid(1));
	assertTrue(new Arity(1, false).isValid(2));
    }
}
