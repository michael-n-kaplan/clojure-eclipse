package org.anachronos.clojure.ui.console;

import org.eclipse.dltk.console.ui.ScriptConsole;
import org.eclipse.dltk.console.ui.ScriptConsoleFactoryBase;

public class ClojureConsoleFactory extends ScriptConsoleFactoryBase {

    @Override
    protected ScriptConsole createConsoleInstance() {
	final ScriptConsole scriptConsole = new ClojureScriptConsole("Clojure",
		"Clojure");
	return scriptConsole;
    }
}
