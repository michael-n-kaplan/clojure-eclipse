package org.anachronos.clojure.ui.console;

import org.anachronos.clojure.core.ClojureNature;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.ScriptInterpreterManager;
import org.eclipse.dltk.console.ui.ScriptConsole;

public class ClojureScriptConsole extends ScriptConsole {

    public ClojureScriptConsole(final String consoleName,
	    final String consoleType) {
	super(consoleName, consoleType);
	final IScriptInterpreter scriptInterpreter = ScriptInterpreterManager
		.getInstance().createInterpreter(ClojureNature.CLOJURE_NATURE);
	// ScriptConsoleServer.getInstance().register(consoleName,
	// scriptInterpreter);
	setInterpreter(scriptInterpreter);
    }
}
