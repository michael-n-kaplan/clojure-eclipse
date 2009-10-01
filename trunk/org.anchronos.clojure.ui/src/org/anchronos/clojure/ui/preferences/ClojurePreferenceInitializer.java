package org.anchronos.clojure.ui.preferences;

import org.anchronos.clojure.ui.Activator;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;

/**
 * Class used to initialize default preference values.
 */
public class ClojurePreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
	final IPreferenceStore store = Activator.getDefault()
		.getPreferenceStore();
	store.setDefault(
		ClojurePreferenceConstants.HIGHLIGHT_MATCHING_BRACKETS, true);
	PreferenceConverter.setDefault(store,
		ClojurePreferenceConstants.MATCHING_BRACKETS_COLOR,
		new RGB(128, 128, 128));
    }
}
