package org.anachronos.clojure.core.parser;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.core.AbstractSourceElementParser;

public class ClojureSourceElementParser extends AbstractSourceElementParser {

    @Override
    protected String getNatureId() {
	return ClojureNature.ID;
    }
}
