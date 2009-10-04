package org.anachronos.clojure.ui.preferences;

import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.ScriptInterpreterPreferencePage;

public class ClojureInterpreterPreferencePage extends
	ScriptInterpreterPreferencePage {

    public ClojureInterpreterPreferencePage() {
    }

    @Override
    public InterpretersBlock createInterpretersBlock() {
	return new ClojureInterpretersBlock();
    }
}
