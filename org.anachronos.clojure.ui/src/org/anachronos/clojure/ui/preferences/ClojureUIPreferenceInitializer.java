package org.anachronos.clojure.ui.preferences;

import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.EditorsUI;

public class ClojureUIPreferenceInitializer extends AbstractPreferenceInitializer {

    @Override
    public void initializeDefaultPreferences() {
	final IPreferenceStore store = ClojureUIPlugin.getDefault()
		.getPreferenceStore();
	PreferenceConstants.initializeDefaultValues(store);
	EditorsUI.useAnnotationsPreferencePage(store);
	EditorsUI.useQuickDiffPreferencePage(store);

	// literals
	final RGB stringDefaultColor = new RGB(165, 42, 42);
	PreferenceConverter.setDefault(store,
		ClojureColorPrefConstants.STRING_PREF_KEY, stringDefaultColor);
	PreferenceConverter.setDefault(store,
		ClojureColorPrefConstants.CHARACTER_COLOR_PREF_KEY,
		stringDefaultColor);
	setDefaultColorWithBold(store, ClojureColorPrefConstants.NUMBER_PREF_KEY,
		new RGB(139, 105, 20));

	// symbols
	setDefaultColorWithStyles(store, ClojureColorPrefConstants.KEYWORD_PREF_KEY, new RGB(30, 144, 255), true, false, true);
	setDefaultColorWithBold(store,
		ClojureColorPrefConstants.PREDEFINED_SYMBOL_PREF_KEY, new RGB(0, 0,
			255));
	setDefaultColorWithBold(store,
		ClojureColorPrefConstants.SYMBOL_PREF_KEY, new RGB(160, 32, 240));

	// data types
	setDefaultColorWithBold(store, ClojureColorPrefConstants.LIST_PREF_KEY,
		new RGB(0, 0, 0));
	setDefaultColorWithBold(store, ClojureColorPrefConstants.MAP_PREF_KEY,
		new RGB(127, 127, 127));
	setDefaultColorWithBold(store, ClojureColorPrefConstants.VECTOR_PREF_KEY,
		new RGB(139, 105, 20));
	
	// comment
	setDefaultColorWithStyles(store,
		ClojureColorPrefConstants.COMMENT_PREF_KEY, new RGB(0, 0, 0),
		false, true, false);
    }

    private void setDefaultColorWithStyles(final IPreferenceStore store,
	    final String key, final RGB defaultColor, final boolean bold,
	    final boolean italic, final boolean underline) {
	PreferenceConverter.setDefault(store, key, defaultColor);
	store.setDefault(getBoldKey(key), bold);
	store.setDefault(getItalicKey(key), italic);
	store.setDefault(getUnderlineKey(key), underline);
    }

    private void setDefaultColorWithBold(final IPreferenceStore store,
	    final String key, final RGB defaultColor) {
	setDefaultColorWithStyles(store, key, defaultColor, true, false, false);
    }

    public static String getBoldKey(String colorKey) {
        return colorKey + PreferenceConstants.EDITOR_BOLD_SUFFIX;
    }

    public static String getItalicKey(String colorKey) {
        return colorKey + PreferenceConstants.EDITOR_ITALIC_SUFFIX;
    }

    public static String getStrikethroughKey(String colorKey) {
        return colorKey + PreferenceConstants.EDITOR_STRIKETHROUGH_SUFFIX;
    }

    public static String getUnderlineKey(String colorKey) {
        return colorKey + PreferenceConstants.EDITOR_UNDERLINE_SUFFIX;
    }
}
