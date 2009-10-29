package org.anachronos.clojure.ui.console;

import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.ui.ScriptConsole;

public class ClojureScriptConsole extends ScriptConsole {
    public static final String CONSOLE_TYPE = "clojure_console";

    public ClojureScriptConsole(final IScriptInterpreter interpreter,
	    final String id) {
	super(id, CONSOLE_TYPE);
	setInterpreter(interpreter);
    }
}
