package org.anachronos.clojure.ui.launch;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.debug.ui.launchConfigurations.IMainLaunchConfigurationTabListenerManager;
import org.eclipse.dltk.debug.ui.launchConfigurations.InterpreterTab;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;

public class ClojureInterpreterTab extends InterpreterTab {

    public ClojureInterpreterTab(
	    IMainLaunchConfigurationTabListenerManager listenerManager) {
	super(listenerManager);
    }

    @Override
    protected AbstractInterpreterComboBlock getInterpreterBlock() {
	return new ClojureInterpreterComboBlock(null);
    }

    @Override
    protected String getNature() {
	return ClojureNature.ID;
    }
}
