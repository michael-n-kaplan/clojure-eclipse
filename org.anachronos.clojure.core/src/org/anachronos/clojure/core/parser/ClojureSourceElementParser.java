package org.anachronos.clojure.core.parser;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.eclipse.dltk.core.ISourceElementParser;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;

public class ClojureSourceElementParser implements ISourceElementParser {

    @Override
    public void parseSourceModule(ISourceModule module, ISourceModuleInfo mifo) {
    }

    @Override
    public void setReporter(IProblemReporter reporter) {
    }

    @Override
    public void setRequestor(ISourceElementRequestor requestor) {
    }
}
