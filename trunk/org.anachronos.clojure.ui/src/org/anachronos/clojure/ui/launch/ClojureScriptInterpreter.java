package org.anachronos.clojure.ui.launch;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.console.ConsoleRequest;
import org.eclipse.dltk.console.IScriptConsoleIO;
import org.eclipse.dltk.console.IScriptExecResult;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.InterpreterResponse;
import org.eclipse.dltk.console.ScriptExecResult;

public class ClojureScriptInterpreter implements IScriptInterpreter,
	ConsoleRequest {
    private IScriptConsoleIO protocol;

    private int state;

    @Override
    public void addInitialListenerOperation(Runnable runnable) {
    }

    @Override
    public InputStream getInitialOutputStream() {
	return null;
    }

    @Override
    public boolean isValid() {
	return protocol != null;
    }

    @Override
    public void close() throws IOException {
	if (protocol != null) {
	    protocol.close();
	}
    }

    @SuppressWarnings("unchecked")
    @Override
    public List getCompletions(final String commandLine, final int position)
	    throws IOException {
	return Collections.emptyList();
    }

    @Override
    public String getDescription(final String commandLine, final int position)
	    throws IOException {
	return "";
    }

    @Override
    public String[] getNames(final String type) throws IOException {
	return null;
    }

    @Override
    public IScriptExecResult exec(final String command) throws IOException {
	final InterpreterResponse response = protocol.execInterpreter(command);

	state = response.getState();
	return new ScriptExecResult(response.getContent());
    }

    @Override
    public int getState() {
	return state;
    }

    @Override
    public void consoleConnected(final IScriptConsoleIO protocol) {
	this.protocol = protocol;
    }
}
