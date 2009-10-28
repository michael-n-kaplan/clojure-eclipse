package org.anachronos.clojure.ui.launch;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.launching.AbstractScriptLaunchConfigurationDelegate;

public class ClojureLaunchConfigurationDelegate extends
	AbstractScriptLaunchConfigurationDelegate {

    @Override
    public String getLanguageId() {
	return ClojureNature.ID;
    }

}
