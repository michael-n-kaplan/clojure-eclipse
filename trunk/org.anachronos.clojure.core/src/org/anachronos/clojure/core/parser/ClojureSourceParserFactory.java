package org.anachronos.clojure.core.parser;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;

public class ClojureSourceParserFactory implements ISourceParserFactory {

    public ClojureSourceParserFactory() {
    }

    @Override
    public ISourceParser createSourceParser() {
	return new ClojureSourceParser();
    }
}
