package org.anachronos.clojure.ui.editor;

import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.dltk.ui.text.ScriptOutlineInformationControl;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Shell;

public class ClojureOutlineInformationControl extends
	ScriptOutlineInformationControl {

    public ClojureOutlineInformationControl(Shell parent, int shellStyle,
	    int treeStyle, String commandId) {
	super(parent, shellStyle, treeStyle, commandId);
    }

    @Override
    protected IPreferenceStore getPreferenceStore() {
	return ClojureUIPlugin.getDefault().getPreferenceStore();
    }
}
