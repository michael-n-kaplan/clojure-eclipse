package org.anachronos.clojure.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

public class ClojureLanguageToolkit extends AbstractLanguageToolkit {
    public static final String CLOJURE_CONTENT_TYPE = ClojureCorePlugin.PLUGIN_ID
	    + ".content-type";

    private static IDLTKLanguageToolkit toolkit;

    public static IDLTKLanguageToolkit getDefault() {
	if (toolkit == null) {
	    toolkit = new ClojureLanguageToolkit();
	}
	return toolkit;
    }

    @Override
    public String getLanguageContentType() {
	return CLOJURE_CONTENT_TYPE;
    }

    @Override
    public String getLanguageName() {
	return "Clojure";
    }

    @Override
    public String getNatureId() {
	return ClojureNature.ID;
    }

    @Override
    public String getPreferenceQualifier() {
	return ClojureCorePlugin.PLUGIN_ID;
    }

    @Override
    public boolean languageSupportZIPBuildpath() {
	return true;
    }
}
