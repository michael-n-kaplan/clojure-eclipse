package org.anachronos.clojure.ui.launch;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.debug.ui.launchConfigurations.IMainLaunchConfigurationTabListenerManager;
import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterComboBlock;

public class ClojureInterpreterComboBlock extends AbstractInterpreterComboBlock {

    public ClojureInterpreterComboBlock(
	    IMainLaunchConfigurationTabListenerManager listenerManager) {
	super(listenerManager);
    }

    @Override
    protected String getCurrentLanguageNature() {
	return ClojureNature.ID;
    }

    @Override
    protected void showInterpreterPreferencePage() {
	// TODO Auto-generated method stub
    }
}
