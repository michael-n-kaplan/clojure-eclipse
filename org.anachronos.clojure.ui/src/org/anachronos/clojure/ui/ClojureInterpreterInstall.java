package org.anachronos.clojure.ui;

import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;

public class ClojureInterpreterInstall extends AbstractInterpreterInstall {

    public ClojureInterpreterInstall(final IInterpreterInstallType type,
	    final String id) {
	super(type, id);
    }

    @Override
    public String getNatureId() {
	return ClojureNature.CLOJURE_NATURE;
    }
}
