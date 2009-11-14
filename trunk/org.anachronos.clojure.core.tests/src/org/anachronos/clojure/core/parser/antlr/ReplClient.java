package org.anachronos.clojure.core.parser.antlr;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class ReplClient {

    @Test
    public void format() {
	assertEquals("0000000100", String.format("%010d", 100));
    }
}
