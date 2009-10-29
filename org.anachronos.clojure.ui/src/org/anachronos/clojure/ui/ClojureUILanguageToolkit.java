package org.anachronos.clojure.ui;

import org.anachronos.clojure.core.ClojureLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.jface.preference.IPreferenceStore;

public class ClojureUILanguageToolkit extends AbstractDLTKUILanguageToolkit {
    private static ClojureUILanguageToolkit instance;

    private ClojureUILanguageToolkit() {
    }

    public static ClojureUILanguageToolkit getDefault() {
	if (instance == null) {
	    instance = new ClojureUILanguageToolkit();
	}
	return instance;
    }

    @Override
    public IDLTKLanguageToolkit getCoreToolkit() {
	return ClojureLanguageToolkit.getDefault();
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
	return ClojureUIPlugin.getDefault().getPreferenceStore();
    }
}
