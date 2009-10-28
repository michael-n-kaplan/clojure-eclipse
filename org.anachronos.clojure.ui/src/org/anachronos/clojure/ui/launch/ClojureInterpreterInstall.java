package org.anachronos.clojure.ui.launch;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterRunner;

public class ClojureInterpreterInstall extends AbstractInterpreterInstall {

    public ClojureInterpreterInstall(final IInterpreterInstallType type,
	    final String id) {
	super(type, id);
    }

    @Override
    public String getNatureId() {
	return ClojureNature.ID;
    }

    @Override
    public IInterpreterRunner getInterpreterRunner(String mode) {
	return new ClojureInterpreterRunner(this);
    }
}
