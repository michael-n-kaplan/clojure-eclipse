package org.anachronos.clojure.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.jface.preference.IPreferenceStore;

public class ClojureUILanguageToolkit extends AbstractDLTKUILanguageToolkit {

    @Override
    public IDLTKLanguageToolkit getCoreToolkit() {
	return ClojureLanguageToolkit.getDefault();
    }

    @Override
    public IPreferenceStore getPreferenceStore() {
	return Activator.getDefault().getPreferenceStore();
    }
}
