package org.anachronos.clojure.ui.launch;

import org.eclipse.dltk.launching.AbstractInterpreterRunner;
import org.eclipse.dltk.launching.IInterpreterInstall;

public class ClojureInterpreterRunner extends AbstractInterpreterRunner {

    protected ClojureInterpreterRunner(IInterpreterInstall install) {
	super(install);
    }

    @Override
    protected String getProcessType() {
	return super.getProcessType();
    }
}
