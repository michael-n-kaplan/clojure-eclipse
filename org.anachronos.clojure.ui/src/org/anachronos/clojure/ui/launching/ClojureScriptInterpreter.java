package org.anachronos.clojure.ui.launching;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import org.eclipse.dltk.console.IScriptConsoleIO;
import org.eclipse.dltk.console.IScriptExecResult;
import org.eclipse.dltk.console.IScriptInterpreter;
import org.eclipse.dltk.console.InterpreterResponse;
import org.eclipse.dltk.console.ScriptExecResult;

public class ClojureScriptInterpreter implements IScriptInterpreter {
    private IScriptConsoleIO protocol;

    private int state;

    @Override
    public void addInitialListenerOperation(Runnable runnable) {
    }

    @Override
    public InputStream getInitialOutputStream() {
	return protocol.getInitialResponseStream();
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
	return new String[] {};
    }

    @Override
    public IScriptExecResult exec(final String command) throws IOException {
	final InterpreterResponse response = protocol.execInterpreter(command);
	final IScriptExecResult result;
	if (response != null) {
	    state = response.getState();
	    result = new ScriptExecResult(response.getContent(), response
		    .isError());
	} else {
	    result = null;
	}
	return result;
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
