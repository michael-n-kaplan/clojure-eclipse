package org.anachronos.clojure.ui.editor;

import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.internal.ui.editor.ScriptOutlinePage;
import org.eclipse.jface.preference.IPreferenceStore;

public class ClojureOutlinePage extends ScriptOutlinePage {

    public ClojureOutlinePage(final ScriptEditor editor,
	    final IPreferenceStore store) {
	super(editor, store);
    }
}
