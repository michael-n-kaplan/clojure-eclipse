package org.anachronos.clojure.ui.console;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.ui.IScriptConsoleFactory;
import org.eclipse.dltk.console.ui.ScriptConsole;
import org.eclipse.dltk.console.ui.ScriptConsoleFactoryBase;

public class ClojureScriptConsoleFactory extends ScriptConsoleFactoryBase
	implements IScriptConsoleFactory {

    @Override
    protected ScriptConsole createConsoleInstance() {
	return createConsoleInstance(null, null);
    }

    @Override
    public void openConsole(final IScriptInterpreter interpreter,
	    final String id, final ILaunch launch) {
	final ScriptConsole console = createConsoleInstance(interpreter, id);
	console.setLaunch(launch);
	registerAndOpenConsole(console);
    }

    private ScriptConsole createConsoleInstance(IScriptInterpreter interpreter,
	    String id) {
	return new ClojureScriptConsole(interpreter, id);
    }
}
