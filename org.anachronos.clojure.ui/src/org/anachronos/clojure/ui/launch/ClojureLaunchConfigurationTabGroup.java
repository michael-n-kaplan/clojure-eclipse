package org.anachronos.clojure.ui.launch;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class ClojureLaunchConfigurationTabGroup extends
	AbstractLaunchConfigurationTabGroup {

    @Override
    public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
	ILaunchConfigurationTab[] tabs = new ILaunchConfigurationTab[] {
		new ClojureMainLaunchConfigurationTab(mode),
		new ClojureInterpreterTab(null), new EnvironmentTab() };
	setTabs(tabs);
    }
}
