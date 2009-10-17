package org.anachronos.clojure.ui.preferences;

import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.dltk.ui.preferences.AbstractConfigurationBlockPreferencePage;
import org.eclipse.dltk.ui.preferences.IPreferenceConfigurationBlock;
import org.eclipse.dltk.ui.preferences.OverlayPreferenceStore;

public class ClojureSyntaxColoringPreferencePage extends
	AbstractConfigurationBlockPreferencePage {

    @Override
    protected IPreferenceConfigurationBlock createConfigurationBlock(
	    final OverlayPreferenceStore overlayPreferenceStore) {
	return new ClojureSyntaxColoringBlock(overlayPreferenceStore);
    }

    @Override
    protected String getHelpId() {
	return "";
    }

    @Override
    protected void setDescription() {
    }

    @Override
    protected void setPreferenceStore() {
	setPreferenceStore(ClojureUIPlugin.getDefault().getPreferenceStore());
    }
}
