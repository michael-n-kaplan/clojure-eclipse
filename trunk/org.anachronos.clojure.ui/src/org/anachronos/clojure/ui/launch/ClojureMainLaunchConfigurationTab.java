package org.anachronos.clojure.ui.launch;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.core.PreferencesLookupDelegate;
import org.eclipse.dltk.debug.ui.launchConfigurations.MainLaunchConfigurationTab;

public class ClojureMainLaunchConfigurationTab extends
	MainLaunchConfigurationTab {

    public ClojureMainLaunchConfigurationTab(String mode) {
	super(mode);
    }

    @Override
    protected boolean breakOnFirstLinePrefEnabled(
	    PreferencesLookupDelegate delegate) {
	return false;
    }

    @Override
    protected boolean dbpgLoggingPrefEnabled(PreferencesLookupDelegate delegate) {
	return false;
    }

    @Override
    protected String getNatureID() {
	return ClojureNature.ID;
    }
}
