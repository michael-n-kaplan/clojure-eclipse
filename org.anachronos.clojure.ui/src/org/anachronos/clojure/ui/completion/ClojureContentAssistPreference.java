package org.anachronos.clojure.ui.completion;

import org.anachronos.clojure.ui.ClojureUIPlugin;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;

public class ClojureContentAssistPreference extends ContentAssistPreference {
    private static ContentAssistPreference instance;

    private ClojureContentAssistPreference() {
    }

    public static ContentAssistPreference getDefault() {
	if (instance == null) {
	    instance = new ClojureContentAssistPreference();
	}
	return instance;
    }

    @Override
    protected ScriptTextTools getTextTools() {
	return ClojureUIPlugin.getDefault().getTextTools();
    }
}
